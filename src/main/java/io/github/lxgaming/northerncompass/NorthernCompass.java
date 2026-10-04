package io.github.lxgaming.northerncompass;

import io.github.lxgaming.northerncompass.configuration.Configuration;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class NorthernCompass implements ClientModInitializer {
    
    private static NorthernCompass instance;
    private Configuration configuration;

    @Override
    public void onInitializeClient() {
        instance = this;
        this.configuration = new Configuration(FabricLoader.getInstance().getConfigDir().resolve("northerncompass"));
        this.configuration.loadConfiguration();
    }

    public static NorthernCompass getInstance() {
        return instance;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
