package me.lenyz.premierregionstask;

import me.lenyz.premierregionstask.api.RegionAPI;
import me.lenyz.premierregionstask.commands.RegionCommand;
import me.lenyz.premierregionstask.configuration.ConfigManager;
import me.lenyz.premierregionstask.database.Database;
import me.lenyz.premierregionstask.database.RegionDAO;
import me.lenyz.premierregionstask.listeners.InternalListener;
import me.lenyz.premierregionstask.listeners.PlayerListener;
import me.lenyz.premierregionstask.managers.RegionManager;
import me.lenyz.premierregionstask.runnables.ParticleTask;
import me.lenyz.premierregionstask.storage.FlagStorage;
import me.lenyz.premierregionstask.storage.RegionStorage;
import me.lenyz.premierregionstask.storage.UserStorage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.logging.Logger;

public final class PremierRegionsTask extends JavaPlugin {

    private ConfigManager configManager;
    private RegionDAO regionDAO;
    private FlagStorage flagStorage;
    private RegionStorage regionStorage;
    private UserStorage userStorage;
    private ParticleTask particleTask;
    private RegionManager regionManager;
    private static RegionAPI regionAPI;
    private Logger logger;

    @Override
    public void onEnable() {
        logger = getLogger();

        configManager = new ConfigManager(this);
        Database.connect(logger, configManager.getConfig("config"));
        regionDAO = new RegionDAO();

        flagStorage = new FlagStorage();
        regionStorage = new RegionStorage(regionDAO);
        userStorage = new UserStorage();

        particleTask = new ParticleTask(userStorage);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, particleTask, 20, 20);

        regionManager = new RegionManager(flagStorage, regionStorage);
        regionAPI = new RegionAPI(flagStorage, regionStorage, userStorage, regionManager);

        this.getServer().getPluginManager().registerEvents(new PlayerListener(userStorage, regionStorage, regionManager), this);
        this.getServer().getPluginManager().registerEvents(new InternalListener(flagStorage, logger), this);
        this.getCommand("region").setExecutor(new RegionCommand(userStorage, regionStorage, flagStorage));

        scheduleTask();
    }

    @Override
    public void onDisable() {
        Database.disconnect(logger);
    }

    public static RegionAPI getRegionAPI() {
        return regionAPI;
    }

    private void scheduleTask() {
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleAsyncRepeatingTask(this, new BukkitRunnable() {
            @Override
            public void run() {
                Database.reloadConnection(logger, configManager.getConfig("config"));
            }
        }, 20L * 60 * 60, 20L * 60 * 60);
    }
}
