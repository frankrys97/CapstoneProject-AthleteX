package francescocristiano.CapstoneProject.player.statistics.repository;

import francescocristiano.CapstoneProject.player.statistics.PlayerStatistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlayerStatisticsRepository extends JpaRepository<PlayerStatistics, UUID> {

    Page<PlayerStatistics> findAllByPlayerId(UUID playerId, Pageable pageable);
}
