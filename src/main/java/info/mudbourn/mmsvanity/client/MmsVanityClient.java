package info.mudbourn.mmsvanity.client;

import info.mudbourn.mmsvanity.client.lowlands.LowlandsArmorSets;
import net.fabricmc.api.ClientModInitializer;

public class MmsVanityClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Clothing of the Lowlands vanity sets carry custom geometry, so the model
        // layers have to exist before anything wearing one is rendered. Registered
        // unconditionally: the sets are keyed by equipment asset id, and a player
        // who never sees one just pays for a few unused baked models.
        LowlandsArmorSets.registerModelLayers();
    }
}
