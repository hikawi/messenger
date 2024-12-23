package dev.frilly.messenger.client.components;

import dev.frilly.messenger.api.Icon;
import dev.frilly.messenger.api.component.Components;
import dev.frilly.messenger.api.gui.LayoutBuilder;
import dev.frilly.messenger.client.AppContext;
import dev.frilly.messenger.client.gui.AppScreen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The panel to show buttons to handle groups and show group chats.
 */
public final class SidebarPanel extends JPanel {

  private final AppScreen app;

  private final JButton addButton = Components.button("")
      .icon(Icon.PLUS, 16)
      .tab()
      .build();

  private final JPanel        groupsPanel  = new JPanel();
  private final JScrollPane   scrollPane   = new JScrollPane(groupsPanel);
  private final List<JButton> groupButtons = new ArrayList<>();

  /**
   * Constructs a new sidebar panel for the client-side app.
   */
  public SidebarPanel(final AppScreen app) {
    this.app = app;
    setup();
    setupActions();
  }

  private void setup() {
    final var l = new LayoutBuilder(this).gaps();

    l.hoz(l.trailingPara().comp(addButton).comp(scrollPane));
    l.ver(l.seq().comp(addButton).gap(8).comp(scrollPane));

    scrollPane.setMinimumSize(new Dimension(100, 800));
    groupsPanel.setLayout(new BoxLayout(groupsPanel, BoxLayout.Y_AXIS));
    groupButtons.add(
        Components.button("Public Chat").icon(Icon.PUBLIC, 16).build());
    groupsPanel.add(groupButtons.getFirst());
  }

  private void setupActions() {
    addButton.addActionListener(e -> {
      app.test("Add Button");
    });

    groupButtons.getFirst()
        .addActionListener(
            e -> AppContext.getMessageRepository().setCurrentGroup("public"));
  }

}
