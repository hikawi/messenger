package dev.frilly.messenger.server.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * The user account.
 */
@Data
@Entity
public class Account {

  public String username;

  @Id
  public String email;

  public String password;

}
