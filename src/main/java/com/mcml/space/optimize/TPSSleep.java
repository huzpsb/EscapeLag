package com.mcml.space.optimize;

import org.bukkit.Bukkit;

import com.mcml.space.config.ConfigOptimize;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.util.PlayerList;
import com.mcml.space.util.TPSAndThread;

public class TPSSleep {
	public static int NowSleepTo = 20;
	public static int ShouldWaitSecondToManager = 0;

	public static void init() {
		Bukkit.getScheduler().runTaskTimer(EscapeLag.PluginMain, new Runnable() {
			public void run() {
				if (ConfigOptimize.TPSSleepNoOneFreezeenable == true && PlayerList.size() == 0) {
					try {
						Thread.sleep(TPSSleep.getTargetSleepTime(5));
					} catch (InterruptedException e) {
					}
				}
				if (ConfigOptimize.TPSSleepSleepMode.equalsIgnoreCase("NoUse")) {
				} else if (ConfigOptimize.TPSSleepSleepMode.equalsIgnoreCase("AutoSleep")) {
					try {
						Thread.sleep(TPSSleep.getTargetSleepTime(NowSleepTo));
					} catch (InterruptedException e) {
					}
				} else if(ConfigOptimize.TPSSleepSleepMode.matches("[0-9]+")){
					try {
						Thread.sleep(TPSSleep.getTargetSleepTime(Integer.parseInt(ConfigOptimize.TPSSleepSleepMode)));
					} catch (InterruptedException e) {
					}
				}
			}
		}, 1, 1);
		Bukkit.getScheduler().runTaskTimerAsynchronously(EscapeLag.PluginMain, new Runnable() {
			public void run() {
				if(ShouldWaitSecondToManager > 0) {
					ShouldWaitSecondToManager = ShouldWaitSecondToManager - 1;
					return;
				}
				if (TPSSleep.NowSleepTo < 20) {
					if (TPSSleep.NowSleepTo - 1 < TPSAndThread.getTPS()) {
						TPSSleep.NowSleepTo = TPSSleep.NowSleepTo + 1;
					} else {
						TPSSleep.NowSleepTo = TPSAndThread.getTPS();
						ShouldWaitSecondToManager = 20;
					}
					return;
				}
				if(TPSAndThread.getTPS() <= 18) {
					TPSSleep.NowSleepTo = TPSAndThread.getTPS();
					ShouldWaitSecondToManager = 20;
				}
			}
		}, 20, 20);
	}

	public static int getTargetSleepTime(int tps) {
		return (50 - ((5 * tps) / 2));
	}
}
