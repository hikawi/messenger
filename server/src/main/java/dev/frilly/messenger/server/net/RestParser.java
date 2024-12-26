package dev.frilly.messenger.server.net;

import dev.frilly.messenger.api.data.ChatMessage;
import dev.frilly.messenger.api.data.FileMessage;
import dev.frilly.messenger.api.data.Message;
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

    System.out.println("Received command " + cmd);
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
      case "deletemessage":
        handleDeleteMessage(args);
        break;
      case "sendfile":
        handleSendFile(args);
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
      case "history":
        handleHistory(args);
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

    final var msg = new ChatMessage();
    msg.setUsername(username);
    msg.setGroupName(group);
    msg.setContent(content);
    msg.setNow();

    // Don't save public history.
    if (!group.equals("public")) {
      MessagesController.saveMessage(msg);
    }

    socket.write("201 ok");
    final var sockets = ServerContext.getWsHandlers();
    sockets.forEach(s -> {
      s.write("sendmessage %s %s %d %s".formatted(username, group,
          msg.getTimestamp(), content));
    });
  }

  private void handleDeleteMessage(final String[] args) {
    if (args.length < 4) {
      return;
    }

    final var user      = args[1];
    final var group     = args[2];
    final var timestamp = args[3];

    final var msg = new Message();
    msg.setUsername(user);
    msg.setGroupName(group);
    msg.setTimestamp(Long.parseLong(timestamp));
    MessagesController.deleteMessage(msg);

    socket.write("200 ok");
    ServerContext.getWsHandlers().forEach(s -> {
      s.write("deletemessage %s %s %s".formatted(user, group, timestamp));
    });
  }

  private void handleSendFile(final String[] args) {
    if (args.length < 2) {
      return;
    }

    final var name = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

    final var fileMsg = new FileMessage();
    fileMsg.setFileName(name);
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
    socket.write(sb.toString());
  }

  private void handleHistory(final String[] args) {
    if (args.length < 2) {
      return;
    }

    final var group = GroupsController.getGroup(UUID.fromString(args[1]));
    if (group.isEmpty()) {
      socket.write("404 not found");
      return;
    }

    socket.write("200 ok");
    final var history = MessagesController.getHistory(args[1]);
    final var wss     = ServerContext.getWsHandlers();
    for (final var ws : wss) {
      new Thread(() -> {
        history.forEach(msg -> {
          if (msg instanceof ChatMessage chat) {
            ws.write("sendmessage %s %s %d %s".formatted(msg.getUsername(),
                msg.getGroupName(), msg.getTimestamp(), chat.getContent()));
          }
        });
      }).start();
    }
  }

}
