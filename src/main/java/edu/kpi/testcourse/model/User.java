package edu.kpi.testcourse.model;

import com.google.gson.JsonObject;
import edu.kpi.testcourse.Main;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import com.google.gson.JsonParser;

public class User implements Serializable {
  private String email;
  private String password;
  private transient String uuid;
  private ArrayList<String> urlList;

  public User(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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
    return "User{" +
      "email='" + email + '\'' +
      ", password='" + password + '\'' +
      ", uuid='" + uuid + '\'' +
      '}';
  }

  public JsonObject toJson(){
    String json =  Main.getGson().toJson(this, User.class);
    JsonObject object = JsonParser.parseString(json).getAsJsonObject();
    return object;
  }
}
