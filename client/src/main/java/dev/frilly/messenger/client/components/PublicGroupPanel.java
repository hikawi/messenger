package dev.frilly.messenger.client.components;

import dev.frilly.messenger.api.Icon;
import dev.frilly.messenger.api.component.Components;
import dev.frilly.messenger.api.gui.LayoutBuilder;
import dev.frilly.messenger.client.gui.AppScreen;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * The panel to show a public group chat channel.
 */
public final class PublicGroupPanel extends JPanel {

  private final AppScreen app;
  private final JLabel    publicName = Components.label("Name")
      .h3()
      .icon(Icon.PUBLIC, 32)
      .build();

  /**
   * Constructs a new panel to display the public group channel.
   */
  public PublicGroupPanel(final AppScreen app) {
    this.app = app;
    setup();
    setupActions();
  }

  private void setup() {
    final var l = new LayoutBuilder(this).border(12, 4).gaps();
    l.hoz(l.seq().comp(publicName));
    l.ver(l.centerPara().comp(publicName));
  }

  private void setupActions() {
    publicName.addMouseListener(new MouseInputListener() {
      @Override
      public void mouseDragged(MouseEvent e) {
        // Intentionally left blank.
      }

      @Override
      public void mouseMoved(MouseEvent e) {
        // Intentionally left blank.
      }

      @Override
      public void mouseClicked(MouseEvent e) {
        // Intentionally left blank.
        System.out.println("Mouse clicked on public chat.");
        app.setLabel("Public");
      }

      @Override
      public void mousePressed(MouseEvent e) {
        // Intentionally left blank.
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        // Intentionally left blank.
      }

      @Override
      public void mouseEntered(MouseEvent e) {
        // Intentionally left blank.
        PublicGroupPanel.this.setBackground(Color.LIGHT_GRAY);
      }

      @Override
      public void mouseExited(MouseEvent e) {
        // Intentionally left blank.
        PublicGroupPanel.this.setBackground(null);
      }
    });
  }

}
