package com.github.antag99.spacelone.system.type;

import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.spacelone.component.Id;
import com.github.antag99.spacelone.component.Name;
import com.github.antag99.spacelone.component.type.FloorType;
import com.github.antag99.spacelone.component.type.ItemType;

public final class ContentSystem extends EntitySystem {
    private Engine engine;
    private Mapper<Id> mId;
    private Mapper<Name> mName;
    private Mapper<FloorType> mFloorType;
    private Mapper<ItemType> mItemType;

    public @SkipWire int air;

    @Override
    protected void initialize() {
        int entity;

        entity = engine.createEntity();
        mId.create(entity).id = "air";
        mName.create(entity).name = "";
        mFloorType.create(entity);
        mItemType.create(entity);
        air = entity;
    }
}
