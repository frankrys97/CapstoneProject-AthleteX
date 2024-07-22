package francescocristiano.CapstoneProject.partecipation.repository;

import francescocristiano.CapstoneProject.partecipation.Partecipation;
import francescocristiano.CapstoneProject.partecipation.StatusOfPartecipation;
import francescocristiano.CapstoneProject.team.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PartecipationRepository extends JpaRepository<Partecipation, UUID> {

    Optional<Partecipation> findByEmailAndTeam(String email, Team team);

    Optional<Partecipation> findByEmailAndStatusOfPartecipation(String email, StatusOfPartecipation statusOfPartecipation);

    Page<Partecipation> findAllByTeam(Team team, Pageable pageable);
}
