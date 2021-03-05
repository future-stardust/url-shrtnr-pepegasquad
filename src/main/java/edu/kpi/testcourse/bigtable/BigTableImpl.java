package edu.kpi.testcourse.bigtable;

import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;

// ⚠️ Please, pay attention, that you should not use any 3rd party persistence library (e.g. data
// ⚠️ base, implementation of key-value storage, etc)

/**
 * Custom database.
 */
public class BigTableImpl implements BigTable {

  public static final Map<String, JsonObject> users = new HashMap<>();
  public static final Map<String, String> urls = new HashMap<>();

  private static Integer urlId = 1;

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
