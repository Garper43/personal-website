package org.garper.plugins.armory.weapons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.garper.plugins.armory.munitions.ArtilleryShell;
import org.garper.plugins.armory.munitions.ExplosiveArtilleryShell;
import org.garper.plugins.armory.munitions.PiercingArtilleryShell;
import org.garper.plugins.armory.munitions.TankShell;

public class TankGun implements Weapon{
    private final int COOLDOWN = 5;

    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.STONE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(3);
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public boolean isFullAuto() {
        return false;
    }

    @Override
    public String getName() {
        return "tank gun";
    }

    @Override
    public int getCoolDown() {
        return COOLDOWN;
    }

    @Override
    public void onTrigger(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Vector facing = player.getEyeLocation().getDirection();
        Location location = player.getEyeLocation().add(facing.multiply(1));

        Arrow arrow = (Arrow) player.getWorld().spawnEntity(location, EntityType.ARROW);
        arrow.setVelocity(facing.multiply(10));
        arrow.setCustomName(new TankShell().getName());

        player.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 10, 0.7f);
    }
}
