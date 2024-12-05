package dev.frilly.messenger.api.component;

import javax.swing.*;

/**
 * A builder for {@link javax.swing.JLabel}.
 */
public final class LabelBuilder {

    private final JLabel label;

    public LabelBuilder(final JLabel label) {
        this.label = label;
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
     * Retrieves the label
     *
     * @return the label
     */
    public JLabel build() {
        return label;
    }

}
