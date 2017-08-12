package com.mcml.space.monitor.inject;

import org.bukkit.plugin.Plugin;

public abstract class AbstractInjector implements IInjector {
	
	/**
	 * 
	 * @author jiongjionger
	 */
	protected final Plugin plugin;

	public AbstractInjector(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public Plugin getPlugin() {
		return plugin;
	}
}
