package club.devcord.gamejam;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public record Team(
        String name,
        Set<UUID> playerUUIDS,
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

    public Team(String name, Player creator, World world) {
        this(name, new HashSet<>(), world, randomColor());
        playerUUIDS.add(creator.getUniqueId());
    }


    public List<Player> players() {
        return playerUUIDS
                .stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .toList();
    }

    public void addPlayer(Player player) {
        playerUUIDS.add(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        playerUUIDS.remove(player.getUniqueId());
    }
}
