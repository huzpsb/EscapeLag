package com.mcml.space.patches;

import static com.mcml.space.util.VersionLevel.modernApi;
import static org.bukkit.Material.*;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.plugin.Plugin;

import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.Locale;
import com.mcml.space.util.VersionLevel;
import com.mcml.space.util.VersionLevel.Version;

public class BonemealDupePatch implements Listener {
	public static void init(Plugin plugin) {
	    if (VersionLevel.isLowerThan(Version.MINECRAFT_1_6_R3) || VersionLevel.isHigherThan(Version.MINECRAFT_1_7_R4)) return;
		Bukkit.getPluginManager().registerEvents(new BonemealDupePatch(), plugin);
		AzureAPI.log(Locale.isNative() ? "子模块 - 骨粉修复 已启动" : "Submodule - BonemealDupePatch has been enabled");
	}
	
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onGrow(StructureGrowEvent evt) {
        if (!evt.isFromBonemeal()) return;
        
        for (BlockState state : evt.getBlocks()) {
            Material material = state.getBlock().getType();
            if (modernApi() ?
                    material != LEGACY_AIR && material != LEGACY_SAPLING :
                    material != AIR && material != SAPLING) {
                evt.setCancelled(true);
            }
        }
    }
}