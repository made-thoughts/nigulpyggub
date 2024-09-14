package club.devcord.gamejam.level.eyes;

import club.devcord.gamejam.Nigulpyggub;
import club.devcord.gamejam.Team;
import club.devcord.gamejam.level.Level;
import club.devcord.gamejam.level.LevelPipeline;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;

public class EyesLevel extends Level {

    private final BlockShower blockShower;
    private final GateOpener opener;
    private final EndListener endListener;

    public EyesLevel(Team team, Nigulpyggub plugin, LevelPipeline pipeline) {
        super(team, plugin, pipeline);
        this.opener = new GateOpener(team, plugin);
        this.blockShower = new BlockShower(team, plugin);
        this.endListener = new EndListener(team, pipeline);
    }

    @Override
    public void start() {
        team().players().forEach(player -> player.teleport(new Location(player.getWorld(), 344, 48, -460, 126, 0)));


        PluginManager pluginManager = plugin().getServer().getPluginManager();
        pluginManager.registerEvents(blockShower, plugin());
        pluginManager.registerEvents(opener, plugin());
        pluginManager.registerEvents(endListener, plugin());
        team().players().forEach(blockShower::setVisibility);

        blockShower.startItemSwitching();
    }

    @Override
    public void stop() {
        PlayerItemHeldEvent.getHandlerList().unregister(blockShower);
        PlayerMoveEvent.getHandlerList().unregister(blockShower);
        PlayerInteractEvent.getHandlerList().unregister(opener);
        PlayerInteractEvent.getHandlerList().unregister(endListener);

        blockShower.stopItemSwitching();
    }
}
