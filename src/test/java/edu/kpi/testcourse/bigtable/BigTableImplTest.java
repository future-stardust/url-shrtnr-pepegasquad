package edu.kpi.testcourse.bigtable;

import com.google.gson.JsonObject;
import edu.kpi.testcourse.logic.UserActions;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BigTableImplTest {

  @Test
  void checkValueSaving() {
    BigTableImpl bigTable = new BigTableImpl();

    JsonObject object = new JsonObject();
    object.addProperty("email" , "testEmail@gmail.com");
    object.addProperty("password" , UserActions.hash("testPassword"));
    object.addProperty("urlList" , "");
    bigTable.putUser("testKey", object);
    JsonObject value = bigTable.getUser("testKey");

    assertThat(value).isEqualTo(object);
  }

}
