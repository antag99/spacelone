package com.github.antag99.spacelone.system.type;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.spacelone.component.type.FloorTexture;
import com.github.antag99.spacelone.component.type.FloorTextureRef;
import com.github.antag99.spacelone.system.BaseRefSystem;

public final class FloorTextureRefSystem extends BaseRefSystem<FloorTexture, FloorTextureRef> {
    private @SkipWire TextureAtlas atlas;

    public FloorTextureRefSystem(TextureAtlas atlas) {
        super(FloorTexture.class, FloorTextureRef.class);
        this.atlas = atlas;
    }

    @Override
    protected void initialize(FloorTexture component, FloorTextureRef ref) {
        component.texture = atlas.findRegion(ref.name);
    }
}
