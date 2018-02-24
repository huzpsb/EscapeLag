package com.mcml.space.patch;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.mcml.space.core.EscapeLag;
import com.mcml.space.util.AzureAPI;

public class AntiWEcalc implements Listener {
	public static void init() {
		if (Bukkit.getPluginManager().getPlugin("WorldEdit") != null) {
			AzureAPI.log("已经启用WEcalc命令炸服Bug!");
			Bukkit.getPluginManager().registerEvents(new AntiWEcalc(), EscapeLag.MainThis);
		}
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		String command = event.getMessage();
		if (command.startsWith("//calc") && event.getPlayer().hasPermission("EscapeLag.admin") == false) {
			event.getPlayer().sendMessage("§a§l[EscapeLag]§c禁止利用WE bug炸服!");
			event.setCancelled(true);
		}
	}
}
