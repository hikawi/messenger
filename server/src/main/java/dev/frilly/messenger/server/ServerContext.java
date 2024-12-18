package dev.frilly.messenger.server;

import dev.frilly.messenger.api.ApplicationFrame;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.UtilityClass;

/**
 * Application context class for server-side.
 */
@UtilityClass
public class ServerContext {

  @Getter
  @Setter
  @NonNull
  private ApplicationFrame frame;

}
