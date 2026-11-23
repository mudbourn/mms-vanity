package info.mudbourn.mmsvanity.mixin.fantasyarmor;

import info.mudbourn.mmsvanity.VanityMarker;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * No full-set potion effects from vanity Fantasy Armor.
 *
 * <p>The mod grants a set's effects (Strength, Regeneration, Night Vision and so on)
 * when all four worn pieces are its items of one set. That is a check of the item
 * class, which nothing on the stack can change, so a full set counts as incomplete
 * whenever any worn piece is vanity.
 */
@Pseudo
@Mixin(targets = "net.kenddie.fantasyarmor.item.armor.ArmorSetEffects", remap = false)
public abstract class FantasyArmorSetEffectsMixin {

    private static final EquipmentSlot[] ARMOR = {
        EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    @Inject(method = "hasFullSet", at = @At("HEAD"), cancellable = true, remap = false)
    private static void mmsVanity$noVanitySet(Player player, CallbackInfoReturnable<Boolean> cir) {
        for (EquipmentSlot slot : ARMOR) {
            if (VanityMarker.isVanity(player.getItemBySlot(slot))) {
                cir.setReturnValue(false);
                return;
            }
        }
    }
}
