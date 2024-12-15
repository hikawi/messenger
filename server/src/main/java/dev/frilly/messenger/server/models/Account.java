package dev.frilly.messenger.server.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * The user account.
 */
@Data
@Entity
public class Account {

  @Id
  @GeneratedValue
  @Setter(AccessLevel.NONE)
  private long id;

  private String username;
  private String password;

}
