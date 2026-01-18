package com.mdt.mindustry.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.mdt.common.utils.CommonUtils;
import lombok.Builder;
import lombok.Generated;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import mindustry.gen.Player;

@Builder(toBuilder = true)
public record MenuOption(
        @NonNull String title,
        @NonNull String message,

        @NonNull String[][] options,
        @NonNull List<Consumer<Player>> actions,
        @NonNull Consumer<Player> userCloseAction) {

    // !----------------------------------------------------------------!

    public static class MenuOptionBuilder extends MenuContentBuilder<MenuOptionBuilder> {
        private @Generated Consumer<Player> userCloseAction = CommonUtils::doNothing;

        public MenuOptionBuilder completeContent() {
            List<List<String>> parsedOptions = new ArrayList<>();
            List<Consumer<Player>> parsedActions = new ArrayList<>();

            for (var row : buttonRows) {
                parsedOptions.add(row.stream().map(Button::text).toList());
                parsedActions.addAll(row.stream().map(Button::action).toList());
            }

            this.options = parsedOptions.stream()
                    .map(list -> list.toArray(new String[0]))
                    .toArray(String[][]::new);

            this.actions = parsedActions;
            return this;
        }
    }

    @Slf4j
    public static class MenuContentBuilder<E extends MenuContentBuilder<E>> {
        protected final List<List<Button>> buttonRows = new ArrayList<>();
        protected List<Button> currentRow = new ArrayList<>();

        // !----------------------------------------------------------------!

        @SuppressWarnings("unchecked")
        public E buttons(Button... buttons) {
            if (buttons.length > 0) this.currentRow.addAll(List.of(buttons));
            return (E) this;
        }

        public E button(String text, Consumer<Player> action) {
            return buttons(Button.of(text, action));
        }

        public E button(Button button) {
            return buttons(button);
        }

        @SuppressWarnings("unchecked")
        public E buttonIf(boolean condition, Supplier<Button> supplier) {
            if (condition) return button(supplier.get());
            return (E) this;
        }

        @SuppressWarnings("unchecked")
        public E row() {
            completeCurrentRow();
            return (E) this;
        }

        @SuppressWarnings("unchecked")
        public E factory(Consumer<MenuContentBuilder<?>> consumer) {
            var factoryBuilder = new MenuContentBuilder<>();
            try {
                consumer.accept(factoryBuilder);
            } catch (Exception e) {
                log.warn("Exception in factory consumer", e);
                return (E) this;
            }

            completeCurrentRow();
            for (var buttons : factoryBuilder.buttonRows) {
                this.currentRow.addAll(buttons);
                completeCurrentRow();
            }

            return (E) this;
        }

        // !----------------------------------------------------------------!

        private void completeCurrentRow() {
            if (!currentRow.isEmpty()) {
                this.buttonRows.add(new ArrayList<>(currentRow));
                this.currentRow.clear();
            }
        }
    }

    public record Button(String text, Consumer<Player> action) {

        public static Button of(String text, Consumer<Player> action) {
            return new Button(text, action);
        }
    }
}