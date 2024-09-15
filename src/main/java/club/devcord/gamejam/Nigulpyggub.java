package club.devcord.gamejam;


import club.devcord.gamejam.commands.GameCommand;
import club.devcord.gamejam.commands.TeamCommand;
import club.devcord.gamejam.level.LevelPipeline;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Nigulpyggub extends JavaPlugin {

    private final Map<Team, LevelPipeline> teamPipelines = new HashMap<>();

    @Override
    public void onEnable() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(), this);
        pluginManager.registerEvents(new EventCancelers(this), this);
        pluginManager.registerEvents(new SantasBellRinger(), this);
        getServer().getPluginCommand("team").setExecutor(new TeamCommand(this));
        getServer().getPluginCommand("game").setExecutor(new GameCommand(this));

        new TabList(this).start();
//        lagger.start();
    }

    public Optional<Team> teamForName(String name) {
        return teamPipelines.keySet()
                .stream()
                .filter(team -> team.name().equalsIgnoreCase(name))
                .findAny();
    }

    public Optional<Team> teamForPlayer(Player player) {
        return teamPipelines.keySet()
                .stream()
                .filter(team -> team.players().contains(player))
                .findAny();
    }

    public Set<Team> teams() {
        return teamPipelines.keySet();
    }

    public Map<Team, LevelPipeline> teamPipelines() {
        return teamPipelines;
    }

    public Component exceptionToComponent(Throwable throwable) {
        var writer = new StringWriter();
        var output = new PrintWriter(writer);
        throwable.printStackTrace(output);

        return Component.text(writer.toString()).color(NamedTextColor.RED);
    }
}
