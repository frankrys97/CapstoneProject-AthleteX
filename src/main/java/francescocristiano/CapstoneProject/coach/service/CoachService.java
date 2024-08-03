package francescocristiano.CapstoneProject.coach.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import francescocristiano.CapstoneProject.coach.Coach;
import francescocristiano.CapstoneProject.coach.payloads.NewUpdateCoachDTO;
import francescocristiano.CapstoneProject.coach.repository.CoachRepository;
import francescocristiano.CapstoneProject.exceptions.BadRequestException;
import francescocristiano.CapstoneProject.exceptions.NotFoundExpetion;
import francescocristiano.CapstoneProject.player.service.PlayerService;
import francescocristiano.CapstoneProject.team.Team;
import francescocristiano.CapstoneProject.team.service.TeamService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class CoachService {
    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private PasswordEncoder bcryptPasswordEncoder;

    @Autowired
    private Cloudinary cloudinaryService;

    @Autowired
    @Lazy
    private PlayerService playerService;

    @Autowired
    @Lazy
    private TeamService teamService;

    public Coach findById(UUID id) {
        return coachRepository.findById(id).orElseThrow(() -> new NotFoundExpetion("Coach not found"));
    }

    public Coach save(Coach coach) {
        return coachRepository.save(coach);
    }

    public void deleteById(UUID id) {
        coachRepository.deleteById(id);
    }

    public Coach updateCoach(NewUpdateCoachDTO body, Coach coach) {
        coach.setName(body.name());
        coach.setSurname(body.surname());
        coach.setEmail(body.email());
        coach.setUsername(body.username());
        if (body.password() != null && !body.password().isEmpty()) {
            if (body.oldPassword() != null && bcryptPasswordEncoder.matches(body.oldPassword(), coach.getPassword())) {
                coach.setPassword(bcryptPasswordEncoder.encode(body.password()));
            } else {
                throw new BadRequestException("Wrong old password");
            }
        }
        return coachRepository.save(coach);
    }

    public Coach uploadAvatar(MultipartFile file, Coach coach) throws IOException {
        String cloudinaryUrl = cloudinaryService.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url").toString();
        coach.setAvatar(cloudinaryUrl);
        return coachRepository.save(coach);
    }

    @Transactional
    public void deleteCoach(Coach coach) {
        Coach coachWithTeams = coachRepository.findById(coach.getId()).orElseThrow(() -> new NotFoundExpetion("Coach not found"));

        Hibernate.initialize(coachWithTeams.getTeamCoached());
        for (Team team : coachWithTeams.getTeamCoached()) {
            playerService.clearTeamFromPlayer(team);
        }
        for (Team team : coachWithTeams.getTeamCoached()) {
            teamService.deleteTeamById(team.getId(), coach);
        }
        coachRepository.delete(coach);
    }

}
