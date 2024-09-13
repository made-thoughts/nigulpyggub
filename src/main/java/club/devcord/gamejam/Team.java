package club.devcord.gamejam;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public record Team(
        Player creator,
        List<Player> players,
        World world
) {
    public List<Player> players() {
        var list = new ArrayList<>(players);
        list.addFirst(creator);
        return list;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }
}
