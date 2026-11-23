package info.mudbourn.mmsvanity.client.firstperson;

import com.geckolib.animation.state.BoneSnapshot;
import com.geckolib.cache.model.GeoBone;
import com.geckolib.renderer.GeoArmorRenderer;
import com.geckolib.renderer.base.BoneSnapshots;
import com.geckolib.renderer.base.RenderPassInfo;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

import java.util.HashSet;
import java.util.Set;

/** Hides every armor bone but the shown arms during a first-person pass. */
public final class FirstPersonArmorBones {

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void hideAllButArms(GeoArmorRenderer<?, ?> renderer, RenderPassInfo<?> pass, BoneSnapshots snapshots) {
        Object raw = pass.renderState();
        if (!(raw instanceof HumanoidRenderState state)) {
            return;
        }
        FirstPersonPass.Shown shown = FirstPersonPass.of(state);
        if (shown == null) {
            return;
        }
        GeoArmorRenderer r = renderer;
        Set<String> keep = new HashSet<>();
        if (shown.armor() && shown.rightArm()) {
            keep.add(r.getBoneNameForSegment(state, GeoArmorRenderer.ArmorSegment.RIGHT_ARM));
        }
        if (shown.armor() && shown.leftArm()) {
            keep.add(r.getBoneNameForSegment(state, GeoArmorRenderer.ArmorSegment.LEFT_ARM));
        }
        for (GeoBone bone : pass.model().boneLookup().get().values()) {
            if (insideKept(bone, keep) || holdsKept(bone, keep)) {
                continue;
            }
            snapshots.ifPresent(bone.name(), s -> hide(s));
        }
    }

    private static void hide(BoneSnapshot s) {
        // Scale as well as skip: Fantasy Armor draws its segment and cape bones one by
        // one, and may clear the skip flags after this runs.
        s.skipRender(true).skipChildrenRender(true).setScale(0f, 0f, 0f);
    }

    private static boolean insideKept(GeoBone bone, Set<String> keep) {
        for (GeoBone b = bone; b != null; b = b.parent()) {
            if (keep.contains(b.name())) {
                return true;
            }
        }
        return false;
    }

    private static boolean holdsKept(GeoBone bone, Set<String> keep) {
        for (GeoBone child : bone.children()) {
            if (keep.contains(child.name()) || holdsKept(child, keep)) {
                return true;
            }
        }
        return false;
    }

    private FirstPersonArmorBones() {}
}
