package org.thrill12.herbsOfLife;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args.length == 0){
            sender.sendMessage("Usage: /hol <command>");
            return false;
        }

        String subCommand = args[0].toLowerCase();
        switch(subCommand){
            case "reload":

                if(args.length != 1){
                    return false;
                }

                Reload(sender);

                return true;

            case "showhistory":

                if(args.length != 2){
                    return false;
                }

                ShowHistory(sender, args[1]);

                return true;

            case "reset":

                if(args.length != 2){
                    return false;
                }

                ResetPlayer(sender, args[1]);

                return true;
        }

        return true;
    }

    public void Reload(CommandSender sender){
        Foods.LoadFoodMap(JavaPlugin.getPlugin(HerbsOfLife.class));

        sender.sendMessage("Herbs of life reloaded successfully.");
    }

    public void ShowHistory(CommandSender sender, String playerName){
        // Get player UID by name
        Player player = Bukkit.getPlayer(playerName);
        String playerUID = player.getUniqueId().toString();

        List<String> ls = Storage.allPlayerHistories.get(playerUID);

        ls = ls.reversed();

        int maxIndex = Math.min(10, ls.size());

        ArrayList<String> newList = new ArrayList<>();

        for(int i = 0; i < maxIndex;i++){
            newList.add(ls.get(i));
        }

        sender.sendMessage("Showing food history for " + playerName + ": " + newList.toString());
    }

    public void ResetPlayer(CommandSender sender, String playerName){
        // Get player UID by name
        Player player = Bukkit.getPlayer(playerName);
        String playerUID = player.getUniqueId().toString();

        Storage.allPlayerHistories.replace(playerUID, new ArrayList<>());

        AttributeInstance maxHealth = player.getAttribute(Attribute.MAX_HEALTH);

        for (Iterator<AttributeModifier> iterator = maxHealth.getModifiers().iterator(); iterator.hasNext(); ) {
            AttributeModifier mod = iterator.next();
            if (mod.getKey().toString().contains("hol-food-bonus")) {
                maxHealth.removeModifier(mod);
            }
        }

        sender.sendMessage("Reset food history and health for " + playerName + ".");
    }
}
