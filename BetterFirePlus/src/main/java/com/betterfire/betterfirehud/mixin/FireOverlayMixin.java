package com.betterfire.betterfirehud.mixin;

import com.betterfire.betterfirehud.BetterFirePlus;
import com.betterfire.betterfirehud.config.BetterFireConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public class FireOverlayMixin {

    /**
     * Cancels the fire overlay render when BetterFire+ decides it should be hidden.
     * The injection targets the renderFireOverlay method (Fabric-mapped name).
     */
    @Inject(
        method = "renderFireOverlay",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void betterfire$cancelFireOverlay(
            MinecraftClient client,
            MatrixStack matrices,
            CallbackInfo ci
    ) {
        if (!BetterFirePlus.shouldSuppressFireOverlay(client)) return;

        // Check fire type setting
        BetterFireConfig cfg = BetterFireConfig.getInstance();
        if (client.player == null) return;

        boolean onFire = client.player.isOnFire();
        boolean inLava = client.player.isInLava();

        switch (cfg.getFireType()) {
            case FIRE -> {
                // Suppress only regular fire (not lava)
                if (onFire && !inLava) ci.cancel();
            }
            case LAVA -> {
                // Suppress only lava
                if (inLava) ci.cancel();
            }
            case BOTH -> {
                // Suppress any fire/lava overlay
                if (onFire || inLava) ci.cancel();
            }
        }
    }
}
