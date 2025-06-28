package org.thrill12.herbsOfLife;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.naming.Name;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.bukkit.Bukkit.getLogger;

public class EventListeners implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerConsumeItem(PlayerItemConsumeEvent consumeEvent){

        if(!Storage.GetConfig().getBoolean("enableFoodHistory")){
            return;
        }

        ItemStack itemConsumed = consumeEvent.getItem();
        Player player = consumeEvent.getPlayer();

        UUID playerID = player.getUniqueId();
        Material mat = itemConsumed.getType();

        int maxFoodHistory = JavaPlugin.getPlugin(HerbsOfLife.class).getConfig().getInt("maxFoodHistory");

        Storage.allPlayerHistories.computeIfAbsent((playerID.toString()), k -> new ArrayList<String>());

        List<String> reversedList = Storage.allPlayerHistories.get(playerID.toString()).reversed();

        if(Storage.GetConfig().getBoolean("enableMaxHealthIncrease")) {

            int maxHealthBonus = Storage.GetConfig().getInt("maxHealthBonus");

            if (!reversedList.contains(String.valueOf(mat))) {
                NamespacedKey key = new NamespacedKey("herbs-of-life", "hol-food-bonus-" + UUID.randomUUID());
                AttributeInstance maxHealth = player.getAttribute(Attribute.MAX_HEALTH);

                if (maxHealth != null) {
                    maxHealth.addModifier(new AttributeModifier(key, maxHealthBonus, AttributeModifier.Operation.ADD_NUMBER));

                    TextComponent comp = Component.text("You feel rejuvenated... (+" + maxHealthBonus + " Hearts)")
                            .color(NamedTextColor.GOLD);
                    player.sendMessage(comp);
                }
            }
        }

        List<String> lastXFoodHistory = new ArrayList<>();

        for (int i = 0; i < maxFoodHistory; i++) {
            if (i < reversedList.size()) {
                String item = reversedList.get(i);
                lastXFoodHistory.add(item != null ? item : "null");
            } else {
                lastXFoodHistory.add("null");
            }
        }

        int historyCount = 0;

        for (String s : lastXFoodHistory) {
            if (s.equalsIgnoreCase(String.valueOf(mat))) {
                historyCount++;
            }
        }

        float percentOccupied = (float) historyCount / (float)maxFoodHistory;

        float multiplier = (float)(1 - percentOccupied);

        int nutritionValue = Math.max(1, (int) (Foods.foodNutritionMap.getOrDefault(mat, -1) * multiplier));

        if(Storage.GetConfig().getBoolean("enableNutritionDecrease")){
            if(nutritionValue >= 0){
                consumeEvent.setCancelled(true);

                // Reduce the item in the inventory
                itemConsumed.setAmount(itemConsumed.getAmount() - 1);

                int newFood = Math.min(player.getFoodLevel() + nutritionValue, 20);
                FoodLevelChangeEvent flce = new FoodLevelChangeEvent(player, newFood);
                Bukkit.getPluginManager().callEvent(flce);

                // If not cancelled, apply hunger change
                if (!flce.isCancelled()) {
                    player.setFoodLevel(flce.getFoodLevel());
                    // Optionally adjust saturation/potions here
                }

                player.updateInventory();

                if(multiplier <= 0.3){
                    TextComponent comp = Component.text("You've eaten this a lot recently. Try changing it up...")
                            .color(NamedTextColor.GOLD);
                    player.sendMessage(comp);
                }
            }
        }

        Storage.allPlayerHistories.get(playerID.toString()).add(String.valueOf(mat));
    }
}
