package edu.kpi.testcourse.propertybased;

import org.junit.jupiter.api.Test;
import static org.quicktheories.QuickTheory.qt;
import static org.quicktheories.generators.SourceDSL.strings;
import edu.kpi.testcourse.logic.UserActions;

public class LogicTestPropertyBased {

  static final int MIN_LENGTH = 6;
  static final int MAX_LENGTH = 10;

  UserActions userActions = new UserActions();

  @Test
  void doesCreatedAliasExist_propertyBased() {
    qt().forAll(
      strings().basicLatinAlphabet().ofLengthBetween(MIN_LENGTH, MAX_LENGTH),
      strings().basicLatinAlphabet().ofLengthBetween(MIN_LENGTH, MAX_LENGTH)
    ).check((alias, url) -> {
      // WHEN
      userActions.putUrl(alias, url);
      // THEN
      return userActions.retrieveUrl(alias)==url;
    });
  }

  @Test
  void shouldDeleteUrl() {
    qt().forAll(
      strings().basicLatinAlphabet().ofLengthBetween(MIN_LENGTH, MAX_LENGTH),
      strings().basicLatinAlphabet().ofLengthBetween(MIN_LENGTH, MAX_LENGTH)
    ).check((alias, url) -> {
      // GIVEN
      UserActions.putUrl(alias, url);
      // WHEN
      UserActions.deleteUrl(alias);
      // THEN
      return UserActions.retrieveUrl(alias) == null;
    });
  }
}
