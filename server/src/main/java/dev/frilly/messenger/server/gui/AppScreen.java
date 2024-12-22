package dev.frilly.messenger.server.gui;

import dev.frilly.messenger.api.component.Components;
import dev.frilly.messenger.api.gui.LayoutBuilder;
import dev.frilly.messenger.api.net.SocketHandler;
import dev.frilly.messenger.server.net.RestParser;
import lombok.SneakyThrows;

import javax.swing.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * Entrypoint screen of the server side app.
 */
public final class AppScreen extends JPanel {

  private final Set<Socket> webSockets = new HashSet<>();

  public AppScreen() {
    setupSocket();
    setup();
  }

  @SneakyThrows
  private void setupSocket() {
    new Thread(() -> {
      try (var restSocket = new ServerSocket(8080)) {
        while (true) {
          final var client  = restSocket.accept();
          final var handler = new SocketHandler(client);
          final var parser  = new RestParser(handler);
          handler.setConsumer(parser::handle);
          handler.start();
        }
      } catch (final Exception exception) {
        exception.printStackTrace();
      }
    }).start();

    // Thread for the WS socket :8081
    new Thread(() -> {
      try (var wsSocket = new ServerSocket(8081)) {
        final var client  = wsSocket.accept();
        final var handler = new SocketHandler(client);

        webSockets.add(client);
        handler.setOnClose(() -> webSockets.remove(client));
        handler.setConsumer(this::handleCommand);
        handler.start();
      } catch (final Exception exception) {
        exception.printStackTrace();
      }
    }).start();
  }

  private void setup() {
    final var l     = new LayoutBuilder(this).gaps().border(24);
    final var label = Components.label("Server").h0().build();

    l.hoz(l.seq().comp(label));
    l.ver(l.centerPara().comp(label));
  }

  private void handleCommand(final String cmd) {

  }

}
