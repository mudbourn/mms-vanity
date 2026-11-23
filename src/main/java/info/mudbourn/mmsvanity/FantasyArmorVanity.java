package info.mudbourn.mmsvanity;

import static info.mudbourn.mmsvanity.VanityKits.add;
import info.mudbourn.mmsvanity.VanityKits.Kit;
import info.mudbourn.mmsvanity.VanityKits.Piece;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.List;

/**
 * Fantasy Armor sets as vanity kits, handed out as the mod's own items.
 *
 * <p>Fantasy Armor is all rights reserved, so nothing of it is repacked: the mod is
 * installed and embargoed (its Moon Crystal and smithing recipes are stubbed by
 * mms-unstable-recipe-embargo), and these kits are the only way to get its pieces.
 *
 * <p>A leather skin will not work here. Every set is a GeckoLib model drawn for the
 * {@code FAArmorItem} class itself, not an equipment asset, so a leather base wearing
 * its asset would render as flat vanilla slabs. The kits give the real items and
 * {@link VanityUtil} strips them as for any plain piece. Two things the mod does by
 * item class are gated on {@link VanityMarker} in the {@code fantasyarmor} mixins:
 * it rewrites a worn piece's attribute modifiers every tick, and it grants potion
 * effects for a full set.
 */
final class FantasyArmorVanity {

    private static final String NAMESPACE = "fantasy_armor";

    static void register() {
        set("eclipse_soldier", "Eclipse Soldier");
        set("golden_horns", "Golden Horns");
        set("dragonslayer", "Dragonslayer");
        set("hero", "Hero");
        set("thief", "Thief");
        set("wandering_wizard", "Wandering Wizard");
        set("sunset_wings", "Sunset Wings");
        set("chess_board_knight", "Chess Board Knight");
        set("dark_lord", "Dark Lord");
        set("fog_guard", "Fog Guard");
        set("dark_cover", "Dark Cover");
        set("spark_of_dawn", "Spark of Dawn");
        set("golden_execution", "Golden Execution");
        set("forgotten_trace", "Forgotten Trace");
        set("redeemer", "Redeemer");
        set("twinned", "Twinned");
        set("gilded_hunt", "Gilded Hunt");
        set("lady_maria", "Lady Maria");
        set("crucible_knight", "Crucible Knight");
        set("evening_ghost", "Evening Ghost");
        set("ronin", "Ronin");
        set("malenia", "Malenia");
        set("old_knight", "Old Knight");
        set("silver_knight", "Silver Knight");
        set("dead_gladiator", "Dead Gladiator");
        set("flesh_of_the_feaster", "Flesh of the Feaster");
        set("wind_worshipper", "Wind Worshipper");
        set("grave_sentinel", "Grave Sentinel");
        set("ornstein", "Ornstein");
        set("dawncrest", "Dawncrest");
    }

    private static void set(String id, String display) {
        String prefix = NAMESPACE + ":" + id;
        add(new Kit(id, display, List.of(
            Piece.of(EquipmentSlot.HEAD, prefix + "_helmet"),
            Piece.of(EquipmentSlot.CHEST, prefix + "_chestplate"),
            Piece.of(EquipmentSlot.LEGS, prefix + "_leggings"),
            Piece.of(EquipmentSlot.FEET, prefix + "_boots")
        )));
    }

    private FantasyArmorVanity() {}
}
