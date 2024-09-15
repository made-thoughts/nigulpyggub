package club.devcord.gamejam.level.eyes;

import club.devcord.gamejam.Nigulpyggub;
import club.devcord.gamejam.Team;
import club.devcord.gamejam.level.thejump.TheJumpLevel;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class BlockShower implements Listener {

    private final Random random = new Random();

    private final Team team;
    private final Nigulpyggub plugin;

    private BukkitTask itemSwitchTask;

    public BlockShower(Team team, Nigulpyggub plugin) {
        this.team = team;
        this.plugin = plugin;
    }

    public void startItemSwitching() {
        itemSwitchTask = plugin.getServer().getScheduler().runTaskTimer(plugin, this::switchItems, 20, 20);
    }

    public void stopItemSwitching() {
        itemSwitchTask.cancel();
    }

    private void switchItems() {
        team.players().forEach(player -> {
            PlayerInventory inventory = player.getInventory();
            if(inventory.getItemInMainHand().isSimilar(TheJumpLevel.THE_EYE) && random.nextInt(0, 100) < 40) {
                var newSlot = random.nextInt(0, 8);
                while(inventory.getItem(newSlot) != null
                        && !inventory.getItem(newSlot).isEmpty()) {
                    newSlot = random.nextInt(0, 8);
                }

                var air = new ItemStack(Material.AIR);
                inventory.setItem(inventory.getHeldItemSlot(), air);
                inventory.setItem(newSlot, TheJumpLevel.THE_EYE);
                setVisibility(player);
            }
        });
    }

    @EventHandler
    public void onFall(PlayerMoveEvent event) {
        if(!team.players().contains(event.getPlayer())) return;

        var player = event.getPlayer();
        var loc = player.getLocation();

        if(loc.getBlockX() >= 361 && loc.getBlockX() <= 377) {
            if(loc.getBlockZ() >= -492 && loc.getBlockZ() <= -482) {
                if(loc.getBlockY() < 27) {
                    player.teleport(new Location(player.getWorld(), 359, 29, -487, -90, 0));
                }
            }
        }

        if(loc.getBlockX() >= 369 && loc.getBlockX() <= 396) {
            if(loc.getBlockZ() >= -474 && loc.getBlockZ() <= -442) {
                if(loc.getBlockY() < 26) {
                    player.teleport(new Location(player.getWorld(), 382, 28, -475, 0, 0));
                }
            }
        }
    }

    @EventHandler
    public void onItemSwitch(PlayerItemHeldEvent event) {
        if(!team.players().contains(event.getPlayer())) return;

        var player = event.getPlayer();

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> setVisibility(player), 2);
    }

    public void setVisibility(Player player) {
        var itemInHand = player.getInventory().getItemInMainHand();
        if(itemInHand.isSimilar(TheJumpLevel.THE_EYE)) {
            showBlocks(player, Material.BONE_BLOCK.createBlockData());
        } else {
            showBlocks(player, Material.BARRIER.createBlockData());
        }
    }

    private void showBlocks(Player player, BlockData blockData) {
        for (int x = 361; x < 377; x++) {
            for (int z = -492; z < -482; z++) {
                var location = new Location(player.getWorld(), x, 28, z);
                if(location.getBlock().getType() == Material.BARRIER) {
                    player.sendBlockChange(location, blockData);
                }
            }
        }

        for (int x = 369; x < 396; x++) {
            for (int z = -474; z < -442; z++) {
                var location = new Location(player.getWorld(), x, 27, z);
                if(location.getBlock().getType() == Material.BARRIER) {
                    player.sendBlockChange(location, blockData);
                }
            }
        }
    }
}
