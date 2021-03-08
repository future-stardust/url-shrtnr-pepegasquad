package edu.kpi.testcourse.api.login;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Map;

/**
 * Unit test that checks login functionality.
 */
@MicronautTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LogInTest {
  @Inject
  @Client("/users/signin")
  private HttpClient client;

  @Test
  @Order(1)
  public void testSignInFailNoPass() {
    User user = new User("signin@gmail.com", "password", new ArrayList<String>());
    UserActions.createUser(user);

    JsonObject jsonObjectNoPass = new JsonObject();
    jsonObjectNoPass.addProperty("email", "signin@gmail.com");
    var request = HttpRequest.POST("/", jsonObjectNoPass.toString());
    try {
      HttpResponse<Object> response = client.toBlocking().exchange(request);
    } catch (HttpClientResponseException e) {
      assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
    }
    assertThat(BigTableImpl.tokens).isEmpty();
  }

  @Test
  @Order(2)
  public void testSignInFailNoEmail() {
    JsonObject jsonObjectNoEmail = new JsonObject();
    jsonObjectNoEmail.addProperty("password", "password");
    var request = HttpRequest.POST("/", jsonObjectNoEmail.toString());
    try {
      HttpResponse<Object> response = client.toBlocking().exchange(request);
    } catch (HttpClientResponseException e) {
      assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
    }
    assertThat(BigTableImpl.tokens).isEmpty();
  }

  @Test
  @Order(3)
  public void testSignInFailWrongPass() {
    JsonObject jsonObjectWrongPass = new JsonObject();
    jsonObjectWrongPass.addProperty("email", "signin@gmail.com");
    jsonObjectWrongPass.addProperty("password", "myPassword");
    var request = HttpRequest.POST("/", jsonObjectWrongPass.toString());
    try {
      HttpResponse<Object> response = client.toBlocking().exchange(request);
    } catch (HttpClientResponseException e) {
      assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
    }
    assertThat(BigTableImpl.tokens).isEmpty();
  }

  @Test
  @Order(4)
  public void testSignInFailWrongEmail() {
    JsonObject jsonObjectWrongEmail = new JsonObject();
    jsonObjectWrongEmail.addProperty("email", "singin@gmail.com");
    jsonObjectWrongEmail.addProperty("password", "password");
    var request = HttpRequest.POST("/", jsonObjectWrongEmail.toString());
    try {
      HttpResponse<Object> response = client.toBlocking().exchange(request);
    } catch (HttpClientResponseException e) {
      assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }
    assertThat(BigTableImpl.tokens).isEmpty();
  }

  @Test
  @Order(5)
  public void testSignIn() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("email", "signin@gmail.com");
    jsonObject.addProperty("password", "password");

    HttpRequest<String> request = HttpRequest.POST("/", jsonObject.toString());
    var response = client.toBlocking().exchange(request, String.class);
    assertEquals(HttpStatus.OK, response.getStatus());

    var body = Main.getGson().fromJson(response.getBody().get(), JsonObject.class);
    assertThat(BigTableImpl.tokens).contains(body.get("access_token").getAsString());
  }
}
