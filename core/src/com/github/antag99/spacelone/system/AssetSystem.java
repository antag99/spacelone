package com.github.antag99.spacelone.system;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.SkipWire;

public final class AssetSystem extends EntitySystem {
    public @SkipWire Skin skin;

    public AssetSystem(Skin skin) {
        this.skin = skin;
    }
}
