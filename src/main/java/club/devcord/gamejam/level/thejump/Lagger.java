package club.devcord.gamejam.level.thejump;

import club.devcord.gamejam.Nigulpyggub;
import club.devcord.gamejam.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Lagger {
    private final Nigulpyggub plugin;
    private final Team team;
    private final Map<Player, List<Location>> playerLocations = new HashMap<>();
    private final Random random = new Random();
    private final List<BukkitTask> tasks = new ArrayList<>();

    public Lagger(Nigulpyggub plugin, Team team) {
        this.plugin = plugin;
        this.team = team;
    }

    public void start() {
        stop();
        tasks.add(plugin.getServer().getScheduler().runTaskTimer(plugin, this::saveLocations, 10, 10));
        tasks.add(plugin.getServer().getScheduler().runTaskTimer(plugin, this::lagPlayers, 15, 60));
    }

    public void stop() {
        tasks.forEach(BukkitTask::cancel);
    }

    private void saveLocations() {
        team.players().forEach(player -> {
            var locations = playerLocations.getOrDefault(player, new ArrayList<>());
            if(locations.size() > 3) {
                locations.removeFirst();
            }
            locations.add(player.getLocation());
            playerLocations.put(player, locations);
        });
    }

    private void lagPlayers() {
        team.players().forEach(player -> {
            var locations = playerLocations.getOrDefault(player, new ArrayList<>());
            if(random.nextInt(0, 100) < 50 && !locations.isEmpty()) {
                var location = locations.get(random.nextInt(locations.size()));
                player.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
            }
        });
    }
}
