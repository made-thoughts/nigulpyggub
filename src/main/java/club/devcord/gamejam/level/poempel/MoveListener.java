package club.devcord.gamejam.level.poempel;

import club.devcord.gamejam.Nigulpyggub;
import club.devcord.gamejam.Team;
import club.devcord.gamejam.level.LevelPipeline;
import io.papermc.paper.math.BlockPosition;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class MoveListener implements Listener {

    private final static Map<BlockPosition, BlockData> emptyChunk;

    static {
        emptyChunk = new HashMap<>();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 70; y++) {
                    BlockPosition position = new Location(null, x + 144, y, -432 + z).toBlock();
                    emptyChunk.put(position, BlockType.AIR.createBlockData());
                }
            }
        }
    }

    private final Nigulpyggub plugin;
    private final Random random = new Random();
    private final Team team;
    private final Map<String, Set<UUID>> alreadyDone = Map.of(
        "completed", new HashSet<>(),
        "void", new HashSet<>(),
        "fart", new HashSet<>()
    );
    private final LevelPipeline pipeline;

    public MoveListener(Nigulpyggub plugin, Team team, LevelPipeline pipeline) {
        this.plugin = plugin;
        this.team = team;
        this.pipeline = pipeline;
    }

    @EventHandler
    void handlePlayerMove(PlayerMoveEvent event) {
        if (!team.players().contains(event.getPlayer())) return;

        Location location = event.getTo();
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (location.blockX() == 163 && location.blockZ() <= -423 && location.blockZ() >= -427 && alreadyDone.get("void").add(uuid)) {
            player.sendMultiBlockChange(emptyChunk);
        }

        if (location.blockX() == 152 && location.blockY() == 40 && location.blockZ() == -402 && alreadyDone.get("fart").add(uuid)) {
            player.playSound(Sound.sound(Key.key("minecraft:random.fart1"), Sound.Source.MASTER, 100, 1));
        }

        if (location.blockX() == 158 && location.blockZ() == -411) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> player.teleport(new Location(player.getWorld(), 160, 41, -411)), random.nextInt(1, 3) * 20L);
        }

        if (location.blockX() == 146 && location.blockZ() == -410) {
            var newLoc = new Location(player.getWorld(), 150, 42, -410, 180.0F, 15.0F);
            player.teleport(newLoc);
            player.sendMessage(plugin.exceptionToComponent(new OutOfMemoryError("Java is very hungry..")));
            player.playSound(Sound.sound(Key.key("minecraft:ambient.toilet"), Sound.Source.MASTER, 100, 1));
        }

        // completed
        if (location.blockZ() == -393 && location.getBlockY() < 40 && alreadyDone.get("completed").add(uuid)) {
            team.players().forEach(p -> {
                p.getInventory().addItem(PoempelLevel.THE_HOLY_POEMPEL);
            });

            pipeline.next();
        }
    }
}
