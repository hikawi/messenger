package dev.frilly.messenger.client.components;

import dev.frilly.messenger.api.Icon;
import dev.frilly.messenger.api.component.Components;
import dev.frilly.messenger.api.gui.LayoutBuilder;
import dev.frilly.messenger.client.gui.AppScreen;

import javax.swing.*;

/**
 * A small lightweight panel to show an "add" button to create a new group.
 */
public final class GroupToolsPanel extends JPanel {

  private final AppScreen app;
  private final JButton   addButton = Components.button("New Group")
      .icon(Icon.PLUS, 16)
      .toolbar()
      .build();

  /**
   * Constructs a new tools panel.
   */
  public GroupToolsPanel(final AppScreen app) {
    this.app = app;
    setup();
    setupActions();
  }

  private void setup() {
    final var l = new LayoutBuilder(this).gaps();
    l.hoz(l.seq().comp(addButton));
    l.ver(l.trailingPara().comp(addButton));
  }

  private void setupActions() {
    addButton.addActionListener(e -> {
      app.setLabel("Add button");
    });
  }

}
