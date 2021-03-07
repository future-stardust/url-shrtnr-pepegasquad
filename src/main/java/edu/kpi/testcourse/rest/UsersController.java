package edu.kpi.testcourse.rest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jose.shaded.json.JSONObject;
import edu.kpi.testcourse.Main;
import edu.kpi.testcourse.bigtable.BigTableImpl;
import edu.kpi.testcourse.logic.UserActions;
import edu.kpi.testcourse.model.User;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;

/**
 * Controller for login.
 */
@Controller("/users")
public class UsersController {
  @Inject
  @Client("/")
  RxHttpClient client;

  /**
   * Method that process requests for signing in.
   *
   * @param object user email and password
   * @return authorization bearer token
   */
  @Secured(SecurityRule.IS_ANONYMOUS)
  @Post(value = "/signin",
      consumes = MediaType.APPLICATION_JSON,
      produces = MediaType.APPLICATION_JSON)
  public HttpResponse signIn(@Body JSONObject object) {
    User user = Main.getGson().fromJson(object.toJSONString(), User.class);

    for (Map.Entry<String, JsonObject> entry : BigTableImpl.users.entrySet()) {
      if ((user.getEmail()
          .equals(Main.getGson().fromJson(entry.getValue(), User.class).getEmail()))) {
        user.setUuid(entry.getKey());
        break;
      }
    }
    UsernamePasswordCredentials credentials =
        new UsernamePasswordCredentials(user.getEmail(), user.getPassword());

    HttpResponse<String> httpResponse;

    boolean response = UserActions.findUserByEmail(user.getEmail());
    if (response) {
      boolean isPasswordValid = UserActions.checkPassword(user.getEmail(), user.getPassword());
      if (isPasswordValid) {
        HttpRequest request = HttpRequest.POST("/login", credentials);
        httpResponse = client.toBlocking().exchange(request, String.class);
      } else {
        return HttpResponse.badRequest("Invalid password");
      }
    } else {
      return HttpResponse.notFound("User with email \"" + user.getEmail() + "\" was not found.");
    }
    JsonObject jsonObject = JsonParser.parseString(httpResponse.body()).getAsJsonObject();
    BigTableImpl.tokens.add(jsonObject.get("access_token").toString().replace("\"", ""));
    return httpResponse;
  }

  /**
   * Method that process requests for signout.
   *
   * @param token authorization bearer token
   * @return response code
   */
  @Secured(SecurityRule.IS_AUTHENTICATED)
  @Post(value = "/signout", consumes = MediaType.APPLICATION_JSON)
  public HttpResponse signOut(@Header("Authorization") String token) {
    if (BigTableImpl.tokens.contains(token.split(" ")[1])) {
      token = token.split(" ")[1]; // delete part 'Bearer '
      BigTableImpl.tokens.remove(token);
      return HttpResponse.noContent();
    } else {
      return HttpResponse.unauthorized();
    }
  }

  /**
   * Method that process requests for signing up.
   *
   * @param object user email and password
   * @return response code
   */
  @Secured(SecurityRule.IS_ANONYMOUS)
  @Post(value = "/signup",
      consumes = MediaType.APPLICATION_JSON,
      produces = MediaType.APPLICATION_JSON)
  public HttpResponse signUp(@Body JSONObject object) {
    User user = Main.getGson().fromJson(object.toJSONString(), User.class);
    user.setUuid(UUID.randomUUID().toString());
    boolean response = UserActions.createUser(user);
    if (response) {
      return HttpResponse.created("User with email \"" + user.getEmail() + "\" was created.");
    } else {
      return HttpResponse.badRequest("User with email \"" + user.getEmail() + "\" already exists.");
    }
  }
}


