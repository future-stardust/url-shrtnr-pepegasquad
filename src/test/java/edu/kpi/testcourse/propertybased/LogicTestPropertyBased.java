package edu.kpi.testcourse.propertybased;

import org.junit.jupiter.api.Test;
import static org.quicktheories.QuickTheory.qt;
import static org.quicktheories.generators.SourceDSL.strings;
import edu.kpi.testcourse.logic.UserActions;

public class LogicTestPropertyBased {

  static final int MIN_LENGTH = 6;
  static final int MAX_LENGTH = 10;

  @Test
  void doesCreatedAliasExist_propertyBased() {
    qt().forAll(
      strings().basicLatinAlphabet().ofLengthBetween(MIN_LENGTH, MAX_LENGTH),
      strings().basicLatinAlphabet().ofLengthBetween(MIN_LENGTH, MAX_LENGTH)
    ).check((alias, url) -> {
      // WHEN
      UserActions.putUrl(alias, url);
      // THEN
      return UserActions.retrieveUrl(alias).equals(url);
    });
  }
}
