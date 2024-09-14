package club.devcord.gamejam.level;

import club.devcord.gamejam.Team;

public abstract class Level {

    private final Team team;

    public Level(Team team) {
        this.team = team;
    }

    abstract public void start();

    abstract public void stop();

    public Team team() {
        return team;
    }
}
