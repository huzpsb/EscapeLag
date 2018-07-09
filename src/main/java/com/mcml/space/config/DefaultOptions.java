package com.mcml.space.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    
    static class TypedSet extends HashSet<String> implements Set<String> {
        private static final long serialVersionUID = 1L;
        
        public TypedSet from(Object e) {
            assert e instanceof Enum || e instanceof String : "Unsafe cast";
            super.add(e.toString());
            return this;
        }
        
        static TypedSet create() {
            return new TypedSet();
        }
    }
	
	public static Set<String> unloadClearEntityTypes() {
	    TypedSet types = TypedSet.create();
		types.from(ZOMBIE)
		     .from(SKELETON)
		     .from(SPIDER)
		     .from(CREEPER);
		return types; // TODO This should be much faster when we switch to koloboke!
	}
	
	public static Set<String> slackEntityTypes() {
	    TypedSet types = TypedSet.create();
        types.from(ZOMBIE)
             .from(SKELETON)
             .from(SPIDER)
             .from(CREEPER)
             .from(SHEEP)
             .from(PIG)
             .from(CHICKEN);
		return types;
	}
	
	public static Set<String> blockedErrorMessages() {
	    TypedSet messages = TypedSet.create();
	    messages.from("ErrorPluginName")
	            .from("ErrorPluginMessage");
		return messages;
	}
	
	public static Set<String> EntityClearEntityTypes() {
	    TypedSet types = TypedSet.create();
        types.from(ZOMBIE)
             .from(SKELETON)
             .from(SPIDER)
             .from(CREEPER)
             .from(SHEEP)
             .from(PIG)
             .from(CHICKEN);
		return types;
	}
	
	public static Set<String> redstoneRemovalMaterialTypes() {
	    TypedSet types = TypedSet.create();
        types.from(REDSTONE_WIRE)
             .from(DIODE_BLOCK_ON)
             .from(DIODE_BLOCK_OFF)
             .from(REDSTONE_TORCH_ON)
             .from(REDSTONE_TORCH_OFF)
             .from(REDSTONE_BLOCK);
        return types;
	}
	
	public static Set<String> spamMessages() {
        TypedSet messages = TypedSet.create();
        messages.from("智障")
                .from("傻逼")
                .from("妈逼");
		return messages;
	}

}
