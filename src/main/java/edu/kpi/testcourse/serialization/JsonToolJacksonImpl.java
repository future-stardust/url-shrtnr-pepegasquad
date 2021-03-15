package edu.kpi.testcourse.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kpi.testcourse.Main;
import java.lang.reflect.Type;

/**
 * JSON serialization tool that uses Jackson as engine.
 */
public class JsonToolJacksonImpl implements JsonTool {

  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public <T> T fromJson(String jsonString, Type type) throws JsonParsingError {
    return Main.getGson().fromJson(jsonString, type);
  }

  @Override
  public <T> T fromJson(String jsonString, Class<T> clazz) {
    try {
      return mapper.readValue(jsonString, clazz);
    } catch (JsonProcessingException e) {
      throw new JsonParsingError(e);
    }
  }

  @Override
  public String toJson(Object obj) {
    return Main.getGson().toJson(obj);
  }
}
