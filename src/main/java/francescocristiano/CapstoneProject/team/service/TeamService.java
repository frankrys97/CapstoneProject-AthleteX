package francescocristiano.CapstoneProject.team.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import francescocristiano.CapstoneProject.coach.Coach;
import francescocristiano.CapstoneProject.exceptions.BadRequestException;
import francescocristiano.CapstoneProject.exceptions.ForbiddenException;
import francescocristiano.CapstoneProject.exceptions.NotFoundExpetion;
import francescocristiano.CapstoneProject.message.room.Room;
import francescocristiano.CapstoneProject.message.room.RoomService;
import francescocristiano.CapstoneProject.player.payload.NewPlayerAddManuallyResponseDTO;
import francescocristiano.CapstoneProject.player.payload.NewPlayerDTO;
import francescocristiano.CapstoneProject.player.playerClass.Player;
import francescocristiano.CapstoneProject.player.playerClass.PlayerPosition;
import francescocristiano.CapstoneProject.player.playerClass.PlayerStatus;
import francescocristiano.CapstoneProject.player.service.PlayerService;
import francescocristiano.CapstoneProject.team.Team;
import francescocristiano.CapstoneProject.team.payload.NewTeamDTO;
import francescocristiano.CapstoneProject.team.payload.TeamComponentsDTO;
import francescocristiano.CapstoneProject.team.repository.TeamRepository;
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

    public Team findById(UUID id) {
        return teamRepository.findById(id).orElseThrow(() -> new NotFoundExpetion("Team not found"));
    }


    public Page<Team> getAllTeamsByCoachId(UUID coachId, int page, int size, String sortBy) {
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

    public Team createTeam(NewTeamDTO teamDTO, Coach coach) {
        List<Team> teams = findAllByCoachId(coach.getId());
        if (teams.stream().anyMatch(t -> t.getName().equals(teamDTO.name())) || teams.stream().anyMatch(t -> t.getEmail().equals(teamDTO.email()))) {
            throw new BadRequestException("Team already exists");
        }
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
        if (teamDTO.avatar() != null && !teamDTO.avatar().isEmpty()) {
            try {
                uploadAvatar(newTeam.getId(), teamDTO.avatar());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return newTeam;
    }

    public Team uploadAvatar(UUID id, MultipartFile file) throws IOException {
        String cloudinaryUrl = cloudinaryService.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url").toString();
        Team foundTeam = findById(id);
        foundTeam.setAvatar(cloudinaryUrl);
        return teamRepository.save(foundTeam);
    }

    public void deleteTeamById(UUID id) {
        Team foundTeam = findById(id);
        if (foundTeam.getCommonRoom() != null) {
            UUID roomId = foundTeam.getCommonRoom().getId();
            foundTeam.setCommonRoom(null);
            teamRepository.save(foundTeam);
            roomService.deleteRoomById(roomId);
        }
        teamRepository.delete(foundTeam);
    }

    public Team updateTeam(UUID id, NewTeamDTO teamDTO) {
        Team foundTeam = findById(id);
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
                uploadAvatar(id, teamDTO.avatar());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return foundTeam;
    }

    public TeamComponentsDTO getTeamComponents(UUID id) {
        Team foundTeam = findById(id);
        Coach coach = foundTeam.getCoach();
        List<Player> players = foundTeam.getPlayers();
        return new TeamComponentsDTO(coach, players);
    }

    public NewPlayerAddManuallyResponseDTO addPlayerToTeam(UUID id, NewPlayerDTO player, Coach currentCoach) {
        if (currentCoach.getTeamCoached().stream().noneMatch(idTeam -> idTeam.getId().equals(id))) {
            throw new ForbiddenException("You are not authorized to add players to this team");
        }

        Team foundTeam = findById(id);
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
                throw new BadRequestException("Player with jersey number " + player.jerseyNumber() + " already exists");
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

    public void removePlayerFromTeam(UUID id, UUID playerId) {
        Team foundTeam = findById(id);
        Player foundPlayer = playerService.findById(playerId);
        foundTeam.getPlayers().remove(foundPlayer);
        teamRepository.save(foundTeam);
        playerService.deleteById(playerId);
    }

    public NewPlayerAddManuallyResponseDTO updatePlayer(UUID id, UUID playerId, NewPlayerDTO player, Coach currentCoach) {
        if (currentCoach.getTeamCoached().stream().noneMatch(idTeam -> idTeam.getId().equals(id))) {
            throw new ForbiddenException("You are not authorized to add players to this team");
        }
        Player foundPlayer = playerService.findById(playerId);
        Team foundTeam = findById(id);
        foundPlayer.setName(player.name());
        foundPlayer.setSurname(player.surname());
        foundPlayer.setBirthDate(player.birthDate());
        foundPlayer.setPosition(PlayerPosition.getPlayerPosition(player.position()));
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
        return new NewPlayerAddManuallyResponseDTO(foundPlayer.getId(),
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
                foundPlayer.getTeam().getCoach().getName() + " " + foundPlayer.getTeam().getCoach().getSurname());
    }
}
