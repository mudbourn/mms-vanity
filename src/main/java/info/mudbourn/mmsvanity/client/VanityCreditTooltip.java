package info.mudbourn.mmsvanity.client;

import info.mudbourn.mmsvanity.VanityKits;
import info.mudbourn.mmsvanity.VanityMarker;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

/**
 * Shows a repacked piece's asset credit (work, author, source) in its tooltip while
 * SHIFT is held, and a "Hold SHIFT" hint otherwise, so the credit stays on hover
 * without crowding sets that already carry long lore. Older pieces had the credit
 * stamped into their lore; those copies are dropped so it does not show twice.
 */
public final class VanityCreditTooltip {
    private VanityCreditTooltip() {}

    public static void register() {
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            if (!stack.has(VanityMarker.COMPONENT)) return;
            List<Component> credit = VanityKits.creditFor(stack);
            if (credit.isEmpty()) return;

            Set<String> creditText = credit.stream().map(Component::getString).collect(Collectors.toSet());
            lines.removeIf(line -> creditText.contains(line.getString()));

            if (Minecraft.getInstance().hasShiftDown()) {
                lines.addAll(credit);
            } else {
                lines.add(Component.literal("Hold SHIFT to view credits")
                    .withStyle(style -> style.withColor(0x888888).withItalic(true)));
            }
        });
    }
}
