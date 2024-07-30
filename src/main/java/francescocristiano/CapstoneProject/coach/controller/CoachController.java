package francescocristiano.CapstoneProject.coach.controller;


import francescocristiano.CapstoneProject.coach.Coach;
import francescocristiano.CapstoneProject.coach.payloads.NewUpdateCoachDTO;
import francescocristiano.CapstoneProject.coach.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/coaches")
public class CoachController {

    @Autowired
    private CoachService coachService;

    @GetMapping("/me")
    public Coach getCoach(@AuthenticationPrincipal Coach coach) {
        return coachService.findById(coach.getId());
    }

    @PutMapping("/me")
    public Coach updateCoach(@RequestBody NewUpdateCoachDTO body, @AuthenticationPrincipal Coach coach) {
        return coachService.updateCoach(body, coach);
    }

    @PatchMapping("/me/avatar")
    public Coach uploadAvatar(@RequestParam("avatar") MultipartFile file, @AuthenticationPrincipal Coach coach) throws IOException {
        return coachService.uploadAvatar(file, coach);
    }

    @DeleteMapping("/me")
    public void deleteCoach(@AuthenticationPrincipal Coach coach) {
        coachService.deleteCoach(coach);
    }
}
