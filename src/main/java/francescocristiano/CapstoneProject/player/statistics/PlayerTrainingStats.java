package francescocristiano.CapstoneProject.player.statistics;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"player", "training"})
public class PlayerTrainingStats extends PlayerStatistics {
    @ManyToOne
    @JoinColumn(name = "training_id")
    private Training training;
    private int duration;

    public PlayerTrainingStats(Player player, boolean attendance, int coachRating, String coachComment, Training training, int duration) {
        super(player, attendance, coachRating, coachComment);
        this.training = training;
        this.duration = duration;
    }
}
