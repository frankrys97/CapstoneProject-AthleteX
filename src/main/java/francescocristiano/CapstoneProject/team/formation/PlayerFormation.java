package francescocristiano.CapstoneProject.team.formation;


import francescocristiano.CapstoneProject.player.playerClass.Player;
import francescocristiano.CapstoneProject.player.playerClass.PlayerPosition;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class PlayerFormation {

    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
    @ManyToOne
    @JoinColumn(name = "formation_id")
    private Formation formation;
    @Enumerated(EnumType.STRING)
    private PlayerPosition position;
}
