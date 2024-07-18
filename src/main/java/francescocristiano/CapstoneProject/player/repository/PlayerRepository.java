package francescocristiano.CapstoneProject.player.repository;

import francescocristiano.CapstoneProject.player.playerClass.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID> {

    @Query("SELECT p FROM Player p WHERE p.jerseyNumber = :jerseyNumber AND p.team.id = :teamId")
    Optional<Player> findByJerseyNumber(int jerseyNumber, UUID teamId);
}
