package dev.frilly.messenger.api.gui;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.*;

/**
 * Represents a {@link javax.swing.GroupLayout} builder, using much shorter
 * method names and much more concise building. This doesn't use a grid
 * system like MiG layout, this just reimplements GroupLayout with shortcuts.
 */
public final class LayoutBuilder {

    final         GroupLayout layout;
    private final Container   container;

    /**
     * Initializes a new builder.
     *
     * @param container The panel to set GroupLayout to.
     */
    public LayoutBuilder(final Container container) {
        this.layout    = new GroupLayout(container);
        this.container = container;
        container.setLayout(this.layout);
    }

    /**
     * Calls {@link GroupLayout#setAutoCreateGaps(boolean)} and
     * {@link GroupLayout#setAutoCreateContainerGaps(boolean)}.
     *
     * @return this
     */
    public LayoutBuilder gaps() {
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        return this;
    }

    /**
     * Sets the horizontal group for this layout.
     *
     * @param group The group
     *
     * @return The layout
     */
    public LayoutBuilder hoz(final AbstractGroupBuilder group) {
        layout.setHorizontalGroup(group.done());
        return this;
    }

    /**
     * Sets the vertical group for this layout.
     *
     * @param group The group
     *
     * @return The layout
     */
    public LayoutBuilder ver(final AbstractGroupBuilder group) {
        layout.setVerticalGroup(group.done());
        return this;
    }

    /**
     * Sets the empty border padding of this panel.
     *
     * @param size the size
     *
     * @return this
     */
    public LayoutBuilder border(final int size) {
        return border(size, size);
    }

    /**
     * Sets the empty border padding of this panel.
     *
     * @param x The horizontal padding
     * @param y The vertical padding
     *
     * @return this
     */
    public LayoutBuilder border(final int x, final int y) {
        return border(y, x, y, x);
    }

    /**
     * Sets the empty border padding of this panel.
     *
     * @param top    the top
     * @param right  the right
     * @param bottom the bottom
     * @param left   the left
     *
     * @return this
     */
    public LayoutBuilder border(
        final int top,
        final int right,
        final int bottom,
        final int left
    ) {
        if (container instanceof JPanel panel) {
            panel.setBorder(
                BorderFactory.createEmptyBorder(top, left, bottom, right));
        }
        return this;
    }

    /**
     * Generates a {@link SequentialBuilder} for building a sequential group.
     *
     * @return the builder
     */
    public SequentialBuilder seq() {
        return new SequentialBuilder(layout.createSequentialGroup());
    }

    /**
     * Generates a {@link ParallelBuilder} for building a parallel group,
     * aligned LEADING.
     *
     * @return the builder
     */
    public ParallelBuilder leadingPara() {
        return new ParallelBuilder(
            layout.createParallelGroup(Alignment.LEADING, true));
    }

    /**
     * Generates a {@link ParallelBuilder} for building a parallel group,
     * aligned CENTER.
     *
     * @return the builder
     */
    public ParallelBuilder centerPara() {
        return new ParallelBuilder(
            layout.createParallelGroup(Alignment.CENTER, true));
    }

    /**
     * Generates a {@link ParallelBuilder} for building a parallel group,
     * aligned TRAILING.
     *
     * @return the builder
     */
    public ParallelBuilder trailingPara() {
        return new ParallelBuilder(
            layout.createParallelGroup(Alignment.TRAILING, true));
    }

    /**
     * Geerates a {@link ParallelBuilder} for building a parallel group,
     * aligned BASELINE.
     *
     * @return the builder
     */
    public ParallelBuilder basePara() {
        return new ParallelBuilder(layout.createBaselineGroup(true, false));
    }

    /**
     * Links the sizes of the provided components.
     *
     * @param components the components
     *
     * @return this
     */
    public LayoutBuilder link(final Component... components) {
        layout.linkSize(components);
        return this;
    }

    /**
     * Links the sizes of the provided components along the horizontal axis.
     *
     * @param components the components
     *
     * @return this
     */
    public LayoutBuilder linkX(final Component... components) {
        layout.linkSize(SwingConstants.HORIZONTAL, components);
        return this;
    }

    /**
     * Links the sizes of the provided components along the vertical axis.
     *
     * @param components the components
     *
     * @return this
     */
    public LayoutBuilder linkY(final Component... components) {
        layout.linkSize(SwingConstants.VERTICAL, components);
        return this;
    }

}
