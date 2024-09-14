package club.devcord.gamejam.level.eyes;

import club.devcord.gamejam.Nigulpyggub;
import club.devcord.gamejam.Team;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class GateOpener implements Listener {

    private final Team team;
    private final Nigulpyggub plugin;

    private boolean gateOpen = false;

    private final List<Note> NOTES = List.of(Note.E, Note.C, Note.H, Note.C, Note.E);

    private int counter = 0;

    public GateOpener(Team team, Nigulpyggub plugin) {
        this.team = team;
        this.plugin = plugin;
    }

    private enum Note {
        C, E, H;

        private static Note forButton(Location location) {
            if (isLocation(location, 354, 45, -486)) return C;
            if (isLocation(location, 354, 45, -484)) return E;
            if (isLocation(location, 354, 45, -482)) return H;
            return null;
        }

        private static boolean isLocation(Location location, int x, int y, int z) {
            return location.getBlockX() == x && location.getBlockY() == y && location.getBlockZ() == z;
        }
    }

    @EventHandler
    public void checkButtonPress(PlayerInteractEvent event) {
        if (!team.players().contains(event.getPlayer())) return;
        if (gateOpen) return;
        if (event.getClickedBlock().getType() == Material.STONE_BUTTON && event.getAction().isRightClick()) {
            Location location = event.getClickedBlock().getLocation();

            Note note = Note.forButton(location);
            if (note == null) return;

            int pitch = switch (note) {
                case C -> 0;
                case E -> 1;
                case H -> 2;
            };
            Sound sound = Sound.sound(Key.key("minecraft:instrument.sax"), Sound.Source.MASTER, 100, pitch);
            team.players().forEach(player -> player.playSound(sound));

            if (NOTES.get(counter) == note) {
                counter++;
            } else  {
                counter = 0;
            }

            if (counter == 5) {
                openGate();
            }
        }
    }

    private void openGate() {
        this.gateOpen = true;

        World world = team.world();
        BlockData airData = Material.AIR.createBlockData();
        BlockData ironData = Material.IRON_BARS.createBlockData();

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            team.players().forEach(player -> player.playSound(Sound.sound(Key.key("minecraft:ride.coaster.bar_close"), Sound.Source.MASTER, 100, 1)));
            for (int i = 0; i < 4; i++) {
                world.setBlockData(355, 45+i, -483, airData);
                world.setBlockData(355, 45+i, -485, airData);

                for (int j = 0; j < 3; j++) {
                    world.setBlockData(357, 45+i, -482 - j*2, ironData);
                }
            }
        }, 20);

    }


}
