package francescocristiano.CapstoneProject.team.repository;

import francescocristiano.CapstoneProject.team.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {

    Page<Team> findByCoachId(UUID coachId, Pageable pageable);

    Optional<Team> findByName(String name);

    Optional<Team> findByEmail(String email);

    List<Team> findAllByCoachId(UUID coachId);
}
