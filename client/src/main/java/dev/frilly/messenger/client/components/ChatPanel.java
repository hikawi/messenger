package dev.frilly.messenger.client.components;

import javax.swing.*;

/**
 * The panel that shows chat messages.
 */
public final class ChatPanel extends JPanel {

  /**
   * Constructs a new chat panel.
   */
  public ChatPanel() {

  }

  private void setup() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
  }

}
