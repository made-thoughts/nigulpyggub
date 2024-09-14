package club.devcord.gamejam.level.thejump;

import club.devcord.gamejam.Team;
import club.devcord.gamejam.level.LevelPipeline;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class MoveListener implements Listener {

    private final Team team;
    private final LevelPipeline levelPipeline;

    public MoveListener(Team team, LevelPipeline levelPipeline) {
        this.team = team;
        this.levelPipeline = levelPipeline;
    }

    @EventHandler
    void handlePlayerMove(PlayerMoveEvent event) {
        if (!team.players().contains(event.getPlayer())) return;

        Location location = event.getTo();
        Player player = event.getPlayer();

        if(location.blockX() == -230 && location.blockY() > 64 && location.blockZ() == -683) {
            var item = new ItemStack(Material.ENDER_EYE);
            var meta = item.getItemMeta();
            meta.displayName(MiniMessage.miniMessage().deserialize("<rainbow>Sehhilfe"));
            item.setItemMeta(meta);
            team.players().forEach(p -> {
                p.getInventory().addItem(item);
            });
            levelPipeline.next();
        }
    }
}
