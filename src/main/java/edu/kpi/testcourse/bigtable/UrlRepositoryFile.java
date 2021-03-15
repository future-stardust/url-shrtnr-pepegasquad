package edu.kpi.testcourse.bigtable;

import com.google.gson.reflect.TypeToken;
import edu.kpi.testcourse.serialization.JsonTool;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for serialization urls to file on disk.
 */
public class UrlRepositoryFile {
  public static Path makeJsonFilePath(Path storageRoot) {
    return storageRoot.resolve("url-repository.json");
  }

  /**
   * Method for reading file with urls from disk.
   *
   * @param jsonTool tool for serialization
   * @param sourceFilePath file path
   *
   * @return map with urls
   */
  public static Map<String, String> readUrlsFromJsonDatabaseFile(
      JsonTool jsonTool, Path sourceFilePath) {
    String json;
    try {
      json = Files.readString(sourceFilePath, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Type type = new TypeToken<HashMap<String, String>>(){}.getType();
    Map<String, String> result = jsonTool.fromJson(json, type);
    if (result == null) {
      throw new RuntimeException("Could not deserialize the user repository");
    }
    return result;
  }

  /**
   * Method for writing data about urls to file on disk.
   *
   * @param jsonTool tool for serialization
   * @param destinationFilePath file path
   */
  public static void writeUrlsToJsonDatabaseFile(
      JsonTool jsonTool, Path destinationFilePath) {
    String json = jsonTool.toJson(BigTableImpl.urls);
    try {
      Files.writeString(destinationFilePath, json);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
