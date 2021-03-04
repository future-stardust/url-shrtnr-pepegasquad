package edu.kpi.testcourse.rest;

import com.nimbusds.jose.shaded.json.JSONObject;
import edu.kpi.testcourse.Main;
import edu.kpi.testcourse.logic.UserActions;
import edu.kpi.testcourse.model.User;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import java.util.UUID;

/**
 * Controller for signup.
 */
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/users")
public class UserSignUpController {
  /**
   * in field "body" in Postman you should provide user email and password like
   * {
   *   "email" : "yourEmail",
   *   "password" : "yourPassword"
   * }
   * then you will see response that your user is created.
   */
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
