package com.mcml.space.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.bukkit.entity.EntityType.*;
import static org.bukkit.Material.*;

public class DefaultOptions {
    
    static class TypedList extends ArrayList<String> implements List<String> {
        private static final long serialVersionUID = 1L;
        
        public TypedList from(Object e) {
            assert e instanceof Enum || e instanceof String : "Unsafe cast";
            super.add(e.toString());
            return this;
        }
        
        static TypedList create() {
            return new TypedList();
        }
    }
	
	public static List<String> unloadClearEntityTypes() {
	    TypedList types = TypedList.create();
		types.from(ZOMBIE)
		     .from(SKELETON)
		     .from(SPIDER)
		     .from(CREEPER);
		return types; // TODO This should be much faster when we switch to koloboke!
	}
	
	public static List<String> slackEntityTypes() {
	    TypedList types = TypedList.create();
        types.from(ZOMBIE)
             .from(SKELETON)
             .from(SPIDER)
             .from(CREEPER)
             .from(SHEEP)
             .from(PIG)
             .from(CHICKEN);
		return types;
	}
	
	public static List<String> blockedErrorMessages() {
	    TypedList messages = TypedList.create();
	    messages.from("ErrorPluginName")
	            .from("ErrorPluginMessage");
		return messages;
	}
	
	public static List<String> EntityClearClearEntityType() {
	    TypedList types = TypedList.create();
        types.from(ZOMBIE)
             .from(SKELETON)
             .from(SPIDER)
             .from(CREEPER)
             .from(SHEEP)
             .from(PIG)
             .from(CHICKEN);
		return types;
	}
	
	public static List<String> redstoneRemovalMaterialType() {
	    TypedList types = TypedList.create();
        types.from(REDSTONE_WIRE)
             .from(DIODE_BLOCK_ON)
             .from(DIODE_BLOCK_OFF)
             .from(REDSTONE_TORCH_ON)
             .from(REDSTONE_TORCH_OFF)
             .from(REDSTONE_BLOCK);
        return types;
	}
	
	public static List<String> spamMessages() {
        TypedList messages = TypedList.create();
        messages.from("智障")
                .from("傻逼")
                .from("妈逼");
		return messages;
	}

}
