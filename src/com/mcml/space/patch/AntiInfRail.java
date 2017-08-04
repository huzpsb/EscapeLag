package com.mcml.space.patch;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

import com.mcml.space.config.ConfigPatch;

public class AntiInfRail implements Listener {

    @SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void PhysicsCheck(BlockPhysicsEvent event) {
        if (ConfigPatch.fixInfRail == true) {
            int checkedtimes = 0;
            if (event.getChangedType().name().contains("RAIL")) {
                checkedtimes = checkedtimes + 1;
            }
            if (event.getChangedTypeId() == 355) {
                checkedtimes = checkedtimes + 1;
            }
            if (checkedtimes >= 2) {
                event.setCancelled(true);
            }
        }
    }
}
// TODO confirm details