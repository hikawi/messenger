package dev.frilly.messenger.api.component;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;

/**
 * Represents a builder for a {@link JTextField}.
 */
public final class TextFieldBuilder extends ComponentBuilder<TextFieldBuilder> {

  private final JTextField field;

  /**
   * Creates a new text field.
   *
   * @param component the text field.
   */
  public TextFieldBuilder(final JTextField component) {
    super(component);
    this.field = component;
  }

  /**
   * Sets the placeholder for the field.
   *
   * @param value the value
   *
   * @return this
   */
  public TextFieldBuilder placeholder(final String value) {
    field.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, value);
    return this;
  }

  /**
   * Sets the text field to display a "clear" button.
   *
   * @return this
   */
  public TextFieldBuilder showClear() {
    field.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON,
        true);
    return this;
  }

  /**
   * Builds out the text field.
   *
   * @return the field.
   */
  public JTextField build() {
    return field;
  }

}
