package dev.frilly.messenger.api;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

/**
 * Singleton manager for icons.
 */
public final class IconsManager {

  public static IconsManager instance;

  private IconsManager() {
  }

  /**
   * Retrieves a default scaled image icon at 24x24.
   *
   * @param icon the icon
   *
   * @return the icon
   */
  public static ImageIcon icon(final Icon icon) {
    return icon(icon, 24);
  }

  /**
   * Retrieves an instance of scaled image icon.
   *
   * @param name the name of the icon
   * @param size the size to scale to
   *
   * @return the icon
   */
  public static ImageIcon icon(final Icon name, final int size) {
    return getInstance().getIcon(name, size);
  }

  private ImageIcon getIcon(final Icon icon, final int size) {
    try {
      final var stream = this.getClass()
          .getClassLoader()
          .getResource("icons/%s.png".formatted(icon.getValue()));
      final var image = ImageIO.read(Objects.requireNonNull(stream));
      return new ImageIcon(
          image.getScaledInstance(size, size, Image.SCALE_SMOOTH));
    } catch (IOException ex) {
      ex.fillInStackTrace();
      return null;
    }
  }

  /**
   * Gets the instance of icons manager.
   *
   * @return the icons manager
   */
  public static IconsManager getInstance() {
    return instance == null ? instance = new IconsManager() : instance;
  }

}
