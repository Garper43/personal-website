package org.garper.plugins.armory.munitions;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TankShell implements Munition {

    //TODO: change item
    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.POISONOUS_POTATO);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(1);
        meta.displayName(Component.text("ArtilleryCommand Shell"));
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public String getName() {
        return "tank_shell";
    }

    @Override
    public void onHit(ProjectileHitEvent event) {
        Projectile entity = event.getEntity();

        entity.getWorld().createExplosion(event.getHitBlock().getLocation(), 4, false, true);

        entity.remove();
    }
}
