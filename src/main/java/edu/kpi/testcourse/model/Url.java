package edu.kpi.testcourse.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.kpi.testcourse.Main;
import edu.kpi.testcourse.bigtable.BigTableImpl;
import edu.kpi.testcourse.logic.UserActions;

/**
 * Model that represents URL.
 */
public class Url {
  private String shortenedUrl;
  private String fullUrl;
  private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz"
      + "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static final int BASE = ALPHABET.length();

  public Url(String url) {
    this.fullUrl = url;
  }

  public String getFullUrl() {
    return fullUrl;
  }

  public String getShortenedUrl() {
    return shortenedUrl;
  }

  /**
   * Method that provides logic of shortening.
   *
   * @param id URL identifier
   * @return shortened URL
   */
  public static String shortenUrl(Integer id) {
    StringBuilder sb;
    int copyId = id;
    do {
      id = copyId;
      sb = new StringBuilder("");
      while (id > 0) {
        sb.append(ALPHABET.charAt(id % BASE));
        id /= BASE;
      }
      copyId++;
    } while (BigTableImpl.urls.containsKey(sb.reverse().toString()));
    BigTableImpl.urlId = copyId - 1;
    return sb.reverse().toString();
  }

  /**
   * Convert Url to Json format.
   *
   * @return  object in json format
   */
  public JsonObject toJson() {
    String json =  Main.getGson().toJson(this, Url.class);

    return JsonParser.parseString(json).getAsJsonObject();
  }

  public void setShortenedUrl(String shortenedUrl) {
    this.shortenedUrl = shortenedUrl;
  }
}
