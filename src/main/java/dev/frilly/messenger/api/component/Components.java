package dev.frilly.messenger.api.component;

import javax.swing.*;

/**
 * Utility class for generating quick and pre-configured components.
 */
public final class Components {

    private Components() {
        // Intentionally left blank.
    }

    /**
     * Constructs a new label.
     *
     * @param value the value
     *
     * @return the label builder
     */
    public static LabelBuilder label(final String value) {
        return new LabelBuilder(new JLabel(value));
    }

}
