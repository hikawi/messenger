package dev.frilly.messenger.api.component;

import com.formdev.flatlaf.FlatClientProperties;
import dev.frilly.messenger.api.Icon;
import dev.frilly.messenger.api.IconsManager;

import javax.swing.*;

/**
 * A builder for {@link JButton}.
 */
public class ButtonBuilder extends ComponentBuilder<ButtonBuilder> {

  private final JButton button;

  /**
   * Creates a new builder with the button's name.
   *
   * @param button the button
   */
  public ButtonBuilder(final JButton button) {
    super(button);
    this.button = button;
  }

  /**
   * Makes the button rounded.
   *
   * @return this
   */
  public ButtonBuilder rounded() {
    this.button.putClientProperty(FlatClientProperties.BUTTON_TYPE,
        FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
    return this;
  }

  /**
   * Makes the button type tab.
   *
   * @return this
   */
  public ButtonBuilder tab() {
    this.button.putClientProperty(FlatClientProperties.BUTTON_TYPE,
        FlatClientProperties.BUTTON_TYPE_TAB);
    return this;
  }

  /**
   * Makes the button type toolbar.
   *
   * @return this
   */
  public ButtonBuilder toolbar() {
    this.button.putClientProperty(FlatClientProperties.BUTTON_TYPE,
        FlatClientProperties.BUTTON_TYPE_TOOLBAR_BUTTON);
    return this;
  }

  /**
   * Sets the icon of this button.
   *
   * @param icon the icon
   * @param size the size
   *
   * @return this
   */
  public ButtonBuilder icon(final Icon icon, final int size) {
    this.button.setIcon(IconsManager.icon(icon));
    return this.iconGap(size / 2);
  }

  /**
   * Sets the button's icon gap.
   *
   * @param size the size
   *
   * @return this
   */
  public ButtonBuilder iconGap(final int size) {
    this.button.setIconTextGap(size);
    return this;
  }

  /**
   * Retrieves the current instance.
   *
   * @return the instance.
   */
  public JButton build() {
    return button;
  }

}
