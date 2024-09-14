package club.devcord.gamejam.level.thejump;

import club.devcord.gamejam.Nigulpyggub;
import club.devcord.gamejam.Team;
import club.devcord.gamejam.level.Level;
import club.devcord.gamejam.level.LevelPipeline;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;

public class TheJumpLevel extends Level {
    private final MoveListener moveListener;
    private final TempleChecker templeChecker;

    public TheJumpLevel(Team team, Nigulpyggub plugin, LevelPipeline pipeline) {
        super(team, plugin, pipeline);
        this.moveListener = new MoveListener(team, pipeline);
        this.templeChecker = new TempleChecker(plugin, team);
    }

    @Override
    public void start() {
        team().players().forEach(player -> {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<gold><bold>Besänftige die Pömpel Götter!"));
            player.teleport(new Location(player.getWorld(), -216, 38, -575, 90F, 0F));
        });
        plugin().getServer().getPluginManager().registerEvents(moveListener, plugin());
        templeChecker.startChecking();
    }

    @Override
    public void stop() {
        PlayerMoveEvent.getHandlerList().unregister(moveListener);
        templeChecker.stopChecking();
    }
}
