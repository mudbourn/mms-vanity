package info.mudbourn.mmsvanity.mixin.firstperson;

import com.geckolib.renderer.GeoArmorRenderer;
import info.mudbourn.mmsvanity.client.firstperson.FirstPersonArmorBones;
import info.mudbourn.mmsvanity.client.firstperson.FirstPersonPass;
import com.geckolib.renderer.base.BoneSnapshots;
import com.geckolib.renderer.base.RenderPassInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fantasy Armor's renderer re-shows its capes, wings and braids after GeckoLib's bone
 * pass, so the first-person arms-only gate runs again at the end of its override.
 */
@Pseudo
@Mixin(targets = "net.kenddie.fantasyarmor.client.render.FAArmorRenderer", remap = false)
public abstract class FantasyArmorFirstPersonMixin {

    @Inject(method = "adjustModelBonesForRender", at = @At("TAIL"), remap = false, require = 0)
    private void mmsVanity$armsOnlyInFirstPerson(RenderPassInfo<?> pass, BoneSnapshots snapshots, CallbackInfo ci) {
        FirstPersonArmorBones.hideAllButArms((GeoArmorRenderer<?, ?>) (Object) this, pass, snapshots);
    }
}
