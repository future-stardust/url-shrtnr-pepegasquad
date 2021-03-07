package edu.kpi.testcourse.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.kpi.testcourse.Main;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Model that represent User.
 */
public class User {
  private String email;
  private String password;
  private transient String uuid;
  private ArrayList<String> urlList;

  public User(String email, String password, ArrayList<String> urlList) {
    this.email = email;
    this.password = password;
    this.urlList = urlList;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public ArrayList<String> getUrlList() {
    return urlList;
  }

  public void addUrlToUrlArray(String shortenedUrl) {
    urlList.add(shortenedUrl);
  }

  public void removeUrlFromUrlArray(String shortenedUrl) {
    urlList.remove(shortenedUrl);
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void initiateUrlList() {
    this.urlList = new ArrayList<>();
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(email, user.email) && Objects
      .equals(password, user.password) && Objects.equals(uuid, user.uuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, password, uuid);
  }

  @Override
  public String toString() {
    return "User{"
      + "email='" + email + '\''
      + ", password='" + password + '\''
      + ", uuid='" + uuid + '\''
      + '}';
  }

  /**
   * Convert User to Json format.
   *
   * @return  object in json format
   */
  public JsonObject toJson() {
    String json =  Main.getGson().toJson(this, User.class);
    JsonObject object = JsonParser.parseString(json).getAsJsonObject();

    return object;
  }
}
