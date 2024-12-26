package dev.frilly.messenger.server.net;

import dev.frilly.messenger.api.net.SocketHandler;
import dev.frilly.messenger.server.ServerContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

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
    if (cmd == null) {
      socket.close();
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
      case "check":
        handleCheck(args);
        break;
      case "newgroup":
        handleNewGroup(args);
        break;
      case "getgroup":
        handleGetGroup(args);
        break;
      case "getgroups":
        handleGetGroups(args);
        break;
      default:
        break;
    }
  }

  private void handleLogin(final String[] args) {
    if (args.length < 3) {
      return;
    }

    final var user = args[1];
    final var pass = args[2];
    final var acc  = AccountsController.matches(user, pass);

    if (!AccountsController.isRegistered(user)) {
      socket.write("404 not found");
      return;
    }

    if (acc.isEmpty()) {
      socket.write("401 unauthorized");
      return;
    }

    socket.write("200 ok");
  }

  private void handleRegister(final String[] args) {
    if (args.length < 2) {
      return;
    }

    final var user = args[1];
    final var pass = args[2];

    if (AccountsController.isRegistered(user)) {
      socket.write("409 conflict");
      return;
    }

    AccountsController.register(user, pass);
    socket.write("201 created");
  }

  private void handleSendMessage(final String[] args) {
    if (args.length < 4) {
      return;
    }

    final var username = args[1];
    final var group    = args[2];
    final var content = String.join(" ",
        Arrays.copyOfRange(args, 3, args.length));
    socket.write("201 ok");
    final var sockets = ServerContext.getWsHandlers();
    sockets.forEach(s -> {
      s.write("sendmessage %s %s %s".formatted(username, group, content));
    });
  }

  private void handleCheck(final String[] args) {
    if (args.length < 2) {
      return;
    }

    final var user = args[1];
    final var acc  = AccountsController.isRegistered(user);
    socket.write(acc ? "yes" : "no");
  }

  private void handleNewGroup(final String[] args) {
    if (args.length < 2) {
      return;
    }

    final var usernames = Arrays.copyOfRange(args, 1, args.length);
    final var group     = GroupsController.createGroup();
    group.setMembers(new HashSet<>(Arrays.asList(usernames)));
    socket.write("201 " + group.getUuid().toString());

    final var s = "newgroup %s".formatted(group.getUuid().toString());
    for (final var ws : ServerContext.getWsHandlers()) {
      ws.write(s);
    }
  }

  private void handleGetGroup(final String[] args) {
    if (args.length < 2) {
      return;
    }

    final var groupId = args[1];
    final var group   = GroupsController.getGroup(UUID.fromString(groupId));
    if (group.isEmpty()) {
      socket.write("404");
      return;
    }

    final var sb = new StringBuilder("200");
    for (final var member : group.get().getMembers()) {
      sb.append(" ").append(member);
    }
    socket.write(sb.toString());
  }

  private void handleGetGroups(final String[] args) {
    if (args.length < 2) {
      return;
    }

    final var user = args[1];
    final var sb   = new StringBuilder("200");
    for (final var group : GroupsController.getGroupChatsOf(user)) {
      sb.append(" ").append(group.getUuid().toString());
    }
    System.out.printf("Received getGroup commands for %s: %s\n", args[0], sb);
    socket.write(sb.toString());
  }

}
