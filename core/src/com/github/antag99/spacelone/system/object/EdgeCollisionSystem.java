package com.github.antag99.spacelone.system.object;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pools;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.spacelone.component.Room;
import com.github.antag99.spacelone.component.object.Collision;
import com.github.antag99.spacelone.component.object.Location;
import com.github.antag99.spacelone.component.object.Position;
import com.github.antag99.spacelone.component.object.Size;
import com.github.antag99.spacelone.system.IdSystem;

public final class EdgeCollisionSystem extends EntityProcessorSystem {
    private IdSystem idSystem;
    private Mapper<Location> mLocation;
    private Mapper<Collision> mCollision;
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;

    private Mapper<Room> mRoom;

    public EdgeCollisionSystem() {
        super(Family.with(Location.class, Collision.class));
    }

    @Override
    protected void process(int entity) {
        Room room = mRoom.get(mLocation.get(entity).room);
        Position position = mPosition.get(entity);
        Size size = mSize.get(entity);
        Collision collision = mCollision.get(entity);

        int airEntity = idSystem.getEntity("air");
        int minX = MathUtils.floor(Math.min(position.x, position.prevX));
        int maxX = MathUtils.ceil(Math.max(position.x, position.prevX) + size.width);
        int minY = MathUtils.floor(Math.min(position.y, position.prevY));
        int maxY = MathUtils.ceil(Math.max(position.y, position.prevY) + size.height);

        for (int i = minX; i < maxX; i++) {
            for (int j = minY; j < maxY; j++) {
                if (room.terrain.get(i, j) == airEntity) {
                    collision.rectangles.add(Pools.obtain(Rectangle.class).set(i, j, 1f, 1f));
                }
            }
        }
    }
}
