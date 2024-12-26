package dev.frilly.messenger.client;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import dev.frilly.messenger.api.ApplicationFrame;
import dev.frilly.messenger.api.data.ChatMessage;
import dev.frilly.messenger.api.data.FileMessage;
import dev.frilly.messenger.api.data.GroupChat;
import dev.frilly.messenger.api.net.RestHandler;
import dev.frilly.messenger.api.net.SocketHandler;
import dev.frilly.messenger.client.gui.LoginScreen;
import lombok.SneakyThrows;

import javax.swing.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The entrypoint of the client app.
 */
public final class Entrypoint {

  /**
   * Main function.
   *
   * @param args the args
   */
  @SneakyThrows
  public static void main(String[] args) {
    final var restSocket = new Socket("localhost", 8080,
        InetAddress.getLocalHost(), 0);
    final var wsSocket = new Socket("localhost", 8081,
        InetAddress.getLocalHost(), 0);

    final var restHandler = new RestHandler(restSocket);
    final var wsHandler   = new SocketHandler(wsSocket);
    AppContext.setRestHandler(restHandler);
    AppContext.setWsHandler(wsHandler);
    AppContext.setMessageRepository(new MessageRepository());
    AppContext.setGroupRepository(new GroupRepository());

    wsHandler.setConsumer(s -> {
      if (s == null) {
        return;
      }

      final var cmdArgs = s.split(" ");
      switch (cmdArgs[0].toLowerCase()) {
        case "sendmessage":
          handleSendMessage(cmdArgs);
          break;
        case "sendfile":
          handleSendFile(cmdArgs);
          break;
        case "deletemessage":
          handleDeleteMessage(cmdArgs);
          break;
        case "newgroup":
          handleNewGroup(cmdArgs);
          break;
        case "sendmessagehistory":
          handleSendMessageHistory(cmdArgs);
          break;
        case "sendfilehistory":
          handleSendFileHistory(cmdArgs);
          break;
      }
    });

    restHandler.start();
    wsHandler.start();

    SwingUtilities.invokeLater(() -> {
      FlatMacLightLaf.setup();
      final var frame = new ApplicationFrame("Messenger");
      AppContext.setFrame(frame);
      frame.addCloseHook(() -> {
        restHandler.close();
        wsHandler.close();
      });

      final var loginScreen = new LoginScreen();
      frame.push(loginScreen);
      frame.display();
    });
  }

  private static void handleSendMessage(final String[] args) {
    if (args.length < 5) {
      return;
    }

    final var username  = args[1];
    final var group     = args[2];
    final var timestamp = args[3];
    final var content = String.join(" ",
        Arrays.copyOfRange(args, 4, args.length));

    final var message = new ChatMessage();
    message.setUsername(username);
    message.setGroupName(group);
    message.setTimestamp(Long.parseLong(timestamp));
    message.setContent(content);

    // Only accept messages that are in my groups or public.
    if (group.equals("public") || AppContext.getGroupRepository()
        .hasGroup(group)) {
      AppContext.getMessageRepository().addMessage(message);
    }
  }

  private static void handleSendFile(final String[] args) {
    if (args.length < 5) {
      return;
    }

    final var user      = args[1];
    final var group     = args[2];
    final var timestamp = Long.parseLong(args[3]);
    final var path      = args[4];

    final var msg = new FileMessage();
    msg.setUsername(user);
    msg.setGroupName(group);
    msg.setTimestamp(timestamp);
    msg.setFilePath(path);

    final var rest = AppContext.getRestHandler();
    final var res  = rest.query("getfilename %s".formatted(path));
    msg.setFileName(res);

    // Only accept messages that are in my groups or public.
    if (group.equals("public") || AppContext.getGroupRepository()
        .hasGroup(group)) {
      AppContext.getMessageRepository().addMessage(msg);
    }
  }

  private static void handleDeleteMessage(final String[] args) {
    if (args.length < 3) {
      return;
    }

    final var username  = args[1];
    final var group     = args[2];
    final var timestamp = args[3];

    final var msg = new ChatMessage();
    msg.setUsername(username);
    msg.setGroupName(group);
    msg.setTimestamp(Long.parseLong(timestamp));

    AppContext.getMessageRepository().deleteMessage(msg);
  }

  private static void handleNewGroup(final String[] args) {
    if (args.length < 2) {
      return;
    }

    final var group   = args[1];
    final var socket  = AppContext.getRestHandler();
    final var res     = socket.query("getgroup %s".formatted(group));
    final var resArgs = res.split(" ");

    final var groupChat = new GroupChat();
    groupChat.setUuid(UUID.fromString(group));
    groupChat.setMembers(
        Arrays.stream(resArgs).skip(1).collect(Collectors.toSet()));
    AppContext.getGroupRepository().addGroupChat(groupChat);
  }

  private static void handleSendMessageHistory(final String[] args) {
    if (args.length < 6) {
      return;
    }

    final var forUser = args[1];
    if (!forUser.equals(AppContext.getUsername())) {
      return;
    }

    final var username  = args[2];
    final var group     = args[3];
    final var timestamp = args[4];
    final var content = String.join(" ",
        Arrays.copyOfRange(args, 5, args.length));

    final var message = new ChatMessage();
    message.setUsername(username);
    message.setGroupName(group);
    message.setTimestamp(Long.parseLong(timestamp));
    message.setContent(content);

    // Only accept messages that are in my groups or public.
    if (group.equals("public") || AppContext.getGroupRepository()
        .hasGroup(group)) {
      AppContext.getMessageRepository().addMessage(message);
    }
  }

  private static void handleSendFileHistory(final String[] args) {
    if (args.length < 6) {
      return;
    }

    final var forUser = args[1];
    if (!forUser.equals(AppContext.getUsername())) {
      return;
    }

    final var user      = args[2];
    final var group     = args[3];
    final var timestamp = Long.parseLong(args[4]);
    final var path      = args[5];

    final var msg = new FileMessage();
    msg.setUsername(user);
    msg.setGroupName(group);
    msg.setTimestamp(timestamp);
    msg.setFilePath(path);

    final var rest = AppContext.getRestHandler();
    final var res  = rest.query("getfilename %s".formatted(path));
    msg.setFileName(res);

    // Only accept messages that are in my groups or public.
    if (group.equals("public") || AppContext.getGroupRepository()
        .hasGroup(group)) {
      AppContext.getMessageRepository().addMessage(msg);
    }
  }

}
