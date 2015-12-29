package com.github.antag99.spacelone.component.object;

import com.badlogic.gdx.utils.Array;
import com.github.antag99.retinazer.Component;
import com.github.antag99.spacelone.util.AABB;

public final class Collision implements Component {

    /**
     * List of close boxes the entity can collide with. The collision resolver
     * will attempt to ensure the entity do not overlap any of these.
     */
    public transient Array<AABB> boxes = new Array<>();
}
