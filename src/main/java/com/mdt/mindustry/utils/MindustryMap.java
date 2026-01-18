package com.mdt.mindustry.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import mindustry.Vars;
import mindustry.io.MapIO;
import mindustry.maps.Map;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@UtilityClass
public class MindustryMap {

    public static Map findOrGetRandom(String input) {
        if (input == null || input.isEmpty()) return getRandom();

        var customMapResult = Vars.maps.customMaps().find(map -> map.name().equalsIgnoreCase(input));
        if (customMapResult != null) return customMapResult;

        var defaultMapResult = Vars.maps.defaultMaps().find(map -> map.name().equalsIgnoreCase(input));
        if (defaultMapResult != null) return defaultMapResult;

        return getRandom();
    }

    public static @Nonnull Map getRandom() {
        if (!Vars.maps.customMaps().isEmpty()) return Vars.maps.customMaps().random();
        return Vars.maps.defaultMaps().random();
    }

    // !------------------------------------------------------------!

    public static Map newFromStream(@Nonnull InputStream stream) {
        try {
            var fi = new arc.files.Fi(File.createTempFile(UUID.randomUUID().toString(), Vars.mapExtension));
            fi.write(stream, false);

            return MapIO.createMap(fi, true);
        } catch (Exception e) {
            log.error("Failed to load map from stream.", e);
            return null;
        }
    }
}
