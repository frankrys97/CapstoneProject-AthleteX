package francescocristiano.CapstoneProject.partecipation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import francescocristiano.CapstoneProject.team.Team;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"team"})
public class Partecipation {

    @Id
    @GeneratedValue
    private UUID id;

    private String email;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    private StatusOfPartecipation statusOfPartecipation;

    public Partecipation(String email, Team team) {
        this.email = email;
        this.team = team;
        this.statusOfPartecipation = StatusOfPartecipation.PENDING;
    }
}
