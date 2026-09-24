package info.mudbourn.mmsvanity.mixin.client;

import net.minecraft.client.renderer.item.ItemStackRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Exposes the layer list, so a model can adjust the layers another model appended. */
@Mixin(ItemStackRenderState.class)
public interface ItemStackRenderStateAccessor {

    @Accessor("activeLayerCount")
    int mmsVanity$activeLayerCount();

    @Accessor("layers")
    ItemStackRenderState.LayerRenderState[] mmsVanity$layers();
}
