package edu.kpi.testcourse.bigtable;

import com.nimbusds.jose.shaded.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.JsonObject;
import netscape.javascript.JSObject;

// ⚠️ Please, pay attention, that you should not use any 3rd party persistence library (e.g. data
// ⚠️ base, implementation of key-value storage, etc)

public class BigTableImpl implements BigTable {

  public static final Map<String, JsonObject> users = new HashMap<>();
  public static final Map<String, String> urls = new HashMap<>();

  @Override
  public void putUser(String key, JsonObject value) {
    users.put(key, value);
  }

  @Override
  public void putUrl(String key, String value) {
    urls.put(key, value);
  }

  @Override
  public JsonObject getUser(String key) {
    return users.get(key);
  }

  @Override
  public String getUrl(String key) {
    return urls.get(key);
  }
}
