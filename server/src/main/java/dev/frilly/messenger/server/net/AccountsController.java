package dev.frilly.messenger.server.net;

import dev.frilly.messenger.server.data.Account;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controller for accounts.
 */
@UtilityClass
public final class AccountsController {

  private final Map<String, Account> accountMap = new HashMap<>();

  /**
   * Registers an account.
   *
   * @param username the username
   * @param password the password
   *
   * @return the account created
   */
  public Account register(final String username, final String password) {
    final var account = new Account(username, password);
    accountMap.put(username.toLowerCase(), account);
    return account;
  }

  /**
   * Checks if the provided credentials match any account.
   *
   * @param username the username
   * @param password the password
   *
   * @return an account optional, present if matches.
   */
  public Optional<Account> matches(
      final String username,
      final String password
  ) {
    if (!isRegistered(username)) {
      return Optional.empty();
    }

    final var acc = accountMap.get(username.toLowerCase());
    if (acc.getPassword().equals(password)) {
      return Optional.of(acc);
    }
    return Optional.empty();
  }

  /**
   * Checks if a username is registered.
   *
   * @param username the username
   *
   * @return true if it is registered
   */
  public boolean isRegistered(final String username) {
    return accountMap.containsKey(username.toLowerCase());
  }

  /**
   * Loads saved accounts registry.
   */
  @SneakyThrows
  public void load() {
    final var dataFolder   = new File(".data");
    final var accountsFile = new File(dataFolder, "accounts.bin");
    if (!accountsFile.exists()) {
      return;
    }

    final @Cleanup var input = new DataInputStream(
        new BufferedInputStream(new FileInputStream(accountsFile)));
    final var length = input.readInt();
    for (int i = 0; i < length; i++) {
      final var account = new Account(input.readUTF(), input.readUTF());
      accountMap.put(account.getUsername().toLowerCase(), account);
    }
  }

  /**
   * Saves all currently available accounts.
   */
  @SneakyThrows
  public void save() {
    final var dataFolder   = new File(".data");
    final var accountsFile = new File(dataFolder, "accounts.bin");
    if (!accountsFile.exists()) {
      dataFolder.mkdirs();
      accountsFile.createNewFile();
    }

    final @Cleanup var output = new DataOutputStream(
        new BufferedOutputStream(new FileOutputStream(accountsFile)));
    output.writeInt(accountMap.size());
    for (final var entry : accountMap.entrySet()) {
      output.writeUTF(entry.getValue().getUsername());
      output.writeUTF(entry.getValue().getPassword());
    }
  }

}
