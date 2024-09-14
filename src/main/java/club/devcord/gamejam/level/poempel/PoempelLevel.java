package club.devcord.gamejam.level.poempel;

import club.devcord.gamejam.Nigulpyggub;
import club.devcord.gamejam.Team;
import club.devcord.gamejam.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class PoempelLevel extends Level {

    private final MoveListener moveListener = new MoveListener();
    private final Nigulpyggub plugin;

    public PoempelLevel(Team team, Nigulpyggub plugin) {
        super(team);
        this.plugin = plugin;
    }

    public void start() {
        var item = new ItemStack(Material.IRON_AXE);
        var meta = item.getItemMeta();
        meta.setCustomModelData(16);
        item.setItemMeta(meta);

        plugin.getServer().getPluginManager().registerEvents(moveListener, plugin);
    }

    @Override
    public void stop() {
        PlayerMoveEvent.getHandlerList().unregister(moveListener);
    }

}
