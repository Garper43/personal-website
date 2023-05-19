package org.garper.plugins.armory.weapons;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitTask;
import org.garper.plugins.armory.Main;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.garper.plugins.armory.munitions.ExplosiveArtilleryShell;
import org.garper.plugins.armory.munitions.PiercingArtilleryShell;

public class JAVELIN implements Weapon {
    private Main plugin;
    private final int COOLDOWN = 0;

    public JAVELIN(Main plugin) {
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
        meta.setCustomModelData(2);
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public String getName() {
        return "JAVELIN";
    }

    @Override
    public int getCoolDown() {
        return COOLDOWN;
    }

    @Override
    public void onTrigger(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Vector facing = player.getEyeLocation().getDirection();
        facing.setY(1);
        facing.normalize();
        Location start = player.getLocation().add(0, 2, 0);
        Entity targetEntity = player.getTargetEntity(120, true);
        Location targetLocation;

        //determine target
        if(targetEntity != null) {
            targetLocation = targetEntity.getLocation();
        } else {
            Block block = player.getTargetBlockExact(120);
            if(block == null) {
                return;
            }
            targetLocation = block.getLocation();
        }

        //spawn arrow (missile)
        Arrow arrow = player.getWorld().spawnArrow(start, facing, 3, 0);
        arrow.customName(Component.text(new ExplosiveArtilleryShell().getName()));
        player.playSound(start, Sound.ITEM_FIRECHARGE_USE, 5, 1.5f);

        //execute correct trajectory until arrow hits something
        new BukkitRunnable() {
            @Override
            public void run() {
                Location location = arrow.getLocation();
                boolean isOnTargetBlock = (
                    Math.abs(targetLocation.getX() - location.getX()) <= 2 &&
                    Math.abs(targetLocation.getY() - location.getY()) <= 2 &&
                    Math.abs(targetLocation.getZ() - location.getZ()) <= 2);

                //kill thread when target is hit
                if(!arrow.isValid() || (isOnTargetBlock && targetEntity == null )) {
                    this.cancel();
                    return;
                }

                arrow.getWorld().playSound(arrow.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1f);

                //check whether target entity is not null
                Location target;
                if(targetEntity != null) {
                    target = targetEntity.getLocation();
                } else {
                    target = targetLocation;
                }

                //spawn exhaust particles
                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.WHITE, 5);
                arrow.getWorld().spawnParticle(Particle.REDSTONE, location, 2, dustOptions);

                //actively follow target
                double distance = Math.sqrt(Math.pow(location.getX() - target.getX(), 2) + Math.pow(location.getZ() - target.getZ(), 2));
                if(distance < 10 || arrow.getVelocity().getY() < 0) {
                    double x = target.getX() - location.getX();
                    double y = target.getY() - location.getY();
                    double z = target.getZ() - location.getZ();

                    Vector vector = new Vector(x, y, z);
                    vector.normalize();
                    vector.multiply(1);

                    arrow.setVelocity(vector);
                }
            }
        }.runTaskTimer(plugin, 2, 2);
    }
}
