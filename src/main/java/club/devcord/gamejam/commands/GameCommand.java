package club.devcord.gamejam.commands;

import club.devcord.gamejam.Nigulpyggub;
import club.devcord.gamejam.Team;
import club.devcord.gamejam.level.Level;
import club.devcord.gamejam.level.LevelPipeline;
import club.devcord.gamejam.level.poempel.PoempelLevel;
import club.devcord.gamejam.world.WorldDuplicator;
import io.papermc.paper.configuration.type.fallback.FallbackValue;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class GameCommand implements CommandExecutor {

    private final Nigulpyggub plugin;

    public GameCommand(Nigulpyggub plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        if(!player.isOp()) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Du musst mehr Rechte tanken."));
            return false;
        }

        if (args.length != 1) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You have to use: /game start"));
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "start" -> {
                for (Team team : plugin.teams()) {
                    team.players().forEach(teamPlayer -> {
                        teamPlayer.setGameMode(GameMode.ADVENTURE);
                        Location playerLocation = teamPlayer.getLocation();
                        playerLocation.setWorld(team.world());
                        teamPlayer.teleport(playerLocation);
                    });

                    LevelPipeline levelPipeline = new LevelPipeline(team, plugin);
                    levelPipeline.start();
                    plugin.teamPipelines().put(team, levelPipeline);
                }
            }
            case "stop" -> {
                var world = plugin.getServer().getWorld("world");
                plugin.teams()
                        .stream()
                        .peek(team -> plugin.teamPipelines().get(team).stop())
                        .map(Team::players)
                        .flatMap(List::stream)
                        .forEach(p -> {
                            Location playerLocation = p.getLocation();
                            playerLocation.setWorld(world);
                            p.teleport(playerLocation);
                        });
            }
            default -> player.sendMessage(MiniMessage.miniMessage().deserialize("<red> Unknown command."));
        }
        return true;
    }
}
