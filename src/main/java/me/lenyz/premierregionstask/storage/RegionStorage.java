package me.lenyz.premierregionstask.storage;

import me.lenyz.premierregionstask.database.RegionDAO;
import me.lenyz.premierregionstask.objects.Region;

import java.util.HashMap;
import java.util.UUID;

public class RegionStorage {

    private RegionDAO regionDAO;
    private HashMap<String, Region> regionsMap;

    public RegionStorage(RegionDAO regionDAO) {
        this.regionDAO = regionDAO;
        this.regionsMap = regionDAO.loadAllRegions();
    }

    public void addRegion(String id, Region region) {
        this.regionsMap.put(id, region);
    }

    public void removeRegion(String id) {
        this.regionsMap.remove(id);
    }

    public Region getRegion(String id) {
        return this.regionsMap.get(id);
    }

    public HashMap<String, Region> getRegionsMap() {
        return regionsMap;
    }

    public boolean isRegionExists(String id) {
        return this.regionsMap.containsKey(id);
    }

    public void createRegion(String id, Region region) {
        this.regionsMap.put(id, region);
        this.regionDAO.updateRegion(region);
    }

    public void deleteRegion(String id){
        this.regionsMap.remove(id);
        regionDAO.removeRegion(id);
    }

    public void updateRegion(Region region){
        this.regionsMap.put(region.getName(), region);
        regionDAO.updateRegion(region);
    }
}
