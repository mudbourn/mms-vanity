package info.mudbourn.mmsvanity.mixin.cosmeticarmor;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * Lets GeckoLib armor render from a Cosmetic Armor slot.
 *
 * <p>Cosmetic Armor swaps the cosmetic stack into the humanoid render state, but
 * GeckoLib captures its per-slot armor data separately, from the entity's real
 * armor slots. A GeckoLib piece in a cosmetic slot (every Fantasy Armor set) then
 * has no render data and falls back to its vanilla equipment asset, a flat netherite
 * piece. This reads the slot the way Cosmetic Armor does, so both agree.
 */
@Pseudo
@Mixin(targets = "com.geckolib.renderer.GeoArmorRenderer", remap = false)
public abstract class GeckoLibCosmeticSlotMixin {

    @Unique
    private static final TagKey<Item> MMS_VANITY$ALWAYS_VISIBLE =
        TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("cosmeticarmor", "always_visible"));

    @Unique
    private static MethodHandle mmsVanity$getCosmetic;

    @Unique
    private static boolean mmsVanity$looked;

    @Redirect(
        method = "getRelevantSlotsForRendering",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;"),
        remap = false
    )
    private static ItemStack mmsVanity$cosmeticSlot(LivingEntity entity, EquipmentSlot slot) {
        ItemStack worn = entity.getItemBySlot(slot);
        MethodHandle handle = mmsVanity$handle();
        if (handle == null) {
            return worn;
        }
        ItemStack cosmetic;
        try {
            cosmetic = (ItemStack) handle.invokeExact(entity, slot);
        } catch (Throwable t) {
            return worn;
        }
        if (cosmetic == null || cosmetic.isEmpty()) {
            return worn;
        }
        if (!worn.isEmpty() && worn.is(MMS_VANITY$ALWAYS_VISIBLE)) {
            return worn;
        }
        return cosmetic;
    }

    @Unique
    private static MethodHandle mmsVanity$handle() {
        if (!mmsVanity$looked) {
            mmsVanity$looked = true;
            try {
                Class<?> owner = Class.forName("io.github.apace100.cosmetic_armor.CosmeticArmor");
                mmsVanity$getCosmetic = MethodHandles.publicLookup().findStatic(owner, "getCosmeticArmor",
                    MethodType.methodType(ItemStack.class, LivingEntity.class, EquipmentSlot.class));
            } catch (ReflectiveOperationException | LinkageError e) {
                mmsVanity$getCosmetic = null;
            }
        }
        return mmsVanity$getCosmetic;
    }
}
