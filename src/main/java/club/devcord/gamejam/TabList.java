package club.devcord.gamejam;

import club.devcord.gamejam.level.LevelPipeline;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.checkerframework.checker.units.qual.C;

import java.util.Map;

public class TabList {

    private final Nigulpyggub plugin;

    public TabList(Nigulpyggub plugin) {
        this.plugin = plugin;
    }

    public void start() {
        plugin.getServer().getScheduler().runTaskTimer(plugin, this::updateTab, 0, 2);
    }

    private void updateTab() {
        for (Map.Entry<Team, LevelPipeline> entry : plugin.teamPipelines().entrySet()) {
            Team team = entry.getKey();
            LevelPipeline pipeline = entry.getValue();
            team.players().forEach(player -> {
                Component name = Component.empty();
                if (pipeline != null) {
                    if (pipeline.finished()) {
                        name = name.append(Component.text(pipeline.duration() + "min"));
                    } else {
                        name = name.append(Component.text("Lvl. %s".formatted(pipeline.level())));
                    }
                    name = name.color(NamedTextColor.GOLD);
                    name = name.append(Component.text(" - "));
                }

                name = name.append(player.name().color(team.color()));
                player.playerListName(name);
            });
        }
    }
}
