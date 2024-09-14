package club.devcord.gamejam.level.thejump;

import club.devcord.gamejam.Nigulpyggub;
import club.devcord.gamejam.Team;
import club.devcord.gamejam.level.Level;
import club.devcord.gamejam.level.LevelPipeline;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class TheJumpLevel extends Level {
    public static final ItemStack THE_EYE;

    static {
        THE_EYE = new ItemStack(Material.ENDER_EYE);
        var meta = THE_EYE.getItemMeta();
        meta.displayName(MiniMessage.miniMessage().deserialize("<rainbow>Sehhilfe"));
        THE_EYE.setItemMeta(meta);
    }

    private final MoveListener moveListener;
    private final TempleChecker templeChecker;
    private final Lagger lagger;
    private final BarrierPlacer barrierPlacer;
    private final MobSpawner mobSpawner;

    private BukkitTask templeOpenedCheckTask;

    public TheJumpLevel(Team team, Nigulpyggub plugin, LevelPipeline pipeline) {
        super(team, plugin, pipeline);
        this.moveListener = new MoveListener(team, pipeline);
        this.templeChecker = new TempleChecker(plugin, team);
        this.lagger = new Lagger(plugin, team);
        this.barrierPlacer = new BarrierPlacer(plugin, team);
        this.mobSpawner = new MobSpawner(team, plugin);
    }

    @Override
    public void start() {
        team().players().forEach(player -> {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<gold><bold>Besänftige die Pömpel Götter!"));
            player.teleport(new Location(player.getWorld(), -216, 38, -575, 90F, 0F));
        });
        plugin().getServer().getPluginManager().registerEvents(moveListener, plugin());
        templeChecker.startChecking();
        templeOpenedCheckTask = plugin().getServer().getScheduler().runTaskTimer(plugin(), this::checkTempleOpened, 0, 20);
        lagger.start();
        barrierPlacer.startPlacing();
        mobSpawner.startSpawning();
    }

    @Override
    public void stop() {
        PlayerMoveEvent.getHandlerList().unregister(moveListener);
        templeChecker.stopChecking();
        templeOpenedCheckTask.cancel();

        lagger.stop();
        barrierPlacer.stopPlacing();
        mobSpawner.stopSpawning();
    }

    private void checkTempleOpened() {
        if(templeChecker.templeOpened()) {
            lagger.stop();
            barrierPlacer.stopPlacing();
            mobSpawner.stopSpawning();
            templeOpenedCheckTask.cancel();
        }
    }
}
