package com.mcml.space.util;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mcml.space.util.AzureAPI.Coord;

/**
 * @author SotrForgotten
 */
public abstract class Configurable {
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    protected static @interface Node {
        String value();
    }
    
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    protected static @interface View {}

    public static void restoreNodes(Coord<File, FileConfiguration> fileCoord, Class<? extends Configurable> providerClass) throws IllegalArgumentException, IllegalAccessException, IOException {
    	FileConfiguration config = fileCoord.getValue();
    	
        for (Field field : providerClass.getDeclaredFields()) {
            Node node = field.getAnnotation(Node.class);
            if (node == null) continue;
            field.setAccessible(true);
            
            Object defaultValue = field.get(null);
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) && !Modifier.isFinal(mod)) {
                String path = node.value();
                Object configuredValue = config.get(path);
                
                View view = field.getAnnotation(View.class);
                if (view != null /* Forcely override */ || configuredValue == null) {
                    // Process and save default values
                    config.set(path, AzureAPI.colorzine(serializeCollection(defaultValue).getValue(), Object.class));
                    continue;
                }
                
                field.set(null, deserializeCollection(configuredValue, defaultValue.getClass()).getValue());
            }
        }
        
        config.save(fileCoord.getKey());
    }
    
    public static Coord<Object, Object> serializeCollection(Object defaultValue) { // field (default) value -> configured (to save) value
        Class<?> prevType = defaultValue.getClass();
        
        if (Set.class.isAssignableFrom(prevType)) {
            List<String> configValue = Lists.newArrayList(AzureAPI.colorzine(((Set<?>) defaultValue).toArray(new String[0]), String[].class));
            return AzureAPI.<Object, Object>wrapCoord(defaultValue, configValue);
        }
        
        if (Map.class.isAssignableFrom(prevType)) {
            // Combine entries to specified format
            List<String> combinedEntries = Lists.newArrayList();
            for (Entry<?, ?> entry : ((Map<?, ?>) defaultValue).entrySet()) {
                boolean canReserve = !isBoolean(entry.getKey()) && isBoolean(entry.getValue());
                boolean hideSeparator = canReserve && equalsTrue(entry.getValue());
                combinedEntries.add(
                        (canReserve ? (hideSeparator ? "" : entry.getValue()) : entry.getKey())
                        + (hideSeparator ? "" : " : ") +
                        (canReserve ? entry.getKey() : entry.getValue()));
            }
            return AzureAPI.<Object, Object>wrapCoord(defaultValue, combinedEntries);
        }
        
        return AzureAPI.<Object, Object>wrapCoord(defaultValue, defaultValue);
    }
    
    private static boolean isBoolean(Object value) {
        return value instanceof Boolean || boolean.class.isAssignableFrom(value.getClass());
    }
    
    private static boolean equalsTrue(Object value) {
        return isBoolean(value) && value.toString().equalsIgnoreCase("true");
    }
    
    public static Coord<Object, Object> deserializeCollection(Object configuredValue, Class<?> targetType) { // configured (saved) value -> field (to set) value
        if (Set.class.isAssignableFrom(targetType)) {
            Set<String> fieldValue = Sets.newHashSet(((List<?>) configuredValue).toArray(new String[0]));
            return AzureAPI.<Object, Object>wrapCoord(configuredValue, fieldValue);
        }
        
        if (Map.class.isAssignableFrom(targetType)) {
            Map<Object, Object> deserializedMap = Maps.newHashMap();
            // Recovery entries from specified format
            List<String> combinedEntries = Lists.newArrayList(((List<?>) configuredValue).toArray(new String[0]));
            for (String entry : combinedEntries) {
                deserializedMap.put(
                        // Impl Note: (boolean, string) format in configuration will be transfrom to (string, boolean) as map
                        hasMapSeparator(entry) ?
                                hasBooleanKey(entry) && !hasBooleanValue(entry) ?
                                        adaptType(StringUtils.replace(StringUtils.substringAfter(entry, " : "), "/:/", ":")) :
                                            adaptType(StringUtils.replace(StringUtils.substringBefore(entry, " : "), "/:/", ":")) // boolean key
                                : adaptType(StringUtils.replace(entry, "/:/", ":")) // no separator
                        ,
                        hasMapSeparator(entry) ?
                                hasBooleanKey(entry) && !hasBooleanValue(entry) ?
                                        adaptType(StringUtils.replace(StringUtils.substringBefore(entry, " : "), "/:/", ":")) :
                                            adaptType(StringUtils.substringAfter(entry, " : ")) // boolean key
                                : true // no separator
                        );
            }
            return AzureAPI.<Object, Object>wrapCoord(combinedEntries, targetType.cast(deserializedMap));
        }
        
        return AzureAPI.<Object, Object>wrapCoord(configuredValue, configuredValue);
    }
    
    private static boolean hasMapSeparator(String value) {
        return StringUtils.contains(value, " : ");
    }
    
    private static boolean hasBooleanKey(String entry) {
        return StringUtils.startsWith(entry, "true : ") || StringUtils.startsWith(entry, "false : ");
    }
    
    private static boolean hasBooleanValue(String entry) {
        return StringUtils.endsWith(entry, " : true") || StringUtils.endsWith(entry, " : false");
    }
    
    private static Object adaptType(String value) {
        return value.equalsIgnoreCase("true") ? true : value.equalsIgnoreCase("false") ? false : value;
    }
}
