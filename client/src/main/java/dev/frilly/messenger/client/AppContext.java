package dev.frilly.messenger.client;

import dev.frilly.messenger.api.ApplicationFrame;
import dev.frilly.messenger.api.net.RestHandler;
import dev.frilly.messenger.api.net.SocketHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

/**
 * Utility class holding application contexts.
 */
@UtilityClass
public final class AppContext {

  @Getter
  @Setter
  private ApplicationFrame frame;

  @Getter
  @Setter
  private RestHandler restHandler;

  @Getter
  @Setter
  private SocketHandler wsHandler;

  @Getter
  @Setter
  private String sessionId;

}
