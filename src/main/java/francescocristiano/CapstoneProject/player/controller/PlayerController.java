package francescocristiano.CapstoneProject.player.controller;

import francescocristiano.CapstoneProject.player.payload.NewUpdateByPlayerDTO;
import francescocristiano.CapstoneProject.player.playerClass.Player;
import francescocristiano.CapstoneProject.player.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/me")
    public Player getPlayer(@AuthenticationPrincipal Player player) {
        return playerService.findById(player.getId());
    }

    @PutMapping("/me")
    public Player updatePlayer(@RequestBody NewUpdateByPlayerDTO player, @AuthenticationPrincipal Player user) {
        return playerService.updatePLayer(player, user);
    }

    @PatchMapping("/me/avatar")
    public Player uploadAvatar(@RequestParam("avatar") MultipartFile file, @AuthenticationPrincipal Player user) throws IOException {
        return playerService.uploadAvatar(file, user);
    }

    @DeleteMapping("/me")
    public void deletePlayer(@AuthenticationPrincipal Player user) {
        playerService.deleteById(user.getId());
    }
}
