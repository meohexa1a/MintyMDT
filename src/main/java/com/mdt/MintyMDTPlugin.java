package com.mdt;

import mindustry.mod.Plugin;
import org.codejargon.feather.Feather;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class MintyMDTPlugin extends Plugin {
    private final Feather feather;

    public MintyMDTPlugin() {
        // Khởi tạo Feather DI với các module cần thiết
        // Ở đây chúng ta pass 'this' vào để có thể inject chính plugin này nếu cần
        this.feather = Feather.with(this);
        log.info("MintyMDTPlugin initialized with Feather DI.");
    }

    @Override
    public void init() {
        log.info("MintyMDTPlugin loaded.");
    }

    /**
     * Helper method để lấy instance từ DI container
     */
    public <T> T getInstance(Class<T> clazz) {
        return feather.instance(clazz);
    }
}
