package dev.frilly.messenger.api.component;

import javax.swing.*;

/**
 * Builder class for a {@link javax.swing.JTextArea}.
 */
public final class TextAreaBuilder extends ComponentBuilder<TextAreaBuilder> {

  private final JTextArea area;

  /**
   * Constructs a new builder for {@link JTextArea}.
   *
   * @param area the area
   */
  public TextAreaBuilder(final JTextArea area) {
    super(area);
    this.area = area;
  }

  /**
   * Turns on line-wrapping for when lines are too long.
   *
   * @return this
   */
  public TextAreaBuilder wrap() {
    area.setLineWrap(true);
    area.setWrapStyleWord(true);
    return this;
  }

  /**
   * Makes this text area un-editable.
   *
   * @return this
   */
  public TextAreaBuilder noEdit() {
    area.setEditable(false);
    return this;
  }

  /**
   * Makes this text area transparent.
   *
   * @return this
   */
  public TextAreaBuilder transparent() {
    area.setOpaque(false);
    return this;
  }

  /**
   * Sets the text area's max char width.
   *
   * @param cols the columns
   *
   * @return this
   */
  public TextAreaBuilder cols(final int cols) {
    area.setColumns(cols);
    return this;
  }

  /**
   * Returns the built text area.
   *
   * @return the text area
   */
  public JTextArea build() {
    return area;
  }

}
