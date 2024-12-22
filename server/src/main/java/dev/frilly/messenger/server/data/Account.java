package dev.frilly.messenger.server.data;

import lombok.Data;

/**
 * Data class for an account.
 */
@Data
public final class Account {

  private final String username;
  private final String password;

}
