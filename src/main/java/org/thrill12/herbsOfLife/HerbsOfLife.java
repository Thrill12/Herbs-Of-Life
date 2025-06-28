package org.thrill12.herbsOfLife;

import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public final class HerbsOfLife extends JavaPlugin {

    @Override
    public void onEnable() {

        // set up commands
        this.getCommand("hol").setExecutor(new Commands());

        getConfig().addDefault("maxFoodHistory", 10);

        Storage.savePath = getDataPath() + Storage.savePath;
        Storage.historiesPath = getDataPath() + Storage.historiesPath;

        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new EventListeners(), this);

        Logs.Log("Herbs of Life - Loading data...");

        if(!Files.exists(Path.of(Storage.savePath))){
            Storage.allPlayerAmounts = new HashMap<>();
        }
        else{
            Storage.LoadFile();
        }

        saveDefaultConfig();
        Foods.LoadFoodMap(this);

        Logs.Log("Herbs of Life - Data fully loaded! Now tracking...");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        // Save Storage stuff

        Storage.SaveFile();
    }
}
