package francescocristiano.CapstoneProject.team.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import francescocristiano.CapstoneProject.coach.Coach;
import francescocristiano.CapstoneProject.coach.service.CoachService;
import francescocristiano.CapstoneProject.event.Event;
import francescocristiano.CapstoneProject.event.enums.EventType;
import francescocristiano.CapstoneProject.event.match.Match;
import francescocristiano.CapstoneProject.event.match.MatchService;
import francescocristiano.CapstoneProject.event.payloads.NewEventDTO;
import francescocristiano.CapstoneProject.event.service.EventService;
import francescocristiano.CapstoneProject.event.training.Training;
import francescocristiano.CapstoneProject.event.training.TrainingService;
import francescocristiano.CapstoneProject.exceptions.BadRequestException;
import francescocristiano.CapstoneProject.exceptions.ForbiddenException;
import francescocristiano.CapstoneProject.exceptions.NotFoundExpetion;
import francescocristiano.CapstoneProject.message.room.Room;
import francescocristiano.CapstoneProject.message.room.RoomService;
import francescocristiano.CapstoneProject.partecipation.service.PartecipationService;
import francescocristiano.CapstoneProject.player.payload.NewJoinTeamDTO;
import francescocristiano.CapstoneProject.player.payload.NewPlayerAddManuallyResponseDTO;
import francescocristiano.CapstoneProject.player.payload.NewPlayerDTO;
import francescocristiano.CapstoneProject.player.payload.NewUpdatePlayerByCoachDTO;
import francescocristiano.CapstoneProject.player.playerClass.Player;
import francescocristiano.CapstoneProject.player.playerClass.PlayerPosition;
import francescocristiano.CapstoneProject.player.playerClass.PlayerStatus;
import francescocristiano.CapstoneProject.player.service.PlayerService;
import francescocristiano.CapstoneProject.stadium.Stadium;
import francescocristiano.CapstoneProject.stadium.StadiumService;
import francescocristiano.CapstoneProject.stadium.payload.NewStadiumDTO;
import francescocristiano.CapstoneProject.team.Team;
import francescocristiano.CapstoneProject.team.payload.NewTeamDTO;
import francescocristiano.CapstoneProject.team.payload.TeamComponentsDTO;
import francescocristiano.CapstoneProject.team.repository.TeamRepository;
import francescocristiano.CapstoneProject.user.User;
import francescocristiano.CapstoneProject.user.enums.UserRole;
import francescocristiano.CapstoneProject.user.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private RoomService roomService;

    @Autowired
    private Cloudinary cloudinaryService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private CoachService coachService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private EventService eventService;

    @Autowired
    private StadiumService stadiumService;

    @Autowired
    private PartecipationService partecipationService;


    public Team findById(UUID id) {
        return teamRepository.findById(id).orElseThrow(() -> new NotFoundExpetion("Team not found"));
    }

    public Team save(Team team) {
        return teamRepository.save(team);
    }


    public Page<Team> getAllTeamsByCoachId(UUID coachId, int page, int size, String sortBy) {

        coachService.findById(coachId);
        if (size > 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return teamRepository.findByCoachId(coachId, pageable);
    }


    public Team findByName(String name) {
        return teamRepository.findByName(name).orElseThrow(() -> new NotFoundExpetion("Team not found"));
    }

    public Team findByEmail(String email) {
        return teamRepository.findByEmail(email).orElseThrow(() -> new NotFoundExpetion("Team not found"));
    }

    public List<Team> findAllByCoachId(UUID coachId) {
        return teamRepository.findAllByCoachId(coachId);
    }

    public Team createTeamByAdmin(NewTeamDTO teamDTO, UUID coachId) {
        Coach coach = coachService.findById(coachId);
        Team newTeam = teamRepository.save(new Team(teamDTO.name(),
                teamDTO.creationDate(),
                teamDTO.phone(),
                teamDTO.email(),
                teamDTO.address(),
                teamDTO.country(),
                teamDTO.primaryColor(),
                teamDTO.secondaryColor(),
                coach));
        Room commonRoom = new Room(teamDTO.name().concat(" - Common Room"), newTeam);
        roomService.saveRoom(commonRoom);
        newTeam.setCommonRoom(commonRoom);
        teamRepository.save(newTeam);
        return newTeam;
    }

    public Team createTeam(NewTeamDTO teamDTO, Coach coach) {
        List<Team> teams = findAllByCoachId(coach.getId());
        if (teams.stream().anyMatch(t -> t.getName().equals(teamDTO.name())) || teams.stream().anyMatch(t -> t.getEmail().equals(teamDTO.email()))) {
            throw new BadRequestException("Team already exists");
        }
   /*     Team newTeam = teamRepository.save(new Team(teamDTO.name(),
                teamDTO.creationDate(),
                teamDTO.phone(),
                teamDTO.email(),
                teamDTO.address(),
                teamDTO.country(),
                teamDTO.primaryColor(),
                teamDTO.secondaryColor(),
                coach));*/

        Team newTeam = new Team();
        newTeam.setName(teamDTO.name());
        newTeam.setCreationDate(teamDTO.creationDate());
        if (teamDTO.phone() != null && !teamDTO.phone().isEmpty()) {
            newTeam.setPhone(teamDTO.phone());
        }
        newTeam.setEmail(teamDTO.email());
        newTeam.setAddress(teamDTO.address());
        newTeam.setCountry(teamDTO.country());
        newTeam.setPrimaryColor(teamDTO.primaryColor());
        newTeam.setSecondaryColor(teamDTO.secondaryColor());
        newTeam.setCoach(coach);

        teamRepository.save(newTeam);
        Room commonRoom = new Room(teamDTO.name().concat(" - Common Room"), newTeam);
        roomService.saveRoom(commonRoom);
        newTeam.setCommonRoom(commonRoom);
        teamRepository.save(newTeam);
        if (teamDTO.avatar() != null && !teamDTO.avatar().isEmpty()) {
            try {
                uploadAvatar(newTeam.getId(), teamDTO.avatar(), coach);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return newTeam;
    }

    public Team uploadAvatar(UUID id, MultipartFile file, User currentUser) throws IOException {
        Team foundTeam = findById(id);
        if (currentUser.getUserType().equals(UserType.COACH) && !foundTeam.getCoach().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You are not allowed to update this team");
        }
        String cloudinaryUrl = cloudinaryService.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url").toString();
        foundTeam.setAvatar(cloudinaryUrl);
        return teamRepository.save(foundTeam);
    }


    @Transactional
    public void deleteTeamById(UUID id, User currentUser) {
        Team foundTeam = findById(id);
        if (currentUser.getUserType().equals(UserType.COACH) && !foundTeam.getCoach().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You are not allowed to delete this team");
        }

        if (foundTeam.getEvents() != null) {
            List<UUID> eventIds = foundTeam.getEvents().stream().map(Event::getId).toList();
            foundTeam.getEvents().clear();
            for (UUID eventId : eventIds) {
                eventService.deleteById(eventId);
            }
        }

        if (foundTeam.getPlayers() != null) {
            playerService.clearTeamFromPlayer(foundTeam);
            foundTeam.getPlayers().clear();
        }

        if (foundTeam.getCommonRoom() != null) {
            UUID roomId = foundTeam.getCommonRoom().getId();
            foundTeam.setCommonRoom(null);
            roomService.deleteRoomById(roomId);
        }

        if (foundTeam.getStadium() != null) {
            Stadium stadium = foundTeam.getStadium();
            foundTeam.setStadium(null);
            stadium.setTeam(null);
            stadiumService.save(stadium);
        }

        if (foundTeam.getPartecipations() != null) {
            partecipationService.clearPartecipationFromTeam(foundTeam.getId());
            foundTeam.getPartecipations().clear();
        }


        teamRepository.save(foundTeam);
        teamRepository.delete(foundTeam);
    }

    public Team updateTeam(UUID id, NewTeamDTO teamDTO, User currentUser) {
        Team foundTeam = findById(id);
        if (currentUser.getUserType().equals(UserType.COACH) && !foundTeam.getCoach().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You are not allowed to update this team");
        }
        foundTeam.setName(teamDTO.name());
        foundTeam.setCreationDate(teamDTO.creationDate());
        foundTeam.setPhone(teamDTO.phone());
        foundTeam.setEmail(teamDTO.email());
        foundTeam.setAddress(teamDTO.address());
        foundTeam.setCountry(teamDTO.country());
        foundTeam.setPrimaryColor(teamDTO.primaryColor());
        foundTeam.setSecondaryColor(teamDTO.secondaryColor());
        teamRepository.save(foundTeam);
        if (teamDTO.avatar() != null && !teamDTO.avatar().isEmpty()) {
            try {
                uploadAvatar(id, teamDTO.avatar(), currentUser);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return foundTeam;
    }

    public TeamComponentsDTO getTeamComponents(UUID id, User currentUser) {
        Team foundTeam = findById(id);
        if (currentUser.getUserType().equals(UserType.COACH) && !currentUser.getId().equals(foundTeam.getCoach().getId()) || currentUser.getUserType().equals(UserType.PLAYER) && foundTeam.getPlayers().stream().noneMatch(p -> p.getId().equals(currentUser.getId()))) {
            throw new ForbiddenException("You are not allowed to access this team");
        }
        Coach coach = foundTeam.getCoach();
        List<Player> players = foundTeam.getPlayers();
        return new TeamComponentsDTO(coach, players);
    }

    public NewPlayerAddManuallyResponseDTO addPlayerToTeam(UUID id, NewPlayerDTO player, User currentUser) {


        Team foundTeam = findById(id);
        if (currentUser.getUserType().equals(UserType.COACH) && !currentUser.getId().equals(foundTeam.getCoach().getId())) {
            throw new ForbiddenException("You are not allowed to add players to this team");
        }
        Player newPlayer = new Player();
        newPlayer.setName(player.name());
        newPlayer.setSurname(player.surname());
        newPlayer.setBirthDate(player.birthDate());
        newPlayer.setRole(UserRole.USER);
        newPlayer.setUserType(UserType.PLAYER);
        newPlayer.setStatus(PlayerStatus.AVAILABLE);
        newPlayer.setPosition(PlayerPosition.getPlayerPosition(player.position()));
        newPlayer.setAvatar("https://ui-avatars.com/api/?name=" + player.name() + "+" + player.surname());
        if (player.weight() != null) {
            newPlayer.setWeight(player.weight());
        }
        if (player.height() != null) {
            newPlayer.setHeight(player.height());
        }
        if (player.jerseyNumber() != null) {
            if (playerService.findByJerseyNumber(player.jerseyNumber(), id).isPresent()) {
                throw new BadRequestException("Jersey number " + player.jerseyNumber() + " already in use");
            }
            newPlayer.setJerseyNumber(player.jerseyNumber());
        }
        newPlayer.setTeam(foundTeam);
        playerService.savePlayer(newPlayer);
        foundTeam.getPlayers().add(newPlayer);
        teamRepository.save(foundTeam);
        return new NewPlayerAddManuallyResponseDTO(newPlayer.getId(),
                newPlayer.getName(),
                newPlayer.getSurname(),
                newPlayer.getBirthDate(),
                newPlayer.getUserType(),
                newPlayer.getPosition(),
                player.jerseyNumber() != null ? newPlayer.getJerseyNumber() : 0,
                player.weight() != null ? newPlayer.getWeight() : 0,
                player.height() != null ? newPlayer.getHeight() : 0,
                newPlayer.getStatus(),
                newPlayer.getTeam().getName(),
                newPlayer.getTeam().getCoach().getName() + " " + newPlayer.getTeam().getCoach().getSurname()
        );
    }

    public Team removePlayerFromTeam(UUID id, UUID playerId, User currentUser) {
        Team foundTeam = findById(id);
        if (currentUser.getUserType().equals(UserType.COACH) && !currentUser.getId().equals(foundTeam.getCoach().getId())) {
            throw new ForbiddenException("You are not allowed to remove players from this team");
        }
        Player foundPlayer = playerService.findById(playerId);
        foundPlayer.setTeam(null);
        foundTeam.getPlayers().remove(foundPlayer);
        teamRepository.save(foundTeam);
        playerService.savePlayer(foundPlayer);
        return foundTeam;
    }

    public List<Player> updatePlayer(UUID id, UUID playerId, NewUpdatePlayerByCoachDTO player, User currentUser) {
        Team foundTeam = findById(id);
        if (currentUser.getUserType().equals(UserType.COACH) && !currentUser.getId().equals(foundTeam.getCoach().getId())) {
            throw new ForbiddenException("You are not allowed to update this player");
        }
        Player foundPlayer = playerService.findById(playerId);
        foundPlayer.setName(player.name());
        foundPlayer.setSurname(player.surname());
        foundPlayer.setBirthDate(player.birthDate());
        if (player.position() != null) {
            foundPlayer.setPosition(PlayerPosition.getPlayerPosition(player.position()));

        }
        if (player.weight() != null) {
            foundPlayer.setWeight(player.weight());
        }
        if (player.height() != null) {
            foundPlayer.setHeight(player.height());
        }
        if (player.jerseyNumber() != null) {
            foundPlayer.setJerseyNumber(player.jerseyNumber());
        }
        foundPlayer.setTeam(foundTeam);
        playerService.savePlayer(foundPlayer);
     /*   return new NewPlayerAddManuallyResponseDTO(foundPlayer.getId(),
                foundPlayer.getName(),
                foundPlayer.getSurname(),
                foundPlayer.getBirthDate(),
                foundPlayer.getUserType(),
                foundPlayer.getPosition(),
                player.jerseyNumber() != null ? foundPlayer.getJerseyNumber() : 0,
                player.weight() != null ? foundPlayer.getWeight() : 0,
                player.height() != null ? foundPlayer.getHeight() : 0,
                foundPlayer.getStatus(),
                foundPlayer.getTeam().getName(),
                foundPlayer.getTeam().getCoach().getName() + " " + foundPlayer.getTeam().getCoach().getSurname());*/
        return foundTeam.getPlayers().stream().toList();
    }

    // EVENTS

    public Page<Event> findEventsByTeamId(UUID id, int page, int size, String sortBy, User currentUser) {
        Team foundTeam = findById(id);
        if (currentUser.getUserType().equals(UserType.COACH) && !foundTeam.getCoach().getId().equals(currentUser.getId()) ||
                currentUser.getUserType().equals(UserType.PLAYER) && foundTeam.getPlayers().stream().noneMatch(player -> player.getId().equals(currentUser.getId()))) {
            throw new ForbiddenException("You are not allowed to see this team's events");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return eventService.findAllByTeamId(id, pageable);
    }

    public Event addEventToTeam(UUID id, NewEventDTO event, User currentUser) {
        Team foundTeam = findById(id);
        if (currentUser.getUserType().equals(UserType.COACH) && !foundTeam.getCoach().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You are not allowed to add events to this team");
        }

        return switch (EventType.getEventType(event.eventType())) {
            case EventType.MATCH -> matchService.addMatchToTeam(foundTeam, event);
            case EventType.TRAINING -> trainingService.addTrainingToTeam(foundTeam, event);
            default -> throw new BadRequestException("Invalid event type, must be either 'MATCH' or 'TRAINING'");
        };
    }

    public void removeEventFromTeam(UUID id, UUID eventId, User currentUser) {
        Team foundTeam = findById(id);
        if (currentUser.getUserType().equals(UserType.COACH) && !foundTeam.getCoach().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You are not allowed to remove events from this team");
        }
        Event foundEvent = eventService.findById(eventId);
        foundTeam.getEvents().remove(foundEvent);
        teamRepository.save(foundTeam);
        eventService.deleteById(eventId);
    }


    public Event updateEvent(UUID id, UUID eventId, NewEventDTO event, User currentUser) {
        Team foundTeam = findById(id);
        if (currentUser.getUserType().equals(UserType.COACH) && !foundTeam.getCoach().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You are not allowed to update this event");
        }
        Event foundEvent = eventService.findById(eventId);

        switch (EventType.getEventType(event.eventType())) {
            case MATCH -> matchService.updateMatchInTeam((Match) foundEvent, event);
            case TRAINING -> trainingService.updateTrainingInTeam((Training) foundEvent, event);
            default -> throw new BadRequestException("Invalid event type, must be either 'MATCH' or 'TRAINING'");
        }
        ;
        return foundEvent;
    }

    public Stadium getStadiumForTeam(UUID id) {
        Team foundTeam = findById(id);
        return foundTeam.getStadium();
    }

    public Stadium createStadiumForTeam(UUID id, NewStadiumDTO stadium, User currentUser) {
        Team foundTeam = findById(id);
        if (currentUser.getUserType().equals(UserType.COACH) && !foundTeam.getCoach().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You are not allowed to add this stadium");
        }

        if (foundTeam.getStadium() != null) {
            throw new BadRequestException("This team already has a stadium");
        }

        List<Stadium> stadiums = stadiumService.findAll();
        for (Stadium s : stadiums) {
            if (s.getName().equals(stadium.name()) && s.getCity().equals(stadium.city())) {
                throw new BadRequestException("Stadium already exists, please choose a different name or city");
            }
        }
        Stadium newStadium = new Stadium(stadium.name(), stadium.city(), stadium.address(), stadium.capacity(), foundTeam);
        foundTeam.setStadium(newStadium);
        return stadiumService.save(newStadium);
    }

    public Team addStadiumToTeam(UUID id, UUID stadiumId, User currentUser) {
        Team foundTeam = findById(id);
        Stadium foundStadium = stadiumService.findById(stadiumId);
        if (currentUser.getUserType().equals(UserType.COACH) && !foundTeam.getCoach().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You are not allowed to add this stadium");
        }

        if (foundTeam.getStadium() != null) {
            throw new BadRequestException("This team already has a stadium");
        }

        foundTeam.setStadium(foundStadium);
        return teamRepository.save(foundTeam);
    }

    public Team removeStadiumFromTeam(UUID id, User currentUser) {
        Team foundTeam = findById(id);
        if (currentUser.getUserType().equals(UserType.COACH) && !foundTeam.getCoach().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You are not allowed to remove this stadium");
        }
        foundTeam.setStadium(null);
        return teamRepository.save(foundTeam);
    }

    public Player joinInTeamFromUser(NewJoinTeamDTO joinTeam, Player player) {
        if (player.getTeam() != null) {
            throw new BadRequestException("Player already in a team");
        }
        Team foundTeam = findById(joinTeam.teamId());
        player.setTeam(foundTeam);
        return playerService.savePlayer(player);
    }

    public void leaveTeamFromUser(Player player) {
        player.setTeam(null);
        playerService.savePlayer(player);
    }

    public Player updatePlayerAvatar(UUID teamId, UUID componentId, MultipartFile avatar, User currentUser) throws IOException {
        Team foundTeam = findById(teamId);
        if (currentUser.getUserType().equals(UserType.COACH) && !foundTeam.getCoach().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You are not allowed to update this player");
        }
        Player foundPlayer = playerService.findById(componentId);
        return playerService.uploadAvatar(avatar, foundPlayer);
    }

}
