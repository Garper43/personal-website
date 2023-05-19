package org.garper.plugins.armory.munitions;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ExplosiveArtilleryShell implements Munition {

    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.POISONOUS_POTATO);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(3);
        meta.displayName(Component.text("ArtilleryCommand Shell"));
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public String getName() {
        return "explosive_artillery_shell";
    }

    @Override
    public void onHit(ProjectileHitEvent event) {
        Projectile entity = event.getEntity();

        entity.getWorld().createExplosion(entity.getLocation(), 3, true, true);
        entity.getWorld().createExplosion(entity.getLocation(), 7, false, false);

        entity.remove();
    }
}
