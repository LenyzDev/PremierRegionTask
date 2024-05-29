package me.lenyz.premierregionstask.configuration;

import me.lenyz.premierregionstask.PremierRegionsTask;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {

    private PremierRegionsTask main;

    public ConfigManager(PremierRegionsTask main) {
        this.main = main;
        createConfig("config");
    }

    public void createConfig(String file) {
        if (!(new File(main.getDataFolder(), file + ".yml")).exists()) {
            main.saveResource(file + ".yml", false);
        }
    }

    public FileConfiguration getConfig(String file) {
        File archive = new File(main.getDataFolder() + File.separator + file + ".yml");
        return YamlConfiguration.loadConfiguration(archive);
    }

}
