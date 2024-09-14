package club.devcord.gamejam.level.thejump;

import club.devcord.gamejam.Nigulpyggub;
import club.devcord.gamejam.Team;
import club.devcord.gamejam.level.poempel.PoempelLevel;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.List;

public class TempleChecker {

    private final Nigulpyggub plugin;
    private final Team team;

    private BukkitTask task;
    private boolean templeOpened;

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

            world.playEffect(location, Effect.MOBSPAWNER_FLAMES, null);
            team.players().forEach(player -> player.playSound(Sound.sound(Key.key("minecraft:effect.questcompleted"), Sound.Source.MASTER, 100, 1)));

            removeGate();

            templeOpened = true;
        }
    }

    private void removeGate() {
        World world = team.world();
        BlockData voidBlockData = Material.VOID_AIR.createBlockData();
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 3; y++) {
                world.setBlockData(new Location(world, -243 - x, 35 + y, -673), voidBlockData);
            }
        }

        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.runTaskLater(plugin, () -> {
            team.players().forEach(player -> player.playSound(Sound.sound(Key.key("minecraft:ride.coaster.bar_open"), Sound.Source.MASTER, 100, 1)));

            world.setBlockData(new Location(world, -246, 37, -672), voidBlockData);
            world.setBlockData(new Location(world, -245, 38, -672), voidBlockData);
            world.setBlockData(new Location(world, -244, 37, -672), voidBlockData);
        }, 20*3);

        scheduler.runTaskLater(plugin, () -> {
            world.setBlockData(new Location(world, -245, 37, -672), voidBlockData);
            world.setBlockData(new Location(world, -246, 36, -672), voidBlockData);
            world.setBlockData(new Location(world, -244, 36, -672), voidBlockData);
        }, 20*4);
    }

    public void stopChecking() {
        task.cancel();
    }

    public boolean templeOpened() {
        return templeOpened;
    }
}
