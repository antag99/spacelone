package com.github.antag99.spacelone.system.object;

import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.spacelone.component.object.Harvested;
import com.github.antag99.spacelone.util.EntityAdapter;

public final class HarvestedSystem extends EntitySystem {
    private Mapper<Harvested> mHarvested;

    @Override
    protected void initialize() {
        engine.getFamily(Family.with(Harvested.class)).addListener(new EntityAdapter() {
            @Override
            protected void inserted(int entity) {
                // Removes the harvested component after all listeners have
                // been notified of it.
                mHarvested.remove(entity);
            }
        });
    }
}
