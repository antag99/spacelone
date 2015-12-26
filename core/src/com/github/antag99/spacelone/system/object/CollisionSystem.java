package com.github.antag99.spacelone.system.object;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.spacelone.component.object.Collision;
import com.github.antag99.spacelone.component.object.Location;
import com.github.antag99.spacelone.component.object.Position;
import com.github.antag99.spacelone.component.object.Size;
import com.github.antag99.spacelone.component.object.Velocity;

public final class CollisionSystem extends EntityProcessorSystem {
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;
    private Mapper<Collision> mCollision;
    private Mapper<Velocity> mVelocity;

    private @SkipWire Rectangle r = new Rectangle();

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

        Array<Rectangle> rectangles = collision.rectangles;
        for (Rectangle rectangle : rectangles) {
            if (rectangle.overlaps(r.set(x, y, w, h))) {

                if (!rectangle.overlaps(r.set(px, y, w, h))) {
                    if (x < px) {
                        x = rectangle.x + rectangle.width + MathUtils.FLOAT_ROUNDING_ERROR;
                    } else if (x > px) {
                        x = rectangle.x - w - MathUtils.FLOAT_ROUNDING_ERROR;
                    }
                    continue;
                }

                if (!rectangle.overlaps(r.set(x, py, w, h))) {
                    if (y < py) {
                        y = rectangle.y + rectangle.height + MathUtils.FLOAT_ROUNDING_ERROR;
                    } else if (y > py) {
                        y = rectangle.y - h - MathUtils.FLOAT_ROUNDING_ERROR;
                    }
                    continue;
                }

                if (!rectangle.overlaps(r.set(px, py, w, h))) {
                    if (y < py) {
                        y = rectangle.y + rectangle.height + MathUtils.FLOAT_ROUNDING_ERROR;
                    } else if (y > py) {
                        y = rectangle.y - h - MathUtils.FLOAT_ROUNDING_ERROR;
                    }
                    if (x < px) {
                        x = rectangle.x - w - MathUtils.FLOAT_ROUNDING_ERROR;
                    } else if (x > px) {
                        x = rectangle.x + rectangle.width + MathUtils.FLOAT_ROUNDING_ERROR;
                    }
                    continue;
                }
            }
        }

        boolean stuck = false;

        for (Rectangle rectangle : rectangles) {
            if (rectangle.overlaps(r.set(x, y, w, h))) {
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

        Pools.freeAll(rectangles, true);
        rectangles.clear();
    }
}
