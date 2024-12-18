package dev.frilly.messenger.api.component;

import javax.swing.*;
import java.awt.*;

/**
 * Builder for {@link JSplitPane}.
 */
public final class SplitPaneBuilder extends ComponentBuilder<SplitPaneBuilder> {

  private final JSplitPane pane;

  /**
   * Constructs a new builder for JSplitPane.
   *
   * @param pane pane
   */
  public SplitPaneBuilder(final JSplitPane pane) {
    super(pane);
    this.pane = pane;
  }

  /**
   * Sets the split direction to HORIZONTAL.
   *
   * @return this
   */
  public SplitPaneBuilder hoz() {
    pane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    return this;
  }

  /**
   * Sets the split direction to VERTICAL.
   *
   * @return this
   */
  public SplitPaneBuilder ver() {
    pane.setOrientation(JSplitPane.VERTICAL_SPLIT);
    return this;
  }

  /**
   * Sets the component on the left.
   *
   * @param component component
   *
   * @return this
   */
  public SplitPaneBuilder left(final Component component) {
    pane.setLeftComponent(component);
    return this;
  }

  /**
   * Sets the component on the right.
   *
   * @param component component
   *
   * @return this
   */
  public SplitPaneBuilder right(final Component component) {
    pane.setRightComponent(component);
    return this;
  }

  /**
   * Sets the component on the top.
   *
   * @param component component
   *
   * @return this
   */
  public SplitPaneBuilder top(final Component component) {
    pane.setTopComponent(component);
    return this;
  }

  /**
   * Sets the component on the bottom.
   *
   * @param component component
   *
   * @return this
   */
  public SplitPaneBuilder bottom(final Component component) {
    pane.setBottomComponent(component);
    return this;
  }

  /**
   * Builds the component.
   *
   * @return the split pane
   */
  public JSplitPane build() {
    return pane;
  }

}
