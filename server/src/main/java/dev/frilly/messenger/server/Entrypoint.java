package dev.frilly.messenger.server;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import dev.frilly.messenger.api.ApplicationFrame;
import dev.frilly.messenger.server.gui.WelcomeScreen;
import dev.frilly.messenger.server.service.JwtService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.swing.*;

/**
 * Main entrypoint of the server side.
 */
@SpringBootApplication
public class Entrypoint {

  /**
   * The main function.
   *
   * @param args arguments
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      try {
        Class.forName("org.postgresql.Driver");
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }

      FlatMacLightLaf.setup();
      final var serverFrame   = new ApplicationFrame("Messenger (Server)");
      final var welcomeScreen = new WelcomeScreen(serverFrame);
      serverFrame.display();
    });
  }

  /**
   * A Spring bean for bCrypt encoder.
   *
   * @return the bean
   */
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * A Spring bean for the JWT service.
   *
   * @return the service
   */
  @Bean
  public JwtService jwtService() {
    return new JwtService();
  }

}
