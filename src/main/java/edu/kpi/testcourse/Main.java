package edu.kpi.testcourse;

import com.google.gson.Gson;
import edu.kpi.testcourse.bigtable.BigTableImpl;
import edu.kpi.testcourse.bigtable.UrlRepositoryFile;
import edu.kpi.testcourse.bigtable.UserRepositoryFile;
import edu.kpi.testcourse.serialization.JsonToolJacksonImpl;
import io.micronaut.runtime.Micronaut;
import java.io.File;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a main entry point to the URL shortener.
 *
 * <p>It creates, connects and starts all system parts.
 */
public class Main {
  private static final Gson gson = new Gson();
  private static final Logger logger = LoggerFactory.getLogger(Main.class);
  public static BigTableImpl bigTable = new BigTableImpl();

  /**
   * Main method in application.
   *
   * @param args input arguments
   */
  public static void main(String[] args) {
    logger.info("Hello world!");
    File usersDataBaseFile = new File("./user-repository.json");
    File urlsDataBaseFile = new File("./url-repository.json");
    if (usersDataBaseFile.exists()) {
      BigTableImpl.users = UserRepositoryFile.readUsersFromJsonDatabaseFile(
        new JsonToolJacksonImpl(), UserRepositoryFile.makeJsonFilePath(Path.of("./")));
    }
    if (urlsDataBaseFile.exists()) {
      BigTableImpl.urls = UrlRepositoryFile.readUrlsFromJsonDatabaseFile(new JsonToolJacksonImpl(),
        UrlRepositoryFile.makeJsonFilePath(Path.of("./")));
    }
    Micronaut.run(Main.class, args);
  }

  public static Gson getGson() {
    return gson;
  }
}
