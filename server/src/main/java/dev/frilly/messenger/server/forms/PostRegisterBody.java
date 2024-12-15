package dev.frilly.messenger.server.forms;

import lombok.Data;

/**
 * The required body for a register body.
 */
@Data
public class PostRegisterBody {

  private String username;
  private String password;

}
