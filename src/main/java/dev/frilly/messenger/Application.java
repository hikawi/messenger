package dev.frilly.messenger;

import com.formdev.flatlaf.intellijthemes.FlatGruvboxDarkHardIJTheme;
import dev.frilly.messenger.api.component.Components;
import dev.frilly.messenger.api.gui.LayoutBuilder;

import javax.swing.*;

/**
 * The main entrypoint of this Messenger application.
 */
public final class Application {

    public static void main(final String[] args) {
        FlatGruvboxDarkHardIJTheme.setup();
        testFrame();
    }

    private static void testFrame() {
        final var frame = new JFrame("Test frame");
        final var pane  = frame.getContentPane();
        final var l     = new LayoutBuilder(pane).gaps().border(24);

        final var userLabel = Components.label("Username").xRight().build();
        final var userField = new JTextField();

        final var emailLabel = Components.label("Email").xRight().build();
        final var emailField = new JTextField();

        final var passLabel = Components.label("Password").xRight().build();
        final var passField = new JPasswordField();

        final var cancel  = new JButton("Cancel");
        final var confirm = new JButton("Confirm");

        l.ver(l.seq()
            .group(l.basePara().comp(userLabel).gap(8).comp(userField))
            .group(l.basePara().comp(emailLabel).gap(8).comp(emailField))
            .group(l.basePara().comp(passLabel).gap(8).comp(passField))
            .gap(16)
            .group(l.basePara().comp(cancel).comp(confirm)));

        l.hoz(l.centerPara()
            .group(l.seq()
                .group(l.trailingPara()
                    .comp(userLabel)
                    .comp(emailLabel)
                    .comp(passLabel))
                .group(l.leadingPara()
                    .comp(userField)
                    .comp(emailField)
                    .comp(passField)))
            .group(l.seq().comp(cancel).gap(4).comp(confirm)));

        l.linkX(cancel, confirm);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
