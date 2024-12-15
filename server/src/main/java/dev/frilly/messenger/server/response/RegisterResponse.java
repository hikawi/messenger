package dev.frilly.messenger.server.response;

import lombok.Data;

/**
 * The response for a successful /register.
 */
@Data(staticConstructor = "of")
public class RegisterResponse {

  private final long   id;
  private final String username;

}
