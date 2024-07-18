package francescocristiano.CapstoneProject.player.service;


import francescocristiano.CapstoneProject.exceptions.NotFoundExpetion;
import francescocristiano.CapstoneProject.player.playerClass.Player;
import francescocristiano.CapstoneProject.player.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public Optional<Player> findByJerseyNumber(int jerseyNumber, UUID teamId) {
        return playerRepository.findByJerseyNumber(jerseyNumber, teamId);
    }

    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    public Player findById(UUID id) {
        return playerRepository.findById(id).orElseThrow(() -> new NotFoundExpetion("Player not found"));
    }

    public void deleteById(UUID id) {
        playerRepository.deleteById(id);
    }
}
