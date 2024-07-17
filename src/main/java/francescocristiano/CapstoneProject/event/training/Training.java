package francescocristiano.CapstoneProject.event.training;

import francescocristiano.CapstoneProject.event.Event;
import francescocristiano.CapstoneProject.excercise.Exercise;
import francescocristiano.CapstoneProject.player.statistics.PlayerTrainingStats;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Training extends Event {
    @Enumerated(EnumType.STRING)
    private TrainingType trainingType;

    @ManyToMany(mappedBy = "trainings")
    private List<Exercise> exercises;

    private int duration;

    @OneToMany(mappedBy = "training")
    private List<PlayerTrainingStats> playersStatistics;
}
