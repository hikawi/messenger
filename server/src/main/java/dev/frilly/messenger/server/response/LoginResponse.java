package dev.frilly.messenger.server.response;

import lombok.Data;

/**
 * The response body for a successful /login.
 */
@Data(staticConstructor = "of")
public class LoginResponse {

  private final String username;
  private final String token;

}
