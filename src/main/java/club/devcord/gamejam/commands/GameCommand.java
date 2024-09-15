package club.devcord.gamejam.commands;

import club.devcord.gamejam.Nigulpyggub;
import club.devcord.gamejam.Team;
import club.devcord.gamejam.level.Level;
import club.devcord.gamejam.level.LevelPipeline;
import club.devcord.gamejam.level.poempel.PoempelLevel;
import club.devcord.gamejam.world.WorldDuplicator;
import io.papermc.paper.configuration.type.fallback.FallbackValue;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        if (args.length < 1) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Du musst: /game start/stop/skip nutzen"));
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "start" -> {
                BukkitScheduler scheduler = plugin.getServer().getScheduler();
                plugin.setInGame(true);
                for (Team team : plugin.teams()) {
                    team.players().forEach(teamPlayer -> {
                        teamPlayer.setGameMode(GameMode.ADVENTURE);
                        Location playerLocation = teamPlayer.getLocation();
                        playerLocation.setWorld(team.world());
                        teamPlayer.setFlying(false);
                        teamPlayer.setAllowFlight(false);
                        teamPlayer.teleport(playerLocation);
                        teamPlayer.playSound(net.kyori.adventure.sound.Sound.sound(Key.key("minecraft:minigame.autopia.countdown"), Sound.Source.MASTER, 100, 1));

                        for (int i = 0; i < 3; i++) {
                            int count = 3 - i;
                            scheduler.runTaskLater(plugin, () -> teamPlayer.showTitle(Title.title(MiniMessage.miniMessage().deserialize("<rainbow>Spiel startet in..."), MiniMessage.miniMessage().deserialize("<white><bold>" + count))), 20 * i);
                        }
                    });

                    scheduler.runTaskLater(plugin, () -> {
                        LevelPipeline levelPipeline = new LevelPipeline(team, plugin);
                        levelPipeline.start();
                        plugin.teamPipelines().put(team, levelPipeline);
                    }, 60);
                }
            }
            case "stop" -> {
                plugin.setInGame(false);
                plugin.teams()
                        .stream()
                        .peek(team -> plugin.teamPipelines().get(team).stop())
                        .map(Team::players)
                        .flatMap(List::stream)
                        .forEach(p -> {
                            p.teleport(new Location(plugin.getServer().getWorld("world"), 90, 43, -475));
                            p.getInventory().clear();
                            p.setFlying(false);
                            p.setAllowFlight(false);
                        });
                plugin.teamPipelines().clear();

                plugin.getServer().broadcast(MiniMessage.miniMessage().deserialize("<red><bold>Spiel gestoppt!"));

                plugin.getServer().getOnlinePlayers().forEach(player1 -> player1.playerListName(player1.name()));
            }
            case "skip" -> {
                Optional<Team> team = plugin.teamForPlayer(player);
                LevelPipeline pipeline = plugin.teamPipelines().get(team.orElseThrow());

                int level = Integer.parseInt(args[1]);
                for (int i = 0; i < level; i++) {
                    pipeline.next();
                }
            }
            default -> player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Unbekannter Command."));
        }
        return true;
    }
}
