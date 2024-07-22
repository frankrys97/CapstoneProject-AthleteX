package francescocristiano.CapstoneProject.event.repository;

import francescocristiano.CapstoneProject.event.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    Page<Event> findByTeamId(UUID teamId, Pageable pageable);
}
