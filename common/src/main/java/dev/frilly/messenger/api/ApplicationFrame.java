package dev.frilly.messenger.api;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Stack;

/**
 * The application frame holding the content pane of what to be displayed on
 * the server application.
 */
public final class ApplicationFrame {

  private final JFrame        frame;
  private final Stack<JPanel> stack = new Stack<>();

  /**
   * Main constructor of an application frame.
   */
  public ApplicationFrame(final String title) {
    this.frame = new JFrame(title);
    this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  /**
   * Adds a hook to be run on this frame closing.
   *
   * @param runnable the runnable
   */
  public void addCloseHook(final Runnable runnable) {
    frame.addWindowListener(new WindowListener() {
      @Override
      public void windowOpened(WindowEvent e) {

      }

      @Override
      public void windowClosing(WindowEvent e) {
        runnable.run();
      }

      @Override
      public void windowClosed(WindowEvent e) {

      }

      @Override
      public void windowIconified(WindowEvent e) {

      }

      @Override
      public void windowDeiconified(WindowEvent e) {

      }

      @Override
      public void windowActivated(WindowEvent e) {

      }

      @Override
      public void windowDeactivated(WindowEvent e) {

      }
    });
  }

  /**
   * Push a new panel into the stack, and display it.
   *
   * @param panel the panel
   */
  public void push(final JPanel panel) {
    stack.add(panel);
    render();
  }

  /**
   * Force this frame to revalidate and repaint.
   */
  public void render() {
    frame.setContentPane(stack.peek());
    frame.revalidate();
    frame.repaint();
    frame.pack();
    frame.setLocationRelativeTo(null);
  }

  /**
   * Push a new panel into the stack, replacing the last panel, and display it.
   * The previous panel will be removed from the stack.
   *
   * @param panel the panel
   */
  public void replace(final JPanel panel) {
    stack.pop();
    stack.add(panel);
    render();
  }

  /**
   * Rewinds back to the previous content pane. This automatically closes the
   * program if there is no previous content pane.
   */
  public void rewind() {
    if (stack.isEmpty()) {
      quit();
      return;
    }

    final var pane = stack.pop();
    frame.setContentPane(pane);
    render();
  }

  /**
   * Quits the frame instantly.
   */
  public void quit() {
    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
  }

  /**
   * Displays the window.
   */
  public void display() {
    render();
    frame.setVisible(true);
  }

}
