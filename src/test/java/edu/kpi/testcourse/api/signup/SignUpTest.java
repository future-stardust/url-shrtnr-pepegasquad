package edu.kpi.testcourse.api.signup;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nimbusds.jose.shaded.json.JSONObject;
import edu.kpi.testcourse.Main;
import edu.kpi.testcourse.bigtable.BigTableImpl;
import edu.kpi.testcourse.logic.UserActions;
import edu.kpi.testcourse.model.User;

import java.util.Map;
import java.util.UUID;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.inject.Inject;

/**
 * Unit test that checks signup functionality.
 */
@MicronautTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SignUpTest {

  @Inject
  @Client("/users/signup")
  private HttpClient client;

  @Test
  @Order(1)
  public void testSignUpFailNoPass() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("email", "nopass@gmail.com");

    HttpRequest<String> request = HttpRequest.POST("/", jsonObject.toString());

    try {
      HttpResponse<Object> response = client.toBlocking().exchange(request);
    } catch (HttpClientResponseException e) {
      assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
    }
    assertThat(BigTableImpl.users.entrySet()).isEmpty();
  }

  @Test
  @Order(2)
  public void testSignUpFailNoEmail() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("password", "myPassword");

    HttpRequest<String> request = HttpRequest.POST("/", jsonObject.toString());

    try {
      HttpResponse<Object> response = client.toBlocking().exchange(request);
    } catch (HttpClientResponseException e) {
      assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
    }
    assertThat(BigTableImpl.users.entrySet()).isEmpty();
  }

  @Test
  @Order(3)
  public void testSignUp() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("email", "signuptest@gmail.com");
    jsonObject.addProperty("password", "myPassword");

    HttpRequest<String> request = HttpRequest.POST("/", jsonObject.toString());
    var response = client.toBlocking().exchange(request);
    assertEquals(HttpStatus.CREATED, response.getStatus());

    JsonObject expected = new JsonObject();
    expected.addProperty("email", "signuptest@gmail.com");
    expected.addProperty("password", UserActions.hash("myPassword"));
    expected.add("urlList", new JsonArray());
    Map.Entry<String, JsonObject> entry = BigTableImpl.users.entrySet().iterator().next();
    assertThat(entry.getValue()).isEqualTo(expected);
  }
}
