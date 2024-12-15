package dev.frilly.messenger.server.forms;

import lombok.Data;

/**
 * The request body for a login form.
 */
@Data
public class PostLoginBody {

  private final String username;
  private final String password;

}
