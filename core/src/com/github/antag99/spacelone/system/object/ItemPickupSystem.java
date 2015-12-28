package com.github.antag99.spacelone.system.object;

import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.spacelone.component.object.Harvested;
import com.github.antag99.spacelone.component.object.Item;
import com.github.antag99.spacelone.system.RoomSystem;
import com.github.antag99.spacelone.util.EntityAdapter;

public final class ItemPickupSystem extends EntitySystem {
    private RoomSystem roomSystem;
    private Mapper<Harvested> mHarvested;

    @Override
    protected void initialize() {
        engine.getFamily(Family.with(Harvested.class, Item.class)).addListener(new EntityAdapter() {
            @Override
            protected void inserted(int entity) {
                roomSystem.pickupItem(entity, mHarvested.get(entity).harvestor);
            }
        });
    }
}
