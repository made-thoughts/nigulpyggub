package club.devcord.gamejam.level.thejump;

import club.devcord.gamejam.Nigulpyggub;
import club.devcord.gamejam.Team;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Random;

public class MobSpawner {

    private final List<Class<? extends Entity>> MOB_LIST = List.of(
            Zombie.class, CaveSpider.class, Skeleton.class
    );

    private final Random random = new Random();

    private final Team team;
    private final Nigulpyggub plugin;

    private BukkitTask task;

    public MobSpawner(Team team, Nigulpyggub plugin) {
        this.team = team;
        this.plugin = plugin;
    }

    public void startSpawning() {
        task = plugin.getServer().getScheduler().runTaskTimer(plugin, this::spawn, 0, 10);
    }

    private void spawn() {
        team.players().forEach(player -> {
            if (random.nextInt(0, 100) < 40) {
                Location location = player.getLocation();
                int amount = 10;
                for (int x = 0; x < amount; x++) {
                    int randomX = this.random.nextInt(1, 10);
                    location.setX(location.getBlockX() - 6 + randomX);
                    for (int z = 0; z < amount; z++) {
                        int randomZ = this.random.nextInt(1, 10);
                        location.setZ(location.getBlockZ() - 6 + randomZ);
                        World world = location.getWorld();

                        double solidY = -1;
                        for (int i = 3; i > -5; i--) {
                            location.setY(location.getY() + i);
                            if (location.getBlock().isSolid() && !location.clone().add(0, 2, 0).getBlock().isSolid()) {
                                solidY = location.getY();
                                break;
                            }
                        }
                        if (solidY == -1) continue;
                        int index = random.nextInt(0, 3);
                        Class<? extends Entity> mobType = MOB_LIST.get(index);
                        Entity mob = world.spawn(location, mobType);
                        plugin.getServer().getScheduler().runTaskLater(plugin, mob::remove, 30);
                    }
                }
            }
        });
    }

    public void stopSpawning() {
        task.cancel();
    }
}
