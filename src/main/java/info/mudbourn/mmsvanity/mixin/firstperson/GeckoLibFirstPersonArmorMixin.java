package info.mudbourn.mmsvanity.mixin.firstperson;

import com.geckolib.animation.state.BoneSnapshot;
import com.geckolib.cache.model.GeoBone;
import com.geckolib.renderer.GeoArmorRenderer;
import info.mudbourn.mmsvanity.client.firstperson.FirstPersonArmorBones;
import info.mudbourn.mmsvanity.client.firstperson.FirstPersonPass;
import com.geckolib.renderer.base.BoneSnapshots;
import com.geckolib.renderer.base.RenderPassInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * Keeps GeckoLib armor to the arms in a first-person pass.
 *
 * <p>Player Animation Library hides the head, body and legs of vanilla armor in
 * Better Combat's first-person swing by editing the model inside
 * {@code renderArmorPiece}. GeckoLib armor (every Fantasy Armor set) skips that
 * method, so the whole outfit, helmet, wings and cape included, drew into the camera.
 * Here only the chest piece's arm bones are drawn, and only when the pass shows armor.
 */
@Pseudo
@Mixin(value = GeoArmorRenderer.class, remap = false)
public abstract class GeckoLibFirstPersonArmorMixin {

    @Inject(method = "tryRenderGeoArmorPiece", at = @At("HEAD"), cancellable = true, remap = false)
    private static void mmsVanity$onlyChestInFirstPerson(BiFunction<?, ?, ?> modelGetter, PoseStack poseStack,
            SubmitNodeCollector collector, ItemStack stack, EquipmentSlot slot, int light,
            HumanoidRenderState state, CallbackInfoReturnable<Boolean> cir) {
        FirstPersonPass.Shown shown = FirstPersonPass.of(state);
        if (shown != null && (slot != EquipmentSlot.CHEST || !shown.armor())) {
            cir.setReturnValue(true); // handled: draw nothing, and keep vanilla from drawing it either
        }
    }

    @Inject(method = "adjustModelBonesForRender", at = @At("TAIL"), remap = false)
    private void mmsVanity$armsOnlyInFirstPerson(RenderPassInfo<?> pass, BoneSnapshots snapshots, CallbackInfo ci) {
        FirstPersonArmorBones.hideAllButArms((GeoArmorRenderer<?, ?>) (Object) this, pass, snapshots);
    }
}
