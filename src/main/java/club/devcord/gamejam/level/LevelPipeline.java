package club.devcord.gamejam.level;

import club.devcord.gamejam.Nigulpyggub;
import club.devcord.gamejam.Team;
import club.devcord.gamejam.level.eyes.EyesLevel;
import club.devcord.gamejam.level.poempel.PoempelLevel;
import club.devcord.gamejam.level.thejump.TheJumpLevel;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class LevelPipeline {
    private final Team team;
    private final Queue<Level> levels;

    public LevelPipeline(Team team, Nigulpyggub plugin) {
        this.team = team;
        var levels = List.of(
                new PoempelLevel(team, plugin, this),
                new TheJumpLevel(team, plugin, this),
                new EyesLevel(team, plugin, this)
        );
        this.levels = new ArrayDeque<>(levels);
    }

    public void next() {
        team.players().forEach(
                p -> p.playSound(Sound.sound(Key.key("minecraft:effect.achievementcompleted"), Sound.Source.MASTER, 100, 1))
        );
        levels.poll().stop();
        if(!levels.isEmpty()) {
            start();
        } else {

        }
    }

    public void start() {
        levels.peek().start();
    }

    public void stop() {
        if (levels.peek() == null) return;
        levels.peek().stop();
    }
}
