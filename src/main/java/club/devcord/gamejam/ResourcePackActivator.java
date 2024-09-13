package club.devcord.gamejam;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class ResourcePackActivator implements Listener {

    @EventHandler
    public void activatePlayerResourcePack(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<rainbow><bold>Welcome to our test server, have fun and enjoy our little theme park!"));

        event.getPlayer().setResourcePack("https://panel.traidio.net/resourcepack.zip");
    }
}
