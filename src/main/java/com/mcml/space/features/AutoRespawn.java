package com.mcml.space.features;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

import com.mcml.space.config.Features;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.PluginExtends;
import com.mcml.space.util.VersionLevel;
import com.mcml.space.util.VersionLevel.Version;

import static com.mcml.space.config.Features.canAutoRespawn;
import static com.mcml.space.config.Features.sendTitleAutoRespawn;
import static com.mcml.space.config.Features.titleAutoRespawn;

import static com.mcml.space.config.Features.subtitleAutoRespawn;

/**
 * @author Vlvxingze, SotrForgotten
 */
public class AutoRespawn implements Listener, PluginExtends {
    public static void init(Plugin plugin) {
        if (!canAutoRespawn) return;
        
        if (!VersionLevel.isSpigot()) {
            AzureAPI.warn("Auto respawn feature only works with servers implemented Spigot API!");
            Features.canAutoRespawn = false;
            EscapeLag.plugin.setupConfig(EscapeLag.CONFIG_FEATURES, Features.class);
            return;
        }
        
        Bukkit.getPluginManager().registerEvents(new AutoRespawn(), plugin);
        AzureAPI.log("自动重生模块已启动");
    }
    
    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void autoRespawn(PlayerDeathEvent evt) {
        final Player player = evt.getEntity();
        Bukkit.getScheduler().runTask(EscapeLag.plugin, () -> {
            player.spigot().respawn();
            if (sendTitleAutoRespawn && VersionLevel.isHigherEquals(Version.MINECRAFT_1_8_R2)) {
                player.sendTitle(titleAutoRespawn, subtitleAutoRespawn);
            }
        });
    }
}
