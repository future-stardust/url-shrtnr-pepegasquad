package edu.kpi.testcourse.api.urlshorten;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@MicronautTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(PER_CLASS)
public class UrlShortenTest {

  @Inject
  @Client("/")
  private HttpClient client;

  private String token;

  @Test
  @Order(1)
  public void testShortenFailUnauthorized() {
    User user = new User("pepega@kpi.ua", "password", new ArrayList<String>());
    UserActions.createUser(user);

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("email", "pepega@kpi.ua");
    jsonObject.addProperty("password", "password");
    HttpRequest<String> request = HttpRequest.POST("/users/signin", jsonObject.toString());
    {
      var response = client.toBlocking().exchange(request, String.class);
      var body = Main.getGson().fromJson(response.getBody().get(), JsonObject.class);
      token = body.get("access_token").getAsString();
    }

    JsonObject jsonEmail = new JsonObject();
    //jsonEmail.addProperty("email", "pepega@kpi.ua");
    jsonEmail.addProperty("url", "http://pepega.kpi.ua");
    request = HttpRequest.POST("/urls/shorten", jsonEmail.toString()).bearerAuth(token.toUpperCase());
    var sizeBeforeRequest = BigTableImpl.urls.size();
    try {
      HttpResponse<Object> response = client.toBlocking().exchange(request);
    } catch (HttpClientResponseException e) {
      assertEquals(HttpStatus.UNAUTHORIZED, e.getStatus());
    }
    assertThat(BigTableImpl.urls.size()).isEqualTo(sizeBeforeRequest);
  }

  @Test
  @Order(2)
  public void testShorten() {
    JsonObject jsonEmail = new JsonObject();
    //jsonEmail.addProperty("email", "pepega@kpi.ua");
    jsonEmail.addProperty("url", "http://pepega.kpi.ua");
    HttpRequest<String> request = HttpRequest.POST("/urls/shorten", jsonEmail.toString()).bearerAuth(token);
    var response = client.toBlocking().exchange(request, String.class);
    assertEquals(HttpStatus.CREATED, response.getStatus());
    var body = Main.getGson().fromJson(response.getBody().get(), JsonObject.class);
    body.has("");
    assertThat(body.keySet()).contains("shortened_url");
    assertThat(BigTableImpl.urls).containsKey(body.get("shortened_url").getAsString());
    assertThat(BigTableImpl.urls.get(body.get("shortened_url").getAsString())).isEqualTo("http://pepega.kpi.ua");
  }
}
