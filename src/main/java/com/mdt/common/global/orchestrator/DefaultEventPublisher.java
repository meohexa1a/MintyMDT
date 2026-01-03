package com.mdt.common.global.orchestrator;

import arc.Events;
import com.mdt.common.constant.CommonConfig;
import mindustry.game.EventType.*;
import mindustry.gen.Building;
import mindustry.gen.Unit;

import com.mdt.common.shared.type.DeduplicationRegistry;

import io.micronaut.context.annotation.Context;
import io.micronaut.context.event.ApplicationEventPublisher;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@Context
@RequiredArgsConstructor
final class DefaultEventPublisher {
    private final ApplicationEventPublisher<Object> eventPublisher;

    private final DeduplicationRegistry<Integer> unitDeduplicated = new DeduplicationRegistry<>(1000, 100);
    private final DeduplicationRegistry<Integer> blockDeduplicated = new DeduplicationRegistry<>(1000, 100);

    // !---------------------------------------------------------------!

    @PostConstruct
    public void init() {
        CommonConfig.DEFAULT_EVENTS.forEach(eventType -> Events.on(eventType, eventPublisher::publishEvent));

        Events.on(UnitBulletDestroyEvent.class, e -> publishIfNewUnit(e.unit, e));
        Events.on(UnitDamageEvent.class,
                e -> publishIfNewUnit(e.unit, new UnitBulletDestroyEvent(e.unit, e.bullet)));

        Events.on(BuildingBulletDestroyEvent.class, e -> publishIfNewBlock(e.build, e));
        Events.on(BuildDamageEvent.class,
                e -> publishIfNewBlock(e.build, new BuildingBulletDestroyEvent(e.build, e.source)));
    }

    private void publishIfNewUnit(Unit unit, UnitBulletDestroyEvent event) {
        if (unit != null && unit.health <= 0 && unitDeduplicated.markIfNew(unit.id))
            eventPublisher.publishEvent(event);
    }

    private void publishIfNewBlock(Building build, BuildingBulletDestroyEvent event) {
        if (build != null && build.health <= 0 && blockDeduplicated.markIfNew(build.id))
            eventPublisher.publishEvent(event);
    }
}
