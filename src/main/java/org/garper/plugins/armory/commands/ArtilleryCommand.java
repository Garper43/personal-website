package org.garper.plugins.armory.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.garper.plugins.armory.munitions.ArtilleryShell;
import org.garper.plugins.armory.munitions.ExplosiveArtilleryShell;
import org.garper.plugins.armory.munitions.Munition;
import org.garper.plugins.armory.munitions.PiercingArtilleryShell;
import org.jetbrains.annotations.NotNull;
import org.bukkit.Color;

import java.util.HashMap;

public class ArtilleryCommand implements CommandExecutor {
    public final int RANGE = 1000;
    public final HashMap<String, Munition> munitions = new HashMap<>();

    public ArtilleryCommand() {
        munitions.put("regular", new ArtilleryShell());
        munitions.put("piercing", new PiercingArtilleryShell());
        munitions.put("explosive", new ExplosiveArtilleryShell());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //check if called by a player
        if(!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        Location location;
        String name;
        int numOfShells;
        int spread;

        try {
            if(args.length == 5) {
                int x = Integer.parseInt(args[0]);
                int z = Integer.parseInt(args[1]);
                location = player.getWorld().getBlockAt(x, 0, z).getLocation();
                location.setY(300);

                //check range
                if(Math.pow(location.getX() - player.getLocation().getX(), 2) + Math.pow(location.getZ() - player.getLocation().getZ(), 2) > Math.pow(RANGE, 2)) {
                    player.sendMessage("Out of range, max range is " + RANGE + " blocks");
                    return false;
                }

                numOfShells = Integer.parseInt(args[3]);

                //check munitions
                if(!checkMunitions(player, munitions.get(args[2]), numOfShells)) {
                    player.sendMessage("You don't have enough shells");
                    return false;
                }

                spread = Integer.parseInt(args[4]);
                name = munitions.get(args[2]).getName();

            } else if(args.length == 3) {
                Block block = player.getTargetBlockExact(200);

                //check if block exists
                if(block == null) {
                    player.sendMessage("Nothing in range of 200 blocks in sight");
                    return false;
                }

                location = block.getLocation();
                location.setY(300);
                numOfShells = Integer.parseInt(args[1]);

                //check if player has munitions
                if(!checkMunitions(player, munitions.get(args[0]), numOfShells)) {
                    player.sendMessage("You don't have enough shells");
                    return false;
                }

                spread = Integer.parseInt(args[2]);
                name = munitions.get(args[0]).getName();

            } else {
                player.sendMessage("Incorrect arguments");
                return false;

            }
        } catch(NumberFormatException e) {
            player.sendMessage("Arguments can only contain integers");
            return false;

        }

        //spawn shells
        for(int i = 0; i < numOfShells; i++) {
            Arrow arrow = player.getWorld().spawnArrow(location, new Vector(0,-1,0), 1, spread);
            arrow.customName(Component.text(name));
        }

        return true;
    }

    private boolean checkMunitions(Player player, Munition munition, int amount) {
        //check for game mode
        if(player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
            ItemStack item = munition.getItem();

            //check if inventory has enough shells
            if(!hasItems(player.getInventory(), item, amount)) {
                return false;
            }

            //remove shells
            removeItems(player.getInventory(), item, amount);
        }

        return true;
    }

    private static boolean hasItems(Inventory inventory, ItemStack itemStack, int amount) {
        ItemStack[] items = inventory.getContents();
        int itemsLeft = amount;

        for(ItemStack i : items) {
            if(i != null) {
                if(i.getType() == itemStack.getType() && i.getItemMeta().getCustomModelData() == itemStack.getItemMeta().getCustomModelData()) {
                    itemsLeft -= i.getAmount();
                }
            }

            if(itemsLeft <= 0) {
                return true;
            }
        }

        return false;
    }

    private static boolean removeItems(Inventory inventory, ItemStack itemStack, int amount) {
        ItemStack[] items = inventory.getContents();
        int itemsLeft = amount;

        for(ItemStack i : items) {
            if(i != null) {
                if(i.getType() == itemStack.getType() && i.getItemMeta().getCustomModelData() == itemStack.getItemMeta().getCustomModelData()) {
                    if(itemsLeft >= i.getAmount()) {
                        itemsLeft -= i.getAmount();
                        i.setAmount(0);
                    } else {
                        i.setAmount(i.getAmount() - itemsLeft);
                        itemsLeft = 0;
                    }
                }
            }

            if(itemsLeft <= 0) {
                return true;
            }
        }

        return false;
    }

}
