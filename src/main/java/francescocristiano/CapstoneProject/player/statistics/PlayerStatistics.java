package francescocristiano.CapstoneProject.player.statistics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import francescocristiano.CapstoneProject.player.playerClass.Player;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"player"})
public abstract class PlayerStatistics {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
    private boolean attendance;
    private int coachRating;
    private String coachComment;

    public PlayerStatistics(Player player, boolean attendance, int coachRating, String coachComment) {
        this.player = player;
        this.attendance = attendance;
        this.coachRating = coachRating;
        this.coachComment = coachComment;
    }
}
