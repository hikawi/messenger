package dev.frilly.messenger.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Reflected class for handling database connections.
 */
@Configuration
public class DatabaseConfig {

  @Value("${db.url}")
  private String dbUrl;

  @Value("${db.username}")
  private String username;

  @Value("${db.password}")
  private String password;

}
