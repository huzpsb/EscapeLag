package com.mcml.space.patches;

import com.mcml.space.config.Patches;
import com.mcml.space.core.DataManager;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.util.AzureAPI;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class AntiCrashOP implements Listener {

    public static List<String> TheOps = new ArrayList();

    public static void init() {
        if (Patches.AntiCrashOPenable) {
            AzureAPI.log("子模块 - 反崩溃OP 已启动");
            if (DataManager.data.isList("OpList") == false) {
                for (OfflinePlayer offplayer : Bukkit.getOperators()) {
                    TheOps.add(offplayer.getName());
                }
                AzureAPI.log("第一次运行模块,正在导入现有OP: " + TheOps);
                AntiCrashOP.save();
                DataManager.save();
            }
            TheOps = DataManager.data.getStringList("OpList");
            Bukkit.getPluginManager().registerEvents(new AntiCrashOP(), EscapeLag.plugin);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (Bukkit.getOperators().contains(player)) {
            if (TheOps.contains(player.getName()) == false) {
                player.setOp(false);
                AzureAPI.log(player, Patches.AntiCrashOPWarnMessage);
            }
        }
    }

    public static void save() {
        DataManager.data.set("OpList", TheOps);
    }
}
