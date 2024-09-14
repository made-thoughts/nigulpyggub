package club.devcord.gamejam;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(MiniMessage.miniMessage().deserialize("<rainbow><bold>Welcome to our test server, have fun and enjoy our little theme park!"));

        if(!event.getPlayer().isOp()) {
            event.getPlayer().setGameMode(GameMode.ADVENTURE);
        }

        event.getPlayer().setResourcePack("https://panel.traidio.net/resourcepack.zip");
        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 2, 100, false, false));
    }
}
