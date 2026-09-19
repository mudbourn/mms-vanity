package info.mudbourn.mmsvanity;

import net.minecraft.network.chat.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The asset sources whose textures are repacked into the "lowlands-vanity" resource
 * pack, and the terms each is used under.
 *
 * A source belongs here only when this project redistributes its textures. Sets that
 * reuse an installed mod's own equipment asset at runtime (Aerial Hell, Frostiful,
 * Scorchful and the rest) are not repacked and are not listed: the player already has
 * those mods and nothing of theirs is redistributed.
 *
 * Every row drives a visible in-game credit line and is the code half of the
 * attribution these authors require; the prose half is ATTRIBUTION.md. Adding a
 * future port that repacks another author's assets means adding its row here, and
 * {@link #validate} fails at load if a row is incomplete.
 */
public final class VanitySources {

    /**
     * One repacked source and the permission it ships under.
     *
     * @param namespace       asset and item-model namespace the pack serves it under
     * @param work            the work's title, as its author names it
     * @param author          the author to credit
     * @param source          where players can find the original, shown in the tooltip
     *                        so the credit points back to the real mod (Anton_Vaedak's
     *                        permission requires the source be clear on hover)
     * @param license         short license tag, ARR or MIT
     * @param monetizable     whether the author permits monetized distribution
     * @param redistributable whether the author permits standalone redistribution
     *                        apart from this project's modpack
     * @param notice          copyright or permission notice that must travel with the
     *                        assets, or null when the author requires none
     */
    public record Source(
        String namespace,
        String work,
        String author,
        String source,
        String license,
        boolean monetizable,
        boolean redistributable,
        String notice
    ) {

        /**
         * The credit block shown in the item tooltip: an italic blue line naming the
         * work and author, then a grey line pointing at the original mod's page, so a
         * player who hovers the item sees at once who made it and where it is from.
         */
        public List<Component> creditLines() {
            Component credit = Component.literal(work + " by " + author)
                .withStyle(style -> style.withColor(0x5555FF).withItalic(true));
            Component origin = Component.literal(source)
                .withStyle(style -> style.withColor(0x888888).withItalic(true));
            return List.of(credit, origin);
        }

        /** One-line terms summary for the load-time compliance log. */
        public String terms() {
            String reuse = redistributable ? "redistributable" : "non-redistributable";
            String money = monetizable ? "monetizable" : "non-commercial";
            String pending = notice == null ? "" : ", notice: " + notice;
            return license + ", " + money + ", " + reuse + pending;
        }
    }

    public static final Map<String, Source> SOURCES = new LinkedHashMap<>();

    static {
        // Used by permission granted 2026-08-16: visible credit (work, author and the
        // CurseForge source, all on hover), non-commercial, no third-party
        // redistribution. All rights reserved. See ATTRIBUTION.md.
        put(new Source(
            "lowlands_clothing",
            "Clothing of the Lowlands",
            "Anton_Vaedak",
            "CurseForge: curseforge.com/minecraft/mc-mods/clothing-of-the-lowlands",
            "ARR",
            false,
            false,
            null
        ));

        // MIT. Upstream ships no copyright notice, so this project supplies one at
        // assets/weaversparadise/LICENSE in the pack, which travels with the copies.
        put(new Source(
            "weaversparadise",
            "Weaver's Paradise",
            "Vortianski",
            "CurseForge: curseforge.com/minecraft/mc-mods/weavers-paradise",
            "MIT",
            true,
            true,
            "assets/weaversparadise/LICENSE"
        ));
    }

    private static void put(Source source) {
        SOURCES.put(source.namespace(), source);
    }

    /** The credit lines for a repacked namespace, or empty when the namespace is not repacked. */
    public static List<Component> creditFor(String namespace) {
        Source source = SOURCES.get(namespace);
        return source == null ? List.of() : source.creditLines();
    }

    /**
     * Fails fast if any row is incomplete, then logs each source and its terms.
     * Called once from mod init so a half-filled entry never ships silently.
     */
    public static void validate(org.slf4j.Logger log) {
        for (Source source : SOURCES.values()) {
            if (blank(source.namespace())
                || blank(source.work())
                || blank(source.author())
                || blank(source.source())
                || blank(source.license())) {
                throw new IllegalStateException(
                    "Incomplete VanitySources row for " + source.namespace());
            }
            log.info("Bundled asset source: {} by {} [{}]",
                source.work(), source.author(), source.terms());
        }
    }

    private static boolean blank(String value) {
        return value == null || value.isBlank();
    }

    private VanitySources() {}
}
