package me.lenyz.premierregionstask.listeners;

import me.lenyz.premierregionstask.events.LoadFlagsEvent;
import me.lenyz.premierregionstask.storage.FlagStorage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.logging.Logger;

public class InternalListener implements Listener {

    private FlagStorage flagStorage;
    private Logger logger;

    public InternalListener(FlagStorage flagStorage, Logger logger) {
        this.flagStorage = flagStorage;
        this.logger = logger;
    }

    @EventHandler
    public void onLoadFlags(LoadFlagsEvent event) {
        if(flagStorage.getFlagsMap().containsKey(event.getId())) {
            logger.warning("Flag " + event.getId() + " already exist.");
        }
        flagStorage.addFlag(event.getId(), event.getFlag());
        logger.info("Flag " + event.getId() + " loaded.");
    }

}
