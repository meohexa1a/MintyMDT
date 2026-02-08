# Developer Guide - Mindustry Plugin Framework (MDT)

This guide provides an overview of the plugin framework architecture and instructions on how to use its core components: Commands, Menus, Popups, and Signals.

## Architecture Overview

This project is built using modern Java 21 features and a functional programming philosophy. Key architectural choices include:

*   **Dependency Injection (DI):** Uses `Feather` (via JSR-330 annotations like `@Inject`, `@Singleton`) to manage component lifecycles and dependencies.
*   **Functional Programming:** Heavy use of `Consumer`, `BiConsumer`, and a custom `Result<T, F>` monad for error handling instead of exceptions.
*   **Immutability:** Usage of Java `records` and immutable collections where possible.
*   **Lombok:** Extensive use of Lombok for boilerplate reduction (`@Builder`, `@Slf4j`, `@Locked`).

## 1. Commands

The framework provides a unified way to register client and console commands using `CommandRegisterService`.

### Defining a Command

Commands are defined using the `ClientCommand` and `ConsoleCommand` records. Use the builder pattern to create them.

```java
import com.mdt.mindustry.command.ClientCommand;
import mindustry.gen.Player;
import java.util.Set;

// ... inside a service or provider class
public ClientCommand createMyCommand() {
    return ClientCommand.builder()
        .prefixes(Set.of("mycmd", "mc")) // Command aliases
        .description("Does something cool")
        .args("<player>")
        .action((args, player) -> {
            player.sendMessage("You invoked the command!");
        })
        .build();
}
```

### Registering Commands

Inject `CommandRegisterService` and call `register`.

```java
import com.mdt.mindustry.command.CommandRegisterService;
import javax.inject.Inject;

public class MyPluginService {
    private final CommandRegisterService commandService;

    @Inject
    public MyPluginService(CommandRegisterService commandService) {
        this.commandService = commandService;
    }

    public void init() {
        commandService.register(
            "my-module",
            Set.of(createMyCommand()), // Set<ClientCommand>
            Set.of()                   // Set<ConsoleCommand>
        );
    }
}
```

## 2. Menus

The `MenuService` handles interactive menus efficiently by reusing a single global menu ID and managing state per player.

### Creating a Menu

Use `MenuOption` and its builder to define the menu structure.

**Important:** You must call `.completeContent()` on the builder before `.build()` to ensure buttons are processed correctly.

```java
import com.mdt.mindustry.menu.MenuOption;

public void openMyMenu(Player player) {
    var menu = MenuOption.builder()
        .title("My Menu")
        .message("Choose an option:")
        .userCloseAction(p -> p.sendMessage("Menu closed."))
        // Start building buttons
        .button("Option 1", p -> p.sendMessage("Clicked 1"))
        .button("Option 2", p -> p.sendMessage("Clicked 2"))
        .row() // New row
        .button("Close", p -> {})
        .completeContent() // REQUIRED!
        .build();

    menuService.showMenu(player, menu);
}
```

### Text Input

Use `MenuInput` for simple text prompts.

```java
import com.mdt.mindustry.menu.MenuInput;

public void askForName(Player player) {
    var input = MenuInput.builder()
        .title("Name Check")
        .message("What is your name?")
        .action((p, text) -> p.sendMessage("Hello, " + text))
        .build();

    menuService.showInput(player, input);
}
```

## 3. Popups (HUD)

Use `PopupRegisterService` to display persistent information on the player's screen (HUD). The service automatically refreshes the popup every second.

### Registering a Popup Provider

```java
import com.mdt.mindustry.popup.PopupRegisterService;
import com.mdt.mindustry.popup.PopupContent;
import com.mdt.mindustry.popup.DisplayZone;

public class MyHudService {
    @Inject
    public MyHudService(PopupRegisterService popupService) {
        popupService.register("my-hud", player -> {
            // Return a list of contents to display
            return List.of(
                new PopupContent(
                    "[accent]My Server Status",
                    DisplayZone.TOP_LEFT // Position
                )
            );
        });
    }
}
```

## 4. Error Handling (Result)

Instead of throwing exceptions, use `Result<T, F>`.

```java
import com.mdt.common.signal.Result;
import com.mdt.common.signal.Failure;

public Result<Integer, Failure> divide(int a, int b) {
    if (b == 0) return Result.error(new Failure.Simple("Cannot divide by zero"));
    return Result.success(a / b);
}

// Usage
var result = divide(10, 0);
result.onSuccess(val -> System.out.println("Result: " + val))
      .onError(fail -> System.err.println("Error: " + fail.message()));
```

## integration Note

Since this framework uses Dependency Injection, you must bootstrap it in your main `Plugin` class. Ensure you construct your object graph (using `Feather` or manual instantiation) and start the services.

Example (Conceptual):
```java
public class MintyMDTPlugin extends Plugin {
    @Override
    public void init() {
         // Initialize DI container or manually create services
         var commandService = new CommandRegisterService(...);
         // ...
    }
}
```
