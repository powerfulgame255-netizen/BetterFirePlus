package com.betterfire.betterfirehud.mixin;

import com.betterfire.betterfirehud.config.BetterFireConfig;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Injects a custom tint for fire blocks when the BetterFire+ mod is toggled OFF.
 * The tint color comes from the user's RGB slider settings.
 */
@Mixin(BlockColors.class)
public class FireTintProviderMixin {

    @Inject(
        method = "getColor(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/util/math/BlockPos;I)I",
        at = @At("RETURN"),
        cancellable = true
    )
    private void betterfire$tintFire(
            BlockState state,
            @Nullable BlockRenderView view,
            @Nullable BlockPos pos,
            int tintIndex,
            CallbackInfoReturnable<Integer> cir
    ) {
        BetterFireConfig cfg = BetterFireConfig.getInstance();

        // Only tint when the mod is off (so the overlay is visible) AND the block is fire
        if (cfg.isEnabled()) return;
        if (!(state.getBlock() instanceof AbstractFireBlock)) return;

        int color = cfg.getFireColor() & 0x00FFFFFF; // strip alpha; MC expects 0xRRGGBB
        cir.setReturnValue(color);
    }
}
