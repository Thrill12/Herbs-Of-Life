package org.thrill12.herbsOfLife;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ResetPlayerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length != 1){
            sender.sendMessage("Incorrect usage. Correct: /hol reset <player>");
            return false;
        }

        // Get player UID by name
        Player player = Bukkit.getPlayer(args[0]);
        String playerUID = player.getUniqueId().toString();

        Storage.allPlayerHistories.replace(playerUID, new ArrayList<>());

        sender.sendMessage("Reset food history for " + args[0] + ".");

        return true;
    }
}
