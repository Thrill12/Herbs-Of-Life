package org.thrill12.herbsOfLife;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

public class Storage {
    public static HashMap<String, HashMap<String, Integer>> allPlayerAmounts = new HashMap<>();

    public static HashMap<String, List<String>> allPlayerHistories = new HashMap<>();

    public static String savePath = "save.txt";
    public static String historiesPath = "histories.txt";

    public static void SaveFile(){
        // Save counts
        try{
            if(!Files.exists(Path.of(Storage.savePath))){
                File saveFile = new File(savePath);
                saveFile.createNewFile();
            }
        }
        catch(IOException e){
            Logs.Log(e.getMessage());
        }

        try(ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(savePath))){
            os.writeObject(allPlayerAmounts);
        } catch (IOException e) {
            Logs.Log(e.getMessage());
        }

        // Save Histories
        try{
            if(!Files.exists(Path.of(Storage.historiesPath))){
                File saveFile = new File(historiesPath);
                saveFile.createNewFile();
            }
        }
        catch(IOException e){
            Logs.Log(e.getMessage());
        }

        try(ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(historiesPath))){
            os.writeObject(allPlayerHistories);
        } catch (IOException e) {
            Logs.Log(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static void LoadFile(){
        try(ObjectInputStream is = new ObjectInputStream(new FileInputStream(savePath))){
            allPlayerAmounts = (HashMap<String, HashMap<String, Integer>>) is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Logs.Log(e.getMessage());
        }

        try(ObjectInputStream is = new ObjectInputStream(new FileInputStream(historiesPath))){
            allPlayerHistories = (HashMap<String, List<String>>) is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Logs.Log(e.getMessage());
        }
    }

    public static FileConfiguration GetConfig(){
        return JavaPlugin.getPlugin(HerbsOfLife.class).getConfig();
    }
}

