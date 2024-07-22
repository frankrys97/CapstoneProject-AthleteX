package francescocristiano.CapstoneProject.player.statistics.service;

import francescocristiano.CapstoneProject.coach.Coach;
import francescocristiano.CapstoneProject.coach.service.CoachService;
import francescocristiano.CapstoneProject.event.Event;
import francescocristiano.CapstoneProject.event.match.Match;
import francescocristiano.CapstoneProject.event.service.EventService;
import francescocristiano.CapstoneProject.event.training.Training;
import francescocristiano.CapstoneProject.exceptions.BadRequestException;
import francescocristiano.CapstoneProject.exceptions.ForbiddenException;
import francescocristiano.CapstoneProject.exceptions.NotFoundExpetion;
import francescocristiano.CapstoneProject.player.playerClass.Player;
import francescocristiano.CapstoneProject.player.service.PlayerService;
import francescocristiano.CapstoneProject.player.statistics.PlayerMatchStats;
import francescocristiano.CapstoneProject.player.statistics.PlayerStatistics;
import francescocristiano.CapstoneProject.player.statistics.PlayerTrainingStats;
import francescocristiano.CapstoneProject.player.statistics.payload.NewPlayerStatsDTO;
import francescocristiano.CapstoneProject.player.statistics.repository.PlayerStatisticsRepository;
import francescocristiano.CapstoneProject.team.Team;
import francescocristiano.CapstoneProject.team.service.TeamService;
import francescocristiano.CapstoneProject.user.User;
import francescocristiano.CapstoneProject.user.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PlayerStatisticsService {
    @Autowired
    private PlayerStatisticsRepository playerStatisticsRepository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private CoachService coachService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private EventService eventService;

    private static PlayerMatchStats getPlayerMatchStats(NewPlayerStatsDTO playerStatistics, PlayerMatchStats playerStatisticsToUpdate) {
        playerStatisticsToUpdate.setAttendance(playerStatistics.attendance());
        playerStatisticsToUpdate.setCoachRating(playerStatistics.coachRating());
        playerStatisticsToUpdate.setCoachComment(playerStatistics.coachComment());
        playerStatisticsToUpdate.setGoals(playerStatistics.goals());
        playerStatisticsToUpdate.setAssists(playerStatistics.assists());
        playerStatisticsToUpdate.setRedCards(playerStatistics.redCards());
        playerStatisticsToUpdate.setYellowCards(playerStatistics.yellowCards());
        return playerStatisticsToUpdate;
    }

    public Page<PlayerStatistics> getAllByPlayerId(UUID playerId, int page, int size, String sortBy, User currentUser) {

        Player player = playerService.findById(playerId);
        Team playerTeam = player.getTeam();

        if (currentUser.getUserType().equals(UserType.COACH)) {
            Coach coach = coachService.findById(currentUser.getId());
            if (!coach.getTeamCoached().contains(playerTeam)) {
                throw new ForbiddenException("Coach is not allowed to view statistics for players outside their team");
            }
        }

        if (currentUser.getUserType().equals(UserType.PLAYER)) {
            if (!currentUser.getId().equals(playerId)) {
                throw new ForbiddenException("Player can only view their own statistics");
            }
        }


        if (size > 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        return playerStatisticsRepository.findAllByPlayerId(playerId, pageable);
    }

    public PlayerStatistics findById(UUID id) {
        return playerStatisticsRepository.findById(id).orElseThrow(() -> new NotFoundExpetion("Player statistics not found"));
    }

    public PlayerStatistics create(UUID playerId, UUID eventId, NewPlayerStatsDTO playerStatistics, User currentUser) {
        Player player = playerService.findById(playerId);
        Event event = eventService.findById(eventId);


        if (currentUser.getUserType().equals(UserType.COACH)) {
            Coach coach = coachService.findById(currentUser.getId());
            if (!coach.getTeamCoached().contains(player.getTeam())) {
                throw new ForbiddenException("Coach is not allowed to create statistics for players outside their team");
            }
        }

        switch (event.getEventType()) {
            case TRAINING -> {
                Training training = (Training) event;
                if (training.getPlayersStatistics().stream().anyMatch(p -> p.getPlayer().getId().equals(playerId))) {
                    throw new BadRequestException("Player already has statistics for this training");
                }
                return playerStatisticsRepository.save(new PlayerTrainingStats(
                        player,
                        playerStatistics.attendance(),
                        playerStatistics.coachRating(), playerStatistics.coachComment(),
                        (Training) event, playerStatistics.duration()));
            }
            case MATCH -> {
                Match match = (Match) event;
                if (match.getPlayerMatchStats().stream().anyMatch(p -> p.getPlayer().getId().equals(playerId))) {
                    throw new BadRequestException("Player already has statistics for this match");
                }

                return playerStatisticsRepository.save(new PlayerMatchStats(
                        player,
                        playerStatistics.attendance(),
                        playerStatistics.coachRating(), playerStatistics.coachComment(),
                        (Match) event, playerStatistics.goals(),
                        playerStatistics.assists(),
                        playerStatistics.redCards(),
                        playerStatistics.yellowCards()));
            }
            default -> {
                throw new BadRequestException("Invalid event type, must be either 'TRAINING' or 'MATCH'");
            }
        }


    }

    public void delete(UUID id, User currentUser) {
        PlayerStatistics playerStatistics = findById(id);
        if (currentUser.getUserType().equals(UserType.COACH)) {
            Coach coach = coachService.findById(currentUser.getId());
            if (coach.getTeamCoached().stream().noneMatch(t -> t.getId().equals(playerStatistics.getPlayer().getTeam().getId()))) {
                throw new ForbiddenException("Coach is not allowed to delete statistics for players outside their team");
            }
        }
        playerStatisticsRepository.deleteById(id);
    }

    public PlayerStatistics update(UUID id, NewPlayerStatsDTO playerStatistics, User currentUser) {
        PlayerStatistics playerStatisticsToUpdate = findById(id);
        if (currentUser.getUserType().equals(UserType.COACH)) {
            Coach coach = coachService.findById(currentUser.getId());
            if (coach.getTeamCoached().stream().noneMatch(t -> t.getId().equals(playerStatisticsToUpdate.getPlayer().getTeam().getId()))) {
                throw new ForbiddenException("Coach is not allowed to update statistics for players outside their team");
            }
        }

        if (playerStatisticsToUpdate.getClass().equals(PlayerTrainingStats.class)) {
            PlayerTrainingStats playerTrainingStats = (PlayerTrainingStats) playerStatisticsToUpdate;
            playerTrainingStats.setAttendance(playerStatistics.attendance());
            playerTrainingStats.setCoachRating(playerStatistics.coachRating());
            playerTrainingStats.setCoachComment(playerStatistics.coachComment());
            playerTrainingStats.setDuration(playerStatistics.duration());
            return playerStatisticsRepository.save(playerTrainingStats);
        } else if (playerStatisticsToUpdate.getClass().equals(PlayerMatchStats.class)) {
            PlayerMatchStats playerMatchStats = getPlayerMatchStats(playerStatistics, (PlayerMatchStats) playerStatisticsToUpdate);
            return playerStatisticsRepository.save(playerMatchStats);
        } else {
            throw new BadRequestException("Invalid player statistics type, must be either 'PLAYER_TRAINING_STATS' or 'PLAYER_MATCH_STATS'");
        }
    }


}
