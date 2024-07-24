package francescocristiano.CapstoneProject.player.avalibilities.repository;

import francescocristiano.CapstoneProject.player.avalibilities.PlayerInjuryPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerInjuryRepository extends JpaRepository<PlayerInjuryPeriod, UUID> {

    Optional<PlayerInjuryPeriod> findByPlayerId(UUID id);
}
