package dev.frilly.messenger.client.gui;

import dev.frilly.messenger.api.Icon;
import dev.frilly.messenger.api.component.Components;
import dev.frilly.messenger.api.gui.LayoutBuilder;
import dev.frilly.messenger.client.AppContext;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

/**
 * The starter screen for logging in when starting up Client Messenger.
 */
public final class LoginScreen extends JPanel {

  private final JLabel title = Components.label("Messenger").h0().build();
  private final JLabel login = Components.label("Login").h3().build();

  private final JLabel     username      = Components.label("Username").build();
  private final JTextField usernameField = Components.textField()
      .showClear()
      .prefSize(300, 20)
      .build();
  private final JLabel     password      = Components.label("Password").build();
  private final JTextField passwordField = Components.passwordField().build();

  private final JLabel status = Components.label("").fg(Color.RED).build();

  private final JButton loginButton    = Components.button("Login")
      .icon(Icon.LOGIN, 10)
      .build();
  private final JButton registerButton = Components.button("Register")
      .icon(Icon.REGISTER, 10)
      .build();

  /**
   * Sets up a constructor for the starter screen.
   */
  public LoginScreen() {
    setup();
    setupActions();
  }

  private void setup() {
    final var l = new LayoutBuilder(this).border(32);

    l.ver(l.seq()
        .comp(title)
        .gap(24)
        .comp(login)
        .gap(24)
        .group(l.basePara().comp(username).comp(usernameField))
        .group(l.basePara().comp(password).comp(passwordField))
        .gap(8)
        .comp(status)
        .gap(8)
        .group(l.basePara().comp(loginButton).comp(registerButton)));

    l.hoz(l.centerPara()
        .comp(title)
        .comp(login)
        .group(l.seq()
            .group(l.trailingPara().comp(username).comp(password))
            .gap(8)
            .group(l.leadingPara().comp(usernameField).comp(passwordField)))
        .comp(status)
        .group(l.seq().comp(loginButton).gap(4).comp(registerButton)));

    l.link(usernameField, passwordField);
    l.link(loginButton, registerButton);
  }

  private void setupActions() {
    loginButton.addActionListener(e -> {
      final var regex = Pattern.compile("^[A-Za-z-_][A-Za-z-_0-9]+$");
      if (!regex.asPredicate().test(usernameField.getText())) {
        status.setText("Username is invalid.");
        return;
      }
      if (!regex.asPredicate().test(passwordField.getText())) {
        status.setText("Password is invalid.");
        return;
      }

      final var restHandler = AppContext.getRestHandler();
      final var res = restHandler.query(
          "login %s %s".formatted(usernameField.getText(),
              passwordField.getText()));

      final var code = res.split(" ")[0];
      if (code.equals("404")) {
        status.setText("That account doesn't exist");
        return;
      }

      if (code.equals("401")) {
        status.setText("Incorrect password.");
        return;
      }

      AppContext.setUsername(usernameField.getText());
      AppContext.setPassword(passwordField.getText());
      final var frame = AppContext.getFrame();
      frame.replace(new AppScreen());
    });

    registerButton.addActionListener(e -> {
      final var frame = AppContext.getFrame();
      frame.replace(new RegisterScreen());
    });
  }

}
