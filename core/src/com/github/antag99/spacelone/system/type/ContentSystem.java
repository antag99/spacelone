package com.github.antag99.spacelone.system.type;

import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.spacelone.component.Id;
import com.github.antag99.spacelone.component.Name;
import com.github.antag99.spacelone.component.type.FloorType;
import com.github.antag99.spacelone.component.type.ItemType;
import com.github.antag99.spacelone.component.type.TerrainType;

public final class ContentSystem extends EntitySystem {
    private Engine engine;
    private Mapper<Id> mId;
    private Mapper<Name> mName;
    private Mapper<FloorType> mFloorType;
    private Mapper<ItemType> mItemType;
    private Mapper<TerrainType> mTerrainType;

    public @SkipWire int air;
    public @SkipWire int ground;

    @Override
    protected void initialize() {
        int entity;

        entity = engine.createEntity();
        mId.create(entity).id = "air";
        mName.create(entity).name = "";
        mFloorType.create(entity);
        mItemType.create(entity);
        mTerrainType.create(entity);
        air = entity;

        entity = engine.createEntity();
        mId.create(entity).id = "ground";
        mName.create(entity).name = "Ground";
        mFloorType.create(entity);
        mItemType.create(entity);
        mTerrainType.create(entity);
        ground = entity;
    }
}
