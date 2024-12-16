package dev.frilly.messenger.server.repository;

import dev.frilly.messenger.server.models.Account;
import org.springframework.data.repository.CrudRepository;

/**
 * CRUD Repository for accounts.
 */
public interface AccountRepository extends CrudRepository<Account, Long> {

  /**
   * Finds an account by their username.
   *
   * @param username the username
   *
   * @return the account if found
   */
  Account findByUsername(final String username);

}
