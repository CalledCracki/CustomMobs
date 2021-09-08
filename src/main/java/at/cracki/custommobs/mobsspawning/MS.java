package at.cracki.custommobs.mobsspawning;

import at.cracki.custommobs.components.CustomMob;
import at.cracki.custommobs.main.CM;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class MS {

    public static World world;
    public static BukkitTask task;
    public static Map<Entity, CustomMob> entities = new HashMap<>();

    public static void spawnMobs(int size, int mobCap, int spawnTime) {
        CustomMob[] mobTypes = CustomMob.values();
        task = new BukkitRunnable() {
            Set<Entity> spawned = entities.keySet();
            List<Entity> removal = new ArrayList<>();
            @Override
            public void run() {
                for(Entity entity : spawned) {
                    if(!entity.isValid() || entity.isDead()) removal.add(entity);
                }
                spawned.removeAll(removal);

                int diff = mobCap - entities.size();
                if(diff <= 0) return;
                int spawnAmount = (int) (Math.random() * (diff + 1)), count = 0;
                while(count <= spawnAmount) {
                    count++;
                    int ranX = getRandomWithNegative(size), ranZ = getRandomWithNegative(size);
                    Block block = world.getHighestBlockAt(ranX, ranZ);
                    double xOffset = getRandomOffset(), zOffset = getRandomOffset();
                    Location loc = block.getLocation().clone().add(xOffset, 1 ,zOffset);
                    if(!isSpawnable(loc)) continue;
                    double random = Math.random() * 101, previous = 0;
                    CustomMob typeToSpawn = mobTypes[0];
                    for(CustomMob type : mobTypes) {
                        previous += type.getSpawnChance();
                        if(random <= previous) {
                            typeToSpawn = type;
                            break;
                        }
                    }
                    entities.put(typeToSpawn.spawn(loc), typeToSpawn);
                }
            }
        }.runTaskTimer(CM.getPlugin(), 0L, spawnTime);
    }

    private static boolean isSpawnable(Location location) {
        Block feetBlock = location.getBlock(), headBlock = location.clone().add(0,1,0).getBlock(), upperBlock
                = location.clone().add(0, 2, 0).getBlock();
        return feetBlock.isPassable() && !feetBlock.isLiquid() && headBlock.isPassable() && !headBlock.isLiquid()
                && upperBlock.isPassable() && !upperBlock.isLiquid();
    }

    public static double getRandomOffset() {
        double random = Math.random();
        if (Math.random() > 0.5) random *= -1;
        return random;
    }

    public static int getRandomWithNegative(int size) {
        int random = (int) (Math.random() * (size + 1));
        if (Math.random() > 0.5) random *= -1;
        return random;
    }

}
