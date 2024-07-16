package francescocristiano.CapstoneProject.player.avalibilities;

import francescocristiano.CapstoneProject.player.playerClass.Player;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class PlayerInjuryPeriod {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
    private LocalDate startDate;
    private LocalDate endDate;

    public PlayerInjuryPeriod(Player player, LocalDate startDate, LocalDate endDate) {
        this.player = player;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
