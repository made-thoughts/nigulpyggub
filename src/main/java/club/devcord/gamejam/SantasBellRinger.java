package club.devcord.gamejam;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SantasBellRinger implements Listener {

    public static final ItemStack THE_BELL;

    static {
        THE_BELL = new ItemStack(Material.IRON_SWORD);
        ItemMeta itemMeta = THE_BELL.getItemMeta();
        itemMeta.setCustomModelData(17);
        itemMeta.displayName(Component.text("Santa's Bell").color(NamedTextColor.DARK_GREEN));
        THE_BELL.setItemMeta(itemMeta);
    }

    @EventHandler
    public void ringRingSantaRingRing(PlayerInteractEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().isSimilar(THE_BELL)) {
            Sound sound = Sound.sound(Key.key("minecraft:item.santabell"), Sound.Source.MASTER, 1, 1);
            Location loc = event.getPlayer().getLocation();
            event.getPlayer().getWorld().playSound(sound, loc.x(), loc.y(), loc.z());
        }
    }
}
