package dev.frilly.messenger.client;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import dev.frilly.messenger.api.ApplicationFrame;
import dev.frilly.messenger.client.gui.LoginScreen;

import javax.swing.*;

/**
 * The entrypoint of the client app.
 */
public class Entrypoint {

  /**
   * Main function.
   *
   * @param args the args
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      FlatMacLightLaf.setup();
      final var frame = new ApplicationFrame("Messenger");
      new LoginScreen(frame);
      frame.display();
    });
  }

}
