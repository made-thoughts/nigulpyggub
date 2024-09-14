package club.devcord.gamejam.level.poempel;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    @EventHandler
    void handlePlayerMove(PlayerMoveEvent event) {
        Location location = event.getTo();

        if (location.blockX() == 163 && location.blockZ() <= -422 && location.blockZ() <= -426) {
            event.getPlayer().sendMessage("test");
        }
    }
}
