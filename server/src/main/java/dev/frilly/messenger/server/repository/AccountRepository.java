package dev.frilly.messenger.server.repository;

import dev.frilly.messenger.server.models.Account;
import org.springframework.data.repository.CrudRepository;

/**
 * CRUD Repository for accounts.
 */
public interface AccountRepository extends CrudRepository<Account, Long> {

  Account findByUsername(final String username);

}
