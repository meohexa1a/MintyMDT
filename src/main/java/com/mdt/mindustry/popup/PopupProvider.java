package com.mdt.mindustry.popup;

import java.util.List;
import java.util.function.Function;

import mindustry.gen.Player;

public record PopupProvider(Function<Player, List<PopupContent>> content) {

}
