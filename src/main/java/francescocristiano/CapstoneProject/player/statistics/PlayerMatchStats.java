package francescocristiano.CapstoneProject.player.statistics;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import francescocristiano.CapstoneProject.event.match.Match;
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
@JsonIgnoreProperties({"match", "player"})
public class PlayerMatchStats extends PlayerStatistics {
    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;
    private int goals;
    private int assists;
    private int yellowCards;
    private int redCards;

    public PlayerMatchStats(Player player, boolean attendance, int coachRating, String coachComment, Match match, int goals, int assists, int yellowCards, int redCards) {
        super(player, attendance, coachRating, coachComment);
        this.match = match;
        this.goals = goals;
        this.assists = assists;
        this.yellowCards = yellowCards;
        this.redCards = redCards;
    }
}
