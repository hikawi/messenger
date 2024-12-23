package dev.frilly.messenger.client.gui;

import dev.frilly.messenger.api.Icon;
import dev.frilly.messenger.api.component.Components;
import dev.frilly.messenger.api.gui.LayoutBuilder;
import dev.frilly.messenger.client.AppContext;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

/**
 * The screen for registering a new account.
 */
public final class RegisterScreen extends JPanel {

  private final JLabel title    = Components.label("Messenger").h0().build();
  private final JLabel register = Components.label("Register").h3().build();

  private final JLabel     username      = Components.label("Username").build();
  private final JTextField usernameField = Components.textField()
      .showClear()
      .prefSize(300, 20)
      .build();

  private final JLabel     password      = Components.label("Password").build();
  private final JTextField passwordField = Components.passwordField().build();

  private final JLabel     confirm      = Components.label("Confirm Password")
      .build();
  private final JTextField confirmField = Components.passwordField().build();

  private final JLabel status = Components.label("").fg(Color.RED).build();

  private final JButton registerButton = Components.button("Register")
      .icon(Icon.REGISTER, 10)
      .build();
  private final JButton loginButton    = Components.button("Login")
      .icon(Icon.LOGIN, 10)
      .build();

  /**
   * Sets up a register screen.
   */
  public RegisterScreen() {
    setup();
    setupActions();
  }

  private void setup() {
    final var l = new LayoutBuilder(this).border(24).gaps();

    l.ver(l.seq()
        .comp(title)
        .gap(24)
        .comp(register)
        .gap(16)
        .group(l.basePara().comp(username).comp(usernameField))
        .group(l.basePara().comp(password).comp(passwordField))
        .group(l.basePara().comp(confirm).comp(confirmField))
        .gap(8)
        .comp(status)
        .gap(8)
        .group(l.basePara().comp(registerButton).comp(loginButton)));

    l.hoz(l.centerPara()
        .comp(title)
        .comp(register)
        .group(l.seq()
            .group(l.trailingPara().comp(username).comp(password).comp(confirm))
            .gap(8)
            .group(l.leadingPara()
                .comp(usernameField)
                .comp(passwordField)
                .comp(confirmField)))
        .comp(status)
        .group(l.seq().comp(registerButton).comp(loginButton)));

    l.linkX(registerButton, loginButton);
  }

  private void setupActions() {
    registerButton.addActionListener(e -> {
      final var regex    = Pattern.compile("^[A-Za-z-_][A-Za-z-_0-9]+$");
      final var username = usernameField.getText();
      final var password = passwordField.getText();
      final var confirm  = confirmField.getText();

      if (!password.equals(confirm)) {
        status.setText("Passwords don't match.");
        return;
      }

      if (!regex.asPredicate().test(username)) {
        status.setText("Username is invalid.");
        return;
      }

      if (!regex.asPredicate().test(password)) {
        status.setText("Password is invalid.");
        return;
      }

      final var socket = AppContext.getRestHandler();
      final var res = socket.query(
          "register %s %s".formatted(username, password));
      final var code = res.split(" ")[0];

      if (code.equals("409")) {
        status.setText("That username is taken.");
        return;
      }

      AppContext.setUsername(username);
      AppContext.setPassword(password);
      final var frame = AppContext.getFrame();
      frame.push(new AppScreen());
    });

    loginButton.addActionListener(e -> {
      final var frame = AppContext.getFrame();
      frame.replace(new LoginScreen());
    });
  }

}
