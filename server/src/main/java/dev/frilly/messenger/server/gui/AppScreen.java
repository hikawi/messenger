package dev.frilly.messenger.server.gui;

import dev.frilly.messenger.api.ApplicationFrame;
import dev.frilly.messenger.api.component.Components;
import dev.frilly.messenger.api.gui.LayoutBuilder;
import dev.frilly.messenger.api.net.HttpFetch;
import dev.frilly.messenger.server.Entrypoint;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.swing.*;

/**
 * The main server application screen.
 */
public final class AppScreen extends JPanel {

  private final ApplicationFrame frame;

  private final JLabel helloThere = Components.label("Hello There")
      .h00()
      .build();

  private final JLabel statusLabel = Components.label("Status Code")
      .h4()
      .build();
  private final JLabel apiVersion  = Components.label("API Version")
      .h4()
      .build();

  private final JLabel status = Components.label("Unknown").build();
  private final JLabel api    = Components.label("Unknown").build();

  private final JButton testConnection = Components.button("Test Connection")
      .rounded()
      .build();

  /**
   * Creates a new app screen instance.
   *
   * @param frame the main frame
   */
  public AppScreen(final ApplicationFrame frame) {
    this.frame = frame;
    setup();
    setupActions();
    frame.push(this);
  }

  private void setup() {
    final var l = new LayoutBuilder(this);
    l.border(32);

    l.ver(l.seq()
        .comp(helloThere)
        .gap(16)
        .group(l.basePara().comp(statusLabel).comp(status))
        .group(l.basePara().comp(apiVersion).comp(api))
        .gap(16)
        .comp(testConnection));

    l.hoz(l.centerPara()
        .comp(helloThere)
        .group(l.seq()
            .group(l.trailingPara().comp(statusLabel).comp(apiVersion))
            .gap(8)
            .group(l.leadingPara().comp(status).comp(api)))
        .comp(testConnection));

    l.link(status, api);
  }

  private void setupActions() {
    // Start the Swing app
    new Thread(() -> {
      try {
        Class.forName("org.postgresql.Driver");
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }

      new SpringApplicationBuilder(Entrypoint.class).headless(false)
          .build()
          .run();
    }).start();

    testConnection.addActionListener(e -> {
      status.setText("Loading...");
      api.setText("Loading...");
      final var res = HttpFetch.fetch("http://localhost:8080/handshake").get();
      status.setText(String.valueOf(res.getCode()));
      api.setText(res.getBody().get("version").asText());
    });
  }

}
