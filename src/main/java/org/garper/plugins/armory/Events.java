package org.garper.plugins.armory;

import io.papermc.paper.event.entity.EntityMoveEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.garper.plugins.armory.munitions.*;
import org.garper.plugins.armory.weapons.Colt;
import org.garper.plugins.armory.weapons.JAVELIN;
import org.garper.plugins.armory.weapons.TankGun;
import org.garper.plugins.armory.weapons.Weapon;

public class Events implements Listener {
    Weapon[] weapons = new Weapon[3];
    Munition[] munitions = {new ArtilleryShell(), new PiercingArtilleryShell(), new ExplosiveArtilleryShell(), new TankShell()};
    Main plugin;

    public Events(Main plugin) {
        this.plugin = plugin;

        weapons[0] = new Colt(plugin);
        weapons[1] = new JAVELIN(plugin);
        weapons[2] = new TankGun();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if((event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        Player player = event.getPlayer();
        Material material = event.getMaterial();

        if(material == Material.STONE_SWORD && event.getPlayer().getCooldown(Material.STONE_SWORD) == 0 && event.getItem().hasItemMeta()) {
            ItemMeta meta = event.getItem().getItemMeta();
            int modelData = meta.getCustomModelData();

            if(modelData == 1) { //colt
                weapons[0].onTrigger(event);
                player.setCooldown(Material.STONE_SWORD, weapons[0].getCoolDown());

                Damageable damageable = (Damageable) player.getInventory().getItemInMainHand().getItemMeta();
                damageable.setDamage(damageable.getDamage() + 1);
                player.getInventory().getItemInMainHand().setItemMeta((ItemMeta) damageable);

            } else if(modelData == 2) { //JAVELIN
                weapons[1].onTrigger(event);
                player.setCooldown(Material.STONE_SWORD, weapons[1].getCoolDown());
            } else if(modelData == 3) { //tank gun
                weapons[2].onTrigger(event);
                player.setCooldown(Material.STONE_SWORD, weapons[2].getCoolDown());
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Arrow)) {
            return;
        }

        Entity entity = event.getEntity();
        Block block = entity.getLocation().getBlock();
        String name = ((TextComponent) entity.customName()).content();
        System.out.println(name);

        if(name == null) {

        }else if(name.equals("bullet")) {
            entity.remove();
        } else if(name.equals(munitions[0].getName())) {
            munitions[0].onHit(event);
        } else if(name.equals(munitions[1].getName())) {
            munitions[1].onHit(event);
        } else if(name.equals(munitions[2].getName())) {
            munitions[2].onHit(event);
        } else if(name.equals(munitions[3].getName())) {
            munitions[3].onHit(event);
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(player.getVehicle() == null || !(player.getVehicle() instanceof Boat)) {
            return;
        }

        Boat boat = (Boat) player.getVehicle();
        RayTraceResult rt = boat.getBoundingBox().rayTrace(boat.getLocation().toVector(), boat.getVelocity(), 1);
        if(rt.getHitBlock() != null) {
            boat.teleport(rt.getHitBlock().getLocation());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.getPlayer().getServer().shutdown();
    }

}
