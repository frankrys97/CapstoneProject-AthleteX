package francescocristiano.CapstoneProject.team.service;

import francescocristiano.CapstoneProject.exceptions.NotFoundExpetion;
import francescocristiano.CapstoneProject.team.Team;
import francescocristiano.CapstoneProject.team.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public Team findById(UUID id) {
        return teamRepository.findById(id).orElseThrow(() -> new NotFoundExpetion("Team not found"));
    }
}
