package com.mcml.space.patches;

import com.mcml.space.config.Patches;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.util.AzureAPI;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.InventoryHolder;

public class AntiDestoryUsingChest implements Listener {

    public static void init() {
        if (Patches.protectUsingChest) {
            Bukkit.getPluginManager().registerEvents(new AntiDestoryUsingChest(), EscapeLag.plugin);
            AzureAPI.log("反箱子刷物品已经加载了!");
        }
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        if(event.isCancelled()){
            return;
        }
        Block block = event.getBlock();
        BlockState state = block.getState();
        if (state instanceof InventoryHolder) {
            InventoryHolder ih = (InventoryHolder) event.getBlock().getState();
            if(ih.getInventory().getViewers().isEmpty() == false){
                event.setCancelled(true);
                event.getPlayer().sendMessage(Patches.AntiBreakUsingChestWarnMessage);
            }
        }
    }
}
