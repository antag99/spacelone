package com.github.antag99.spacelone.desktop;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public final class EclipseLauncher {
    public static void main(String[] args) {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.minWidth = 512;
        settings.minHeight = 512;
        settings.maxHeight = 2048;
        settings.maxWidth = 2048;

        TexturePacker.process(settings, "../core/assets/", "../core/assets/", "skin.atlas");

        // Eclipse does not automatically refresh the bin folder when assets are
        // changed, copy over manually.
        FileHandle skinFile = new FileHandle("../core/assets/skin.png");
        if (skinFile.exists()) {
            skinFile.copyTo(new FileHandle("bin/skin.png"));

            for (int i = 2; (skinFile = new FileHandle("../core/assets/skin" + i + ".png")).exists(); i++) {
                skinFile.copyTo(new FileHandle("bin/skin" + i + ".png"));
            }
        }
        FileHandle atlasFile = new FileHandle("../core/assets/skin.atlas");
        if (atlasFile.exists()) {
            atlasFile.copyTo(new FileHandle("bin/skin.atlas"));
        }

        DesktopLauncher.main(args);
    }
}
