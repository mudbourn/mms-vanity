package info.mudbourn.mmsvanity;

import static info.mudbourn.mmsvanity.VanityKits.add;
import info.mudbourn.mmsvanity.VanityKits.Kit;
import info.mudbourn.mmsvanity.VanityKits.Piece;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.List;

/**
 * Armor of the Ages sets as vanity skins.
 *
 * <p>Each piece is a vanilla leather base carrying {@code equippable.asset_id} and
 * {@code item_model}, so nothing is registered. The "lowlands-vanity" resource pack holds
 * the look under {@code assets/armoroftheages/}: a {@code geo_armor/<asset>.json} per skin,
 * which mms-render-common draws for any stack wearing that asset, the geo and animations it
 * names, the armor and item textures, item models and display names. The geo is generated
 * from the source mod's Java models by render-common's {@code tools/java_model_to_geo.py}
 * with the manifest in {@code ports/armoroftheages.json}.
 *
 * <p>A skin is a recolour of a set, drawn from the same geo with its own texture, and is its
 * own kit. Patreon-tier skins are not ported.
 */
final class ArmorOfTheAgesVanity {

    private static final String NAMESPACE = "armoroftheages";

    static void register() {
        set("anubis", "Anubis", "anubis_armor", "");
        set("centurion", "Centurion", "centurion_armor", "");
        set("exalted_aurum", "Exalted Aurum", "exalted_aurum_armor", "");
        set("holy", "Holy", "holy_armor", "");
        set("iron_plate", "Iron Plate", "iron_plate_armor", "");
        set("iron_plate_ashes", "Iron Plate (Ashes)", "iron_plate_armor", "ashes_");
        set("do_maru", "Do-maru", "japanese_light_armor", "");
        set("o_yoroi", "O-Yoroi", "o_yoroi_armor", "");
        set("o_yoroi_amber", "O-Yoroi (Amber)", "o_yoroi_armor", "amber_");
        set("o_yoroi_nightblue", "O-Yoroi (Nightblue)", "o_yoroi_armor", "nightblue_");
        set("o_yoroi_dusk", "O-Yoroi (Dusk)", "o_yoroi_armor", "dusk_");
        set("o_yoroi_ink", "O-Yoroi (Ink)", "o_yoroi_armor", "ink_");
        set("o_yoroi_jade", "O-Yoroi (Jade)", "o_yoroi_armor", "jade_");
        set("o_yoroi_pinky", "O-Yoroi (Pinky)", "o_yoroi_armor", "pinky_");
        set("pharaoh", "Pharaoh", "pharaoh_armor", "");
        set("quetzalcoatl", "Quetzalcoatl", "quetzalcoatl_armor", "");
        set("raijin", "Raijin", "raijin_armor", "");
    }

    private static void set(String id, String display, String armor, String skin) {
        String asset = NAMESPACE + ":" + skin + armor;
        add(new Kit(id, display, List.of(
            piece(EquipmentSlot.HEAD, "minecraft:leather_helmet", asset, armor + "_head"),
            piece(EquipmentSlot.CHEST, "minecraft:leather_chestplate", asset, armor + "_chest"),
            piece(EquipmentSlot.LEGS, "minecraft:leather_leggings", asset, armor + "_legs"),
            piece(EquipmentSlot.FEET, "minecraft:leather_boots", asset, armor + "_feet")
        )));
    }

    private static Piece piece(EquipmentSlot slot, String base, String asset, String item) {
        return new Piece(slot, base, asset, NAMESPACE + ":" + item, "item." + NAMESPACE + "." + item);
    }

    private ArmorOfTheAgesVanity() {}
}
