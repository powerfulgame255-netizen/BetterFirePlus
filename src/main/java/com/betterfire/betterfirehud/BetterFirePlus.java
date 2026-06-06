package com.betterfire.betterfirehud;

import com.betterfire.betterfirehud.config.BetterFireConfig;
import com.betterfire.betterfirehud.gui.BetterFireScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterFirePlus implements ClientModInitializer {

    public static final String MOD_ID = "betterfirehud";
    public static final String MOD_NAME = "BetterFire+";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static KeyBinding openGuiKey;

    @Override
    public void onInitializeClient() {
        LOGGER.info("BetterFire+ initialized!");

        // Load config
        BetterFireConfig.load();

        // Register keybind (default: J)
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.betterfirehud.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
                "category.betterfirehud"
        ));

        // Listen for key press each tick
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.player != null) {
                    client.setScreen(new BetterFireScreen(client.currentScreen));
                }
            }
        });
    }

    /**
     * Returns true if the fire overlay should be suppressed right now.
     */
    public static boolean shouldSuppressFireOverlay(MinecraftClient client) {
        BetterFireConfig cfg = BetterFireConfig.getInstance();

        if (!cfg.isEnabled()) return false;

        switch (cfg.getOverlayMode()) {
            case ALWAYS:
                return true;
            case CREATIVE:
                return client.player != null && client.player.isCreative();
            case FIRE_RESISTANCE:
            default:
                return client.player != null && client.player.hasStatusEffect(
                        net.minecraft.entity.effect.StatusEffects.FIRE_RESISTANCE
                );
        }
    }
}
