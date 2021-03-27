package edu.kpi.testcourse.propertybased;

import static org.quicktheories.QuickTheory.qt;
import static org.quicktheories.generators.SourceDSL.strings;

import edu.kpi.testcourse.logic.UserActions;
import edu.kpi.testcourse.model.User;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class UserTestPropertyBased {

  private static final int MIN_LENGTH = 3;
  private static final int MAX_LENGTH = 10;

  @Test
  void shouldCreateUser() {
    qt().forAll(
      strings().basicLatinAlphabet().ofLengthBetween(MIN_LENGTH, MAX_LENGTH),
      strings().basicLatinAlphabet().ofLengthBetween(MIN_LENGTH, MAX_LENGTH)
    ).check((email, password) -> {
      // WHEN
      if (!UserActions.createUser(new User(email, password, new ArrayList<>()))) {
        return false;
      }
      // THEN
      return UserActions.retrieveUserByEmail(email) != null;
    });
  }

  @Test
  void shouldUpdateUserUrlList() {
    qt().forAll(
      strings().basicLatinAlphabet().ofLengthBetween(MIN_LENGTH, MAX_LENGTH),
      strings().basicLatinAlphabet().ofLengthBetween(MIN_LENGTH, MAX_LENGTH),
      strings().basicLatinAlphabet().ofLengthBetween(MIN_LENGTH, MAX_LENGTH),
      strings().basicLatinAlphabet().ofLengthBetween(MIN_LENGTH, MAX_LENGTH)
    ).check((email, password, shortUrl, fullUrl) -> {
      // GIVEN
      UserActions.createUser(new User(email, password, new ArrayList<>()));
      User user = UserActions.retrieveUserByEmail(email);

      // WHEN
      user.addUrlToUrlArray(shortUrl);
      UserActions.updateUser(user);

      // THEN
      return UserActions.retrieveUserByEmail(email).getUrlList().contains(shortUrl);
    });
  }
}
