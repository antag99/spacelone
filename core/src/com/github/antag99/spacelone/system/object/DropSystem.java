package com.github.antag99.spacelone.system.object;

import com.badlogic.gdx.math.MathUtils;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.spacelone.component.object.Harvested;
import com.github.antag99.spacelone.component.object.Location;
import com.github.antag99.spacelone.component.object.Position;
import com.github.antag99.spacelone.component.object.Size;
import com.github.antag99.spacelone.component.type.Drop;
import com.github.antag99.spacelone.system.IdSystem;
import com.github.antag99.spacelone.system.RoomSystem;
import com.github.antag99.spacelone.system.type.ContentSystem;
import com.github.antag99.spacelone.util.EntityAdapter;

public final class DropSystem extends EntitySystem {
    private IdSystem idSystem;
    private ContentSystem contentSystem;
    private RoomSystem roomSystem;
    private Mapper<Location> mLocation;
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;
    private Mapper<Drop> mDrop;

    @Override
    protected void initialize() {
        engine.getFamily(Family.with(Drop.class, Harvested.class)).addListener(new EntityAdapter() {
            @Override
            protected void inserted(int entity) {
                int roomEntity = mLocation.get(entity).room;
                Position position = mPosition.get(entity);
                Size size = mSize.get(entity);

                Drop drop = mDrop.get(entity);

                for (int i = 0, n = MathUtils.random(drop.minAmount, drop.maxAmount); i < n; i++) {
                    int itemEntity = contentSystem.createItem(idSystem.getEntity(drop.type));
                    roomSystem.dropItem(roomEntity, itemEntity,
                            position.x + size.width * 0.5f + MathUtils.random(-2f, 2f),
                            position.y + size.height * 0.5f + MathUtils.random(-2f, 2f));
                }
            }
        });
    }
}
