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
import com.github.antag99.spacelone.component.type.Harvestable;
import com.github.antag99.spacelone.system.DeltaSystem;
import com.github.antag99.spacelone.system.RoomSystem;

public final class HarvestorSystem extends EntityProcessorSystem {
    private DeltaSystem deltaSystem;
    private RoomSystem roomSystem;
    private Mapper<Location> mLocation;
    private Mapper<Position> mPosition;
    private Mapper<Harvestor> mHarvestor;
    private Mapper<Control> mControl;

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
        Harvestor harvestor = mHarvestor.get(entity);
        Control control = mControl.get(entity);

        float range = 5f; // XXX temporary (shh) hack

        if (control.harvest && !harvestor.active) {
            // Find entities within range, filter out those that are not closest
            // to the player, or not objects at all. (Bit hacky, implementation
            // dependent).
            EntitySet objects = roomSystem.getEntities(roomEntity,
                    position.x - range, position.y - range,
                    position.x + range, position.y + range);

            float closestDistance = range * range + MathUtils.FLOAT_ROUNDING_ERROR;

            for (int i = 0, n = objects.size(), items[] = objects.getIndices().items; i < n; i++) {
                int object = items[i];
                Position objectPosition = mPosition.get(object);
                float distance = Vector2.dst2(position.x, position.y, objectPosition.x, objectPosition.y);

                Harvestable harvestable = mHarvestable.get(object);
                if (harvestable != null && !harvestable.isBeingHarvested) {
                    if (distance <= closestDistance) {
                        // If this object is closer than the earlier then remove them
                        if (distance < closestDistance)
                            for (int ii = 0, nn = i; ii < nn; ii++)
                                objects.edit().removeEntity(items[ii]);

                        closestDistance = distance;
                        continue;
                    }
                }

                objects.edit().removeEntity(object);
            }

            if (!objects.isEmpty()) {
                harvestor.active = true;
                harvestor.counter = 0f;
                harvestor.target = objects.getIndices().random();
                mHarvestable.get(harvestor.target).isBeingHarvested = true;
            }
        }

        if (harvestor.active) {
            harvestor.counter += deltaTime;
            float time = 0.5f; // XXX temporary (shh) hack
            if (harvestor.counter >= time) {
                harvestor.active = false;
                mHarvestable.get(harvestor.target).isBeingHarvested = false;
                mHarvested.create(harvestor.target).harvestor = entity;
            }
        }
    }
}
