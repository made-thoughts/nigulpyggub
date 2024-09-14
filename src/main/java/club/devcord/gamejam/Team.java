package club.devcord.gamejam;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public record Team(
        String name,
        List<UUID> playerUUIDS,
        World world
) {

    public Team(Player creator, World world) {
        this(creator.getName(), new ArrayList<>(), world);
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
