package org.garper.plugins.armory.weapons;

import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.garper.plugins.armory.Main;

public interface Weapon {
    boolean isFullAuto();
    ItemStack getItem();
    String getName();
    int getCoolDown();

    void onTrigger(PlayerInteractEvent event);
}
