package club.devcord.gamejam.level.poempel;

import club.devcord.gamejam.Nigulpyggub;
import club.devcord.gamejam.Team;
import club.devcord.gamejam.level.Level;
import club.devcord.gamejam.level.LevelPipeline;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class PoempelLevel extends Level {

    public static final ItemStack THE_HOLY_POEMPEL;

    static {
        THE_HOLY_POEMPEL = new ItemStack(Material.DIAMOND_HOE);
        var meta = THE_HOLY_POEMPEL.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize("<rainbow>Pömpel"));
        meta.setCustomModelData(16);
        THE_HOLY_POEMPEL.setItemMeta(meta);
    }

    private final MoveListener moveListener;

    public PoempelLevel(Team team, Nigulpyggub plugin, LevelPipeline pipeline) {
        super(team, plugin, pipeline);
        this.moveListener = new MoveListener(plugin, team, pipeline);
    }

    public void start() {
        plugin().getServer().getPluginManager().registerEvents(moveListener, plugin());
        team().players().forEach(player -> {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<gold><bold>Find ze Pömpel!"));
            player.teleport(new Location(player.getWorld(), 163, 41, -412, 91.2F, -0.4F));
        });
    }

    @Override
    public void stop() {
        PlayerMoveEvent.getHandlerList().unregister(moveListener);
    }

}
