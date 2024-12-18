package dev.frilly.messenger.client.gui;

import dev.frilly.messenger.api.component.Components;
import dev.frilly.messenger.api.gui.LayoutBuilder;
import dev.frilly.messenger.client.components.GroupListSection;

import javax.swing.*;
import java.awt.*;

/**
 * The main screen of the client-side application.
 */
public final class AppScreen extends JPanel {

  private final GroupListSection groups = new GroupListSection(this);
  private final JPanel           other  = new JPanel();

  private final JSplitPane pane = Components.split()
      .hoz()
      .left(groups)
      .right(other)
      .build();

  /**
   * Constructs a new app screen.
   */
  public AppScreen() {
    setup();
  }

  private void setup() {
    final var l = new LayoutBuilder(this).gaps();
    l.hoz(l.seq().comp(pane));
    l.ver(l.centerPara().comp(pane));

    other.setLayout(new BorderLayout());
    setLabel("Nothing");
  }

  /**
   * Testing by setting the label of the pane.
   *
   * @param value the value
   */
  public void setLabel(final String value) {
    other.add(new JLabel(value), BorderLayout.CENTER);
    other.revalidate();
    other.repaint();
  }

}
