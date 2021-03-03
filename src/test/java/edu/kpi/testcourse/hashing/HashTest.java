package edu.kpi.testcourse.hashing;

import edu.kpi.testcourse.logic.UserActions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HashTest {
  @Test
  void testEqualsHashForOneObject(){
    String first = UserActions.hash("testString");
    String second = UserActions.hash("testString");
    assertThat(first).isEqualTo(second);
  }

  @Test
  void testNotEqualsForDifferentValues(){
    String first = UserActions.hash("testString");
    String second = UserActions.hash("differentString");
    assertThat(first).isNotEqualTo(second);
  }
}
