package info.mudbourn.mmsvanity.mixin.fantasyarmor;

import info.mudbourn.mmsvanity.VanityMarker;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Keeps a vanity Fantasy Armor piece stripped.
 *
 * <p>The mod writes its configured attribute modifiers, durability and unbreakable
 * flag back onto every worn piece each inventory tick and on every equipment change,
 * which would undo {@code VanityUtil} the moment the piece is put on. Vanity stacks
 * skip that rewrite.
 */
@Pseudo
@Mixin(targets = "net.kenddie.fantasyarmor.item.armor.FAArmorItem", remap = false)
public abstract class FantasyArmorStackSettingsMixin {

    @Inject(method = "applyServerSettings", at = @At("HEAD"), cancellable = true, remap = false)
    private void mmsVanity$keepVanityStripped(ItemStack stack, CallbackInfo ci) {
        if (VanityMarker.isVanity(stack)) {
            ci.cancel();
        }
    }
}
