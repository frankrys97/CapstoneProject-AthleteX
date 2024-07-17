package francescocristiano.CapstoneProject.team.controller;

import francescocristiano.CapstoneProject.coach.Coach;
import francescocristiano.CapstoneProject.exceptions.BadRequestException;
import francescocristiano.CapstoneProject.team.Team;
import francescocristiano.CapstoneProject.team.payload.NewTeamDTO;
import francescocristiano.CapstoneProject.team.service.TeamService;
import francescocristiano.CapstoneProject.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping
    public Page<Team> getAllTeamsByCoach(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "id") String sortBy, @AuthenticationPrincipal User currentUser) {
        return teamService.getAllTeamsByCoachId(currentUser.getId(), page, size, sortBy);
    }

    @PostMapping(consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Team createTeam(@Valid @ModelAttribute NewTeamDTO teamDTO, @AuthenticationPrincipal User currentUser, BindingResult validationResult) {
        // ModelAttribute è un'annotazione che viene usata per impostare il valore di una variabile sul metodo POST come
        // body di una richiesta HTTP multipart
        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).
                    collect(Collectors.joining(", ")));
        }
        return teamService.createTeam(teamDTO, (Coach) currentUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeamById(@PathVariable UUID id) {
        teamService.deleteTeamById(id);
    }
}
