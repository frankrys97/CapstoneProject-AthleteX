package francescocristiano.CapstoneProject.team;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import francescocristiano.CapstoneProject.coach.Coach;
import francescocristiano.CapstoneProject.event.Event;
import francescocristiano.CapstoneProject.message.room.Room;
import francescocristiano.CapstoneProject.partecipation.Partecipation;
import francescocristiano.CapstoneProject.player.playerClass.Player;
import francescocristiano.CapstoneProject.stadium.Stadium;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"players", "events", "stadium", "commonRoom"})
public class Team {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private LocalDate creationDate;
    private String phone;
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

    @OneToOne
    @JoinColumn(name = "stadium_id")
    private Stadium stadium;

    @OneToOne
    @JoinColumn(name = "common_room_id")
    private Room commonRoom;

    @OneToMany(mappedBy = "team")
    private List<Partecipation> partecipations;


    public Team(String name, LocalDate creationDate, String phone, String email, String address, String country, String primaryColor, String secondaryColor, Coach coach) {
        this.name = name;
        this.creationDate = creationDate;
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
