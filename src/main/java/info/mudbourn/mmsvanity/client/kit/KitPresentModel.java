package info.mudbourn.mmsvanity.client.kit;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import info.mudbourn.mmsvanity.VanityKitItem;
import info.mudbourn.mmsvanity.mixin.client.ItemStackRenderStateAccessor;
import info.mudbourn.mmsvanity.mixin.client.LayerRenderStateAccessor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

/**
 * Item model type {@code mms_vanity:kit_present}: in an inventory slot, the present
 * shrunk to the bottom with the wrapped kit's helmet drawn smaller on top of it.
 * Everywhere else (hand, ground, item frame) it is the plain present.
 *
 * <p>The helmet is whatever model the kit's icon piece resolves to, so it comes from
 * the resource pack or the source mod exactly as the real piece would. Its layers
 * are appended normally and then rescaled in place, which is why this needs the
 * render-state accessors.
 */
public record KitPresentModel(ItemModel present) implements ItemModel {

    private static final float PRESENT_SCALE = 0.8F;
    private static final Vector3f PRESENT_OFFSET = new Vector3f(0.0F, -0.1F, 0.0F);
    private static final float HELMET_SCALE = 0.6F;
    private static final Vector3f HELMET_OFFSET = new Vector3f(0.0F, 0.22F, 0.5F);

    /** Registers the type. Call once, from client init, before resources load. */
    public static void register() {
        ItemModels.ID_MAPPER.put(Identifier.fromNamespaceAndPath("mms_vanity", "kit_present"), Unbaked.MAP_CODEC);
    }

    @Override
    public void update(ItemStackRenderState state, ItemStack stack, ItemModelResolver resolver,
                       ItemDisplayContext context, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        state.appendModelIdentityElement(this);
        int start = layerCount(state);
        present.update(state, stack, resolver, context, level, owner, seed);
        if (context != ItemDisplayContext.GUI) return;
        int helmetStart = layerCount(state);

        ItemStack helmet = VanityKitItem.iconStack(stack);
        if (helmet.isEmpty()) return;
        shrink(state, start, helmetStart, PRESENT_SCALE, PRESENT_OFFSET);
        resolver.appendItemLayers(state, helmet, context, level, owner, seed);
        shrink(state, helmetStart, layerCount(state), HELMET_SCALE, HELMET_OFFSET);
    }

    private static int layerCount(ItemStackRenderState state) {
        return ((ItemStackRenderStateAccessor) state).mmsVanity$activeLayerCount();
    }

    /** Scales layers {@code from} to {@code to} about their own origin and shifts them. */
    private static void shrink(ItemStackRenderState state, int from, int to, float scale, Vector3f offset) {
        ItemStackRenderState.LayerRenderState[] layers = ((ItemStackRenderStateAccessor) state).mmsVanity$layers();
        for (int i = from; i < to; i++) {
            ItemTransform base = ((LayerRenderStateAccessor) layers[i]).mmsVanity$transform();
            layers[i].setTransform(new ItemTransform(
                base.rotation(),
                new Vector3f(base.translation()).add(offset),
                new Vector3f(base.scale()).mul(scale)));
        }
    }

    /** The JSON form: {@code {"type": "mms_vanity:kit_present", "present": <item model>}}. */
    public record Unbaked(ItemModel.Unbaked present) implements ItemModel.Unbaked {

        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemModels.CODEC.fieldOf("present").forGetter(Unbaked::present)
        ).apply(instance, Unbaked::new));

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public ItemModel bake(ItemModel.BakingContext context) {
            return new KitPresentModel(present.bake(context));
        }

        @Override
        public void resolveDependencies(ResolvableModel.Resolver resolver) {
            present.resolveDependencies(resolver);
        }
    }
}
