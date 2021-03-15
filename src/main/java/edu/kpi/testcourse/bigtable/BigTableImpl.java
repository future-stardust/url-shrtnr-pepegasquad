package edu.kpi.testcourse.bigtable;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import edu.kpi.testcourse.model.User;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// ⚠️ Please, pay attention, that you should not use any 3rd party persistence library (e.g. data
// ⚠️ base, implementation of key-value storage, etc)

/**
 * Custom database.
 */
public class BigTableImpl implements BigTable {

  public static Map<String, JsonObject> users = new HashMap<>();
  public static Map<String, String> urls = new HashMap<>();
  public static final Set<String> tokens = new HashSet<>();

  public static Integer urlId = 1;

  @Override
  public void putUser(String key, JsonObject value) {
    users.put(key, value);
  }


  @Override
  public void putUrl(String key, String value) {
    urls.put(key, value);
    urlId++;
  }

  @Override
  public void putToken(String token) {
    tokens.add(token);
  }

  @Override
  public Set<String> getTokens() {
    return tokens;
  }

  @Override
  public Integer getUrlId() {
    return urlId;
  }

  @Override
  public JsonObject getUser(String key) {
    return users.get(key);
  }

  @Override
  public String getUrl(String key) {
    return urls.get(key);
  }

  @Override
  public void deleteUrl(String key) {
    urls.remove(key);
  }

}
