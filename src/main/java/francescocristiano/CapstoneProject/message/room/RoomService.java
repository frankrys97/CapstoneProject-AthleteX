package francescocristiano.CapstoneProject.message.room;

import francescocristiano.CapstoneProject.exceptions.NotFoundExpetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    public Room deleteRoomById(UUID id) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new NotFoundExpetion("Room not found"));
        roomRepository.delete(room);
        return room;
    }
}
