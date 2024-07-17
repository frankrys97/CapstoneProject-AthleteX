package francescocristiano.CapstoneProject.partecipation;

import francescocristiano.CapstoneProject.team.Team;
import francescocristiano.CapstoneProject.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Partecipation {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    private StatusOfPartecipation statusOfPartecipation;

    public Partecipation(User user, Team team) {
        this.user = user;
        this.team = team;
        this.statusOfPartecipation = StatusOfPartecipation.PENDING;
    }
}
