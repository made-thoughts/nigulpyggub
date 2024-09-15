package club.devcord.gamejam;

import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.PrintWriter;
import java.io.StringWriter;

public class EventCancelers implements Listener {
    private final Nigulpyggub plugin;

    public EventCancelers(Nigulpyggub plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        event.setCancelled(true);
        event.getEntity().setFireTicks(0);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getLocation().getWorld().getName().equals("world") && event.getPlayer().getGameMode() == GameMode.ADVENTURE && event.getAction() != Action.RIGHT_CLICK_AIR) {
            event.setCancelled(true);
        }

        if (event.getMaterial().equals(Material.ENDER_EYE) &&
                (event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.END_PORTAL_FRAME)) {
            Exception exception = dasAugeBrauchstDuNoch();
            Component component = plugin.exceptionToComponent(exception);
            event.getPlayer().sendMessage(component);
            event.setCancelled(true);
        }
    }

    private Exception dasAugeBrauchstDuNoch() {
        NullPointerException nullPointerException = new NullPointerException();
        nullPointerException.printStackTrace(new PrintWriter(new StringWriter()));
        return nullPointerException;
    }
}
