package francescocristiano.CapstoneProject.coach.service;

import francescocristiano.CapstoneProject.coach.Coach;
import francescocristiano.CapstoneProject.coach.payloads.NewUpdateCoachDTO;
import francescocristiano.CapstoneProject.coach.repository.CoachRepository;
import francescocristiano.CapstoneProject.exceptions.NotFoundExpetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CoachService {
    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private PasswordEncoder bcryptPasswordEncoder;

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
        coach.setPassword(bcryptPasswordEncoder.encode(body.password()));
        return coachRepository.save(coach);
    }

}
