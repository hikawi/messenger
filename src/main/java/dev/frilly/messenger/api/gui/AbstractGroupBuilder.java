package dev.frilly.messenger.api.gui;

import javax.swing.GroupLayout.Group;

/**
 * Abstract class for group builders (mainly SequentialBuilder and
 * ParallelBuilder).
 */
public sealed abstract class AbstractGroupBuilder
    permits SequentialBuilder, ParallelBuilder {

    /**
     * Creates the final {@link Group} object.
     *
     * @return the {@link Group} object
     */
    abstract Group done();

}
