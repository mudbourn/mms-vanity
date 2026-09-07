package info.mudbourn.mmsvanity;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Common entrypoint: registers the vanity component and the /vanity command. */
public class MmsVanity implements ModInitializer {
    private static final Logger LOG = LoggerFactory.getLogger("mms_vanity");

    @Override
    public void onInitialize() {
        VanityMarker.register();
        // /vanity command (permission level 0 -- public)
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
            VanityCommand.register(dispatcher));
        LOG.info("MMS Vanity loaded -- /vanity, vanity kits, Lowlands and Weaver's sets.");
    }
}
