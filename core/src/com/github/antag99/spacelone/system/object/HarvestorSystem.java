package com.github.antag99.spacelone.system.object;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.EntitySet;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.spacelone.component.object.Control;
import com.github.antag99.spacelone.component.object.Harvested;
import com.github.antag99.spacelone.component.object.Harvestor;
import com.github.antag99.spacelone.component.object.Location;
import com.github.antag99.spacelone.component.object.Position;
import com.github.antag99.spacelone.component.object.RoomObject;
import com.github.antag99.spacelone.component.object.Size;
import com.github.antag99.spacelone.component.type.Harvestable;
import com.github.antag99.spacelone.system.DeltaSystem;
import com.github.antag99.spacelone.system.RoomSystem;

public final class HarvestorSystem extends EntityProcessorSystem {
    private DeltaSystem deltaSystem;
    private RoomSystem roomSystem;
    private Mapper<Location> mLocation;
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;
    private Mapper<Harvestor> mHarvestor;
    private Mapper<Control> mControl;

    private Mapper<RoomObject> mRoomObject;
    private Mapper<Harvestable> mHarvestable;
    private Mapper<Harvested> mHarvested;

    public HarvestorSystem() {
        super(Family.with(Location.class, Harvestor.class, Control.class));
    }

    @Override
    protected void process(int entity) {
        int roomEntity = mLocation.get(entity).room;
        float deltaTime = deltaSystem.getDeltaTime();
        Position position = mPosition.get(entity);
        Size size = mSize.get(entity);
        Harvestor harvestor = mHarvestor.get(entity);
        Control control = mControl.get(entity);

        float centerX = position.x + size.width * 0.5f;
        float centerY = position.y + size.height * 0.5f;

        float range = 5f; // XXX temporary (shh) hack

        if (control.harvest && !harvestor.active) {
            // Find entities within range, filter out those that are not closest
            // to the player, or not objects at all. (Bit hacky, implementation
            // dependent).
            EntitySet objects = roomSystem.getEntities(roomEntity,
                    centerX - range, centerY - range,
                    range * 2, range * 2);

            float closestDistance = range * range + MathUtils.FLOAT_ROUNDING_ERROR;

            for (int i = 0, n = objects.size(), items[] = objects.getIndices().items; i < n; i++) {
                int object = items[i];
                Position objectPosition = mPosition.get(object);
                Size objectSize = mSize.get(object);
                float objectCenterX = objectPosition.x + objectSize.width * 0.5f;
                float objectCenterY = objectPosition.y + objectSize.height * 0.5f;
                float distance = Vector2.dst2(centerX, centerY, objectCenterX, objectCenterY);

                RoomObject roomObject = mRoomObject.get(object);
                if (roomObject != null) {
                    Harvestable harvestable = mHarvestable.get(roomObject.type);
                    if (harvestable != null) {
                        if (distance <= closestDistance) {
                            // If this object is closer than the earlier then remove them
                            if (distance < closestDistance)
                                for (int ii = 0, nn = i; ii < nn; ii++)
                                    objects.edit().removeEntity(items[ii]);

                            closestDistance = distance;
                            continue;
                        }
                    }
                }

                objects.edit().removeEntity(object);
            }

            if (!objects.isEmpty()) {
                harvestor.active = true;
                harvestor.counter = 0f;
                harvestor.target = objects.getIndices().random();
            }
        }

        if (harvestor.active) {
            harvestor.counter += deltaTime;
            float time = 0.5f; // XXX temporary (shh) hack
            if (harvestor.counter >= time) {
                harvestor.active = false;
                mHarvested.create(harvestor.target);
            }
        }
    }
}
