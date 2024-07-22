package francescocristiano.CapstoneProject.event.training;

import francescocristiano.CapstoneProject.event.Event;
import francescocristiano.CapstoneProject.event.enums.LocationType;
import francescocristiano.CapstoneProject.event.payloads.NewEventDTO;
import francescocristiano.CapstoneProject.exceptions.NotFoundExpetion;
import francescocristiano.CapstoneProject.team.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TrainingService {
    @Autowired
    private TrainingRepository trainingRepository;

    public Training addTrainingToTeam(Team team, NewEventDTO training) {
        return trainingRepository.save(new Training(training.title(),
                training.description(),
                training.startDate(),
                training.endDate(),
                training.startTime(),
                training.endTime(),
                training.meetTime(),
                LocationType.getLocationType(training.locationType()),
                team,
                TrainingType.getTrainingType(training.trainingType()),
                training.duration()));
    }

    public Event findById(UUID id) {
        return trainingRepository.findById(id).orElseThrow(() -> new NotFoundExpetion("Training not found"));
    }

    public Training updateTrainingInTeam(Training training, NewEventDTO newTraining) {
        training.setTitle(newTraining.title());
        training.setDescription(newTraining.description());
        training.setStartDate(newTraining.startDate());
        training.setEndDate(newTraining.endDate());
        training.setStartTime(newTraining.startTime());
        training.setEndTime(newTraining.endTime());
        training.setMeetTime(newTraining.meetTime());
        training.setLocationType(LocationType.getLocationType(newTraining.locationType()));
        training.setTrainingType(TrainingType.getTrainingType(newTraining.trainingType()));
        training.setDuration(newTraining.duration());
        return trainingRepository.save(training);
    }
}
