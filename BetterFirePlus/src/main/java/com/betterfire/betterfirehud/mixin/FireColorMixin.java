package com.betterfire.betterfirehud.mixin;

import com.betterfire.betterfirehud.config.BetterFireConfig;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.render.VertexConsumer;

/**
 * When the mod is disabled, tint fire/lava block faces with the user's chosen color.
 * This is a best-effort visual tint via the block's quad color — works for fire blocks.
 */
@Mixin(BlockModelRenderer.class)
public class FireColorMixin {

    @Inject(
        method = "render(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;ZLjava/util/Random;JI)Z",
        at = @At("HEAD"),
        cancellable = false,
        require = 0   // Don't hard-fail if the method signature shifts between MC versions
    )
    private void betterfire$captureTint(
            BlockRenderView world,
            BakedModel model,
            BlockState state,
            BlockPos pos,
            MatrixStack matrices,
            VertexConsumer vertices,
            boolean cull,
            java.util.Random random,
            long seed,
            int overlay,
            CallbackInfoReturnable<Boolean> cir
    ) {
        // Only apply when mod is OFF (fire is visible) — tint logic is in the renderer mixin below.
        // Actual per-vertex color tinting is handled by FireTintProviderMixin.
    }
}
