package info.mudbourn.mmsvanity;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.equipment.Equippable;

/** The /vanity command: mark held armour cosmetic, or hand out a vanity kit. */
public final class VanityCommand {

    private VanityCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vanity")
            .executes(ctx -> {
                if (!(ctx.getSource().getEntity() instanceof ServerPlayer player)) {
                    ctx.getSource().sendFailure(Component.literal("§cPlayers only."));
                    return 0;
                }
                ItemStack stack = player.getMainHandItem();
                if (stack.isEmpty()) {
                    ctx.getSource().sendFailure(Component.literal("§cHold an item in your main hand."));
                    return 0;
                }
                // Check for streak/artifact items (inline to avoid cross-mod dependency)
                if (isStreakItem(stack)) {
                    ctx.getSource().sendFailure(Component.literal("§cCannot vanity a streak item."));
                    return 0;
                }
                // Validate the item is equippable armor (has EQUIPPABLE component with an armor slot)
                Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
                if (equippable == null || !isArmorSlot(equippable.slot())) {
                    ctx.getSource().sendFailure(Component.literal("§cOnly armor can be made vanity."));
                    return 0;
                }

                VanityUtil.applyVanity(stack, VanityKits.creditFor(stack));
                ctx.getSource().sendSuccess(() ->
                    Component.literal("§7Item made §evanity§7 (cosmetic, no stats, no enchants, unbreakable)"), false);
                return 1;
            })
            .then(Commands.literal("kits")
                .executes(ctx -> {
                    String names = String.join("§7, §e", VanityKits.KITS.keySet());
                    ctx.getSource().sendSuccess(() ->
                        Component.literal("§7Vanity kits: §e" + names), false);
                    return 1;
                })
            )
            .then(Commands.literal("kit")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("kit", StringArgumentType.word())
                    .suggests((ctx, builder) ->
                        SharedSuggestionProvider.suggest(VanityKits.KITS.keySet(), builder))
                    .executes(ctx -> {
                        if (!(ctx.getSource().getEntity() instanceof ServerPlayer player)) {
                            ctx.getSource().sendFailure(Component.literal("§cPlayers only."));
                            return 0;
                        }
                        String name = StringArgumentType.getString(ctx, "kit");
                        VanityKits.Kit kit = VanityKits.KITS.get(name);
                        if (kit == null) {
                            ctx.getSource().sendFailure(Component.literal(
                                "§cNo such vanity kit: §e" + name + "§c — try §e/vanity kits"));
                            return 0;
                        }

                        int given = VanityKits.give(player, kit);
                        if (given == 0) {
                            ctx.getSource().sendFailure(Component.literal(
                                "§cKit §e" + name + "§c has no items available in this modset."));
                            return 0;
                        }
                        final int count = given;
                        ctx.getSource().sendSuccess(() -> Component.literal(
                            "§7Gave §e" + count + "§7 vanity piece(s) — §e" + kit.display()), false);
                        return count;
                    })
                )
            )
        );
    }

    private static boolean isStreakItem(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return false;
        CompoundTag tag = customData.copyTag();
        return tag.contains("streak_item") || tag.contains("artifact");
    }

    private static boolean isArmorSlot(EquipmentSlot slot) {
        return slot == EquipmentSlot.HEAD
            || slot == EquipmentSlot.CHEST
            || slot == EquipmentSlot.LEGS
            || slot == EquipmentSlot.FEET;
    }
}
