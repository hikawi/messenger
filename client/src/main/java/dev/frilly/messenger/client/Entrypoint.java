package dev.frilly.messenger.client;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import dev.frilly.messenger.api.ApplicationFrame;
import dev.frilly.messenger.api.net.RestHandler;
import dev.frilly.messenger.api.net.SocketHandler;
import dev.frilly.messenger.client.gui.LoginScreen;
import lombok.SneakyThrows;

import javax.swing.*;
import java.net.Socket;

/**
 * The entrypoint of the client app.
 */
public class Entrypoint {

  /**
   * Main function.
   *
   * @param args the args
   */
  @SneakyThrows
  public static void main(String[] args) {
    final var restSocket = new Socket("localhost", 8080);
    final var wsSocket   = new Socket("localhost", 8081);

    final var restHandler = new RestHandler(restSocket);
    final var wsHandler   = new SocketHandler(wsSocket);
    AppContext.setRestHandler(restHandler);
    AppContext.setWsHandler(wsHandler);

    SwingUtilities.invokeLater(() -> {
      FlatMacLightLaf.setup();
      final var frame = new ApplicationFrame("Messenger");
      AppContext.setFrame(frame);

      final var loginScreen = new LoginScreen();
      frame.push(loginScreen);
      frame.display();
    });
  }

}
