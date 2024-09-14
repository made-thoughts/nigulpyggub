package club.devcord.gamejam.level;

import club.devcord.gamejam.Nigulpyggub;
import club.devcord.gamejam.Team;

public abstract class Level {

    private final Team team;
    private final Nigulpyggub plugin;
    private final LevelPipeline pipeline;

    public Level(Team team, Nigulpyggub plugin, LevelPipeline pipeline) {
        this.team = team;
        this.plugin = plugin;
        this.pipeline = pipeline;
    }

    abstract public void start();

    abstract public void stop();

    public Team team() {
        return team;
    }

    public Nigulpyggub plugin() {
        return plugin;
    }
}
