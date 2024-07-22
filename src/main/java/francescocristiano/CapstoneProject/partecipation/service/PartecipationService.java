package francescocristiano.CapstoneProject.partecipation.service;

import francescocristiano.CapstoneProject.exceptions.ForbiddenException;
import francescocristiano.CapstoneProject.exceptions.NotFoundExpetion;
import francescocristiano.CapstoneProject.partecipation.NewPartecipationDTO;
import francescocristiano.CapstoneProject.partecipation.Partecipation;
import francescocristiano.CapstoneProject.partecipation.StatusOfPartecipation;
import francescocristiano.CapstoneProject.partecipation.mail.MailService;
import francescocristiano.CapstoneProject.partecipation.repository.PartecipationRepository;
import francescocristiano.CapstoneProject.team.Team;
import francescocristiano.CapstoneProject.team.service.TeamService;
import francescocristiano.CapstoneProject.user.User;
import francescocristiano.CapstoneProject.user.enums.UserType;
import francescocristiano.CapstoneProject.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PartecipationService {
    @Autowired
    private PartecipationRepository partecipationRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;


    public Partecipation createPartecipation(UUID teamId, User currentUser, NewPartecipationDTO body) {
        Team foundTeam = teamService.findById(teamId);
        if (currentUser.getUserType().equals(UserType.COACH) && !foundTeam.getCoach().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You are not allowed to create a partecipation for this team");
        }

        if (partecipationRepository.findByEmailAndStatusOfPartecipation(body.email(), StatusOfPartecipation.PENDING).isPresent()) {
            throw new ForbiddenException("Invitation already sent");
        }

        Partecipation partecipation = new Partecipation(body.email(), foundTeam);
        partecipationRepository.save(partecipation);

        String acceptUrl = "http://localhost:3001/partecipations/" + partecipation.getId() + "/accept";
        String rejectUrl = "http://localhost:3001/partecipations/" + partecipation.getId() + "/reject";

        mailService.sendInvitationEmail(body.email(), body.name(), body.surname(), acceptUrl, rejectUrl);
        return partecipation;
    }

    public Partecipation findById(UUID id) {
        return partecipationRepository.findById(id).orElseThrow(() -> new NotFoundExpetion("Partecipation not found"));
    }

    public String acceptPartecipation(UUID partecipationId) {
        Partecipation foundPartecipation = findById(partecipationId);
        UUID teamId = foundPartecipation.getTeam().getId();
        String registrationUrl = "http://localhost:5432/register?teamId=" + teamId;
        return "redirect:" + registrationUrl;
    }

    public String rejectPartecipation(UUID partecipationId) {
        Partecipation foundPartecipation = findById(partecipationId);
        foundPartecipation.setStatusOfPartecipation(StatusOfPartecipation.REJECTED);
        partecipationRepository.save(foundPartecipation);
        String thankYouUrl = "http://localhost:5432/thankyou";
        return "redirect:" + thankYouUrl;
    }

    public void acceptPartecipationByEmail(String email) {
        Partecipation partecipation = partecipationRepository.findByEmailAndStatusOfPartecipation(email, StatusOfPartecipation.PENDING).orElse(null);
        if (partecipation != null) {
            partecipation.setStatusOfPartecipation(StatusOfPartecipation.ACCEPTED);
            partecipationRepository.save(partecipation);
        }
    }


    public void deleteById(UUID id, User currentUser) {
        if (currentUser.getUserType().equals(UserType.COACH) && findById(id).getTeam().getCoach().getId() != currentUser.getId()) {
            throw new ForbiddenException("You are not allowed to delete this partecipation");
        }
        partecipationRepository.deleteById(id);
    }

    public Page<Partecipation> getAllByTeam(User currentUser, int page, int size, String sortBy, UUID teamId) {
        if (currentUser.getUserType().equals(UserType.COACH) && !currentUser.getId().equals(teamService.findById(teamId).getCoach().getId())) {
            throw new ForbiddenException("You are not allowed to see this partecipation");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Team team = teamService.findById(teamId);
        return partecipationRepository.findAllByTeam(team, pageable);
    }
}
