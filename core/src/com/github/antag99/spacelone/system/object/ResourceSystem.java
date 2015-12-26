package com.github.antag99.spacelone.system.object;

import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.spacelone.component.object.Harvested;
import com.github.antag99.spacelone.component.type.Resource;
import com.github.antag99.spacelone.util.EntityAdapter;

public final class ResourceSystem extends EntitySystem {
    private Mapper<Resource> mResource;

    @Override
    protected void initialize() {
        engine.getFamily(Family.with(Resource.class, Harvested.class)).addListener(new EntityAdapter() {
            @Override
            protected void inserted(int entity) {
                Resource resource = mResource.get(entity);
                if (resource.amount == 0 || --resource.amount == 0) {
                    engine.destroyEntity(entity);
                }
            }
        });
    }
}
