package me.lenyz.premierregionstask.runnables;

import me.lenyz.premierregionstask.objects.BlockPosition;
import me.lenyz.premierregionstask.storage.UserStorage;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class ParticleTask implements Runnable {

    private UserStorage userStorage;

    public ParticleTask(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void run() {
        userStorage.getUsersMap().values().forEach(user -> {
            Player player = user.getPlayer();
            if(player == null) return;

            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(255, 0, 128), 1.0F);
            BlockPosition firstPosition = user.getFirstPosition();
            if(firstPosition != null){
                user.getPlayer().spawnParticle(Particle.REDSTONE, firstPosition.getX()+0.5D, firstPosition.getY()+0.5D, firstPosition.getZ()+0.5D, 50, 0, 1, 0, 0, dustOptions);
            }

            BlockPosition secondPosition = user.getSecondPosition();
            if(secondPosition != null){
                user.getPlayer().spawnParticle(Particle.REDSTONE, secondPosition.getX()+0.5D, secondPosition.getY()+0.5D, secondPosition.getZ()+0.5D, 50, 0, 1, 0, 0, dustOptions);
            }
        });
    }
}
