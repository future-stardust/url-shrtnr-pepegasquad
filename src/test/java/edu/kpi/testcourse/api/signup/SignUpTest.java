package edu.kpi.testcourse.api.signup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.nimbusds.jose.shaded.json.JSONObject;
import edu.kpi.testcourse.Main;
import edu.kpi.testcourse.bigtable.BigTableImpl;
import edu.kpi.testcourse.logic.UserActions;
import edu.kpi.testcourse.model.User;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import java.net.http.HttpClient;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SignUpTest {
  @Test
  void testSignUp(){
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("email" , "alexandr123@gmail.com");
    jsonObject.put("password" , "myPassword");

    User user = Main.getGson().fromJson(jsonObject.toJSONString(), User.class);
    user.setUuid(UUID.randomUUID().toString());
    UserActions.createUser(user);

    JsonObject object1 = new JsonObject();
    object1.addProperty("email" , "alexandr123@gmail.com");
    object1.addProperty("password" , UserActions.hash("myPassword"));
    Map.Entry<String, JsonObject> entry = BigTableImpl.users.entrySet().iterator().next();
    assertThat(entry.getValue()).isEqualTo(object1);
  }
}
