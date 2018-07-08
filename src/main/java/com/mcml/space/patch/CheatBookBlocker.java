package com.mcml.space.patch;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.PluginExtends;

import static com.mcml.space.config.ConfigPatch.messageCheatBook;
import static com.mcml.space.config.ConfigPatch.noCheatBook;

import java.util.Map.Entry;

/**
 * @author SotrForgotten
 */
public class CheatBookBlocker implements Listener, PluginExtends {
    public static void init(JavaPlugin plugin) {
        //Bukkit.getPluginManager().registerEvents(new CheatBookBlocker(), plugin);
        AzureAPI.log("书与笔修复模块被临时禁用");
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBookEdit(PlayerEditBookEvent evt) {
        if (!noCheatBook) return;
        
        BookMeta prev = evt.getPreviousBookMeta();
        BookMeta meta = evt.getNewBookMeta();
        if (prev.equals(meta)) return;
        
        // Illegally modify lore
        if (prev.hasLore()) {
            if (!meta.hasLore() || !prev.getLore().equals(meta.getLore())) {
                meta.setLore(prev.getLore());
            }
        } else if (meta.hasLore()) {
            meta.setLore(null);
        }
        
        // Illegally modify enchants
        if (prev.hasEnchants()) {
            if (!meta.hasEnchants()) {
                addEnchantFrom(prev, meta);
            } else if (!prev.getLore().equals(meta.getLore())) {
                clearEnchant(meta);
                addEnchantFrom(prev, meta);
            }
        } else if (meta.hasEnchants()) {
            clearEnchant(meta);
        }
        
        // They cannot change title by edit it!
        String title = prev.getTitle();
        if (!title.equals(meta.getTitle())) {
            meta.setTitle(title);
        }
        
        // Book and quill doesn't has a generation!
        if (meta.getGeneration() != null) meta.setGeneration(null);
        
        // Book and quill doesn't has an author!
        if (meta.getAuthor() != null) meta.setAuthor(null);
        
        evt.setNewBookMeta(meta);
        
        AzureAPI.log(evt.getPlayer(), messageCheatBook);
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
