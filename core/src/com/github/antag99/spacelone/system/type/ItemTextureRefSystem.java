package com.github.antag99.spacelone.system.type;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.spacelone.component.type.ItemTexture;
import com.github.antag99.spacelone.component.type.ItemTextureRef;
import com.github.antag99.spacelone.system.BaseRefSystem;

public final class ItemTextureRefSystem extends BaseRefSystem<ItemTexture, ItemTextureRef> {
    private @SkipWire TextureAtlas atlas;

    public ItemTextureRefSystem(TextureAtlas atlas) {
        super(ItemTexture.class, ItemTextureRef.class);
        this.atlas = atlas;
    }

    @Override
    protected void initialize(ItemTexture component, ItemTextureRef ref) {
        component.texture = atlas.findRegion(ref.name);
    }
}
