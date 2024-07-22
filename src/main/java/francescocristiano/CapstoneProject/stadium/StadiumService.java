package francescocristiano.CapstoneProject.stadium;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StadiumService {

    @Autowired
    private StadiumRepository stadiumRepository;

    public Stadium save(Stadium stadium) {
        return stadiumRepository.save(stadium);
    }
}
