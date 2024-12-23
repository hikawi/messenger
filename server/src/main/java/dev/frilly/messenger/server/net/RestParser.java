package dev.frilly.messenger.server.net;

import dev.frilly.messenger.api.net.SocketHandler;
import dev.frilly.messenger.server.ServerContext;

import java.util.Arrays;

/**
 * A sub-class for parsing and reading REST-like requests.
 */
public final class RestParser {

  private final SocketHandler socket;

  /**
   * Creates a new parser for writing into the socket.
   *
   * @param handler the handler
   */
  public RestParser(final SocketHandler handler) {
    this.socket = handler;
  }

  /**
   * Handles the provided command.
   *
   * @param cmd the cmd
   */
  public void handle(final String cmd) {
    System.out.println("Caught cmd " + cmd);
    if (cmd == null) {
      return;
    }

    final var args = cmd.split(" ");
    if (args.length == 0) {
      return;
    }

    switch (args[0].toLowerCase()) {
      case "login":
        handleLogin(args);
        break;
      case "register":
        handleRegister(args);
        break;
      case "sendmessage":
        handleSendMessage(args);
        break;
    }
  }

  private void handleLogin(final String[] args) {
    if (args.length < 2) {
      return;
    }

    final var user = args[1];
    final var pass = args[2];
    final var acc  = AccountsController.matches(user, pass);

    if (!AccountsController.isRegistered(user)) {
      socket.write("404 not found\n");
      return;
    }

    if (acc.isEmpty()) {
      socket.write("401 unauthorized\n");
      return;
    }

    socket.write("200 ok\n");
  }

  private void handleRegister(final String[] args) {
    if (args.length < 2) {
      return;
    }

    final var user = args[1];
    final var pass = args[2];

    if (AccountsController.isRegistered(user)) {
      socket.write("409 conflict\n");
      return;
    }

    AccountsController.register(user, pass);
    socket.write("201 created\n");
  }

  private void handleSendMessage(final String[] args) {
    if (args.length < 4) {
      return;
    }

    final var username = args[1];
    final var group    = args[2];
    final var content = String.join(" ",
        Arrays.copyOfRange(args, 3, args.length));

    socket.write("201 ok\n");
    final var sockets = ServerContext.getWsHandlers();
    sockets.forEach(s -> {
      s.write("sendmessage %s %s %s".formatted(username, group, content));
    });
  }

}
