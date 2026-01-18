package com.mdt.mindustry.popup.service;

import com.mdt.mindustry.popup.type.GlobalPopupProvider;
import com.mdt.mindustry.popup.type.PopupProvider;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Singleton
@RequiredArgsConstructor
final class GlobalInfoPopupRegister {
    private final Set<GlobalPopupProvider> providers;

    private final PopupRegisterService registerService;

    // !-----------------------------------------------------!

    @PostConstruct
    public void init() {
        Set<PopupProvider> popupProviders = new HashSet<>();

        for (var provider : providers)
            popupProviders.addAll(provider.providers());

        registerService.register("global", popupProviders);
    }
}
