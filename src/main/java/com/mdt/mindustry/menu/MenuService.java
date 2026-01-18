package com.mdt.mindustry.menu;

import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mdt.common.type.Pair;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.inject.Singleton;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Slf4j
@Singleton
@RequiredArgsConstructor
public final class MenuService {
    private final int menuId = Menus.registerMenu(this::handleMenuSelection);
    private final int inputId = Menus.registerTextInput(this::handleTextInput);

    private final Cache<@NotNull String, Pair<List<Consumer<Player>>, Consumer<Player>>> showedMenuOption =
            Caffeine.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build();
    private final Cache<@NotNull String, Pair<BiConsumer<Player, String>, Consumer<Player>>> showedMenuInput =
            Caffeine.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build();

    // !----------------------------------------------------------------!

    public void showMenu(@NotNull Player player, @NotNull MenuOption menuOption) {
        showedMenuOption.put(player.uuid(), Pair.of(menuOption.actions(), menuOption.userCloseAction()));

        Call.hideFollowUpMenu(player.con, menuId);
        Call.followUpMenu(player.con, menuId, menuOption.title(), menuOption.message(), menuOption.options());
    }

    public void showInput(@NotNull Player player,@NotNull MenuInput menuInput) {
        showedMenuInput.put(player.uuid(), Pair.of(menuInput.action(), menuInput.userCloseAction()));

        Call.textInput(player.con, inputId, menuInput.title(), menuInput.message(), 1024, menuInput.holder(), menuInput.isNumber());
    }

    // !----------------------------------------------------------------!

    private void handleMenuSelection(Player player, int option) {
        Call.hideFollowUpMenu(player.con, menuId);

        var menuOption = showedMenuOption.asMap().remove(player.uuid());
        if (menuOption == null) {
            log.warn("Menu actions not found/expired for player: {}", player.name);
            return;
        }

        try {
            if (option < 0) menuOption.second().accept(player); // userCloseAction
            else if (option < menuOption.first().size()) menuOption.first().get(option).accept(player);
            else log.warn("Invalid menu selection by player: {}, option: {}", player.name, option);
        } catch (Exception e) {
            log.error("Error executing menu action for player: {}, option: {}", player.name, option, e);
        }
    }

    private void handleTextInput(Player player, String text) {
        var menuInput = showedMenuInput.asMap().remove(player.uuid());
        if (menuInput == null) {
            log.warn("Menu input action not found/expired for player: {}", player.name);
            return;
        }

        try {
            if (text == null) menuInput.second().accept(player); // userCloseAction
            else menuInput.first().accept(player, text);
        } catch (Exception e) {
            log.error("Error executing text input action for player: {}, inputId: {}", player.name, inputId, e);
        }
    }
}
