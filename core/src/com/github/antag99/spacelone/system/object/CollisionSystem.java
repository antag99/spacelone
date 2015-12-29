package com.github.antag99.spacelone.system.object;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.spacelone.component.object.Collision;
import com.github.antag99.spacelone.component.object.Location;
import com.github.antag99.spacelone.component.object.Position;
import com.github.antag99.spacelone.component.object.Size;
import com.github.antag99.spacelone.component.object.Velocity;
import com.github.antag99.spacelone.util.AABB;

public final class CollisionSystem extends EntityProcessorSystem {
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;
    private Mapper<Collision> mCollision;
    private Mapper<Velocity> mVelocity;

    public CollisionSystem() {
        super(Family.with(Location.class, Position.class, Size.class, Collision.class));
    }

    @Override
    protected void process(int entity) {
        Position position = mPosition.get(entity);
        Size size = mSize.get(entity);
        Collision collision = mCollision.get(entity);

        float px = position.prevX;
        float py = position.prevY;

        float x = position.x;
        float y = position.y;
        float w = size.width;
        float h = size.height;

        Array<AABB> boxes = collision.boxes;
        for (AABB box : boxes) {
            if (box.overlaps(x, y, w, h)) {
                if (!box.overlaps(px, y, w, h)) {
                    if (x < px) {
                        x = box.x + box.w * 0.5f + w * 0.5f + MathUtils.FLOAT_ROUNDING_ERROR;
                    } else if (x > px) {
                        x = box.x - box.w * 0.5f - w * 0.5f - MathUtils.FLOAT_ROUNDING_ERROR;
                    }
                } else if (!box.overlaps(x, py, w, h)) {
                    if (y < py) {
                        y = box.y + box.h * 0.5f + h * 0.5f + MathUtils.FLOAT_ROUNDING_ERROR;
                    } else if (y > py) {
                        y = box.y - box.h * 0.5f - h * 0.5f - MathUtils.FLOAT_ROUNDING_ERROR;
                    }
                } else if (!box.overlaps(px, py, w, h)) {
                    if (x < px) {
                        x = box.x + box.w * 0.5f + w * 0.5f + MathUtils.FLOAT_ROUNDING_ERROR;
                    } else if (x > px) {
                        x = box.x - box.w * 0.5f - w * 0.5f - MathUtils.FLOAT_ROUNDING_ERROR;
                    }
                    if (y < py) {
                        y = box.y + box.h * 0.5f + h * 0.5f + MathUtils.FLOAT_ROUNDING_ERROR;
                    } else if (y > py) {
                        y = box.y - box.h * 0.5f - h * 0.5f - MathUtils.FLOAT_ROUNDING_ERROR;
                    }
                }
            }
        }

        boolean stuck = false;

        for (AABB box : boxes) {
            if (box.overlaps(x, y, w, h)) {
                stuck = true;
                break;
            }
        }

        Velocity velocity = mVelocity.get(entity);
        if (!stuck) {
            if (position.x != x) {
                position.x = x;
                velocity.y = 0f;
            }
            if (position.y != y) {
                position.y = y;
                velocity.y = 0f;
            }
        } else {
            velocity.x = 0f;
            velocity.y = 0f;
        }

        Pools.freeAll(boxes, true);
        boxes.clear();
    }
}
