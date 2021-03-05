package edu.kpi.testcourse.model;

import edu.kpi.testcourse.Main;
import edu.kpi.testcourse.logic.UserActions;

/**
 * Model that represents URL.
 */
public class Url {
  private String shortenedUrl;
  private String fullUrl;
  private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static final int BASE = ALPHABET.length();

  public Url (String url) {
    this.fullUrl = url;
  }

  public String getFullUrl() {
    return fullUrl;
  }
  public String getShortenedUrl() {
    return shortenedUrl;
  }

  public static String shortenUrl(Integer id) {
    StringBuilder sb = new StringBuilder();
    while (id > 0) {
      sb.append(ALPHABET.charAt(id % BASE));
      id /= BASE;
    }
    return sb.reverse().toString();
  }

  public void setShortenedUrl (String shortenedUrl) {
    this.shortenedUrl = shortenedUrl;
  }
}
