package dev.frilly.messenger.server.gui;

import dev.frilly.messenger.api.ApplicationFrame;
import dev.frilly.messenger.api.Icon;
import dev.frilly.messenger.api.component.Components;
import dev.frilly.messenger.api.gui.LayoutBuilder;
import dev.frilly.messenger.server.ServerContext;

import javax.swing.*;
import java.awt.*;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The welcome screen for the server side of Messenger.
 */
public final class WelcomeScreen extends JPanel {

  private final JLabel welcomeLabel     = Components.label("Messenger (server)")
      .h0()
      .build();
  private final JLabel instructionLabel = Components.label(
      "Please connect a PostgreSQL server").h3().build();

  private final JLabel host   = Components.label("Hostname").build();
  private final JLabel port   = Components.label("Port").build();
  private final JLabel name   = Components.label("Database").build();
  private final JLabel user   = Components.label("Username").build();
  private final JLabel pass   = Components.label("Password").build();
  private final JLabel status = Components.label("Status").build();

  private final JTextField hostField   = Components.textField("")
      .prefSize(300, 20)
      .placeholder("localhost")
      .showClear()
      .build();
  private final JTextField portField   = Components.textField("")
      .placeholder("5432")
      .showClear()
      .build();
  private final JTextField nameField   = Components.textField("")
      .showClear()
      .build();
  private final JTextField userField   = Components.textField("")
      .showClear()
      .build();
  private final JTextField passField   = Components.passwordField().build();
  private final JLabel     statusField = Components.label("NOT CONNECTED")
      .h3()
      .build();

  private final JButton tryButton   = Components.button("Try")
      .icon(Icon.WIFI, 10)
      .rounded()
      .build();
  private final JButton quitButton  = Components.button("Quit")
      .icon(Icon.CLOSE, 10)
      .rounded()
      .build();
  private final JButton startButton = Components.button("Start")
      .icon(Icon.START, 10)
      .rounded()
      .build();

  /**
   * Constructs a new instance of the welcome screen.
   *
   * @param frame the frame to push onto.
   */
  public WelcomeScreen(final ApplicationFrame frame) {
    setup();
    setupActions();
  }

  private void setup() {
    final var l = new LayoutBuilder(this).gaps();
    l.border(24);

    l.hoz(l.centerPara()
        .comp(welcomeLabel)
        .comp(instructionLabel)
        .group(l.seq()
            .group(l.trailingPara()
                .comp(host)
                .comp(port)
                .comp(name)
                .comp(user)
                .comp(pass))
            .gap(8)
            .group(l.leadingPara()
                .comp(hostField)
                .comp(portField)
                .comp(nameField)
                .comp(userField)
                .comp(passField)))
        .group(l.seq().comp(status).gap(8).comp(statusField))
        .group(l.seq()
            .comp(tryButton)
            .gap(4)
            .comp(quitButton)
            .gap(4)
            .comp(startButton)));

    l.ver(l.seq()
        .comp(welcomeLabel)
        .gap(32)
        .comp(instructionLabel)
        .gap(16)
        .group(l.basePara().comp(host).comp(hostField))
        .group(l.basePara().comp(port).comp(portField))
        .group(l.basePara().comp(name).comp(nameField))
        .group(l.basePara().comp(user).comp(userField))
        .group(l.basePara().comp(pass).comp(passField))
        .gap(8)
        .group(l.basePara().comp(status).comp(statusField))
        .gap(16)
        .group(
            l.basePara().comp(tryButton).comp(quitButton).comp(startButton)));

    userField.setPreferredSize(new Dimension(300, 20));
    l.link(userField, hostField, portField, nameField, passField);
    l.link(tryButton, quitButton, startButton);
  }

  private void setupActions() {
    startButton.setEnabled(false);
    tryButton.addActionListener(e -> {
      tryButton.setEnabled(false);

      final var url = "jdbc:postgresql://%s:%s/%s".formatted(
          hostField.getText(), portField.getText(), nameField.getText());
      try {
        DriverManager.getConnection(url, userField.getText(),
            passField.getText());
        statusField.setText("Yay");

        // Setup database property.
        System.setProperty("db.url", url);
        System.setProperty("db.username", userField.getText());
        System.setProperty("db.password", passField.getText());

        startButton.setEnabled(true);
      } catch (SQLException ex) {
        ex.printStackTrace();
        statusField.setText("Failed");
      } finally {
        tryButton.setEnabled(true);
      }
    });

    startButton.addActionListener(e -> {
      final var frame     = ServerContext.getFrame();
      final var appScreen = new AppScreen();
      frame.push(appScreen);
    });

    quitButton.addActionListener(e -> {
      final var frame = ServerContext.getFrame();
      frame.quit();
    });
  }

}
