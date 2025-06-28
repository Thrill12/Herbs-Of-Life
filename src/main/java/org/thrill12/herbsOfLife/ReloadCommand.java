package org.thrill12.herbsOfLife;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        Foods.LoadFoodMap(JavaPlugin.getPlugin(HerbsOfLife.class));

        sender.sendMessage("Herbs of life reloaded successfully.");

        return true;
    }
}
