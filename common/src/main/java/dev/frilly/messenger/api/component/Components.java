package dev.frilly.messenger.api.component;

import javax.swing.*;

/**
 * Utility class for generating quick and pre-configured components.
 */
public final class Components {

  /**
   * The magic property string for FlatLaf.
   */
  public static String FLATLAF = "FlatLaf.styleClass";

  private Components() {
    // Intentionally left blank.
  }

  /**
   * Constructs a new label.
   *
   * @param value the value
   *
   * @return the label builder
   */
  public static LabelBuilder label(final String value) {
    return new LabelBuilder(new JLabel(value));
  }

  /**
   * Constructs a new button.
   *
   * @param value the value
   *
   * @return the button builder
   */
  public static ButtonBuilder button(final String value) {
    return new ButtonBuilder(new JButton(value));
  }

  /**
   * Constructs a new empty text field.
   *
   * @return the text field builder
   */
  public static TextFieldBuilder textField() {
    return textField("");
  }

  /**
   * Constructs a new text field.
   *
   * @param value the value
   *
   * @return the text field builder
   */
  public static TextFieldBuilder textField(final String value) {
    return new TextFieldBuilder(new JTextField(value));
  }

  /**
   * Constructs a new empty password field.
   *
   * @return the password field builder.
   */
  public static PasswordFieldBuilder passwordField() {
    return new PasswordFieldBuilder(new JPasswordField());
  }

}
