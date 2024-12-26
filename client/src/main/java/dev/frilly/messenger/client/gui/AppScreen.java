package dev.frilly.messenger.client.gui;

import dev.frilly.messenger.api.gui.LayoutBuilder;
import dev.frilly.messenger.client.components.ChatViewPanel;
import dev.frilly.messenger.client.components.SidebarPanel;

import javax.swing.*;

/**
 * The main screen of the client-side application.
 */
public final class AppScreen extends JPanel {

  private final SidebarPanel  sidebar  = new SidebarPanel();
  private final ChatViewPanel chatView = new ChatViewPanel();

  private final JSplitPane splitPane = new JSplitPane();

  /**
   * Constructs a new app screen.
   */
  public AppScreen() {
    setup();
  }

  private void setup() {
    splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    splitPane.setDividerSize(0);
    splitPane.setLeftComponent(sidebar);
    splitPane.setRightComponent(chatView);

    final var l = new LayoutBuilder(this).gaps();
    l.ver(l.seq().comp(splitPane));
    l.hoz(l.seq().comp(splitPane));
  }

}
