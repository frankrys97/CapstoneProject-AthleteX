package francescocristiano.CapstoneProject.player.statistics;

import francescocristiano.CapstoneProject.coachRating.CoachRating;
import francescocristiano.CapstoneProject.player.playerClass.Player;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@NoArgsConstructor
public abstract class PlayerStatistics {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
    private boolean attendance;
    @OneToOne(mappedBy = "playerStatistics")
    private CoachRating coachRating;

    public PlayerStatistics(Player player, boolean attendance, CoachRating coachRating) {
        this.player = player;
        this.attendance = attendance;
        this.coachRating = coachRating;
    }
}
