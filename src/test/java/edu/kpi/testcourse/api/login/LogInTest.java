package edu.kpi.testcourse.api.login;

import com.nimbusds.jose.shaded.json.JSONObject;
import edu.kpi.testcourse.Main;
import edu.kpi.testcourse.bigtable.BigTableImpl;
import edu.kpi.testcourse.logic.UserActions;
import edu.kpi.testcourse.model.User;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LogInTest {
  @Test
  void testLogIn(){
    //TODO this test
    assertThat(2).isEqualTo(2);
  }
}
