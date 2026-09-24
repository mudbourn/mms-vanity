package info.mudbourn.mmsvanity.mixin.client;

import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Exposes a layer's display transform, which has a setter but no getter. */
@Mixin(ItemStackRenderState.LayerRenderState.class)
public interface LayerRenderStateAccessor {

    @Accessor("transform")
    ItemTransform mmsVanity$transform();
}
