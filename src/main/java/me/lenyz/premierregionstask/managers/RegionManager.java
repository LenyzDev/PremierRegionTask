package me.lenyz.premierregionstask.managers;

import me.lenyz.premierregionstask.enums.FlagMode;
import me.lenyz.premierregionstask.objects.BlockPosition;
import me.lenyz.premierregionstask.objects.Flag;
import me.lenyz.premierregionstask.objects.Region;
import me.lenyz.premierregionstask.storage.FlagStorage;
import me.lenyz.premierregionstask.storage.RegionStorage;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class RegionManager {

    private FlagStorage flagStorage;
    private RegionStorage regionStorage;

    public RegionManager(FlagStorage flagStorage, RegionStorage regionStorage) {
        this.flagStorage = flagStorage;
        this.regionStorage = regionStorage;
    }

    public List<Region> getRegionsAt(String world, BlockPosition blockPosition) {
        return CompletableFuture.supplyAsync(() -> {
            List<Region> regions = new ArrayList<>();
            for (Region region : regionStorage.getRegionsMap().values()) {
                if (region.getCuboid().contains(world, blockPosition)) {
                    regions.add(region);
                }
            }
            return regions;
        }).join();
    }

    public FlagMode getFlagValue(Region region, String flagId) {
        if(region.getFlags().containsKey(flagId)) return region.getFlags().get(flagId);

        Flag flag = flagStorage.getFlag(flagId);
        if(flag == null) return null;
        return flag.getDefaultValue();
    }
}
