package at.cracki.custommobs.main;

import at.cracki.custommobs.listeners.EventsClass;
import at.cracki.custommobs.mobsspawning.MS;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static at.cracki.custommobs.listeners.EventsClass.*;

public class CM extends JavaPlugin {

    public static CM plugin;

    @Override
    public void onEnable() {
        plugin = this;
        MS.world = Bukkit.getWorld("world");
        MS.spawnMobs(9, 10, 5 * 20);

        new BukkitRunnable() {
            Set<Entity> stands = indicators.keySet();
            List<Entity> removal = new ArrayList<>();
            @Override
            public void run(){
                for(Entity stand : stands) {
                    int ticksLeft = indicators.get(stand);
                    if(ticksLeft == 0) {
                        stand.remove();
                        removal.add(stand);
                        continue;
                    }
                    ticksLeft--;
                    indicators.put(stand, ticksLeft);
                }
                stands.removeAll(removal);
            }
        }.runTaskTimer(this, 0L, 1L);

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new EventsClass(), this);

    }

    public static CM getPlugin() {
        return plugin;
    }
}
