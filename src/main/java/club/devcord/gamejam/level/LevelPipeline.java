package club.devcord.gamejam.level;

import club.devcord.gamejam.Nigulpyggub;
import club.devcord.gamejam.Team;
import club.devcord.gamejam.level.eyes.EyesLevel;
import club.devcord.gamejam.level.poempel.PoempelLevel;
import club.devcord.gamejam.level.thejump.TheJumpLevel;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class LevelPipeline {
    private final Team team;
    private final Nigulpyggub plugin;
    private final Queue<Level> levels;
    private final int initialSize;

    private Instant startTime;
    private Instant endTime;

    public LevelPipeline(Team team, Nigulpyggub plugin) {
        this.team = team;
        this.plugin = plugin;
        var levels = List.of(
                new PoempelLevel(team, plugin, this),
                new TheJumpLevel(team, plugin, this),
                new EyesLevel(team, plugin, this)
        );
        this.initialSize = levels.size();
        this.levels = new ArrayDeque<>(levels);
    }

    public void next() {
        levels.poll().stop();
        if(!levels.isEmpty()) {
            team.players().forEach(
                    p -> p.playSound(Sound.sound(Key.key("minecraft:effect.achievementcompleted"), Sound.Source.MASTER, 100, 1))
            );

            levels.peek().start();

            showLevelTitle();
        } else {
            endTime = Instant.now();
            team.players().forEach(player -> {
                player.teleport(new Location(plugin.getServer().getWorld("world"), 89, 42, -609, 0F, -3.3F));
                player.setAllowFlight(true);
                player.setFlying(true);

                team.players().forEach(
                        p -> p.playSound(Sound.sound(Key.key("minecraft:ui.toast.challenge_complete"), Sound.Source.MASTER, 100, 1))
                );

                player.showTitle(Title.title(MiniMessage.miniMessage().deserialize("<rainbow>Danke fürs Spielen!"), MiniMessage.miniMessage().deserialize("<white>Bis zum nächsten mal :)")));
            });

            plugin.getServer().broadcast(MiniMessage.miniMessage().deserialize("<yellow>Team %s hat alle Level in %smin abgeschlossen.".formatted(team.name(), duration())));
        }
    }

    private void showLevelTitle() {
        team.players().forEach(player -> player.showTitle(Title.title(Component.text("Level %s".formatted(level())).color(NamedTextColor.AQUA), Component.empty())));
    }


    public void start() {
        levels.peek().start();
        startTime = Instant.now();
        showLevelTitle();
    }

    public int level() {
        return initialSize - levels.size() + 1;
    }

    public long duration() {
        return startTime.until(endTime, ChronoUnit.MINUTES);
    }

    public boolean finished() {
        return levels.isEmpty();
    }

    public void stop() {
        if (levels.peek() == null) return;
        levels.peek().stop();
    }
}
