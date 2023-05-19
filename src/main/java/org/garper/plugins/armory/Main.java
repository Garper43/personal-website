package org.garper.plugins.armory;

import org.bukkit.plugin.java.JavaPlugin;
import org.garper.plugins.armory.commands.ArtilleryCommand;
import org.garper.plugins.armory.commands.HorseSpawnCommand;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Armory loaded");

        ArtilleryCommand artilleryCommandExecutor = new ArtilleryCommand();
        getCommand("artillery").setExecutor(artilleryCommandExecutor);
        HorseSpawnCommand horseSpawnCommand = new HorseSpawnCommand(this);
        getCommand("horse").setExecutor(horseSpawnCommand);

        getServer().getPluginManager().registerEvents(new Events(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
