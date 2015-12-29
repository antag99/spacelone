package com.github.antag99.spacelone.util;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * Axis-aligned bounding box; the position is the center of the box.
 */
public final class AABB implements Poolable {
    public float x, y;
    public float w, h;

    public AABB() {
    }

    public AABB(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public AABB set(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        return this;
    }

    @Override
    public void reset() {
        x = y = w = h = 0f;
    }

    public boolean overlaps(float x, float y, float w, float h) {
        return Rectangle.tmp.set(this.x - this.w * 0.5f, this.y - this.h * 0.5f, this.w, this.h)
                .overlaps(Rectangle.tmp2.set(x - w * 0.5f, y - h * 0.5f, w, h));
        // return this.x + this.w * 0.5f > x - w * 0.5f && x + w * 0.5f > this.x - this.w * 0.5f &&
        // this.y + this.h * 0.5f > y - h * 0.5f && y + h * 0.5f > this.y - this.h * 0.5f;
    }

    public boolean overlaps(AABB other) {
        return overlaps(other.x, other.y, other.w, other.h);
    }
}
