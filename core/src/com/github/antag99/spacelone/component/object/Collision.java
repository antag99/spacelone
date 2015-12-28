package com.github.antag99.spacelone.component.object;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.github.antag99.retinazer.Component;

public final class Collision implements Component {

    /**
     * List of rectangles the entity is colliding with. The collision resolver
     * will attempt to undo it's movement until the entity no longer overlaps.
     */
    public transient Array<Rectangle> rectangles = new Array<>();
}
