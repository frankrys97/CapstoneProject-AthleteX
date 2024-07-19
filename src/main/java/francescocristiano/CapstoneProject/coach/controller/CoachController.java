package francescocristiano.CapstoneProject.coach.controller;


import francescocristiano.CapstoneProject.coach.Coach;
import francescocristiano.CapstoneProject.coach.payloads.NewUpdateCoachDTO;
import francescocristiano.CapstoneProject.coach.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coaches")
public class CoachController {

    @Autowired
    private CoachService coachService;

    @PutMapping("/me")
    public Coach updateCoach(@RequestBody NewUpdateCoachDTO body, @AuthenticationPrincipal Coach coach) {
        return coachService.updateCoach(body, coach);
    }
}
