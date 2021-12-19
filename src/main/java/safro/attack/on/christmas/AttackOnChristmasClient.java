package safro.attack.on.christmas;

import net.fabricmc.api.ClientModInitializer;
import safro.attack.on.christmas.registry.ClientRegistry;

public class AttackOnChristmasClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientRegistry.init();
    }
}
