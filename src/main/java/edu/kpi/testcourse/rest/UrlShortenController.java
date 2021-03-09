package edu.kpi.testcourse.rest;

import com.google.gson.JsonObject;
import com.nimbusds.jose.shaded.json.JSONObject;
import edu.kpi.testcourse.Main;
import edu.kpi.testcourse.bigtable.BigTableImpl;
import edu.kpi.testcourse.logic.UserActions;
import edu.kpi.testcourse.model.Url;
import edu.kpi.testcourse.model.User;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import java.security.Principal;
import java.util.ArrayList;

/**
 * Controller for URLs.
 */
@Controller("/urls")
public class UrlShortenController {

  record UrlListClass(ArrayList<JsonObject> urls) {}

  /**
   * Method that process requests for shortening URL.
   *
   * @param token authorization bearer token
   * @param object full URL in JSON format
   * @param principal object to get identity of user
   * @return response code
   */
  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Post(value = "/shorten",
      consumes = MediaType.APPLICATION_JSON,
      produces = MediaType.APPLICATION_JSON)
  public MutableHttpResponse<String> shortenUrl(@Header("Authorization") String token,
      @Body JSONObject object, Principal principal) {
    if (BigTableImpl.tokens.contains(token.replaceFirst("Bearer[ ]+", ""))) {
      JsonObject urlJson = Main.getGson().fromJson(object.toJSONString(), JsonObject.class);

      if (!urlJson.has("url")) {
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("reason_code", 0);
        jsonResponse.addProperty("reason_text", "Cannot parse json");
        return HttpResponse.badRequest(jsonResponse.toString()).contentType("application/json");
      }

      Url url = new Url(urlJson.get("url").getAsString());
      url.setShortenedUrl(Url.shortenUrl(Main.bigTable.getUrlId()));
      UserActions.putUrl(url.getShortenedUrl(), url.getFullUrl());

      User user = UserActions.retrieveUserByEmail(principal.getName());

      if (user == null) {
        return HttpResponse.unauthorized();
      }

      user.addUrlToUrlArray(url.getShortenedUrl());
      UserActions.updateUser(user);

      JsonObject jsonResponse = new JsonObject();
      jsonResponse.addProperty("shortened_url", url.getShortenedUrl());
      return HttpResponse.created(jsonResponse.toString()).contentType("application/json");
    } else {
      return HttpResponse.unauthorized();
    }
  }

  /**
   * Method that process requests for deleting URLs from user's account.
   *
   * @param token authorization bearer token
   * @param shortenedUrl shortened URL
   * @param principal object to get identity of user
   * @return response code
   */
  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Delete(value = "/{shortenedUrl}",
      consumes = MediaType.APPLICATION_JSON,
      produces = MediaType.APPLICATION_JSON)
  public MutableHttpResponse<String> deleteUrl(@Header("Authorization") String token,
      @Body String shortenedUrl, Principal principal) {
    if (BigTableImpl.tokens.contains(token.split(" ")[1])) {
      String fullUrl = Main.bigTable.getUrl(shortenedUrl);
      User user = UserActions.retrieveUserByEmail(principal.getName());

      if (fullUrl == null) {
        return HttpResponse.notFound("Shortened url with name '" + shortenedUrl
          + "' doesn't exist");
      }
      if (user == null) {
        return HttpResponse.unauthorized();
      }

      UserActions.deleteUrl(shortenedUrl);
      user.removeUrlFromUrlArray(shortenedUrl);

      return HttpResponse.noContent();
    } else {
      return HttpResponse.unauthorized();
    }
  }

  /**
   * Method that process requests for getting list of URLs from user's account.
   *
   * @param token authorization bearer token
   * @param principal object to get identity of user
   * @return list of URLs
   */
  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Get(value = "/",
      consumes = MediaType.APPLICATION_JSON,
      produces = MediaType.APPLICATION_JSON)
  public MutableHttpResponse<String> listUrls(@Header("Authorization") String token,
      Principal principal) {
    if (BigTableImpl.tokens.contains(token.split(" ")[1])) {
      User user = UserActions.retrieveUserByEmail(principal.getName());

      if (user == null) {
        return HttpResponse.unauthorized();
      }

      ArrayList<String> shortenedUrls = user.getUrlList();

      ArrayList<JsonObject> urlList = UserActions.listUrls(shortenedUrls);

      return HttpResponse.ok(Main.getGson().toJson(new UrlListClass(urlList)));
    } else {
      return HttpResponse.unauthorized();
    }
  }
}
