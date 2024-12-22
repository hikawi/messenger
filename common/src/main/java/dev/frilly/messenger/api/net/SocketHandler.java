package dev.frilly.messenger.api.net;

import lombok.Setter;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A handler for passive listening of a socket.
 */
public final class SocketHandler extends Thread {

  private final Socket socket;

  @Setter
  private Consumer<String> consumer;

  @Setter
  private Runnable onClose;

  /**
   * Constructs a new handler for a socket.
   *
   * @param socket the socket
   */
  public SocketHandler(final Socket socket) {
    this.socket = socket;
  }

  /**
   * Writes to the socket.
   *
   * @param res the response
   */
  @SneakyThrows
  public void write(final String res) {
    final var writer = new BufferedWriter(
        new OutputStreamWriter(socket.getOutputStream()));
    writer.write(res + "\n");
    writer.flush();
    writer.close();
  }

  /**
   * Closes this socket handler.
   */
  @SneakyThrows
  public void close() {
    socket.close();
    Optional.ofNullable(onClose).ifPresent(Runnable::run);
  }

  @Override
  @SneakyThrows
  public void run() {
    try (final var input = new BufferedReader(
        new InputStreamReader(socket.getInputStream()))
    ) {
      while (!socket.isClosed()) {
        final var res = input.readLine();
        if (consumer == null) {
          continue;
        }
        consumer.accept(res);
      }
    }
  }

}
