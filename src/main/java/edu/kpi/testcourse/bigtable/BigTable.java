package edu.kpi.testcourse.bigtable;

// ⚠️ Please, pay attention, that you should not use any 3rd party persistence library (e.g. data
// ⚠️ base, implementation of key-value storage, etc)

import com.google.gson.JsonObject;
import com.nimbusds.jose.shaded.json.JSONObject;

/**
 * BigTable is a key-value storage...
 */
public interface BigTable {
  void putUser(String key, JsonObject value);
  void putUrl(String key, String value);

  JsonObject getUser(String key);
  String getUrl(String key);
}
