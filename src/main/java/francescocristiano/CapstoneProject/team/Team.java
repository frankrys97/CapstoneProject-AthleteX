package francescocristiano.CapstoneProject.team;

import francescocristiano.CapstoneProject.coach.Coach;
import francescocristiano.CapstoneProject.event.Event;
import francescocristiano.CapstoneProject.player.playerClass.Player;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Team {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private int yearOfCreation;
    private int phone;
    private String email;
    private String address;
    private String country;
    private String avatar;
    private String primaryColor;
    private String secondaryColor;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    private Coach coach;

    @OneToMany(mappedBy = "team")
    private List<Player> players;

    @OneToMany(mappedBy = "team")
    private List<Event> events;

    public Team(String name, int yearOfCreation, int phone, String email, String address, String country, String avatar, String primaryColor, String secondaryColor, Coach coach) {
        this.name = name;
        this.yearOfCreation = yearOfCreation;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.country = country;
        this.avatar = "https://ui-avatars.com/api/?name=" + name;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.coach = coach;
    }
}
