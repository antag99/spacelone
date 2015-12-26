package com.github.antag99.spacelone.system.type;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.spacelone.component.type.ObjectTexture;
import com.github.antag99.spacelone.component.type.ObjectTextureRef;
import com.github.antag99.spacelone.system.BaseRefSystem;

public final class ObjectTextureRefSystem extends BaseRefSystem<ObjectTexture, ObjectTextureRef> {
    private @SkipWire TextureAtlas atlas;

    public ObjectTextureRefSystem(TextureAtlas atlas) {
        super(ObjectTexture.class, ObjectTextureRef.class);
        this.atlas = atlas;
    }

    @Override
    protected void initialize(ObjectTexture component, ObjectTextureRef ref) {
        component.texture = atlas.findRegion(ref.name);
        if (component.texture == null)
            throw new IllegalArgumentException("no such texture: " + ref.name);
    }
}
