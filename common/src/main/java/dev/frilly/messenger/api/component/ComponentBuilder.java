package dev.frilly.messenger.api.component;

import lombok.AccessLevel;
import lombok.Getter;

import java.awt.*;

/**
 * An abstract implementation of a builder for a generic Swing component.
 */
@SuppressWarnings("unchecked")
public abstract class ComponentBuilder<T extends ComponentBuilder<T>> {

  @Getter(AccessLevel.PROTECTED)
  private final Component component;

  ComponentBuilder(final Component component) {
    this.component = component;
  }

  /**
   * Sets the preferred size of this component.
   *
   * @param width  the width
   * @param height the height
   *
   * @return this
   */
  public final T prefSize(final int width, final int height) {
    component.setPreferredSize(new Dimension(width, height));
    return (T) this;
  }

}
