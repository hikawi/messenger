package dev.frilly.messenger.server.response;

import lombok.Data;

/**
 * Response for the POST /groups route.
 */
@Data
public final class PostGroupResponse {

  private final long id;

}
