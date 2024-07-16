package francescocristiano.CapstoneProject.coachRating;

import francescocristiano.CapstoneProject.coach.Coach;
import francescocristiano.CapstoneProject.player.statistics.PlayerStatistics;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class CoachRating {
    @Id
    @GeneratedValue
    private UUID id;
    private int rating;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    private Coach coach;

    @OneToOne
    private PlayerStatistics playerStatistics;

    public CoachRating(int rating, String comment, Coach coach, PlayerStatistics playerStatistics) {
        this.rating = rating;
        this.comment = comment;
        this.coach = coach;
        this.playerStatistics = playerStatistics;
    }
}
