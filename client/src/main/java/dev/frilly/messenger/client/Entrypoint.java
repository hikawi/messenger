package dev.frilly.messenger.client;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import dev.frilly.messenger.api.ApplicationFrame;
import dev.frilly.messenger.api.data.ChatMessage;
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
        case "newgroup":
          handleNewGroup(cmdArgs);
          break;
      }
    });

    restHandler.start();
    wsHandler.start();

    SwingUtilities.invokeLater(() -> {
      FlatMacLightLaf.setup();
      final var frame = new ApplicationFrame("Messenger");
      AppContext.setFrame(frame);

      final var loginScreen = new LoginScreen();
      frame.push(loginScreen);
      frame.display();
    });
  }

  private static void handleSendMessage(final String[] args) {
    if (args.length < 4) {
      return;
    }

    final var username = args[1];
    final var group    = args[2];
    final var content = String.join(" ",
        Arrays.copyOfRange(args, 3, args.length));

    final var message = new ChatMessage();
    message.setUsername(username);
    message.setGroupName(group);
    message.setContent(content);
    AppContext.getMessageRepository().addMessage(message);
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

}
