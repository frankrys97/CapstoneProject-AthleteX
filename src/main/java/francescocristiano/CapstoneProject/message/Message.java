package francescocristiano.CapstoneProject.message;

import francescocristiano.CapstoneProject.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;
    private String content;
    private boolean readStatus;
    private LocalDateTime timeStamp;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public Message(User sender, User receiver, String content, boolean readStatus, LocalDateTime timeStamp, Room room) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.readStatus = readStatus;
        this.timeStamp = timeStamp;
        this.room = room;
    }
}
