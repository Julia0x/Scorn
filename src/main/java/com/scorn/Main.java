package com.scorn;

import net.fabricmc.api.ModInitializer;

public final class Main implements ModInitializer {
    @Override
    public void onInitialize() {
        new Client();
        System.out.println("Loading Scorn...");
    }
}
