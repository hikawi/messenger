package dev.frilly.messenger.api.gui;

import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import java.awt.*;

/**
 * Builder for building {@link ParallelGroup}, sister of
 * {@link SequentialBuilder}.
 */
public final class ParallelBuilder extends AbstractGroupBuilder {

    private final ParallelGroup group;

    /**
     * Constructs a new instance of the builder of {@link ParallelBuilder}.
     *
     * @param group the group to bind to
     */
    ParallelBuilder(final ParallelGroup group) {
        this.group = group;
    }

    /**
     * Adds a component to this group with default alignment.
     *
     * @param component the component
     *
     * @return this
     */
    public ParallelBuilder comp(final Component component) {
        group.addComponent(component);
        return this;
    }

    /**
     * Adds a component to this group with LEADING alignment.
     *
     * @param component the component
     *
     * @return this
     */
    public ParallelBuilder leading(final Component component) {
        group.addComponent(component, Alignment.LEADING);
        return this;
    }

    /**
     * Adds a component to this group with CENTER alignment.
     *
     * @param component the component
     *
     * @return this
     */
    public ParallelBuilder center(final Component component) {
        group.addComponent(component, Alignment.CENTER);
        return this;
    }

    /**
     * Adds a component to this group with TRAILING alignment.
     *
     * @param component the component
     *
     * @return this
     */
    public ParallelBuilder trailing(final Component component) {
        group.addComponent(component, Alignment.TRAILING);
        return this;
    }

    /**
     * Adds a component to this group with BASELINE alignment.
     *
     * @param component the component
     *
     * @return this
     */
    public ParallelBuilder baseline(final Component component) {
        group.addComponent(component, Alignment.BASELINE);
        return this;
    }

    /**
     * Adds a group to this group with default alignment.
     *
     * @param builder the group builder
     *
     * @return this
     */
    public ParallelBuilder group(final AbstractGroupBuilder builder) {
        group.addGroup(builder.done());
        return this;
    }

    /**
     * Adds a group to this group with LEADING alignment.
     *
     * @param group the group
     *
     * @return this
     */
    public ParallelBuilder leading(final AbstractGroupBuilder group) {
        this.group.addGroup(Alignment.LEADING, group.done());
        return this;
    }

    /**
     * Adds a group to this group with CENTER alignment.
     *
     * @param group the group
     *
     * @return this
     */
    public ParallelBuilder center(final AbstractGroupBuilder group) {
        this.group.addGroup(Alignment.CENTER, group.done());
        return this;
    }

    /**
     * Adds a group to this group with TRAILING alignment.
     *
     * @param group the group
     *
     * @return this
     */
    public ParallelBuilder trailing(final AbstractGroupBuilder group) {
        this.group.addGroup(Alignment.TRAILING, group.done());
        return this;
    }

    /**
     * Adds a group to this group with BASELINE alignment.
     *
     * @param group the group
     *
     * @return this
     */
    public ParallelBuilder baseline(final AbstractGroupBuilder group) {
        this.group.addGroup(Alignment.BASELINE, group.done());
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
    public ParallelBuilder gap(final int size) {
        group.addGap((int) (size * 0.75), size, (int) (size * 1.25));
        return this;
    }

    @Override
    ParallelGroup done() {
        return group;
    }

}
