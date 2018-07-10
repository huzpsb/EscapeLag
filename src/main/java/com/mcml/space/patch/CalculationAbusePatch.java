package com.mcml.space.patch;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import com.mcml.space.config.ConfigPatch;
import com.mcml.space.util.AzureAPI;

public class CalculationAbusePatch implements Listener {
    private final static String CALC_LABEL = "//calc ";
    
	public static void init(Plugin plugin) {
	    Plugin worldedit = Bukkit.getPluginManager().getPlugin("WorldEdit");
		if (worldedit == null || hasCalculationPerms(worldedit.getDescription().getVersion())) return;
		
        AzureAPI.log("已经启用WEcalc命令炸服Bug!");
        Bukkit.getPluginManager().registerEvents(new CalculationAbusePatch(), plugin);
	}
	
	/**
	 * Fyi. https://github.com/sk89q/WorldEdit/commit/23d6fa7579566b0d92697548afc6a1e27c455a2f
	 * @param worldeditVersion
	 * @return
	 */
	public static boolean hasCalculationPerms(String worldeditVersion) {
	    // versions above 6.1.1-SNAPSHOT is fixed
	    try {
	        if (Integer.valueOf(worldeditVersion.charAt(0)) > 6) return true;
	        if (Integer.valueOf(worldeditVersion.charAt(2)) > 1) return true;
	        if (Integer.valueOf(worldeditVersion.charAt(4)) > 1) return true;
	    } catch (Throwable t) {
	        // In case they changed version format
	    }
        return false;
    }

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onCommand(PlayerCommandPreprocessEvent evt) {
		String command = evt.getMessage();
		if (!command.startsWith(CALC_LABEL) || AzureAPI.hasPerm(evt.getPlayer(), "worldedit.calc")) return;
		
        evt.getPlayer().sendMessage(ConfigPatch.AntiWEcalcWarnMessage);
        evt.setCancelled(true);
	}
}
