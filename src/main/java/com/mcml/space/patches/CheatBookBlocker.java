package com.mcml.space.patches;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.Plugin;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.Locale;

import static com.mcml.space.config.Patches.noCheatBook;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author SotrForgotten
 */
public class CheatBookBlocker implements Listener {
    public static void init(Plugin plugin) {
        if (!noCheatBook) return;
        
        Bukkit.getPluginManager().registerEvents(new CheatBookBlocker(), plugin);
        AzureAPI.log(Locale.isNative() ? "子模块 - 书与笔修复 已启动" : "Submodule - CheatBookBlocker has been enabled");
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBookEdit(PlayerEditBookEvent evt) {
        BookMeta prev = evt.getPreviousBookMeta();
        BookMeta meta = evt.getNewBookMeta();
        if (prev.equals(meta)) return;
        
        // Illegally modify lore
        List<String> lore = prev.getLore();
        if (lore == null || lore.isEmpty()) {
            meta.setLore(null);
        } else if (!lore.equals(meta.getLore())) {
            meta.setLore(lore);
        }
        
        // Illegally modify enchants
        Map<Enchantment, Integer> enchants = prev.getEnchants();
        if (enchants == null || enchants.isEmpty()) {
            clearEnchant(meta);
        } else if (!enchants.equals(meta.getEnchants())) {
            clearEnchant(meta);
            addEnchantFrom(prev, meta);
        }
        
        // Illegally modify item flags
        Set<ItemFlag> itemFlags = prev.getItemFlags();
        if (itemFlags == null || itemFlags.isEmpty()) {
            meta.removeItemFlags(ItemFlag.values());
        } else if (!itemFlags.equals(meta.getItemFlags())) {
            meta.removeItemFlags(ItemFlag.values());
            meta.addItemFlags(itemFlags.toArray(new ItemFlag[0]));
        }
        
        evt.setNewBookMeta(meta);
    }
    
    public static BookMeta clearEnchant(BookMeta meta) {
        for (Enchantment e : meta.getEnchants().keySet()) {
            meta.removeEnchant(e);
        }
        return meta;
    }
    
    public static BookMeta addEnchantFrom(BookMeta source, BookMeta meta) {
        for (Entry<Enchantment, Integer> e : source.getEnchants().entrySet()) {
            meta.addEnchant(e.getKey(), e.getValue(), true);
        }
        return meta;
    }
}
