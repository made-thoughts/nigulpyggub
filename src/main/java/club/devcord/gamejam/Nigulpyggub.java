package club.devcord.gamejam;


import club.devcord.gamejam.commands.GameCommand;
import club.devcord.gamejam.commands.TeamCommand;
import club.devcord.gamejam.level.LevelPipeline;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

public class Nigulpyggub extends JavaPlugin {

    private final Map<Team, LevelPipeline> teamPipelines = new HashMap<>();
    private final Lagger lagger = new Lagger(this);

    @Override
    public void onDisable() {
        lagger.stop();
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginCommand("team").setExecutor(new TeamCommand(this));
        getServer().getPluginCommand("game").setExecutor(new GameCommand(this));
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

    public Lagger lagger() {
        return lagger;
    }

    public Component exceptionToComponent(Throwable throwable) {
        var writer = new StringWriter();
        var output = new PrintWriter(writer);
        throwable.printStackTrace(output);

        return Component.text(writer.toString()).color(NamedTextColor.RED);
    }
}
