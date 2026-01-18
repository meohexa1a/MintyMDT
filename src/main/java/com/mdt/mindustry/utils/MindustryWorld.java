package com.mdt.mindustry.utils;

import arc.files.Fi;
import com.mdt.common.signal.Result;
import com.mdt.common.signal.Unit;
import com.mdt.mindustry.utils.exception.WorldCaptureException;
import com.mdt.mindustry.utils.exception.WorldLoadException;
import lombok.experimental.UtilityClass;
import mindustry.Vars;
import mindustry.io.SaveIO;
import mindustry.maps.Map;
import mindustry.net.WorldReloader;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class MindustryWorld {

    public static Result<Unit, WorldCaptureException> captureSnapshot(@NotNull Fi file) {
        if (Vars.state.isMenu())
            return Result.error(new WorldCaptureException.WorldClosedException());

        if (file.isDirectory())
            return Result.error(new WorldCaptureException.InvalidInputException("Snapshot target is a directory: " + file));

        try {
            SaveIO.save(file);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(new WorldCaptureException.WorldCaptureRuntimeException(e));
        }
    }

    // !----------------------------------------------------------------!

    public static Result<Unit, WorldLoadException> loadMap(@NotNull Map map) {
        if (!SaveIO.isSaveValid(map.file))
            return Result.error(new WorldLoadException.InvalidInputException("Invalid map: " + map.file.name()));

        return doLoad(() -> Vars.world.loadMap(map));
    }

    public static Result<Unit, WorldLoadException> loadFile(@NotNull Fi file) {
        if (!SaveIO.isSaveValid(file))
            return Result.error(new WorldLoadException.InvalidInputException("Invalid save file: " + file.name()));

        return doLoad(() -> SaveIO.load(file));
    }

    private static Result<Unit, WorldLoadException> doLoad(Runnable loader) {
        WorldReloader reloader = null;

        try {
            if (Vars.net.active()) {
                reloader = new WorldReloader();
                reloader.begin();
            }

            Vars.logic.reset();
            loader.run();
            Vars.logic.play();
            Vars.state.rules.canGameOver = false;

            if (reloader != null) reloader.end();
            else Vars.netServer.openServer();

            return Result.ok();
        } catch (Throwable t) {
            Vars.logic.reset();
            if (reloader != null) reloader.end();
            return Result.error(new WorldLoadException.WorldLoadRuntimeException(t));
        }
    }
}
