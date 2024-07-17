package francescocristiano.CapstoneProject.event.match;

import francescocristiano.CapstoneProject.event.Event;
import francescocristiano.CapstoneProject.location.Location;
import francescocristiano.CapstoneProject.player.statistics.PlayerMatchStats;
import francescocristiano.CapstoneProject.team.Team;
import francescocristiano.CapstoneProject.team.formation.Formation;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Match extends Event {
    private String opponent;
    private boolean home;
    @OneToOne(mappedBy = "match")
    private Formation formation;
    @OneToMany(mappedBy = "match")
    private List<PlayerMatchStats> playerMatchStats;

    public Match(String title, String description, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, LocalTime meetTime, Location location, Team team, String opponent, boolean home, Formation formation) {
        super(title, description, startDate, endDate, startTime, endTime, meetTime, location, team);
        this.opponent = opponent;
        this.home = home;
        this.formation = formation;
    }
}
