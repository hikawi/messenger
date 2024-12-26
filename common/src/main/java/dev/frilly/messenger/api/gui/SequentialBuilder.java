package dev.frilly.messenger.api.gui;

import javax.swing.*;
import javax.swing.GroupLayout.SequentialGroup;
import java.awt.*;

/**
 * A builder class for constructing the horizontal group in
 * {@link javax.swing.GroupLayout}.
 */
public final class SequentialBuilder extends AbstractGroupBuilder {

  private final SequentialGroup group;

  /**
   * Constructs a new horizontal group builder from a layout builder.
   * This should be constructed from the LayoutBuilder class itself.
   *
   * @param group The builder
   */
  SequentialBuilder(final SequentialGroup group) {
    this.group = group;
  }

  /**
   * Adds a component.
   *
   * @param component The component
   *
   * @return this
   */
  public SequentialBuilder comp(final Component component) {
    this.group.addComponent(component);
    return this;
  }

  /**
   * Adds a component and uses it as a baseline.
   *
   * @param component The component
   *
   * @return this
   */
  public SequentialBuilder bcomp(final Component component) {
    group.addComponent(true, component);
    return this;
  }

  /**
   * Adds a group to this sequential group.
   *
   * @param group The group to add
   *
   * @return this
   */
  public SequentialBuilder group(final AbstractGroupBuilder group) {
    this.group.addGroup(group.done());
    return this;
  }

  /**
   * Adds a scaling gap. With the min size being 75% and max size being 125%.
   * <p>
   * That means, adding a gap of 24 would add a gap with min 18, preferred
   * 24, and max 30.
   *
   * @param size the preferred size
   *
   * @return this
   */
  public SequentialBuilder gap(final int size) {
    group.addGap((int) (size * 0.75), size, (int) (size * 1.25));
    return this;
  }

  /**
   * Adds a gap that stretches for a space-between like behavior.
   *
   * @return the gap
   */
  public SequentialBuilder fullGap() {
    group.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
    return this;
  }

  /**
   * Finishes this builder and retrieves the group instance.
   *
   * @return the group instance
   */
  @Override
  SequentialGroup done() {
    return this.group;
  }

}
