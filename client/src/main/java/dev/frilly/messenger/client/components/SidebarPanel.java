package dev.frilly.messenger.client.components;

import dev.frilly.messenger.api.Icon;
import dev.frilly.messenger.api.component.Components;
import dev.frilly.messenger.api.data.GroupChat;
import dev.frilly.messenger.api.gui.LayoutBuilder;
import dev.frilly.messenger.client.AppContext;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The panel to show buttons to handle groups and show group chats.
 */
public final class SidebarPanel extends JPanel {

  private final JButton addButton = Components.button("")
      .icon(Icon.PLUS, 16)
      .tab()
      .build();

  private final JPanel        groupsPanel  = new JPanel();
  private final JScrollPane   scrollPane   = new JScrollPane(groupsPanel);
  private final List<JButton> groupButtons = new ArrayList<>();

  private final GridBagConstraints gbc = new GridBagConstraints();

  /**
   * Constructs a new sidebar panel for the client-side app.
   */
  public SidebarPanel() {
    setup();
    setupActions();
    setupHooks();
    new Thread(this::loadData).start();
  }

  private void setup() {
    final var l = new LayoutBuilder(this).gaps();

    l.hoz(l.trailingPara().comp(addButton).comp(scrollPane));
    l.ver(l.seq().comp(addButton).gap(8).comp(scrollPane));

    gbc.gridx     = 0;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.fill      = GridBagConstraints.HORIZONTAL;
    gbc.anchor    = GridBagConstraints.NORTH;

    scrollPane.setMinimumSize(new Dimension(100, 800));
    groupsPanel.setLayout(new GridBagLayout());
    groupButtons.add(
        Components.button("Public Chat").icon(Icon.PUBLIC, 16).build());
    groupsPanel.add(groupButtons.getFirst(), gbc);
  }

  private void setupActions() {
    addButton.addActionListener(e -> {
      final var frame = AppContext.getFrame();
      final String newMembers = JOptionPane.showInputDialog(frame.getFrame(),
          "Type usernames of members to add to group, separated by spaces");
      if (newMembers == null) {
        return;
      }

      final var usernames = newMembers.split(" ");

      final var regex  = Pattern.compile("[A-Za-z-_][A-Za-z-_0-9]+");
      final var socket = AppContext.getRestHandler();

      for (final var username : usernames) {
        if (!regex.asPredicate().test(username)) {
          JOptionPane.showMessageDialog(frame.getFrame(),
              "Username \"%s\" is invalid".formatted(username), "Error!",
              JOptionPane.ERROR_MESSAGE);
          return;
        }

        final var res = socket.query("check %s".formatted(username));
        if (res.equals("no")) {
          JOptionPane.showMessageDialog(frame.getFrame(),
              "Username \"%s\" doesn't exist".formatted(username), "Error!",
              JOptionPane.ERROR_MESSAGE);
          return;
        }
      }

      final var members = new HashSet<String>();
      Collections.addAll(members, usernames);
      members.add(AppContext.getUsername());

      final var res = socket.query(
          "newgroup %s".formatted(String.join(" ", members)));
      if (!res.startsWith("201")) {
        JOptionPane.showMessageDialog(frame.getFrame(),
            "An error occurred while creating the group", "Error!",
            JOptionPane.ERROR_MESSAGE);
        return;
      }

      JOptionPane.showMessageDialog(frame.getFrame(),
          "Created group with %d members.".formatted(members.size()),
          "Success!", JOptionPane.INFORMATION_MESSAGE);
    });

    groupButtons.getFirst().addActionListener(e -> {
      AppContext.getMessageRepository().setCurrentGroup("public");
      resetButtonStates();
      groupButtons.getFirst().setEnabled(false);
    });

    groupButtons.getFirst().doClick();
  }

  private void setupHooks() {
    final var repo = AppContext.getGroupRepository();
    repo.setAddHook(this::addNewGroup);
  }

  private void loadData() {
    final var rest   = AppContext.getRestHandler();
    final var groups = rest.query("getgroups " + AppContext.getUsername());
    final var split  = groups.split(" ");

    for (int i = 1; i < split.length; i++) {
      final var groupId   = split[i];
      final var groupData = rest.query("getgroup " + groupId);

      // Retrieve group chat's data
      final var groupChat = new GroupChat();
      groupChat.setUuid(UUID.fromString(groupId));
      groupChat.setMembers(Arrays.stream(groupData.split(" "))
          .skip(1)
          .collect(Collectors.toSet()));
      AppContext.getGroupRepository().addGroupChat(groupChat);

      // Retrieve group chat's message history.
      rest.query("history %s %s".formatted(groupId, AppContext.getUsername()));
    }
  }

  private void resetButtonStates() {
    groupButtons.forEach(btn -> btn.setEnabled(true));
  }

  /**
   * Adds a new group to the group panel.
   */
  public void addNewGroup(final GroupChat chat) {
    final var btn = Components.button(String.join(", ", chat.getMembers()))
        .build();
    btn.addActionListener(e -> {
      AppContext.getMessageRepository()
          .setCurrentGroup(chat.getUuid().toString());
      resetButtonStates();
      btn.setEnabled(false);
    });

    SwingUtilities.invokeLater(() -> {
      groupButtons.add(btn);
      groupsPanel.add(btn, gbc);
      groupsPanel.revalidate();
      groupsPanel.repaint();
    });
  }

}
