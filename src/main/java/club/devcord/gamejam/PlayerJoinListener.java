package club.devcord.gamejam;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(MiniMessage.miniMessage().deserialize("<rainbow><bold>Willkommen in unserem kleinen Themenpark!"));
        player.sendMessage(MiniMessage.miniMessage().deserialize("<blue><bold>Nutze /team create um ein Team zu erstellen oder /team join <name> um einem Team beizutreten!"));

        if(!player.isOp()) {
            player.setGameMode(GameMode.ADVENTURE);
        }

        player.setResourcePack("https://panel.traidio.net/resourcepack.zip");
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 2, 100, false, false));

        player.teleport(new Location(player.getWorld(), 90, 43, -475));
    }
}
