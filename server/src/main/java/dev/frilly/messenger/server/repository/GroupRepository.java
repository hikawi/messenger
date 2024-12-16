package dev.frilly.messenger.server.repository;

import dev.frilly.messenger.server.models.GroupChat;
import org.springframework.data.repository.CrudRepository;

/**
 * Public repository for CRUD actions on {@link GroupChat}.
 */
public interface GroupRepository extends CrudRepository<GroupChat, Long> {

}
