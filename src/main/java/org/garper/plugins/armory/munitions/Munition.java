package org.garper.plugins.armory.munitions;

import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

public interface Munition {
    ItemStack getItem();
    String getName();

    void onHit(ProjectileHitEvent event);
}
