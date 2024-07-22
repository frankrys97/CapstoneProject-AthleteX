package francescocristiano.CapstoneProject.event;

import francescocristiano.CapstoneProject.event.enums.EventType;
import francescocristiano.CapstoneProject.event.enums.LocationType;
import francescocristiano.CapstoneProject.team.Team;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Event {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private String title;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private LocalTime meetTime;

    @Enumerated(EnumType.STRING)
    private LocationType locationType;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    public Event(EventType eventType, String title, String description, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, LocalTime meetTime, LocationType locationType, Team team) {
        this.eventType = eventType;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.meetTime = meetTime;
        this.locationType = locationType;
        this.team = team;
    }
}
