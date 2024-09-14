package club.devcord.gamejam;


import club.devcord.gamejam.commands.GameCommand;
import club.devcord.gamejam.commands.TeamCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;

import java.awt.*;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Nigulpyggub extends JavaPlugin {

    private final List<Team> teams = new ArrayList<>();
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
        return teams.stream()
                .filter(team -> team.creator().getName().equalsIgnoreCase(name))
                .findAny();
    }

    public Optional<Team> teamForPlayer(Player player) {
        return teams.stream()
                .filter(team -> team.players().contains(player))
                .findAny();
    }

    public List<Team> teams() {
        return teams;
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
