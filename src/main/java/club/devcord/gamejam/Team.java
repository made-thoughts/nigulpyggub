package club.devcord.gamejam;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public record Team(
        String name,
        List<UUID> playerUUIDS,
        World world,
        TextColor color
) {

    private static TextColor randomColor() {
        Random random = new Random();
        int red = random.nextInt(0, 256);
        int yellow = random.nextInt(0, 256);
        int blue = random.nextInt(0, 256);
        return TextColor.color(red, yellow, blue);
    }

    public Team(Player creator, World world) {
        this(creator.getName(), new ArrayList<>(), world, randomColor());
        playerUUIDS.add(creator.getUniqueId());
    }


    public List<Player> players() {
        return playerUUIDS
                .stream()
                .map(Bukkit::getPlayer)
                .toList();
    }

    public void addPlayer(Player player) {
        playerUUIDS.add(player.getUniqueId());
    }
}
