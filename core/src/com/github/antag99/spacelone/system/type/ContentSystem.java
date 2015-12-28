package com.github.antag99.spacelone.system.type;

import java.util.Set;

import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.antag99.retinazer.Component;
import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.spacelone.Spacelone;
import com.github.antag99.spacelone.component.Id;
import com.github.antag99.spacelone.component.Name;
import com.github.antag99.spacelone.component.Solid;
import com.github.antag99.spacelone.component.object.Colored;
import com.github.antag99.spacelone.component.object.Item;
import com.github.antag99.spacelone.component.object.ItemAmount;
import com.github.antag99.spacelone.component.object.Position;
import com.github.antag99.spacelone.component.object.RoomObject;
import com.github.antag99.spacelone.component.object.Size;
import com.github.antag99.spacelone.component.object.Tree;
import com.github.antag99.spacelone.component.type.Aligned;
import com.github.antag99.spacelone.component.type.Drop;
import com.github.antag99.spacelone.component.type.FloorType;
import com.github.antag99.spacelone.component.type.Harvestable;
import com.github.antag99.spacelone.component.type.ItemPrefab;
import com.github.antag99.spacelone.component.type.ItemTextureRef;
import com.github.antag99.spacelone.component.type.ObjectPrefab;
import com.github.antag99.spacelone.component.type.Resource;
import com.github.antag99.spacelone.component.type.TerrainType;
import com.github.antag99.spacelone.system.IdSystem;
import com.github.antag99.spacelone.system.RoomSystem;
import com.github.antag99.spacelone.util.SkipProperty;

public final class ContentSystem extends EntitySystem {
    private Engine engine;

    private RoomSystem roomSystem;
    private IdSystem idSystem;

    private Mapper<Id> mId;

    private Mapper<ItemPrefab> mItemPrefab;
    private Mapper<ObjectPrefab> mObjectPrefab;

    private Mapper<ItemTextureRef> mItemTextureRef;

    private Mapper<FloorType> mFloorType;
    private Mapper<TerrainType> mTerrainType;

    private Mapper<Item> mItem;
    private Mapper<ItemAmount> mItemAmount;

    private Mapper<Name> mName;

    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;

    private Mapper<Aligned> mAligned;
    private Mapper<Harvestable> mHarvestable;
    private Mapper<Drop> mDrop;
    private Mapper<Resource> mResource;
    private Mapper<Tree> mTree;
    private Mapper<Solid> mSolid;

    private Mapper<RoomObject> mRoomObject;

    private Mapper<Colored> mColored;

    private Kryo kryo;

    private @SkipWire Mapper<?>[] propertyMappers;

    @SuppressWarnings("unchecked")
    public void initializePrefab(int targetEntity, int prefabEntity) {
        for (Mapper<?> propertyMapper : propertyMappers) {
            Component component = propertyMapper.get(prefabEntity);
            if (component != null) {
                ((Mapper<Component>) propertyMapper).add(targetEntity, kryo.copy(component));
            }
        }
    }

    public int createItem(int prefabEntity) {
        int itemEntity = engine.createEntity();
        mItem.create(itemEntity);
        initializePrefab(itemEntity, prefabEntity);
        return itemEntity;
    }

    public int createObject(int roomEntity, int prefabEntity, float x, float y) {
        int objectEntity = roomSystem.createEntity(roomEntity);
        mPosition.create(objectEntity).xy(x, y);
        mRoomObject.create(objectEntity).prefab = prefabEntity;
        initializePrefab(objectEntity, prefabEntity);
        return objectEntity;
    }

    @SuppressWarnings("unchecked")
    private void registerPropertyTypes() {
        Reflections reflections = new Reflections(Spacelone.class.getPackage().getName());
        Set<Class<? extends Component>> types = ReflectionUtils.getAll(reflections.getSubTypesOf(Component.class));
        Array<Class<? extends Component>> propertyTypes = new Array<>();
        for (Class<? extends Component> type : types) {
            if (type.getAnnotation(SkipProperty.class) == null)
                propertyTypes.add(type);
        }
        propertyMappers = new Mapper<?>[propertyTypes.size];
        for (int i = 0, n = propertyTypes.size; i < n; i++) {
            propertyMappers[i] = engine.getMapper(propertyTypes.get(i));
        }
    }

    private void registerSerializers() {
        kryo.getRegistration(Item.class).setSerializer(new Serializer<Item>() {
            @Override
            public void write(Kryo kryo, Output output, Item object) {
                output.writeString(mId.get(object.prefab).id);
            }

            @Override
            public Item read(Kryo kryo, Input input, Class<Item> type) {
                Item object = new Item();
                object.prefab = idSystem.getEntity(input.readString());
                return object;
            }
        });

        kryo.getRegistration(RoomObject.class).setSerializer(new Serializer<RoomObject>() {
            @Override
            public void write(Kryo kryo, Output output, RoomObject object) {
                output.writeString(mId.get(object.prefab).id);
            }

            @Override
            public RoomObject read(Kryo kryo, Input input, Class<RoomObject> type) {
                RoomObject object = new RoomObject();
                object.prefab = idSystem.getEntity(input.readString());
                return object;
            }
        });
    }

    public @SkipWire int air;

    @Override
    protected void initialize() {
        registerPropertyTypes();
        registerSerializers();

        int entity;

        air = entity = engine.createEntity();
        mTerrainType.create(entity);
        mFloorType.create(entity);
        mItemPrefab.create(entity);
        mId.create(entity).id = "air";
        mName.create(entity).name = "";

        initializeFloorTypes();
        initializeItemPrefabs();
        initializeTerrainTypes();
        initializeObjectPrefabs();
    }

    private void initializeTerrainTypes() {
        int entity;

        entity = engine.createEntity();
        mTerrainType.create(entity);
        mId.create(entity).id = "ground";
        mName.create(entity).name = "Ground";
    }

    private void initializeFloorTypes() {
    }

    private void initializeItemPrefabs() {
        int entity;

        entity = engine.createEntity();
        mItemPrefab.create(entity);
        mId.create(entity).id = "log";
        mName.create(entity).name = "Log";
        mSize.create(entity).set(1f, 1f);
        mItemAmount.create(entity);
        mItemTextureRef.create(entity).name = "images/log";
        mColored.create(entity).color(Color.BROWN);
    }

    private void initializeObjectPrefabs() {
        int entity;

        entity = engine.createEntity();
        mObjectPrefab.create(entity);
        mId.create(entity).id = "tree";
        mName.create(entity).name = "Tree";
        mAligned.create(entity);
        mSize.create(entity).set(1f, 1f);
        mDrop.create(entity).type("log").amount(2, 5);
        mResource.create(entity).amount = 1;
        mTree.create(entity);
        mSolid.create(entity);
        mHarvestable.create(entity);
    }
}
