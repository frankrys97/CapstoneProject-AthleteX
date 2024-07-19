package francescocristiano.CapstoneProject.player.service;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import francescocristiano.CapstoneProject.exceptions.NotFoundExpetion;
import francescocristiano.CapstoneProject.player.payload.NewUpdateByPlayerDTO;
import francescocristiano.CapstoneProject.player.playerClass.Player;
import francescocristiano.CapstoneProject.player.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private Cloudinary cloudinaryService;

    @Autowired
    private PasswordEncoder bcryptPasswordEncoder;


    public Optional<Player> findByJerseyNumber(int jerseyNumber, UUID teamId) {
        return playerRepository.findByJerseyNumber(jerseyNumber, teamId);
    }

    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    public Player findById(UUID id) {
        return playerRepository.findById(id).orElseThrow(() -> new NotFoundExpetion("Player not found"));
    }

    public void deleteById(UUID id) {
        playerRepository.deleteById(id);
    }


    public Player updatePLayer(NewUpdateByPlayerDTO playerDTO, Player player) {
        player.setName(playerDTO.name());
        player.setSurname(playerDTO.surname());
        player.setUsername(playerDTO.username());
        player.setBirthDate(playerDTO.birthDate());
        player.setEmail(playerDTO.email());
        player.setPassword(bcryptPasswordEncoder.encode(playerDTO.password()));
        if (playerDTO.jerseyNumber() != null && playerDTO.jerseyNumber() != player.getJerseyNumber() && player.getTeam().getPlayers().stream().noneMatch(p -> p.getJerseyNumber() == playerDTO.jerseyNumber())) {
            player.setJerseyNumber(playerDTO.jerseyNumber());
        }

        if (playerDTO.height() != null && playerDTO.height() != player.getHeight()) {
            player.setHeight(playerDTO.height());
        }

        if (playerDTO.weight() != null && playerDTO.weight() != player.getWeight()) {
            player.setWeight(playerDTO.weight());
        }

        return playerRepository.save(player);
    }


    public Player uploadAvatar(MultipartFile file, Player player) throws IOException {
        String cloudinaryUrl = cloudinaryService.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url").toString();
        player.setAvatar(cloudinaryUrl);
        return playerRepository.save(player);
    }
}
