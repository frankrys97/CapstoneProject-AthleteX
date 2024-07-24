package francescocristiano.CapstoneProject.player.avalibilities.service;

import francescocristiano.CapstoneProject.exceptions.BadRequestException;
import francescocristiano.CapstoneProject.exceptions.NotFoundExpetion;
import francescocristiano.CapstoneProject.player.avalibilities.PlayerInjuryPeriod;
import francescocristiano.CapstoneProject.player.avalibilities.payload.NewPlayerInjuryDTO;
import francescocristiano.CapstoneProject.player.avalibilities.repository.PlayerInjuryRepository;
import francescocristiano.CapstoneProject.player.playerClass.Player;
import francescocristiano.CapstoneProject.player.service.PlayerService;
import francescocristiano.CapstoneProject.user.User;
import francescocristiano.CapstoneProject.user.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PlayerInjuryPeriodService {

    @Autowired
    private PlayerInjuryRepository playerInjuryRepository;

    @Autowired
    @Lazy
    private PlayerService playerService;

    public PlayerInjuryPeriod save(PlayerInjuryPeriod playerInjuryPeriod) {
        return playerInjuryRepository.save(playerInjuryPeriod);
    }

    public PlayerInjuryPeriod findById(UUID id) {
        return playerInjuryRepository.findById(id).orElseThrow(() -> new NotFoundExpetion("Player injury not found"));
    }

    public PlayerInjuryPeriod findByPlayerId(UUID id) {
        return playerInjuryRepository.findByPlayerId(id).orElseThrow(() -> new NotFoundExpetion("Player not found"));
    }

    public PlayerInjuryPeriod createInjuryPeriod(UUID playerId, NewPlayerInjuryDTO playerInjuryDTO, User currentUser) {
        if (playerInjuryDTO.startDate().isAfter(playerInjuryDTO.endDate())) {
            throw new BadRequestException("Start date cannot be after end date");
        }
        Player foundPlayer = playerService.findById(playerId);
        if (currentUser.getUserType().equals(UserType.COACH) && !currentUser.getId().equals(foundPlayer.getTeam().getCoach().getId())) {
            throw new NotFoundExpetion("You are not allowed to add injury to this player");
        }
        PlayerInjuryPeriod playerInjuryPeriod = new PlayerInjuryPeriod(foundPlayer, playerInjuryDTO.startDate(), playerInjuryDTO.endDate());
        return save(playerInjuryPeriod);
    }

    public void delete(UUID playerId, UUID injuryId, User currentUser) {
        PlayerInjuryPeriod playerInjuryPeriod = findById(injuryId);
        Player foundPlayer = playerService.findById(playerId);
        if (currentUser.getUserType().equals(UserType.COACH) && !currentUser.getId().equals(playerInjuryPeriod.getPlayer().getTeam().getCoach().getId())) {
            throw new NotFoundExpetion("You are not allowed to delete this injury");
        }
        if (!foundPlayer.getId().equals(playerInjuryPeriod.getPlayer().getId())) {
            throw new NotFoundExpetion("Player not found");
        }
        playerInjuryRepository.delete(playerInjuryPeriod);
    }

    public PlayerInjuryPeriod update(UUID playerId, UUID injuryId, NewPlayerInjuryDTO playerInjuryDTO, User currentUser) {
        PlayerInjuryPeriod playerInjuryPeriod = findById(injuryId);
        Player foundPlayer = playerService.findById(playerId);
        if (currentUser.getUserType().equals(UserType.COACH) && !currentUser.getId().equals(playerInjuryPeriod.getPlayer().getTeam().getCoach().getId())) {
            throw new NotFoundExpetion("You are not allowed to update this injury");
        }
        if (!foundPlayer.getId().equals(playerInjuryPeriod.getPlayer().getId())) {
            throw new NotFoundExpetion("Player not found");
        }
        if (playerInjuryDTO.startDate().isAfter(playerInjuryDTO.endDate())) {
            throw new BadRequestException("Start date cannot be after end date");
        }
        playerInjuryPeriod.setStartDate(playerInjuryDTO.startDate());
        playerInjuryPeriod.setEndDate(playerInjuryDTO.endDate());
        return save(playerInjuryPeriod);
    }
}
