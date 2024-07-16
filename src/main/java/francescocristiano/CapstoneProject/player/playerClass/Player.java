package francescocristiano.CapstoneProject.player.playerClass;

import francescocristiano.CapstoneProject.player.avalibilities.PlayerInjuryPeriod;
import francescocristiano.CapstoneProject.player.statistics.PlayerStatistics;
import francescocristiano.CapstoneProject.team.Team;
import francescocristiano.CapstoneProject.user.User;
import francescocristiano.CapstoneProject.user.UserRole;
import francescocristiano.CapstoneProject.user.UserType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Player extends User {
    @Enumerated(EnumType.STRING)
    private PlayerPosition position;
    private int jerseyNumber;
    @Enumerated(EnumType.STRING)
    private PlayerStatus status;
    private LocalDate birthDate;
    private double weight;
    private double height;
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
    @OneToMany(mappedBy = "player")
    private List<PlayerStatistics> statistics;
    @OneToMany(mappedBy = "player")
    private List<PlayerInjuryPeriod> injuryPeriods;
    @OneToMany(mappedBy = "sender")
    private List<Message> sentMessages;
    @OneToMany(mappedBy = "receiver")
    private List<Message> receivedMessages;


    public Player(String nome, String cognome, String username, String password, String email, PlayerPosition position, int jerseyNumber, PlayerStatus status, LocalDate birthDate, double weight, double height, Team team) {
        super(nome, cognome, username, password, email, UserRole.USER, UserType.PLAYER);
        this.position = position;
        this.jerseyNumber = jerseyNumber;
        this.status = status;
        this.birthDate = birthDate;
        this.weight = weight;
        this.height = height;
        this.team = team;
    }
}
