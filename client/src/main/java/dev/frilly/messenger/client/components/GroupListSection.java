package dev.frilly.messenger.client.components;

import dev.frilly.messenger.client.gui.AppScreen;

import javax.swing.*;
import java.awt.*;

/**
 * The left side section for holding all groups.
 */
public class GroupListSection extends JPanel {

  private final AppScreen app;

  /**
   * Instantiates a new instance of section to display all available groups.
   */
  public GroupListSection(final AppScreen app) {
    this.app = app;
    setup();
  }

  private void setup() {
    this.setLayout(new GridLayout(0, 1));

    this.add(new GroupToolsPanel(app));
    this.add(new PublicGroupPanel(app));
  }

}
