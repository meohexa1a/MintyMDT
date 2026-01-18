package com.mdt.mindustry.popup.type;

import lombok.Builder;
import lombok.Singular;

import java.util.Set;

@Builder(toBuilder = true)
public record GlobalPopupProvider(@Singular Set<PopupProvider> providers) {

}
