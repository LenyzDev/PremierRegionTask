package me.lenyz.premierregionstask.storage;

import me.lenyz.premierregionstask.enums.FlagMode;
import me.lenyz.premierregionstask.objects.Flag;

import java.util.HashMap;
import java.util.List;

public class FlagStorage {

    private HashMap<String, Flag> flagsMap;

    public FlagStorage() {
        this.flagsMap = new HashMap<>();
        flagsMap.put("block-break", new Flag(
                "block-break",
                "&cBlock Break",
                List.of("Allows the player to break blocks in the region"),
                FlagMode.WHITELIST)
        );
        flagsMap.put("block-place", new Flag(
                "block-place",
                "&cBlock Place",
                List.of("Allows the player to place blocks in the region"),
                FlagMode.WHITELIST)
        );
        flagsMap.put("interact", new Flag(
                "interact",
                "&cInteract",
                List.of("Allows the player to interact in the region"),
                FlagMode.WHITELIST)
        );
        flagsMap.put("entity-damage", new Flag(
                "entity-damage",
                "&cEntity Damage",
                List.of("Allows the player to attack other entities in the region"),
                FlagMode.WHITELIST)
        );
    }

    public void addFlag(String id, Flag flag) {
        this.flagsMap.put(id, flag);
    }

    public void removeFlag(String id) {
        this.flagsMap.remove(id);
    }

    public Flag getFlag(String id) {
        return this.flagsMap.get(id);
    }

    public HashMap<String, Flag> getFlagsMap() {
        return flagsMap;
    }
}
