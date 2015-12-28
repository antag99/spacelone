package com.github.antag99.spacelone.component.type;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.antag99.retinazer.Component;
import com.github.antag99.spacelone.util.SkipProperty;
import com.github.antag99.spacelone.util.SkipSerialization;

@SkipSerialization
@SkipProperty
public final class FloorTexture implements Component {
    public TextureRegion texture;
}
