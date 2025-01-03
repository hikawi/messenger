package dev.frilly.messenger.server;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import dev.frilly.messenger.api.ApplicationFrame;
import dev.frilly.messenger.server.gui.AppScreen;
import dev.frilly.messenger.server.net.AccountsController;
import dev.frilly.messenger.server.net.GroupsController;
import dev.frilly.messenger.server.net.MessagesController;

import javax.swing.*;

/**
 * Main entrypoint of the server side.
 */
public class Entrypoint {

  /**
   * The main function.
   *
   * @param args arguments
   */
  public static void main(String[] args) {
    AccountsController.load();
    GroupsController.load();
    MessagesController.load();

    SwingUtilities.invokeLater(() -> {
      FlatMacLightLaf.setup();
      final var serverFrame = new ApplicationFrame("Messenger (Server)");
      serverFrame.addCloseHook(() -> {
        System.out.println("Saving");
        AccountsController.save();
        GroupsController.save();
        MessagesController.save();
      });

      ServerContext.setFrame(serverFrame);

      final var welcomeScreen = new AppScreen();
      serverFrame.push(welcomeScreen);
      serverFrame.display();
    });
  }

}
