package me.lenyz.premierregionstask.api;

import me.lenyz.premierregionstask.events.LoadFlagsEvent;
import me.lenyz.premierregionstask.managers.RegionManager;
import me.lenyz.premierregionstask.objects.Flag;
import me.lenyz.premierregionstask.storage.FlagStorage;
import me.lenyz.premierregionstask.storage.RegionStorage;
import me.lenyz.premierregionstask.storage.UserStorage;
import org.bukkit.Bukkit;

public class RegionAPI {

    private FlagStorage flagStorage;
    private RegionStorage regionStorage;
    private UserStorage userStorage;
    private RegionManager regionManager;

    public RegionAPI(FlagStorage flagStorage, RegionStorage regionStorage, UserStorage userStorage, RegionManager regionManager) {
        this.flagStorage = flagStorage;
        this.regionStorage = regionStorage;
        this.userStorage = userStorage;
        this.regionManager = regionManager;
    }

    public FlagStorage getFlagStorage() {
        return flagStorage;
    }

    public RegionStorage getRegionStorage() {
        return regionStorage;
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }

    public void loadCustomFlag(String flagId, Flag flag){
        Bukkit.getPluginManager().callEvent(new LoadFlagsEvent(flagId, flag));
    }
}
