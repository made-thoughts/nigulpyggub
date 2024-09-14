package club.devcord.gamejam.level.thejump;

import club.devcord.gamejam.Nigulpyggub;
import club.devcord.gamejam.Team;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;

public class BarrierPlacer {
    private final Random random = new Random();

    private final Nigulpyggub plugin;
    private final Team team;

    private BukkitTask placeTask;

    public BarrierPlacer(Nigulpyggub plugin, Team team) {
        this.plugin = plugin;
        this.team = team;
    }

    public void startPlacing() {
        placeTask = plugin.getServer().getScheduler().runTaskTimer(plugin, this::place, 0, 40);
    }

    public void stopPlacing() {
        placeTask.cancel();
    }

    private void place() {
        team.players().forEach(player -> {
            if(random.nextInt(0, 100) < 50) {
                placeAtPlayer(player);
            }
        });
    }

    private void placeAtPlayer(Player player) {
        var direction = player.getLocation().getDirection();
        var placeLocation = player.getLocation().add(direction).add(new Vector(0, 2, 0));
        var previousBlockData = placeLocation.getBlock().getBlockData();
        if(placeLocation.getBlock().getType().name().endsWith("AIR")) {
            player.sendBlockChange(placeLocation, Material.BARRIER.createBlockData());
            plugin.getServer().getScheduler().runTaskLater(
                    plugin,
                    () -> player.sendBlockChange(placeLocation, previousBlockData),
                    40
            );
        }
    }
}
