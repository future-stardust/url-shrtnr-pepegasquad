package edu.kpi.testcourse.logic;

import com.google.gson.JsonObject;
import edu.kpi.testcourse.Main;
import edu.kpi.testcourse.bigtable.BigTableImpl;
import edu.kpi.testcourse.model.User;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class UserActions {
  public static boolean createUser(User user){
    user.setPassword(hash(user.getPassword()));
    if(BigTableImpl.users.isEmpty()){
      BigTableImpl.users.put(user.getUuid(), user.toJson());
      return true;
    }else {
      boolean find = false;
      for (Map.Entry<String, JsonObject> entry : BigTableImpl.users.entrySet()){
        if((user.getEmail().equals(Main.getGson().fromJson(entry.getValue(), User.class).getEmail()))){
          find = true;
          break;
        }
      }
      if(!find){
        BigTableImpl.users.put(user.getUuid(), user.toJson());
        return true;
      }
    }
    return false;
  }

  public static String hash(String password){
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

  public static boolean findUserByEmail(String email){
    boolean find = false;
    for (Map.Entry<String, JsonObject> entry : BigTableImpl.users.entrySet()){
      if(email.equals(Main.getGson().fromJson(entry.getValue(), User.class).getEmail())){
        find = true;
        break;
      }
    }
    return find;
  }

  public static boolean checkPassword(String providedEmail, String providedPassword) {
    boolean find = false;
    for (Map.Entry<String, JsonObject> entry : BigTableImpl.users.entrySet()){
      if((providedEmail.equals(Main.getGson().fromJson(entry.getValue(), User.class)
        .getEmail())) && (hash(providedPassword).equals(Main.getGson().fromJson(entry.getValue(), User.class)
        .getPassword()))){
        find = true;
        break;
      }
    }
    return find;
  }
}
