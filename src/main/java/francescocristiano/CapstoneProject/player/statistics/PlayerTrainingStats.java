package francescocristiano.CapstoneProject.player.statistics;

import francescocristiano.CapstoneProject.coachRating.CoachRating;
import francescocristiano.CapstoneProject.event.training.Training;
import francescocristiano.CapstoneProject.player.playerClass.Player;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PlayerTrainingStats extends PlayerStatistics {
    @ManyToOne
    @JoinColumn(name = "training_id")
    private Training training;
    private int duration;

    public PlayerTrainingStats(Player player, boolean attendance, CoachRating coachRating, Training training, int duration) {
        super(player, attendance, coachRating);
        this.training = training;
        this.duration = duration;
    }
}
