package com.mdt.mindustry.popup;

import javax.inject.Singleton;
import arc.util.Timer;
import mindustry.gen.Call;
import lombok.Locked;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mindustry.gen.Groups;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Slf4j
@Singleton
@RequiredArgsConstructor
public final class PopupRegisterService {
    private final Map<String, List<PopupProvider>> registered = new HashMap<>();

    // !----------------------------------------------------------------!

    {
        Timer.schedule(() -> arc.Core.app.post(this::applyProviders), 5000, 1000);
    }

    // !----------------------------------------------------------------!

    @Locked.Write
    public void register(@NotNull String group, @NotNull Set<PopupProvider> providers) {
        registered.computeIfAbsent(group, k -> new ArrayList<>()).addAll(providers);
    }

    @Locked.Write
    public void unregister(@NotNull String group) {
        registered.remove(group);
    }

    // !----------------------------------------------------------------!

    @Locked.Read
    private List<PopupProvider> copyProviders() {
        return registered.values().stream().flatMap(Collection::stream).toList();
    }

    private void applyProviders() {
        if (registered.isEmpty()) return;

        for (var player : Groups.player) {
            for (var provider : copyProviders()) {
                try {
                    for (var content : provider.content().apply(player)) {
                        var margin = content.zone().getMargin(player);

                        Call.infoPopupReliable(player.con, content.content(), 1.05f,
                                content.zone().getAlignFlag(),
                                margin.top(), margin.left(), margin.bottom(), margin.right());
                    }
                } catch (Exception e) {
                    log.error("Provider {} failed for player {}", provider.getClass().getSimpleName(), player.name, e);
                }
            }
        }
    }
}
