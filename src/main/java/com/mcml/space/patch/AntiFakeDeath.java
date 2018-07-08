package com.mcml.space.patch;

import org.bukkit.entity.Player;

import com.google.common.base.Predicate;
import com.mcml.space.config.ConfigPatch;
import com.mcml.space.util.PlayerList;

public class AntiFakeDeath implements Runnable{

    @Override
    public void run() {
        if(ConfigPatch.noFakedeath){
            PlayerList.forEach(new Predicate<Player>() {
                @Override
                public boolean apply(Player player) {
                    if(player.getHealth() <= 0 && !player.isDead()){
                        player.setHealth(0.0);
                        player.kickPlayer(ConfigPatch.messageFakedeath);
                    }
                    return true;
                }
            });
        }
    }
}
