package francescocristiano.CapstoneProject.event.service;

import francescocristiano.CapstoneProject.event.Event;
import francescocristiano.CapstoneProject.event.repository.EventRepository;
import francescocristiano.CapstoneProject.exceptions.NotFoundExpetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Page<Event> findAllByTeamId(UUID teamId, Pageable pageable) {
        return eventRepository.findByTeamId(teamId, pageable);
    }

    public Event findById(UUID id) {
        return eventRepository.findById(id).orElseThrow(() -> new NotFoundExpetion("Event not found"));
    }

    public void deleteById(UUID id) {
        eventRepository.deleteById(id);
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }
}
