package org.garper.plugins.armory.commands;

import com.sun.tools.javac.code.TypeMetadata;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.garper.plugins.armory.munitions.TankShell;
import org.garper.plugins.armory.weapons.TankGun;
import org.garper.plugins.armory.weapons.Weapon;
import org.garper.plugins.armory.weapons.Weapon;
import org.garper.plugins.armory.Main;
import org.jetbrains.annotations.NotNull;

public class HorseSpawnCommand implements CommandExecutor {
    JavaPlugin plugin;

    public HorseSpawnCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //check if called by a player
        if(!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        Location location = player.getTargetBlock(20).getLocation().add(0, 1, 0);
        Boat boat = (Boat) player.getWorld().spawnEntity(location, EntityType.BOAT);

        boat.setInvulnerable(true);
        player.getEquipment().setHelmet(new TankGun().getItem());
        boat.setGravity(false);
        boat.getBoundingBox().resize(0, 0, 0, 0, 0, 0);
        boat.getBoundingBox().shift(0, -2, 0);
        boat.getBoundingBox().expand(5);

        new BukkitRunnable() {
            @Override
            public void run() {
                boat.setVelocity(boat.getLocation().getDirection().multiply(0.5));
            }
        }.runTaskTimer(plugin, 10, 1);

        return true;
    }
}
