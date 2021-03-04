package edu.kpi.testcourse.bigtable;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.google.gson.JsonObject;
import edu.kpi.testcourse.logic.UserActions;
import org.junit.jupiter.api.Test;

class BigTableImplTest {

  @Test
  void checkValueSaving() {
    JsonObject object = new JsonObject();
    object.addProperty("email", "testEmail@gmail.com");
    object.addProperty("password", UserActions.hash("testPassword"));
    object.addProperty("urlList", "");

    BigTableImpl bigTable = new BigTableImpl();
    bigTable.putUser("testKey", object);
    JsonObject value = bigTable.getUser("testKey");

    assertThat(value).isEqualTo(object);
  }

}
