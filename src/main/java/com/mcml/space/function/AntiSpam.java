package com.mcml.space.function;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.google.common.collect.Maps;
import com.mcml.space.config.ConfigFunction;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.PlayerList;
import com.mcml.space.util.QuitReactor;

public class AntiSpam implements Listener, QuitReactor {
	private final Map<Player, Long> timeRecord;

	public AntiSpam() {
		timeRecord = Maps.newHashMap();
		PlayerList.bind(this);

		Bukkit.getScheduler().runTaskTimer(EscapeLag.plugin, new Runnable() {
			@Override
			public void run() {
				timeRecord.clear();
			}
		}, AzureAPI.toTicks(TimeUnit.SECONDS, 30), AzureAPI.toTicks(TimeUnit.SECONDS, 30));
	}

	private boolean isSpamming(Player player, long now) {
		Long lastChat = timeRecord.get(player);
		if (lastChat == null)
			return false;

		return System.currentTimeMillis() - lastChat.longValue() <= ConfigFunction.AntiSpamPeriodPeriod * 1000;
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void spamChecker(AsyncPlayerChatEvent evt) {
		if (!ConfigFunction.AntiSpamenable)
			return;

		Player player = evt.getPlayer();
		if (AzureAPI.hasPerm(player, "EscapeLag.bypass.Spam")) {
			return;
		}

		long now = System.currentTimeMillis();
		if (isSpamming(player, now)) {
			evt.setCancelled(true);
			AzureAPI.log(player, ConfigFunction.AntiSpamPeriodWarnMessage);
		} else {
			timeRecord.put(player, now);
		}
	}

	@EventHandler
	public void checkSign(SignChangeEvent event) {
		if (ConfigFunction.AntiSpamenable && ConfigFunction.enableAntiDirty) {
			String[] lines = event.getLines();
			for (String line : lines) {
				Player player = event.getPlayer();
				if (AzureAPI.hasPerm(player, "EscapeLag.bypass.Spam")) {
					return;
				}
				for (String each : ConfigFunction.AntiSpamDirtyList) {
					boolean deny = true;
					for (char c : each.toLowerCase().toCharArray()) {
						if (!StringUtils.contains(line, c))
							deny = false;
					}
					if (deny) {
						event.setCancelled(true);
						AzureAPI.log(player, ConfigFunction.AntiSpamDirtyWarnMessage);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void checkDirty(AsyncPlayerChatEvent evt) {
		if (ConfigFunction.AntiSpamenable && ConfigFunction.enableAntiDirty) {
			Player player = evt.getPlayer();
			String message = evt.getMessage().toLowerCase();
			if (AzureAPI.hasPerm(player, "EscapeLag.bypass.Spam")) {
				return;
			}

			for (String each : ConfigFunction.AntiSpamDirtyList) {
				boolean deny = true;
				for (char c : each.toLowerCase().toCharArray()) {
					if (!StringUtils.contains(message, c))
						deny = false;
				}
				if (deny) {
					evt.setCancelled(true);
					AzureAPI.log(player, ConfigFunction.AntiSpamDirtyWarnMessage);
				}
			}
		}
	}

	@Override
	public void react(PlayerQuitEvent evt) {
		timeRecord.remove(evt.getPlayer());
	}
}
