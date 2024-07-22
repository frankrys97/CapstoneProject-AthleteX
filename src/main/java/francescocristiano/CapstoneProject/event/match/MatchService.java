package francescocristiano.CapstoneProject.event.match;

import francescocristiano.CapstoneProject.event.payloads.NewEventDTO;
import francescocristiano.CapstoneProject.exceptions.NotFoundExpetion;
import francescocristiano.CapstoneProject.team.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MatchService {
    @Autowired
    private MatchRepository matchRepository;


    public Match addMatchToTeam(Team team, NewEventDTO match) {
        return matchRepository.save(new Match(match.title(),
                match.description(),
                match.startDate(),
                match.endDate(),
                match.startTime(),
                match.endTime(),
                match.meetTime(),
                team,
                match.opponent(),
                match.home()));
    }

    public Match findById(UUID id) {
        return matchRepository.findById(id).orElseThrow(() -> new NotFoundExpetion("Match not found"));
    }

    public Match updateMatchInTeam(Match match, NewEventDTO newMatch) {
        match.setTitle(newMatch.title());
        match.setDescription(newMatch.description());
        match.setStartDate(newMatch.startDate());
        match.setEndDate(newMatch.endDate());
        match.setStartTime(newMatch.startTime());
        match.setEndTime(newMatch.endTime());
        match.setMeetTime(newMatch.meetTime());
        match.setOpponent(newMatch.opponent());
        match.setHome(newMatch.home());
        return matchRepository.save(match);
    }
}
