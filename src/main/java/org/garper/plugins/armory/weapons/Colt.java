package org.garper.plugins.armory.weapons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Mob;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.garper.plugins.armory.Main;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class Colt implements Weapon {
    private Main plugin;
    private final float RECOIL = 1.0f;
    private final int COOLDOWN = 0;

    public Colt(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isFullAuto() {
        return false;
    }

    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.STONE_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(1);
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public String getName() {
        return "colt";
    }

    @Override
    public int getCoolDown() {
        return COOLDOWN;
    }

    @Override
    public void onTrigger(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Entity entity = player.getTargetEntity(120);
        if(entity != null && entity instanceof Mob) {
            ((Mob) entity).damage(10);
        }

        Location eyeLocation = player.getEyeLocation();

//        org.bukkit.util.Vector vector = eyeLocation.getDirection();
//        Arrow arrow = player.getWorld().spawnArrow(eyeLocation, vector, 100, 2);
//
//        arrow.setDamage(0.1);
//        arrow.setKnockbackStrength(0);
//        arrow.setSilent(true);
//        arrow.setCustomName("bullet");
//        arrow.setPersistent(true);
//        arrow.setShooter(player);
//        arrow.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 1, 2), true);

//        player.playSound(eyeLocation, Sound.ENTITY_GENERIC_EXPLODE, 5, 1.5f);
//        ItemMeta meta = player.getActiveItem().getItemMeta();
//        if() {
//
//        }
//        meta.setCustomModelData(2);
//        player.getActiveItem().setItemMeta(meta);

//        Random random = new Random();
//        Location locaion = player.getLocation();
//        Vector velocity = player.getVelocity();
//
//        locaion.setPitch(locaion.getPitch() + (random.nextFloat()*2 - 1)*RECOIL);
//        locaion.setYaw(locaion.getYaw() + (random.nextFloat()*2 - 1)*RECOIL);
//
//        player.teleport(locaion);
//        player.setVelocity(velocity);
//        player.sendMessage("" + velocity.length() + player.getVelocity().length());
    }
}

