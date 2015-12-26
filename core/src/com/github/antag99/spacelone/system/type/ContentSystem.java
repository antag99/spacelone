package com.github.antag99.spacelone.system.type;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.github.antag99.retinazer.Component;
import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.spacelone.component.Id;
import com.github.antag99.spacelone.component.Name;
import com.github.antag99.spacelone.component.Solid;
import com.github.antag99.spacelone.component.object.Position;
import com.github.antag99.spacelone.component.object.RoomObject;
import com.github.antag99.spacelone.component.object.Size;
import com.github.antag99.spacelone.component.object.Tree;
import com.github.antag99.spacelone.component.type.Aligned;
import com.github.antag99.spacelone.component.type.Drop;
import com.github.antag99.spacelone.component.type.FloorType;
import com.github.antag99.spacelone.component.type.Harvestable;
import com.github.antag99.spacelone.component.type.ItemType;
import com.github.antag99.spacelone.component.type.MaterialType;
import com.github.antag99.spacelone.component.type.ObjectTextureRef;
import com.github.antag99.spacelone.component.type.ObjectType;
import com.github.antag99.spacelone.component.type.Resource;
import com.github.antag99.spacelone.component.type.TerrainType;
import com.github.antag99.spacelone.system.RoomSystem;

public final class ContentSystem extends EntitySystem {
    private Engine engine;

    private RoomSystem roomSystem;

    private Mapper<Id> mId;
    private Mapper<FloorType> mFloorType;
    private Mapper<ItemType> mItemType;
    private Mapper<TerrainType> mTerrainType;
    private Mapper<MaterialType> mMaterialType;
    private Mapper<ObjectType> mObjectType;

    private Mapper<Name> mName;
    private Mapper<Size> mSize;
    private Mapper<Aligned> mAligned;
    private Mapper<Harvestable> mHarvestable;
    private Mapper<Drop> mDrop;
    private Mapper<Resource> mResource;
    private Mapper<Position> mPosition;
    private Mapper<Tree> mTree;
    private Mapper<Solid> mSolid;

    private Mapper<RoomObject> mRoomObject;

    private Kryo kryo;
    private @SkipWire Array<Mapper<?>> propertyTypes = new Array<>();

    public void registerPropertyType(Class<? extends Component> propertyType) {
        propertyTypes.add(engine.getMapper(propertyType));
    }

    @SuppressWarnings("unchecked")
    public int createObject(int roomEntity, int typeEntity, float x, float y) {
        int objectEntity = roomSystem.createEntity(roomEntity);
        mPosition.create(objectEntity).xy(x, y);
        mRoomObject.create(objectEntity).type = typeEntity;
        for (Mapper<?> propertyMapper : propertyTypes) {
            Component component = propertyMapper.get(typeEntity);
            if (component != null) {
                ((Mapper<Component>) propertyMapper).add(objectEntity, kryo.copy(component));
            }
        }
        return objectEntity;
    }

    private void registerPropertyTypes() {
        registerPropertyType(Name.class);
        registerPropertyType(Size.class);
        registerPropertyType(Aligned.class);
        registerPropertyType(Harvestable.class);
        registerPropertyType(Drop.class);
        registerPropertyType(Resource.class);
        registerPropertyType(ObjectTextureRef.class);
        registerPropertyType(Tree.class);
        registerPropertyType(Solid.class);
    }

    public @SkipWire int air;

    @Override
    protected void initialize() {
        registerPropertyTypes();

        int entity;

        air = entity = engine.createEntity();
        mTerrainType.create(entity);
        mFloorType.create(entity);
        mItemType.create(entity);
        mId.create(entity).id = "air";
        mName.create(entity).name = "";

        initializeMaterialTypes();
        initializeFloorTypes();
        initializeItemTypes();
        initializeTerrainTypes();
        initializeObjectTypes();
    }

    private void initializeMaterialTypes() {
        int entity;

        entity = engine.createEntity();
        mMaterialType.create(entity);
        mId.create(entity).id = "wood";
        mName.create(entity).name = "Wood";

        entity = engine.createEntity();
        mMaterialType.create(entity);
        mId.create(entity).id = "stone";
        mName.create(entity).name = "Stone";
    }

    private void initializeFloorTypes() {
    }

    private void initializeItemTypes() {
        int entity;

        entity = engine.createEntity();
        mItemType.create(entity);
        mId.create(entity).id = "log";
        mName.create(entity).name = "Log";
        mSize.create(entity).set(0.5f, 0.5f);
    }

    private void initializeObjectTypes() {
        int entity;

        entity = engine.createEntity();
        mObjectType.create(entity);
        mId.create(entity).id = "tree";
        mName.create(entity).name = "Tree";
        mAligned.create(entity);
        mSize.create(entity).set(1f, 1f);
        mHarvestable.create(entity).material = "wood";
        mDrop.create(entity).type("log").amount(2, 5);
        mResource.create(entity).amount = 1;
        mTree.create(entity);
        mSolid.create(entity);
    }

    private void initializeTerrainTypes() {
        int entity;

        entity = engine.createEntity();
        mTerrainType.create(entity);
        mId.create(entity).id = "ground";
        mName.create(entity).name = "Ground";
    }
}
