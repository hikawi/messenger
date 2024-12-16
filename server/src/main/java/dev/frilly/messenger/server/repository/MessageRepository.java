package dev.frilly.messenger.server.repository;

import dev.frilly.messenger.server.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Paging and sorting repository for messages.
 */
public interface MessageRepository extends JpaRepository<Message, Long> {

}
