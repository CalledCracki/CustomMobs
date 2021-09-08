package at.cracki.custommobs.components;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

import static at.cracki.custommobs.utils.Utils.color;
import static at.cracki.custommobs.utils.Utils.createItem;

public enum CustomMob {

    TRICERATOPS("&fTriceratops", 8, 60, EntityType.RAVAGER, null,
            null, new LootItem(createItem(Material.ROTTEN_FLESH, 1, false, false,
            false, "&aDino Fleisch", "&7Fleisch eines Dinos"), 1, 3, 100)),

    VELOCIRAPTOR("&fVelociraptor", 9, 20, EntityType.HUSK, null,
            null, new LootItem(createItem(Material.ROTTEN_FLESH, 1, false, false,
            false, "&aDino Fleisch", "&7Fleisch eines Dinos"), 1, 3, 100)),

    MOSASAURUS("&fMosasaurus", 10, 20, EntityType.DOLPHIN, null,
            null, new LootItem(createItem(Material.ROTTEN_FLESH, 1, false, false,
            false, "&aDino Fleisch", "&7Fleisch eines Dinos"), 1, 3, 100)),

    DIMORPHODON("&fDimorphodon", 16, 15, EntityType.PHANTOM, null,
            null, new LootItem(createItem(Material.ROTTEN_FLESH, 1, false, false,
            false, "&aDino Fleisch", "&7Fleisch eines Dinos"), 1, 3, 100)),

    TYRANNOSAURUS("&fTyrannosaurus", 20, 10, EntityType.GIANT, null,
            null, new LootItem(createItem(Material.ROTTEN_FLESH, 1, false, false,
            false, "&aDino Fleisch", "&7Fleisch eines Dinos"), 1, 3, 100)),
    ;

    private String name;
    private double maxHealth, spawnChance;
    private EntityType type;
    private ItemStack mainItem;
    private ItemStack[] armor;
    private List<LootItem> lootTable;

    CustomMob(String name, double maxHealth, double spawnChance, EntityType type, ItemStack mainItem,
              ItemStack[] armor, LootItem... lootItems) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.spawnChance = spawnChance;
        this.type = type;
        this.mainItem = mainItem;
        this.armor = armor;
        lootTable = Arrays.asList(lootItems);
    }

    public LivingEntity spawn(Location loc) {
        LivingEntity entity = (LivingEntity) loc.getWorld().spawnEntity(loc, type);
        entity.setCanPickupItems(false);
        entity.setCustomNameVisible(true);
        entity.setCustomName(color(name + "&r&c " + (int) maxHealth + "/" + (int) maxHealth + "❤"));
        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
        entity.setHealth(maxHealth);
        EntityEquipment inv = entity.getEquipment();
        if (armor != null) inv.setArmorContents(armor);
        inv.setHelmetDropChance(0f);
        inv.setChestplateDropChance(0f);
        inv.setLeggingsDropChance(0f);
        inv.setBootsDropChance(0f);
        inv.setItemInMainHand(mainItem);
        inv.setItemInMainHandDropChance(0f);
        return entity;
    }

    public void tryDropLoot(Location loc) {
        for(LootItem item : lootTable) {
            item.tryDropItem(loc);
        }
    }

    public String getName() {
        return name;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public double getSpawnChance() {
        return spawnChance;
    }
}
