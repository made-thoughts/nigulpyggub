package club.devcord.gamejam;


import org.bukkit.plugin.java.JavaPlugin;

public class Nigulpyggub extends JavaPlugin {

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ResourcePackActivator(), this);
    }
}
