package com.mdt.mindustry.popup.type;

import com.mdt.mindustry.popup.enums.DisplaySection;
import com.mdt.mindustry.popup.enums.DisplayZone;
import lombok.Builder;
import lombok.Generated;
import lombok.NonNull;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Builder(toBuilder = true)
public record PopupContent(@NonNull DisplayZone zone, @NonNull String content) {

    @Generated
    public static class PopupContentBuilder {
        private final EnumMap<DisplaySection, StringBuilder> sections = new EnumMap<>(DisplaySection.class);

        // !------------------------------------------------------!

        public PopupContentBuilder append(DisplaySection section, String content) {
            sections.computeIfAbsent(section, s -> new StringBuilder())
                    .append(content)
                    .append("\n");

            return this;
        }

        public PopupContentBuilder appendIf(boolean condition, DisplaySection section, Supplier<String> content) {
            if (condition) return append(section, content.get());

            return this;
        }

        public PopupContentBuilder completeContent() {
            var joined = sections.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(e -> e.getValue().toString())
                    .collect(Collectors.joining("\n\n"));

            this.content(joined.trim());
            return this;
        }
    }
}
