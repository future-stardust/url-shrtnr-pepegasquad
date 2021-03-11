package edu.kpi.testcourse.bigtable;

// ⚠️ Please, pay attention, that you should not use any 3rd party persistence library (e.g. data
// ⚠️ base, implementation of key-value storage, etc)

import com.google.gson.JsonObject;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import java.util.Set;

/**
 * BigTable is a key-value storage...
 */
public interface BigTable {
  void putUser(String key, JsonObject value);

  void putUrl(String key, String value);

  void putToken(String token);

  Set<String> getTokens();

  Integer getUrlId();

  JsonObject getUser(String key);

  String getUrl(String key);

  void deleteUrl(String key);
}
