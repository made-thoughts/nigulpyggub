package club.devcord.gamejam.level.thejump;

import club.devcord.gamejam.Nigulpyggub;
import club.devcord.gamejam.Team;
import club.devcord.gamejam.level.poempel.PoempelLevel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.List;

public class TempleChecker {

    private final Nigulpyggub plugin;
    private final Team team;

    private BukkitTask task;

    public TempleChecker(Nigulpyggub plugin, Team team) {
        this.plugin = plugin;
        this.team = team;
    }

    public void startChecking() {
        task = plugin.getServer().getScheduler().runTaskTimer(plugin, this::checkForTemple, 0, 5);
    }

    private void checkForTemple() {
        World world = team.world();
        Location location = new Location(world, -245, 35, -671);
        Collection<Entity> entities = world.getNearbyEntities(location, 1, 1, 1);
        List<Item> poempelEntities = entities
                .stream()
                .filter(entity -> entity instanceof Item)
                .map(entity -> (Item) entity)
                .filter(item -> item.getItemStack().isSimilar(PoempelLevel.THE_HOLY_POEMPEL))
                .toList();
        if (poempelEntities.size() == team.players().size()) {
            poempelEntities.forEach(Entity::remove);
            removeGate();
        }
    }

    private void removeGate() {
        World world = team.world();
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 3; y++) {
                world.setBlockData(new Location(world, -243 + x, 35 - y, -675), Material.VOID_AIR.createBlockData());
            }
        }
    }

    public void stopChecking() {
        task.cancel();
    }
}
