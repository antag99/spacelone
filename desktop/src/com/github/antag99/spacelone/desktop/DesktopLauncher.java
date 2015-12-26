package com.github.antag99.spacelone.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.antag99.spacelone.Spacelone;

public class DesktopLauncher {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Spacelone";
        config.fullscreen = false;
        config.width = 800;
        config.height = 600;
        // config.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());

        new LwjglApplication(Spacelone.INSTANCE, config);
    }
}
