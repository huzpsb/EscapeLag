package com.mcml.space.patch;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

import com.mcml.space.config.ConfigPatch;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.VersionLevel;
import com.mcml.space.util.VersionLevel.Version;

public class BonemealDupePatch implements Listener {
	
	public static void init() {
		if(VersionLevel.isHigherEquals(Version.MINECRAFT_1_6_R3) && VersionLevel.isLowerEquals(Version.MINECRAFT_1_7_R4)) {
			Bukkit.getPluginManager().registerEvents(new BonemealDupePatch(), EscapeLag.plugin);
		}else {
			AzureAPI.log("您的服务器版本在1.6.4-1.7.10以外，无需防御骨粉Bug！现在已经关闭了防御骨粉系统！");
		}
	}

    @EventHandler
    public void TreeGrowChecker(StructureGrowEvent event) {
        if (ConfigPatch.safetyBonemeal) {
        	if(event.isFromBonemeal() == false) {
        		return;
        	}
        	List<BlockState> blocks = event.getBlocks();
        	int bs = blocks.size();
        	for(int i = 0;i<bs;i++){
        		Block block = blocks.get(i).getBlock();
        		if(block.getType() != Material.AIR && block.getType() != Material.SAPLING && block.getType() != event.getLocation().getBlock().getRelative(BlockFace.DOWN).getType()){
        			event.setCancelled(true);
                    if (event.getPlayer() != null) {
                        AzureAPI.log(event.getPlayer(), "§c这棵树生长区域有方块阻挡，请不要尝试利用骨粉BUG！");
                        return;
                    }
        		}
        	}
        }
    }
}