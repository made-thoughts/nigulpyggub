package club.devcord.gamejam.level.eyes;

import club.devcord.gamejam.Nigulpyggub;
import club.devcord.gamejam.Team;
import club.devcord.gamejam.level.Level;
import club.devcord.gamejam.level.LevelPipeline;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.plugin.PluginManager;

public class EyesLevel extends Level {

    private final BlockShower blockShower;
    private final GateOpener opener;

    public EyesLevel(Team team, Nigulpyggub plugin, LevelPipeline pipeline) {
        super(team, plugin, pipeline);
        this.opener = new GateOpener(team);
        this.blockShower = new BlockShower(team, plugin);
    }

    @Override
    public void start() {
        team().players().forEach(player -> player.teleport(new Location(player.getWorld(), 344, 48, -460, 126, 0)));


        PluginManager pluginManager = plugin().getServer().getPluginManager();
        pluginManager.registerEvents(blockShower, plugin());
        pluginManager.registerEvents(opener, plugin());
        team().players().forEach(blockShower::setVisibility);

        blockShower.startItemSwitching();
    }

    @Override
    public void stop() {
        PlayerItemHeldEvent.getHandlerList().unregister(blockShower);
        PlayerInteractEvent.getHandlerList().unregister(opener);

        blockShower.stopItemSwitching();
    }
}
