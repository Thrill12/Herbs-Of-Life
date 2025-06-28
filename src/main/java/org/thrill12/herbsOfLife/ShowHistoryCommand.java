package org.thrill12.herbsOfLife;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static org.thrill12.herbsOfLife.Storage.allPlayerHistories;

public class ShowHistoryCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length != 1){
            sender.sendMessage("Incorrect usage. Correct: /hol showHistory <player>");
            return false;
        }

        // Get player UID by name
        Player player = Bukkit.getPlayer(args[0]);
        String playerUID = player.getUniqueId().toString();

        List<String> ls = Storage.allPlayerHistories.get(playerUID);

        ls = ls.reversed();

        int maxIndex = Math.min(10, ls.size());

        ArrayList<String> newList = new ArrayList<>();

        for(int i = 0; i < maxIndex;i++){
            newList.add(ls.get(i));
        }

        sender.sendMessage("Showing food history for " + args[0] + ": " + newList.toString());

        return true;
    }
}
