package dev.frilly.messenger.api.component;

import javax.swing.*;

/**
 * Builder for a {@link JPasswordField}.
 */
public final class PasswordFieldBuilder
    extends ComponentBuilder<PasswordFieldBuilder> {

  private final JPasswordField field;

  /**
   * Constructs a new builder.
   *
   * @param field the password field.
   */
  public PasswordFieldBuilder(final JPasswordField field) {
    super(field);
    this.field = field;
  }

  /**
   * Builds out the text field.
   *
   * @return the field.
   */
  public JPasswordField build() {
    return field;
  }

}
