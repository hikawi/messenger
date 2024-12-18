package dev.frilly.messenger.api.component;

import dev.frilly.messenger.api.Icon;
import dev.frilly.messenger.api.IconsManager;

import javax.swing.*;
import java.awt.*;

/**
 * A builder for {@link javax.swing.JLabel}.
 */
public final class LabelBuilder extends ComponentBuilder<LabelBuilder> {

  private final JLabel label;

  LabelBuilder(final JLabel label) {
    super(label);
    this.label = label;
  }

  /**
   * Sets the foreground color of the label.
   *
   * @param color the color
   *
   * @return this
   */
  public LabelBuilder fg(final Color color) {
    label.setForeground(color);
    return this;
  }

  /**
   * Sets the icon of this label.
   *
   * @param icon the icon
   * @param size the size
   *
   * @return this
   */
  public LabelBuilder icon(final Icon icon, final int size) {
    this.label.setIcon(IconsManager.icon(icon));
    return this.iconGap(size / 2);
  }

  /**
   * Sets the label's icon gap.
   *
   * @param size the size
   *
   * @return this
   */
  public LabelBuilder iconGap(final int size) {
    this.label.setIconTextGap(size);
    return this;
  }

  /**
   * Sets the horizontal alignment of the label to the left.
   *
   * @return this
   */
  public LabelBuilder xLeft() {
    label.setHorizontalTextPosition(JLabel.LEFT);
    return this;
  }

  /**
   * Sets the horizontal alignment of the label to the right
   *
   * @return this
   */
  public LabelBuilder xRight() {
    label.setHorizontalTextPosition(JLabel.RIGHT);
    return this;
  }

  /**
   * Sets the horizontal alignment of the label to the center.
   *
   * @return this
   */
  public LabelBuilder xCenter() {
    label.setHorizontalTextPosition(JLabel.CENTER);
    return this;
  }

  /**
   * Sets the vertical alignment of the label to the top.
   *
   * @return this
   */
  public LabelBuilder yLeft() {
    label.setVerticalTextPosition(JLabel.TOP);
    return this;
  }

  /**
   * Sets the vertical alignment of the label to the center.
   *
   * @return this
   */
  public LabelBuilder yCenter() {
    label.setVerticalTextPosition(JLabel.CENTER);
    return this;
  }

  /**
   * Sets the vertical alignment of the label to the bottom.
   *
   * @return this
   */
  public LabelBuilder yRight() {
    label.setVerticalTextPosition(JLabel.BOTTOM);
    return this;
  }

  /**
   * Put the client property FlatLaf to h00.
   *
   * @return this
   */
  public LabelBuilder h00() {
    label.putClientProperty(Components.FLATLAF, "h00");
    return this;
  }

  /**
   * Put the client property FlatLaf to h0.
   *
   * @return this
   */
  public LabelBuilder h0() {
    label.putClientProperty(Components.FLATLAF, "h0");
    return this;
  }

  /**
   * Put the client property FlatLaf to h1.
   *
   * @return this
   */
  public LabelBuilder h1() {
    label.putClientProperty(Components.FLATLAF, "h1");
    return this;
  }

  /**
   * Put the client property FlatLaf to h2.
   *
   * @return this
   */
  public LabelBuilder h2() {
    label.putClientProperty(Components.FLATLAF, "h2");
    return this;
  }

  /**
   * Put the client property FlatLaf to h3.
   *
   * @return this
   */
  public LabelBuilder h3() {
    label.putClientProperty(Components.FLATLAF, "h3");
    return this;
  }

  /**
   * Put the client property FlatLaf to h3.
   *
   * @return this
   */
  public LabelBuilder h4() {
    label.putClientProperty(Components.FLATLAF, "h4");
    return this;
  }

  /**
   * Retrieves the label
   *
   * @return the label
   */
  public JLabel build() {
    return label;
  }

}
