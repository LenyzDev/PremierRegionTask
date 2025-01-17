package me.lenyz.premierregionstask.objects;

import org.bukkit.Location;

public class BlockPosition {

    private int x, y, z;

    public BlockPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPosition(Location location){
        this.x = (int) location.getX();
        this.y = (int) location.getY();
        this.z = (int) location.getZ();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
