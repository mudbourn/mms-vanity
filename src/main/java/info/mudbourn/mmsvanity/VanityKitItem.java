package info.mudbourn.mmsvanity;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * A wrapped vanity kit: right-click to consume it and receive the kit it names.
 *
 * <p>One registered item serves every kit. The kit id rides on the {@link #KIT}
 * component. The item model draws a present with the kit's helmet on top (see the
 * client {@code KitPresentModel}), so each kit is recognisable in an inventory or a
 * shop listing. A stack without a kit is a bare present, the creative tab icon.
 */
public final class VanityKitItem extends Item {

    public static final DataComponentType<String> KIT = DataComponentType.<String>builder()
        .persistent(Codec.STRING)
        .networkSynchronized(ByteBufCodecs.STRING_UTF8)
        .build();

    public static final ResourceKey<Item> KEY = ResourceKey.create(Registries.ITEM, id("vanity_kit"));

    public static final Item ITEM = new VanityKitItem(new Item.Properties().setId(KEY).stacksTo(16));

    private VanityKitItem(Properties properties) {
        super(properties);
    }

    /** Registers the component, the item and the creative tab. Call once, from mod init. */
    public static void register() {
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id("kit"), KIT);
        Registry.register(BuiltInRegistries.ITEM, KEY, ITEM);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id("kits"), FabricItemGroup.builder()
            .title(Component.translatable("itemGroup.mms_vanity.kits"))
            .icon(() -> new ItemStack(ITEM))
            .displayItems((params, output) -> {
                for (VanityKits.Kit kit : VanityKits.KITS.values()) {
                    if (VanityKits.available(kit)) output.accept(of(kit));
                }
            })
            .build());
    }

    /** A wrapped stack of {@code kit}, named after it. */
    public static ItemStack of(VanityKits.Kit kit) {
        ItemStack stack = new ItemStack(ITEM);
        stack.set(KIT, kit.id());
        stack.set(DataComponents.ITEM_NAME,
            Component.translatable("item.mms_vanity.vanity_kit.named", kit.display()));
        stack.set(DataComponents.LORE, new ItemLore(List.of(
            Component.translatable("item.mms_vanity.vanity_kit.hint")
                .withStyle(ChatFormatting.GRAY))));
        return stack;
    }

    /**
     * The piece drawn on a wrapped kit: its head piece, or its first piece for a kit
     * with no head slot, carrying that piece's sprite. Empty for a stack with no kit.
     */
    public static ItemStack iconStack(ItemStack kitStack) {
        VanityKits.Kit kit = VanityKits.KITS.get(kitStack.get(KIT));
        if (kit == null) return ItemStack.EMPTY;
        VanityKits.Piece icon = kit.pieces().stream()
            .filter(piece -> piece.slot() == EquipmentSlot.HEAD)
            .findFirst()
            .orElse(kit.pieces().getFirst());
        Identifier itemId = Identifier.tryParse(icon.itemId());
        if (itemId == null) return ItemStack.EMPTY;
        ItemStack stack = new ItemStack(BuiltInRegistries.ITEM.getValue(itemId));
        if (icon.itemModel() != null) stack.set(DataComponents.ITEM_MODEL, Identifier.parse(icon.itemModel()));
        return stack;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        VanityKits.Kit kit = VanityKits.KITS.get(stack.get(KIT));
        if (kit == null) return InteractionResult.FAIL;
        if (!(player instanceof ServerPlayer serverPlayer)) return InteractionResult.SUCCESS;

        if (VanityKits.give(serverPlayer, kit) == 0) {
            serverPlayer.sendSystemMessage(Component.literal(
                "§cKit §e" + kit.display() + "§c has no items available in this modset."));
            return InteractionResult.FAIL;
        }
        stack.consume(1, player);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
            SoundEvents.BUNDLE_DROP_CONTENTS, SoundSource.PLAYERS, 1.0F, 1.0F);
        return InteractionResult.SUCCESS_SERVER;
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath("mms_vanity", path);
    }
}
