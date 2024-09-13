package club.devcord.gamejam;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<rainbow><bold>Welcome to our test server, have fun and enjoy our little theme park!"));

        if(!event.getPlayer().isOp()) {
            event.getPlayer().setGameMode(GameMode.ADVENTURE);
        }

        event.getPlayer().setResourcePack("https://panel.traidio.net/resourcepack.zip");
    }
}
