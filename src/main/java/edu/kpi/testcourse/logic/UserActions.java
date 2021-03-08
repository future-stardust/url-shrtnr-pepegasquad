package edu.kpi.testcourse.logic;

import com.google.gson.JsonObject;
import edu.kpi.testcourse.Main;
import edu.kpi.testcourse.bigtable.BigTableImpl;
import edu.kpi.testcourse.model.Url;
import edu.kpi.testcourse.model.User;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Provides methods with Users.
 */
public class UserActions {
  /**
   * Create user.
   *
   * @param user user
   * @return result of creation
   */
  public static boolean createUser(User user) {
    user.setPassword(hash(user.getPassword()));
    user.initiateUrlList();
    if (BigTableImpl.users.isEmpty()) {
      BigTableImpl.users.put(user.getUuid(), user.toJson());
      return true;
    } else {
      boolean find = false;
      for (Map.Entry<String, JsonObject> entry : BigTableImpl.users.entrySet()) {
        if ((user.getEmail()
            .equals(Main.getGson().fromJson(entry.getValue(), User.class).getEmail()))) {
          find = true;
          break;
        }
      }
      if (!find) {
        BigTableImpl.users.put(user.getUuid(), user.toJson());

        return true;
      }
    }

    return false;
  }

  /**
   * Update user info.
   *
   * @param user user
   */
  public static void updateUser(User user) {
    BigTableImpl.users.put(user.getUuid(), user.toJson());
  }

  /**
   * Hashes user password.
   *
   * @param password password
   * @return hash
   */
  public static String hash(String password) {
    String hash = "";
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
      byte[] hashBytes = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
      BigInteger noHash = new BigInteger(1, hashBytes);
      hash = noHash.toString(16);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    return hash;
  }

  /**
   * Find user by email.
   *
   * @param email user email
   * @return result of search
   */
  public static boolean findUserByEmail(String email) {
    for (Map.Entry<String, JsonObject> entry : BigTableImpl.users.entrySet()) {
      if (email.equals(Main.getGson().fromJson(entry.getValue(), User.class).getEmail())) {

        return true;
      }
    }

    return false;
  }

  /**
   * Find user by email.
   *
   * @param email user email
   * @return result of search
   */
  public static User retrieveUserByEmail(String email) {
    for (Map.Entry<String, JsonObject> entry : BigTableImpl.users.entrySet()) {
      if (email.equals(Main.getGson().fromJson(entry.getValue(), User.class).getEmail())) {
        User user = Main.getGson().fromJson(entry.getValue(), User.class);
        user.setUuid(entry.getKey());
        return user;
      }
    }
    return null;
  }

  /**
   * Check password.
   *
   * @param providedEmail    email which provided
   * @param providedPassword password which provided
   * @return result of validation
   */
  public static boolean checkPassword(String providedEmail, String providedPassword) {
    for (Map.Entry<String, JsonObject> entry : BigTableImpl.users.entrySet()) {
      if ((providedEmail.equals(Main.getGson().fromJson(entry.getValue(), User.class)
          .getEmail()))
          && (hash(providedPassword).equals(Main.getGson().fromJson(entry.getValue(), User.class)
          .getPassword()))) {
        return true;
      }
    }

    return false;
  }

  /**
   * Create url.
   *
   * @param shortenedUrl shortened url
   * @param fullUrl full url provided by user
   */
  public static void putUrl(String shortenedUrl, String fullUrl) {
    Main.bigTable.putUrl(shortenedUrl, fullUrl);
  }

  /**
   * Delete url.
   *
   * @param shortenedUrl shortened url
   */
  public static void deleteUrl(String shortenedUrl) {
    Main.bigTable.deleteUrl(shortenedUrl);
  }

  /**
   * List user urls.
   *
   * @param shortenedUrlList shortened url
   * @return url list consisting of { shortenedUrl: string, fullUrl: string }
   */
  public static ArrayList<JsonObject> listUrls(ArrayList<String> shortenedUrlList) {
    ArrayList<JsonObject> urlList = new ArrayList<>();

    for (String shortenedUrl : shortenedUrlList) {
      Url buf = new Url(BigTableImpl.urls.get(shortenedUrl));
      buf.setShortenedUrl(shortenedUrl);

      urlList.add(buf.toJson());
    }

    return urlList;
  }

  /**
   * Retrieve full url from shortened one.
   *
   * @param shortenedUrl - shortened url
   * @return fullUrl
   */
  public static String retrieveUrl(String shortenedUrl) {
    return BigTableImpl.urls.get(shortenedUrl);
  }
}
