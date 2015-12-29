package com.github.antag99.spacelone.system.object;

import com.badlogic.gdx.utils.Pools;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.EntitySet;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.spacelone.component.Solid;
import com.github.antag99.spacelone.component.object.Collision;
import com.github.antag99.spacelone.component.object.Location;
import com.github.antag99.spacelone.component.object.Position;
import com.github.antag99.spacelone.component.object.RoomObject;
import com.github.antag99.spacelone.component.object.Size;
import com.github.antag99.spacelone.system.RoomSystem;
import com.github.antag99.spacelone.util.AABB;

public final class ObjectCollisionSystem extends EntityProcessorSystem {
    private @SkipWire float maxMovementDistance;

    private RoomSystem roomSystem;
    private Mapper<Location> mLocation;
    private Mapper<Collision> mCollision;
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;
    private Mapper<RoomObject> mRoomObject;
    private Mapper<Solid> mSolid;

    public ObjectCollisionSystem(float maxMovementDistance) {
        super(Family.with(Location.class, Position.class, Size.class, Collision.class));
    }

    @Override
    protected void process(int entity) {
        int roomEntity = mLocation.get(entity).room;
        Position position = mPosition.get(entity);
        Collision collision = mCollision.get(entity);
        Size size = mSize.get(entity);
        EntitySet entities = roomSystem.getEntities(roomEntity,
                position.x - size.width * 0.5f - maxMovementDistance,
                position.y - size.height * 0.5f - maxMovementDistance,
                position.x + size.width * 0.5f + maxMovementDistance,
                position.y + size.height * 0.5f + maxMovementDistance);
        for (int i = 0, n = entities.size(), items[] = entities.getIndices().items; i < n; i++) {
            int objectEntity = items[i];
            if (mRoomObject.has(objectEntity) && mSolid.has(objectEntity)) {
                Position objectPosition = mPosition.get(objectEntity);
                Size objectSize = mSize.get(objectEntity);

                collision.boxes.add(Pools.obtain(AABB.class).set(
                        objectPosition.x, objectPosition.y,
                        objectSize.width, objectSize.height));
            }
        }
    }
}
