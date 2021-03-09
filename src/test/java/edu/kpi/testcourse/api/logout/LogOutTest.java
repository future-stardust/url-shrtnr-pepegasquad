package edu.kpi.testcourse.api.logout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.kpi.testcourse.Main;
import edu.kpi.testcourse.bigtable.BigTableImpl;
import edu.kpi.testcourse.logic.UserActions;
import edu.kpi.testcourse.model.User;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@MicronautTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(PER_CLASS)
public class LogOutTest {

  @Inject
  @Client("/users")
  private HttpClient client;

  private String token;

  @Test
  @Order(1)
  public void testSignOutFailNoAuth() {
    User user = new User("signout@gmail.com", "password", new ArrayList<String>());
    UserActions.createUser(user);

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("email", "signout@gmail.com");
    jsonObject.addProperty("password", "password");
    HttpRequest<String> request = HttpRequest.POST("/signin", jsonObject.toString());
    {
      var response = client.toBlocking().exchange(request, String.class);
      var body = Main.getGson().fromJson(response.getBody().get(), JsonObject.class);
      token = body.get("access_token").getAsString();
    }
    assertThat(BigTableImpl.tokens).contains(token);

    request = HttpRequest.POST("/signout", "");

    try {
      HttpResponse<Object> response = client.toBlocking().exchange(request);
    } catch (HttpClientResponseException e) {
      assertEquals(HttpStatus.UNAUTHORIZED, e.getStatus());
    }
    assertThat(BigTableImpl.tokens).contains(token);
  }

  @Test
  @Order(2)
  public void testSignOutFailWrongAuth() {
    HttpRequest<String> request = HttpRequest.POST("/signout", "").bearerAuth(token.toUpperCase());

    try {
      HttpResponse<Object> response = client.toBlocking().exchange(request);
    } catch (HttpClientResponseException e) {
      assertEquals(HttpStatus.UNAUTHORIZED, e.getStatus());
    }
    assertThat(BigTableImpl.tokens).contains(token);
  }

  @Test
  @Order(3)
  public void testSignOut() {
    HttpRequest<String> request = HttpRequest.POST("/signout", "").bearerAuth(token);
    var response = client.toBlocking().exchange(request);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatus());

    assertThat(BigTableImpl.tokens).doesNotContain(token);
  }
}
