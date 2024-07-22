package francescocristiano.CapstoneProject.stadium;

import francescocristiano.CapstoneProject.exceptions.ForbiddenException;
import francescocristiano.CapstoneProject.exceptions.NotFoundExpetion;
import francescocristiano.CapstoneProject.stadium.payload.NewStadiumDTO;
import francescocristiano.CapstoneProject.team.Team;
import francescocristiano.CapstoneProject.team.service.TeamService;
import francescocristiano.CapstoneProject.user.User;
import francescocristiano.CapstoneProject.user.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StadiumService {

    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    @Lazy
    private TeamService teamService;

    public Stadium save(Stadium stadium) {
        return stadiumRepository.save(stadium);
    }

    public Stadium findById(UUID id) {
        return stadiumRepository.findById(id).orElseThrow(() -> new NotFoundExpetion("Stadium not found"));
    }

    public void deleteById(UUID id) {
        Stadium stadium = findById(id);
        if (stadium.getTeam() != null) {
            Team team = stadium.getTeam();
            team.setStadium(null);
            teamService.save(team);
        }
        stadiumRepository.deleteById(id);
    }

    public Stadium update(NewStadiumDTO body, Stadium stadium, User currentUser) {
        if (currentUser.getUserType().equals(UserType.COACH) && !stadium.getTeam().getCoach().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You are not allowed to update this stadium");
        }
        stadium.setName(body.name());
        stadium.setCity(body.city());
        stadium.setAddress(body.address());
        stadium.setCapacity(body.capacity());
        return stadiumRepository.save(stadium);
    }

    public Page<Stadium> getAll(int page, int size, String sortBy) {
        return stadiumRepository.findAll(PageRequest.of(page, size, Sort.by(sortBy)));
    }

    public Stadium createStadium(NewStadiumDTO body) {
        List<Stadium> stadiums = stadiumRepository.findAll();

        for (Stadium stadium : stadiums) {
            if (stadium.getName().equals(body.name()) && stadium.getCity().equals(body.city())) {
                throw new ForbiddenException("Stadium already exists");
            }
        }
        Stadium stadium = new Stadium();
        stadium.setName(body.name());
        stadium.setCity(body.city());
        stadium.setAddress(body.address());
        stadium.setCapacity(body.capacity());
        return stadiumRepository.save(stadium);
    }

    public List<Stadium> findAll() {
        return stadiumRepository.findAll();
    }


}
