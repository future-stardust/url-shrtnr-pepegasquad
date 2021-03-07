package edu.kpi.testcourse.rest;

import edu.kpi.testcourse.Main;
import edu.kpi.testcourse.bigtable.BigTableImpl;
import edu.kpi.testcourse.logic.UserActions;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * REST API controller that provides logic for Micronaut framework.
 */
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller
public class RedirectController {
  record ExampleClass(String first, String second) {}

  /**
   * Method that process requests for redirecting you on full link.
   *
   * @param shortenedUrl provided shortened URL
   * @return response code
   */
  @Get(value = "/r/{shortenedUrl}", produces = MediaType.APPLICATION_JSON)
  public MutableHttpResponse<String> redirect(@Body String shortenedUrl) {
    String fullUrl = UserActions.retrieveUrl(shortenedUrl);

    if (fullUrl == null) {
      return HttpResponse.notFound("Shortened url with name '" + shortenedUrl + "' not found");
    }

    try {
      URI location = new URI(fullUrl);
      return HttpResponse.redirect(location);
    } catch (URISyntaxException e1) {
      e1.printStackTrace();
      return HttpResponse.badRequest();
    }
  }
}
