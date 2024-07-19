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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @PreAuthorize("hasAuthority('COACH')")
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
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COACH')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeamById(@PathVariable UUID id, @AuthenticationPrincipal User currentUser) {
        teamService.deleteTeamById(id, currentUser);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COACH')")
    public Team updateTeam(@PathVariable UUID id, @Valid @ModelAttribute NewTeamDTO teamDTO, BindingResult validationResult, @AuthenticationPrincipal User currentUser) {
        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }
        return teamService.updateTeam(id, teamDTO, currentUser);
    }

    @PatchMapping("/{id}/avatar")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COACH')")
    public Team uploadAvatar(@PathVariable UUID id, @RequestParam("avatar") MultipartFile file, @AuthenticationPrincipal User currentUser) throws IOException {
        return teamService.uploadAvatar(id, file, currentUser);
    }

    // COMPONENTS OF TEAMS

    @GetMapping("/{id}/components")
    public TeamComponentsDTO getTeamComponents(@PathVariable UUID id, @AuthenticationPrincipal User currentUser) {
        return teamService.getTeamComponents(id, currentUser);
    }

    @PostMapping("/{id}/components")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COACH')")
    @ResponseStatus(HttpStatus.CREATED)
    public NewPlayerAddManuallyResponseDTO addPlayerToTeam(@PathVariable UUID id,
                                                           @RequestBody @Validated NewPlayerDTO player,
                                                           BindingResult validationResult,
                                                           @AuthenticationPrincipal User currentUser) {

        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }


        return teamService.addPlayerToTeam(id, player, currentUser);

    }

    @DeleteMapping("/{id}/components/{idComponent}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COACH')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePlayerFromTeam(@PathVariable UUID id, @PathVariable UUID idComponent, @AuthenticationPrincipal User currentUser) {
        teamService.removePlayerFromTeam(id, idComponent, currentUser);
    }

    @PutMapping("/{id}/components/{idComponent}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COACH')")
    public NewPlayerAddManuallyResponseDTO updatePlayerInTeam(@PathVariable UUID id,
                                                              @PathVariable UUID idComponent,
                                                              @RequestBody @Validated NewPlayerDTO player,
                                                              BindingResult validationResult,
                                                              @AuthenticationPrincipal User currentUser) {
        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", ")));
        }
        return teamService.updatePlayer(id, idComponent, player, currentUser);
    }


}
