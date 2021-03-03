package edu.kpi.testcourse.api.signup;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.gson.JsonObject;
import com.nimbusds.jose.shaded.json.JSONObject;
import edu.kpi.testcourse.Main;
import edu.kpi.testcourse.bigtable.BigTableImpl;
import edu.kpi.testcourse.logic.UserActions;
import edu.kpi.testcourse.model.User;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Unit test that checks signup functionality.
 */
public class SignUpTest {
  @Test
  void testSignUp() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("email", "alexandr123@gmail.com");
    jsonObject.put("password", "myPassword");

    User user = Main.getGson().fromJson(jsonObject.toJSONString(), User.class);
    user.setUuid(UUID.randomUUID().toString());
    UserActions.createUser(user);

    JsonObject object1 = new JsonObject();
    object1.addProperty("email", "alexandr123@gmail.com");
    object1.addProperty("password", UserActions.hash("myPassword"));
    Map.Entry<String, JsonObject> entry = BigTableImpl.users.entrySet().iterator().next();
    assertThat(entry.getValue()).isEqualTo(object1);
  }
}
