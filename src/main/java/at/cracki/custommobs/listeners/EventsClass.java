package at.cracki.custommobs.listeners;

import at.cracki.custommobs.components.CustomMob;
import at.cracki.custommobs.mobsspawning.MS;
import at.cracki.custommobs.utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import static at.cracki.custommobs.utils.Utils.*;
import static at.cracki.custommobs.mobsspawning.MS.*;

public class EventsClass implements Listener {

    public static Map<Entity, Integer> indicators = new HashMap<>();
    private DecimalFormat formatter = new DecimalFormat("#");

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if(!entities.containsKey(event.getEntity())) return;
        event.setDroppedExp(0);
        event.getDrops().clear();
        entities.remove(event.getEntity()).tryDropLoot(event.getEntity().getLocation());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity rawEntity = event.getEntity();
        if(!entities.containsKey(rawEntity)) return;
        CustomMob mob = entities.get(rawEntity);
        LivingEntity entity = (LivingEntity) rawEntity;
        double damage = event.getFinalDamage(), health = entity.getHealth() + entity.getAbsorptionAmount();
        if(health > damage) {
            health -= damage;
            entity.setCustomName(color(mob.getName() + "&r&c " + (int) health
                    + "/" + (int) mob.getMaxHealth() + "❤"));
        }
        Location loc = entity.getLocation().clone().add(getRandomOffset(), 1, getRandomOffset());
        world.spawn(loc, ArmorStand.class, armorStand -> {
            armorStand.setMarker(true);
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setSmall(true);
            armorStand.setCustomNameVisible(true);
            if(event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)) {
                armorStand.setCustomName(color("&6" + formatter.format((damage))));
            } else if(event.getCause().equals(EntityDamageEvent.DamageCause.POISON)) {
                armorStand.setCustomName(color("&2" + formatter.format((damage))));
            } else if(event.getCause().equals(EntityDamageEvent.DamageCause.DROWNING)) {
                armorStand.setCustomName(color("&b" + formatter.format((damage))));
            } else {
                armorStand.setCustomName(color("&c" + formatter.format((damage))));
            }
            indicators.put(armorStand, 30);
        });
    }
}
