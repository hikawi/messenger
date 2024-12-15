package dev.frilly.messenger.api.net;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 * A wrapper class for a HTTP response.
 */
@Data
public class HttpResponse {

  private final int      code;
  private final JsonNode body;

}
