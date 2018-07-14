package com.mcml.space.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import static org.bukkit.entity.EntityType.*;
import static org.bukkit.Material.*;

public abstract class DefaultOptions {
    public static class TypedList extends ArrayList<String> implements List<String> {
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
    
    public static class TypedSet extends HashSet<String> implements Set<String> {
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
    
    public static class TypedMap extends HashMap<String, Boolean> implements Map<String, Boolean> {
        private static final long serialVersionUID = 1L;
        
        public TypedMap from(Object e) {
            assert e instanceof Enum || e instanceof String : "Unsafe cast";
            super.put(e.toString(), Boolean.TRUE);
            return this;
        }
        
        public TypedMap from(Object e, Boolean b) {
            assert e instanceof Enum || e instanceof String : "Unsafe cast";
            super.put(e.toString(), b);
            return this;
        }
        
        static TypedMap create() {
            return new TypedMap();
        }
    }
	
	public static Set<String> unloadClearEntityTypes() {
	    TypedSet types = TypedSet.create();
		types.from(ZOMBIE)
		     .from(SKELETON)
		     .from(SPIDER)
		     .from(CREEPER);
		return Sets.newHashSet(types); // TODO This should be much faster when we switch to koloboke!
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
		return Sets.newHashSet(types);
	}
	
	public static Set<String> blockedErrorMessages() {
	    TypedSet messages = TypedSet.create();
	    messages.from("ErrorPluginName")
	            .from("ErrorPluginMessage");
		return Sets.newHashSet(messages);
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
		return Sets.newHashSet(types);
	}
	
	public static Set<String> redstoneRemovalMaterialTypes() {
	    TypedSet types = TypedSet.create();
        types.from(REDSTONE_WIRE)
             .from(DIODE_BLOCK_ON)
             .from(DIODE_BLOCK_OFF)
             .from(REDSTONE_TORCH_ON)
             .from(REDSTONE_TORCH_OFF)
             .from(REDSTONE_BLOCK);
        return Sets.newHashSet(types);
	}
	
	public static Map<String, Boolean> spamMessages() {
	    TypedMap messages = TypedMap.create();
        messages.from("垃圾服") // server
                .from("破服")
                .from("狗服")
                .from("炸服")
                .from("laji服")
                
                .from("垃圾管理") // operator
                .from("死管理")
                .from("狗管理")
                .from("垃圾op")
                .from("狗op")
                .from("死op")
                
                .from("操你") // you
                .from("日你")
                .from("干你")
                .from("草你")
                .from("吊你")
                .from("丢你")
                .from("刁你")
                .from("叼你")
                .from("屌你")
                
                .from("你妈") // mother
                .from("亲妈")
                .from("他妈")
                .from("亲娘")
                .from("你母亲")
                .from("您母亲")
                .from("你娘")
                .from("死妈")
                
                .from("全家") // family
                
                .from("升天") // common
                .from("飞天")
                .from("的逼")
                .from("个逼")
                .from("贱")
                .from("屎")
                .from("屁")
                .from("艸")
                .from("屄")
                .from("肏")
                .from("婊")
                .from("弱智")
                .from("傻逼")
                .from("二叉")
                .from("傻蛋")
                .from("傻狗")
                .from("杂犬")
                .from("司马")
                .from("儿子")
                .from("艹")
                .from("傻b")
                .from("臭b")
                .from("烂b")
                .from("臭sb")
                .from("mlgb")
                
                .from("草泥马")
                .from("拟吗")
                .from("妮玛")
                .from("哈麻批")
                .from("蛤蟆皮")
                .from("你麻痹")
                .from("马币")
                .from("马勒戈壁")
                .from("买了个表")
                
                .from("草泥馬") // tradition
                .from("親媽")
                .from("他媽")
                .from("你媽")
                .from("腦殘")
                .from("賤");
		return Maps.newHashMap(messages);
	}
	
	public static Set<String> spamWhitelist() {
        TypedSet messages = TypedSet.create();
        messages.from("全家福");
        return Sets.newHashSet(messages);
    }

}
