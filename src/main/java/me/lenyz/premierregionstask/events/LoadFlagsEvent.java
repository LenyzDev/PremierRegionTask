package me.lenyz.premierregionstask.events;

import me.lenyz.premierregionstask.objects.Flag;

public class LoadFlagsEvent extends PremierRegionEvent {

    private final String id;
    private final Flag flag;

    public LoadFlagsEvent(String id, Flag flag) {
        this.id = id;
        this.flag = flag;
    }

    public String getId() {
        return id;
    }

    public Flag getFlag() {
        return flag;
    }

}
