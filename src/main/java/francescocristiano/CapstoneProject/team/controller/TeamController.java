package francescocristiano.CapstoneProject.team.controller;

import francescocristiano.CapstoneProject.coach.Coach;
import francescocristiano.CapstoneProject.exceptions.BadRequestException;
import francescocristiano.CapstoneProject.player.payload.NewPlayerAddManuallyResponseDTO;
import francescocristiano.CapstoneProject.player.payload.NewPlayerDTO;
import francescocristiano.CapstoneProject.team.Team;
import francescocristiano.CapstoneProject.team.payload.NewTeamDTO;
import francescocristiano.CapstoneProject.team.payload.TeamComponentsDTO;
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
import org.springframework.validation.annotation.Validated;
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
    public Team createTeam(@Valid @ModelAttribute NewTeamDTO teamDTO, BindingResult validationResult, @AuthenticationPrincipal User currentUser) {
        // ModelAttribute è un'annotazione che viene usata per impostare il valore di una variabile sul metodo POST come
        // body di una richiesta HTTP multipart
        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }
        return teamService.createTeam(teamDTO, (Coach) currentUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeamById(@PathVariable UUID id) {
        teamService.deleteTeamById(id);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Team updateTeam(@PathVariable UUID id, @Valid @ModelAttribute NewTeamDTO teamDTO, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }
        return teamService.updateTeam(id, teamDTO);
    }

    // COMPONENTS OF TEAMS

    @GetMapping("/{id}/components")
    public TeamComponentsDTO getTeamComponents(@PathVariable UUID id) {
        return teamService.getTeamComponents(id);
    }

    @PostMapping("/{id}/components")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public NewPlayerAddManuallyResponseDTO addPlayerToTeam(@PathVariable UUID id,
                                                           @RequestBody @Validated NewPlayerDTO player,
                                                           BindingResult validationResult,
                                                           @AuthenticationPrincipal Coach currentCoach) {

        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }


        return teamService.addPlayerToTeam(id, player, currentCoach);

    }

    @DeleteMapping("/{id}/components/{idComponent}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePlayerFromTeam(@PathVariable UUID id, @PathVariable UUID idComponent) {
        teamService.removePlayerFromTeam(id, idComponent);
    }

    @PutMapping("/{id}/components/{idComponent}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public NewPlayerAddManuallyResponseDTO updatePlayerInTeam(@PathVariable UUID id,
                                                              @PathVariable UUID idComponent,
                                                              @RequestBody @Validated NewPlayerDTO player,
                                                              BindingResult validationResult,
                                                              @AuthenticationPrincipal Coach currentCoach) {
        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }
        return teamService.updatePlayer(id, idComponent, player, currentCoach);
    }

    // TODO INSERIRE L'UPLOAD SIA DEI TEAM CHE DEI COMPONENTI

}
