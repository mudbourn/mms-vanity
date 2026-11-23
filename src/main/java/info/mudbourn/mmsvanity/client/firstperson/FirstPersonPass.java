package info.mudbourn.mmsvanity.client.firstperson;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * Reads Player Animation Library's first-person pass flags without a compile-time
 * dependency. Better Combat draws its first-person swings through that library: the
 * local player model is rendered with only the arms shown.
 */
public final class FirstPersonPass {

    private static boolean looked;
    private static Class<?> stateType;
    private static MethodHandle isFirstPerson;
    private static MethodHandle animManager;
    private static MethodHandle config;
    private static MethodHandle showArmor;
    private static MethodHandle showRightArm;
    private static MethodHandle showLeftArm;

    /** What a first-person pass shows, or null when the state is not in one. */
    public record Shown(boolean armor, boolean rightArm, boolean leftArm) {}

    public static Shown of(Object renderState) {
        if (!init() || renderState == null || !stateType.isInstance(renderState)) {
            return null;
        }
        try {
            if (!(boolean) isFirstPerson.invoke(renderState)) {
                return null;
            }
            Object cfg = config.invoke(animManager.invoke(renderState));
            return new Shown((boolean) showArmor.invoke(cfg),
                (boolean) showRightArm.invoke(cfg), (boolean) showLeftArm.invoke(cfg));
        } catch (Throwable t) {
            return null;
        }
    }

    private static boolean init() {
        if (!looked) {
            looked = true;
            try {
                MethodHandles.Lookup l = MethodHandles.publicLookup();
                stateType = Class.forName("com.zigythebird.playeranim.accessors.IAvatarAnimationState");
                Class<?> manager = Class.forName("com.zigythebird.playeranim.animation.AvatarAnimManager");
                Class<?> cfg = Class.forName("com.zigythebird.playeranimcore.api.firstPerson.FirstPersonConfiguration");
                isFirstPerson = l.findVirtual(stateType, "playerAnimLib$isFirstPersonPass", MethodType.methodType(boolean.class));
                animManager = l.findVirtual(stateType, "playerAnimLib$getAnimManager", MethodType.methodType(manager));
                config = l.findVirtual(manager, "getFirstPersonConfiguration", MethodType.methodType(cfg));
                showArmor = l.findVirtual(cfg, "isShowArmor", MethodType.methodType(boolean.class));
                showRightArm = l.findVirtual(cfg, "isShowRightArm", MethodType.methodType(boolean.class));
                showLeftArm = l.findVirtual(cfg, "isShowLeftArm", MethodType.methodType(boolean.class));
            } catch (ReflectiveOperationException | LinkageError e) {
                stateType = null;
            }
        }
        return stateType != null;
    }

    private FirstPersonPass() {}
}
