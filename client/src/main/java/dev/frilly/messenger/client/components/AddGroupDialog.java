package dev.frilly.messenger.client.components;

import dev.frilly.messenger.api.component.Components;
import dev.frilly.messenger.api.gui.LayoutBuilder;
import dev.frilly.messenger.client.AppContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * A dialog that shows a simple UI to add names to the group chat.
 */
public final class AddGroupDialog extends JDialog {

  private final JLabel     label     = Components.label(
      "Type usernames of users, separated by commas").h3().build();
  private final JTextField field     = Components.textField()
      .placeholder("usernames...")
      .showClear()
      .prefSize(200, 60)
      .build();
  private final JLabel     errorText = Components.label("")
      .fg(Color.RED)
      .h4()
      .build();

  private final JButton cancel  = Components.button("Cancel").build();
  private final JButton confirm = Components.button("Create Group").build();

  private final SidebarPanel sidebar;

  /**
   * Creates a new dialog.
   */
  public AddGroupDialog(final SidebarPanel sidebar) {
    this.sidebar = sidebar;
    setup();
    setupActions();
  }

  private void setup() {
    final var l = new LayoutBuilder(this).gaps();

    l.hoz(l.leadingPara()
        .comp(label)
        .comp(field)
        .comp(errorText)
        .trailing(l.seq().comp(cancel).comp(confirm)));
    l.ver(l.seq()
        .comp(label)
        .comp(field)
        .comp(errorText)
        .gap(8)
        .group(l.basePara().comp(cancel).comp(confirm)));
  }

  private void setupActions() {
    cancel.addActionListener(e -> close());

    confirm.addActionListener(e -> {
      final var regex     = Pattern.compile("[A-Za-z-_][A-Za-z-_0-9]+");
      final var usernames = field.getText().split(",");
      final var socket    = AppContext.getRestHandler();

      for (final var username : usernames) {
        if (!regex.asPredicate().test(username)) {
          errorText.setText("\"%s\" is invalid username.".formatted(username));
          return;
        }

        final var res = socket.query("check %s".formatted(username));
        if (res.equals("no")) {
          errorText.setText("\"%s\" isn't registered.");
          return;
        }
      }

      close();

      final var members = new HashSet<String>();
      Collections.addAll(members, usernames);
      members.add(AppContext.getUsername());

      final var res = socket.query(
          "newgroup %s".formatted(String.join(" ", members)));
      final var frame = AppContext.getFrame();
      if (!res.startsWith("201")) {
        JOptionPane.showMessageDialog(frame.getFrame(),
            "An error occurred while creating the group", "Error!",
            JOptionPane.ERROR_MESSAGE);
      }
    });
  }

  private void close() {
    this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
  }

}
