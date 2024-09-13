package club.devcord.gamejam.commands;

import club.devcord.gamejam.Nigulpyggub;
import club.devcord.gamejam.Team;
import club.devcord.gamejam.world.WorldDuplicator;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TeamCommand implements TabExecutor {

    private final Nigulpyggub plugin;
    private final WorldDuplicator duplicator;

    public TeamCommand(Nigulpyggub plugin) {
        this.plugin = plugin;
        this.duplicator = new WorldDuplicator(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length < 1) {
            player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You have to use: /team create"));
            return false;
        }

        switch (args[0].toLowerCase()) {

            case "delete" -> plugin.teamForName(player.getName())
                    .ifPresentOrElse(team -> {
                        plugin.teams().remove(team);
                        player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Your team was deleted!"));
                    }, () -> player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You're not the creator of a team.")));

            case "create" -> {
                boolean alreadyInTeam = plugin.teamForPlayer(player).isPresent();

                if (alreadyInTeam) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You are already in a team!"));
                    return true;
                }

                World teamWorld = duplicator.duplicate(player.getName());
                Team team = new Team(player, List.of(), teamWorld);
                plugin.teams().add(team);
                player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Team with name %s created".formatted(player.getName())));
            }
            case "join" -> {
                if (args.length != 2) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You have to use: /team join <creator>"));
                    return false;
                }
                
                if(plugin.teamForPlayer(player).isPresent()) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You are already in a team!"));
                    return false;
                }

                plugin.teamForName(args[1]).ifPresentOrElse(team -> {
                    team.addPlayer(player);
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<green>You joined the team of %s".formatted(team.creator().getName())));
                }, () -> player.sendMessage(MiniMessage.miniMessage().deserialize("<red>No team found for creator %s".formatted(args[1]))));
            }
            case "tp" -> {
                if (!player.isOp()) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Du musst mehr Rechte tanken."));
                    return false;
                }

                if (args.length != 2) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You have to use: /team tp <team>"));
                }
                String name = args[1];

                plugin.getServer().getWorlds().stream()
                        .filter(world -> world.getName().equalsIgnoreCase(name))
                        .findAny()
                        .ifPresentOrElse(world -> {
                            Location playerLocation = player.getLocation();
                            playerLocation.setWorld(world);
                            player.teleport(playerLocation);

                            player.sendMessage(MiniMessage.miniMessage().deserialize("You were teleport to world %s".formatted(world.getName())));
                        }, () -> player.sendMessage(MiniMessage.miniMessage().deserialize("<red> Team with name %s not found".formatted(name))));

            }
            default -> player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Unknown command"));
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 1) {
            return completions(sender, List.of("create", "join", "delete"), List.of("tp"));
        }

        if (args[0].equalsIgnoreCase("tp") && sender.isOp()) {
            return plugin.getServer().getWorlds().stream()
                    .map(World::getName)
                    .toList();
        }
        return List.of();
    }

    private List<String> completions(CommandSender player, List<String> normal, List<String> admin) {
        if (!player.isOp()) {
            return normal;
        }

        ArrayList<String> strings = new ArrayList<>(normal);
        strings.addAll(admin);
        return strings;
    }
}
