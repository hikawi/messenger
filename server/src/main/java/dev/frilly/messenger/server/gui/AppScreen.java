package dev.frilly.messenger.server.gui;

import dev.frilly.messenger.api.component.Components;
import dev.frilly.messenger.api.gui.LayoutBuilder;
import dev.frilly.messenger.api.net.SocketHandler;
import dev.frilly.messenger.server.ServerContext;
import dev.frilly.messenger.server.net.FileSendSocketHandler;
import dev.frilly.messenger.server.net.FileSocketHandler;
import dev.frilly.messenger.server.net.RestParser;
import lombok.SneakyThrows;

import javax.swing.*;
import java.net.ServerSocket;

/**
 * Entrypoint screen of the server side app.
 */
public final class AppScreen extends JPanel {

  private final JLabel  label = Components.label("Server").h0().build();
  private final JButton quit  = Components.button("Quit").rounded().build();

  /**
   * Constructs a new Server App Screen.
   */
  public AppScreen() {
    setupSocket();
    setup();
    setupActions();
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
        while (true) {
          final var client  = wsSocket.accept();
          final var handler = new SocketHandler(client);

          ServerContext.getWsHandlers().add(handler);
          handler.setOnClose(
              () -> ServerContext.getWsHandlers().remove(handler));
          handler.setConsumer(this::handleCommand);
          handler.start();
        }
      } catch (final Exception exception) {
        exception.printStackTrace();
      }
    }).start();

    // Thread for file receiving socket
    new Thread(() -> {
      try (var fileSocket = new ServerSocket(8082)) {
        while (true) {
          final var client  = fileSocket.accept();
          final var handler = new FileSocketHandler(client);
          handler.start();
        }
      } catch (final Exception exception) {
        exception.printStackTrace();
      }
    }).start();

    // Thread for file sending away socket
    new Thread(() -> {
      try (var sendSocket = new ServerSocket(8083)) {
        while (true) {
          final var client  = sendSocket.accept();
          final var handler = new FileSendSocketHandler(client);
          handler.start();
        }
      } catch (final Exception exception) {
        exception.printStackTrace();
      }
    }).start();
  }

  private void setup() {
    final var l = new LayoutBuilder(this).gaps().border(24);
    l.hoz(l.centerPara().comp(label).comp(quit));
    l.ver(l.seq().comp(label).gap(16).comp(quit));
  }

  private void setupActions() {
    quit.addActionListener(e -> {
      final var frame = ServerContext.getFrame();
      frame.quit();
    });
  }

  private void handleCommand(final String cmd) {

  }

}
