package dev.frilly.messenger.server;

import dev.frilly.messenger.api.ApplicationFrame;
import dev.frilly.messenger.api.net.SocketHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;

/**
 * Application context class for server-side.
 */
@UtilityClass
public class ServerContext {

  @Getter
  private final Set<SocketHandler> wsHandlers = new HashSet<>();
  @Getter
  @Setter
  private ApplicationFrame frame;

}
