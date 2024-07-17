package francescocristiano.CapstoneProject.team.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import francescocristiano.CapstoneProject.coach.Coach;
import francescocristiano.CapstoneProject.exceptions.BadRequestException;
import francescocristiano.CapstoneProject.exceptions.NotFoundExpetion;
import francescocristiano.CapstoneProject.message.room.Room;
import francescocristiano.CapstoneProject.message.room.RoomService;
import francescocristiano.CapstoneProject.team.Team;
import francescocristiano.CapstoneProject.team.payload.NewTeamDTO;
import francescocristiano.CapstoneProject.team.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private RoomService roomService;

    @Autowired
    private Cloudinary cloudinaryService;

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
        teamRepository.deleteById(id);
    }
}
