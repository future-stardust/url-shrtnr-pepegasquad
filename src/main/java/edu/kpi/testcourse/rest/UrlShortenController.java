package edu.kpi.testcourse.rest;

import com.nimbusds.jose.shaded.json.JSONObject;
import edu.kpi.testcourse.Main;
import edu.kpi.testcourse.logic.UserActions;
import edu.kpi.testcourse.model.Url;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import javax.validation.Valid;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/url")
public class UrlShortenController {

  record UrlClass(String url, String shortenedUrl) {}

  @Post(value = "/shorten",
    consumes = MediaType.APPLICATION_JSON,
    produces = MediaType.APPLICATION_JSON)
  public MutableHttpResponse<String> shortenUrl(@Body JSONObject object) {
    Url url = Main.getGson().fromJson(object.toJSONString(), Url.class);

    url.setShortenedUrl(Url.shortenUrl(Main.bigTable.getUrlId()));

    UserActions.putUrl(url.getShortenedUrl(), url.getFullUrl());

    return HttpResponse.created(Main.getGson().toJson(new UrlClass(url.getFullUrl(), url.getShortenedUrl())));
  }
}
