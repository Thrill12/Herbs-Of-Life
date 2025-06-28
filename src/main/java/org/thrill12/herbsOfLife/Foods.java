package org.thrill12.herbsOfLife;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getLogger;

public class Foods {
    public static Map<Material, Integer> foodNutritionMap = new HashMap<>();
    public static Map<Material, Integer> foodSaturationMap = new HashMap<>();

    // TODO: Make a reload function for this
    public static void LoadFoodMap(JavaPlugin plg) {
        ConfigurationSection sec = plg.getConfig().getConfigurationSection("food-values");
        if (sec == null) return;

        for (String key : sec.getKeys(false)) {
            Material mat;
            try {
                mat = Material.valueOf(key);
            } catch (IllegalArgumentException ex) {
                getLogger().warning("Unknown material in config: " + key);
                continue;
            }

            int val = sec.getInt(key, -1);
            if (val >= 0) {
                foodNutritionMap.put(mat, val);
            } else {
                getLogger().warning(key + " has an invalid hunger value: " + val);
            }
        }

        getLogger().info("Loaded food-values for " + foodNutritionMap.size() + " items");
    }
}
