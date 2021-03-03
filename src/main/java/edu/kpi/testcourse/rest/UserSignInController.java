package edu.kpi.testcourse.rest;

import com.google.gson.JsonObject;
import com.nimbusds.jose.shaded.json.JSONObject;
import edu.kpi.testcourse.Main;
import edu.kpi.testcourse.bigtable.BigTableImpl;
import edu.kpi.testcourse.model.User;
import edu.kpi.testcourse.logic.UserActions;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import java.util.Map;
import javax.inject.Inject;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/users")
public class UserSignInController {
  @Inject
  @Client("/")
  RxHttpClient client;
  /**
   * in field "body" in Postman you should provide user email and password like
   * {
   *   "email" : "yourEmail",
   *   "password" : "yourPassword"
   * }
   */
  @Post(value = "/signin", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
  public HttpResponse signIn(@Body JSONObject object){
    User user = Main.getGson().fromJson(object.toJSONString(), User.class);

    for (Map.Entry<String, JsonObject> entry : BigTableImpl.users.entrySet()){
      if((user.getEmail().equals(Main.getGson().fromJson(entry.getValue(), User.class).getEmail()))){
        user.setUuid(entry.getKey());
        break;
      }
    }
    UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user.getEmail(), user.getPassword());

    HttpResponse<BearerAccessRefreshToken> httpResponse;
    boolean response = UserActions.findUserByEmail(user.getEmail());
    if(response){
      boolean isPasswordValid = UserActions.checkPassword(user.getEmail(), user.getPassword());
      if(isPasswordValid) {
        HttpRequest request = HttpRequest.POST("/login", credentials);
        httpResponse = client.toBlocking().exchange(request, BearerAccessRefreshToken.class);
      }else return HttpResponse.badRequest("Invalid password");
    } else return HttpResponse.notFound("User with email \"" + user.getEmail() + "\" was not found.");
    return httpResponse;
  }
}


