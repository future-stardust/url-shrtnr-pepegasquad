package edu.kpi.testcourse.rest;

import edu.kpi.testcourse.Main;
import edu.kpi.testcourse.bigtable.BigTableImpl;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

/**
 * REST API controller that provides logic for Micronaut framework.
 */
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller
public class ApiController {
  record ExampleClass(String first, String second) {}

  @Get(value = "/hello", produces = MediaType.APPLICATION_JSON)
  public MutableHttpResponse<String> hello(@Header("Authorization") String token) {
    if(BigTableImpl.tokens.contains(token.split(" ")[1])) {
      return HttpResponse.created(Main.getGson().toJson(new ExampleClass("Hello", "world!")));
    } else {
      return HttpResponse.unauthorized();
    }
  }
}
