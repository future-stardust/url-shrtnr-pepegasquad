package edu.kpi.testcourse.propertybased;

import static org.quicktheories.QuickTheory.qt;
import static org.quicktheories.generators.SourceDSL.strings;

import edu.kpi.testcourse.logic.UserActions;
import org.junit.jupiter.api.Test;

public class PasswordTestPropertyBased {

  static final int MIN_LENGTH = 6;
  static final int MAX_LENGTH = 10;

  UserActions userActions = new UserActions();

  @Test
  public void checkPasswordEqual_propertyBased() {
    qt().forAll(
      strings().basicMultilingualPlaneAlphabet().ofLengthBetween(MIN_LENGTH, MAX_LENGTH)
    ).check(password -> {
      // WHEN
      String pass1 = UserActions.hash(password);
      String pass2 = UserActions.hash(password);
      // THEN
      return pass1==pass2;
    });
  }

  @Test
  public void checkPasswordNotEqual_propertyBased() {
    qt().forAll(
      strings().basicMultilingualPlaneAlphabet().ofLengthBetween(MIN_LENGTH, MAX_LENGTH),
      strings().basicMultilingualPlaneAlphabet().ofLengthBetween(MIN_LENGTH, MAX_LENGTH)
    ).check((password1, password2) -> {
      // WHEN
      String pass1 = UserActions.hash(password1);
      String pass2 = UserActions.hash(password2);
      // THEN
      return pass1!=pass2;
    });
  }
}
