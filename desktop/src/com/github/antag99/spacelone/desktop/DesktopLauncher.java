package com.github.antag99.spacelone.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.github.antag99.spacelone.Spacelone;

public class DesktopLauncher {
    public static void main(String[] args) {
        if (args.length > 0 && "--deletesave".equals(args[0])) {
            new FileHandle("save").deleteDirectory();
        }

        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.minWidth = 512;
        settings.minHeight = 512;
        settings.maxHeight = 2048;
        settings.maxWidth = 2048;

        TexturePacker.process(settings, "../core/assets/", "../core/assets/", "skin.atlas");

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Spacelone";
        config.fullscreen = false;
        config.width = 800;
        config.height = 600;
        // config.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());

        new LwjglApplication(Spacelone.INSTANCE, config);
    }
}
