package club.devcord.gamejam.commands;

import club.devcord.gamejam.Nigulpyggub;
import club.devcord.gamejam.Team;
import club.devcord.gamejam.world.WorldDuplicator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.world.scores.PlayerTeam;
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
import java.util.Optional;
import java.util.stream.Collectors;

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
                        player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Dein Team wurde gelöscht!"));
                    }, () -> player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Du bist in keinem Team!")));

            case "create" -> {
                boolean alreadyInTeam = plugin.teamForPlayer(player).isPresent();

                if (alreadyInTeam) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Du bist schon in einem Team!"));
                    return true;
                }

                World teamWorld = duplicator.duplicate(player.getName());
                Team team = new Team(player, teamWorld);
                plugin.teamPipelines().put(team, null);
                player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Team %s erstellt.".formatted(player.getName())));
            }
            case "join" -> {
                if (args.length != 2) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Du musst: /team join <name> nutzen!"));
                    return false;
                }
                
                if(plugin.teamForPlayer(player).isPresent()) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Du bist schon in einem Team!"));
                    return false;
                }

                plugin.teamForName(args[1]).ifPresentOrElse(team -> {
                    team.players().forEach(p -> p.sendMessage(MiniMessage.miniMessage().deserialize("<green>%s ist dem Team beigetreten.".formatted(player.getName()))));
                    team.addPlayer(player);
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<green>Du bist dem Team %s beigetreten.".formatted(team.name())));
                }, () -> player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Kein Team mit dem Namen %s gefunden.".formatted(args[1]))));
            }
            case "info" -> plugin.teamForPlayer(player)
                    .ifPresentOrElse(team -> {
                        TextComponent text = Component.text("Dein Team: %s".formatted(team.name()))
                                .append(Component.newline())
                                .append(Component.text("Mitglieder: %s".formatted(team.players().stream().map(Player::getName).collect(Collectors.joining(", ")))));
                        player.sendMessage(text);
                    }, () -> player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Du bist nicht in einem Team!")));
            case "tp" -> {
                if (!player.isOp()) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Du musst mehr Rechte tanken."));
                    return false;
                }

                if (args.length != 2) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Du musst: /team tp <team/welt> nutzen"));
                }
                String name = args[1];

                plugin.getServer().getWorlds().stream()
                        .filter(world -> world.getName().equalsIgnoreCase(name))
                        .findAny()
                        .ifPresentOrElse(world -> {
                            Location playerLocation = player.getLocation();
                            playerLocation.setWorld(world);
                            player.teleport(playerLocation);

                            player.sendMessage(MiniMessage.miniMessage().deserialize("Du wurdest zur Welt %s teleportiert.".formatted(world.getName())));
                        }, () -> player.sendMessage(MiniMessage.miniMessage().deserialize("<red> Team mit name %s nicht gefunden.".formatted(name))));

            }
            case "world" -> {
                if (!player.isOp()) {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Du musst mehr Rechte tanken."));
                    return false;
                }

                player.sendMessage(MiniMessage.miniMessage().deserialize("Derzeitige Welt: %s".formatted(player.getWorld().getName())));
            }
            default -> player.sendMessage(MiniMessage.miniMessage().deserialize("<red>Unbekannter Befehl."));
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 1) {
            return completions(sender, label, List.of("create", "join", "delete", "info"), List.of("tp", "world"));
        }

        if (args[0].equalsIgnoreCase("tp") && sender.isOp()) {
            return plugin.getServer().getWorlds().stream()
                    .map(World::getName)
                    .filter(s -> s.startsWith(args[0]))
                    .toList();
        }
        
        if (args[0].equalsIgnoreCase("join")) {
            return plugin.teams().stream()
                    .map(Team::name)
                    .filter(s -> s.startsWith(args[0]))
                    .toList();
        }
        
        return List.of();
    }

    private List<String> completions(CommandSender player, String current, List<String> normal, List<String> admin) {
        if (!player.isOp()) {
            return normal;
        }

        ArrayList<String> strings = new ArrayList<>(normal);
        strings.addAll(admin);
        return strings
                .stream()
                .filter(s -> s.startsWith(current))
                .toList();
    }
}
