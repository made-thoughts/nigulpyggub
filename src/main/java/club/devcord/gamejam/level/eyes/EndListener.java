package club.devcord.gamejam.level.eyes;

import club.devcord.gamejam.Team;
import club.devcord.gamejam.level.LevelPipeline;
import club.devcord.gamejam.level.thejump.TheJumpLevel;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class EndListener implements Listener {

    private final Team team;
    private final LevelPipeline levelPipeline;

    public EndListener(Team team, LevelPipeline levelPipeline) {
        this.team = team;
        this.levelPipeline = levelPipeline;
    }

    @EventHandler
    public void onEnderEyePlace(PlayerInteractEvent event) {
        if (!team.players().contains(event.getPlayer())) return;

        if (event.getMaterial() == Material.ENDER_EYE && event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.END_PORTAL_FRAME) {
            var thropy = new ItemStack(Material.DIAMOND_HOE);
            var meta = thropy.getItemMeta();
            meta.displayName(MiniMessage.miniMessage().deserialize("<rainbow>You are a Bug survivor"));
            meta.setCustomModelData(85);
            thropy.setItemMeta(meta);
            team.players().forEach(player -> {
                player.getInventory().addItem(thropy);
                player.getInventory().remove(TheJumpLevel.THE_EYE);
            });

            levelPipeline.next();
        }
    }
}
