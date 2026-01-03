package com.mdt.common.constant;

import lombok.experimental.UtilityClass;
import mindustry.game.EventType.*;

import java.util.List;

@UtilityClass
public class CommonConfig {

    public static final List<Class<?>> DEFAULT_EVENTS = List.of(
            WorldLoadBeginEvent.class, WorldLoadEvent.class, WorldLoadEndEvent.class,

            TileChangeEvent.class, TileFloorChangeEvent.class, TileOverlayChangeEvent.class,
            BuildTeamChangeEvent.class, CoreChangeEvent.class,
            BlockBuildBeginEvent.class, BlockBuildEndEvent.class,
            BuildRotateEvent.class, BuildSelectEvent.class, BlockDestroyEvent.class,

            BuildingCommandEvent.class, ConfigEvent.class,

            WithdrawEvent.class, DepositEvent.class, PayloadDropEvent.class,

            UnitControlEvent.class, UnitCreateEvent.class, UnitSpawnEvent.class, UnitUnloadEvent.class,
            UnitChangeEvent.class, UnitDestroyEvent.class, UnitDrownEvent.class,
            PickupEvent.class,

            PlayerJoin.class, PlayerLeave.class, PlayerConnect.class, PlayerConnectionConfirmed.class,
            PlayerBanEvent.class, PlayerUnbanEvent.class, PlayerIpBanEvent.class, PlayerIpUnbanEvent.class,

            ConnectionEvent.class, ConnectPacketEvent.class, AdminRequestEvent.class,

            MenuOptionChooseEvent.class, TextInputEvent.class, TapEvent.class, PlayerChatEvent.class);
}