package francescocristiano.CapstoneProject.event.training;

import francescocristiano.CapstoneProject.event.Event;
import francescocristiano.CapstoneProject.event.enums.EventType;
import francescocristiano.CapstoneProject.event.enums.LocationType;
import francescocristiano.CapstoneProject.player.statistics.PlayerTrainingStats;
import francescocristiano.CapstoneProject.team.Team;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
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
public class Training extends Event {
    @Enumerated(EnumType.STRING)
    private TrainingType trainingType;

    private int duration;

    @OneToMany(mappedBy = "training")
    private List<PlayerTrainingStats> playersStatistics;

    public Training(String title, String description, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, LocalTime meetTime, LocationType locationType, Team team, TrainingType trainingType, int duration) {
        super(EventType.TRAINING, title, description, startDate, endDate, startTime, endTime, meetTime, locationType, team);
        this.trainingType = trainingType;
        this.duration = duration;
    }
}
