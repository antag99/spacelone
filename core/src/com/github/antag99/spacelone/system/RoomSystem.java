package com.github.antag99.spacelone.system;

import java.util.Arrays;
import java.util.UUID;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.antag99.retinazer.EntitySet;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.spacelone.component.Id;
import com.github.antag99.spacelone.component.Room;
import com.github.antag99.spacelone.component.World;
import com.github.antag99.spacelone.component.object.InventoryItem;
import com.github.antag99.spacelone.component.object.Location;
import com.github.antag99.spacelone.component.object.Player;
import com.github.antag99.spacelone.component.object.Position;
import com.github.antag99.spacelone.component.object.Size;
import com.github.antag99.spacelone.component.type.Harvestable;
import com.github.antag99.spacelone.system.object.SpatialSystem;
import com.github.antag99.spacelone.system.type.PrefabSystem;
import com.github.antag99.spacelone.util.IntMatrix;
import com.github.antag99.spacelone.util.UUIDUtils;

public final class RoomSystem extends EntitySystem {
    private Kryo kryo;
    private IdSystem idSystem;
    private PrefabSystem contentSystem;
    private WorldSystem worldSystem;
    private SpatialSystem spatialSystem;
    private Mapper<World> mWorld;
    private Mapper<Room> mRoom;
    private Mapper<Location> mLocation;
    private Mapper<Id> mId;
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;
    private Mapper<InventoryItem> mInventoryItem;
    private Mapper<Harvestable> mHarvestable;

    private Mapper<Player> mPlayer;

    @Override
    protected void initialize() {
        kryo.getRegistration(Room.class).setSerializer(new Serializer<Room>() {
            @Override
            public Room read(Kryo kryo, Input input, Class<Room> type) {
                Room room = new Room();
                room.width = input.readInt();
                room.height = input.readInt();
                room.spawnPosition.set(input.readFloat(), input.readFloat());
                room.terrain = new IntMatrix(room.width, room.height);
                for (int i = 0, v[] = room.terrain.getValues(); i < v.length; i++) {
                    v[i] = idSystem.getEntity(input.readString());
                }
                room.floor = new IntMatrix(room.width, room.height);
                for (int i = 0, v[] = room.floor.getValues(); i < v.length; i++) {
                    v[i] = idSystem.getEntity(input.readString());
                }
                return room;
            }

            @Override
            public void write(Kryo kryo, Output output, Room object) {
                // kryo.writeObject(output, object.uuid);
                output.writeInt(object.width);
                output.writeInt(object.height);
                output.writeFloat(object.spawnPosition.x);
                output.writeFloat(object.spawnPosition.y);
                for (int i = 0, v[] = object.terrain.getValues(); i < v.length; i++) {
                    output.writeString(mId.get(v[i]).id);
                }
                for (int i = 0, v[] = object.floor.getValues(); i < v.length; i++) {
                    output.writeString(mId.get(v[i]).id);
                }
            }
        });
    }

    public int getFloor(int room, int x, int y) {
        return mRoom.get(room).floor.get(x, y);
    }

    public void setFloor(int room, int x, int y, int floor) {
        mRoom.get(room).floor.set(x, y, floor);
    }

    public int createEntity(int roomEntity) {
        int entity = engine.createEntity();
        mLocation.create(entity).room = roomEntity;
        return entity;
    }

    public int createRoom(int worldEntity, int width, int height) {
        int roomEntity = engine.createEntity();
        World world = mWorld.get(worldEntity);
        world.rooms.edit().addEntity(roomEntity);
        Room room = mRoom.create(roomEntity);
        room.width = width;
        room.height = height;
        room.world = worldEntity;
        room.terrain = new IntMatrix(width, height);
        Arrays.fill(room.terrain.getValues(), contentSystem.air);
        room.floor = new IntMatrix(width, height);
        Arrays.fill(room.floor.getValues(), contentSystem.air);
        room.uuid = UUID.randomUUID();
        return roomEntity;
    }

    public int loadRoom(int worldEntity, UUID roomUuid) {
        int roomEntity = engine.createEntity();
        World world = mWorld.get(worldEntity);
        world.rooms.edit().addEntity(roomEntity);
        Input input = new Input(world.directory.child("rooms/" +
                UUIDUtils.toHexString(roomUuid)).read());
        Room room = kryo.readObject(input, Room.class);
        room.world = worldEntity;
        room.uuid = roomUuid;
        mRoom.add(roomEntity, room);
        for (int i = 0, n = input.readInt(); i < n; i++) {
            int entity = createEntity(roomEntity);
            worldSystem.loadEntity(worldEntity, entity, input);
        }
        input.close();
        return roomEntity;
    }

    public void saveRoom(int roomEntity) {
        Room room = mRoom.get(roomEntity);
        int worldEntity = room.world;
        World world = mWorld.get(worldEntity);
        Output output = new Output(world.directory.child("rooms/" +
                UUIDUtils.toHexString(room.uuid)).write(false));
        world.kryo.writeObject(output, room);
        int count = 0;
        for (int i = 0, n = room.entities.size(), items[] = room.entities.getIndices().items; i < n; i++) {
            if (!mPlayer.has(items[i])) {
                count++;
            }
        }
        output.writeInt(count);
        for (int i = 0, n = room.entities.size(), items[] = room.entities.getIndices().items; i < n; i++) {
            int entity = items[i];
            if (!mPlayer.has(entity)) {
                worldSystem.saveEntity(worldEntity, entity, output);
            }
        }
        output.close();
    }

    public void destroyRoom(int roomEntity) {
        Room room = mRoom.get(roomEntity);
        World world = mWorld.get(room.world);
        world.rooms.edit().removeEntity(roomEntity);
        for (EntitySet entities : mRoom.get(roomEntity).partitions.values()) {
            for (int i = 0, n = entities.size(), items[] = entities.getIndices().items; i < n; i++) {
                engine.destroyEntity(items[i]);
            }
        }
        engine.destroyEntity(roomEntity);
    }

    private @SkipWire EntitySet tmpEntities = new EntitySet();
    private @SkipWire GridPoint2 key = new GridPoint2();

    public EntitySet getEntities(int roomEntity, float minX, float minY, float maxX, float maxY) {
        int partitionWidth = spatialSystem.getPartitionWidth();
        int partitionHeight = spatialSystem.getPartitionHeight();

        Room room = mRoom.get(roomEntity);
        int minPartitionX = MathUtils.floor(minX / partitionWidth);
        int minPartitionY = MathUtils.floor(minY / partitionHeight);
        int maxPartitionX = MathUtils.ceil(maxX / partitionWidth);
        int maxPartitionY = MathUtils.ceil(maxY / partitionHeight);

        tmpEntities.edit().clear();
        EntitySet set = tmpEntities;

        for (int i = minPartitionX; i < maxPartitionX; i++) {
            for (int j = minPartitionY; j < maxPartitionY; j++) {
                EntitySet partition = room.partitions.get(key.set(i, j));
                if (partition == null) {
                    continue;
                }

                for (int k = 0, n = partition.size(), items[] = partition.getIndices().items; k < n; k++) {
                    int entity = items[k];
                    Position position = mPosition.get(entity);
                    Size size = mSize.get(entity);

                    if (position.x + size.width * 0.5f > minX &&
                            position.y + size.height * 0.5f > minY &&
                            position.x - size.width * 0.5f < maxX &&
                            position.y - size.height * 0.5f < maxY) {
                        set.edit().addEntity(entity);
                    }
                }
            }
        }

        return set;
    }

    public void dropItem(int roomEntity, int itemEntity, float x, float y) {
        if (mLocation.has(itemEntity))
            throw new IllegalArgumentException("entity already exists in a room: " + itemEntity);
        if (mInventoryItem.has(itemEntity))
            mInventoryItem.remove(itemEntity);
        mLocation.create(itemEntity).room = roomEntity;
        mPosition.create(itemEntity).xy(x, y);
        mHarvestable.create(itemEntity);
    }

    public void pickupItem(int itemEntity, int inventoryEntity) {
        if (!mLocation.has(itemEntity))
            throw new IllegalArgumentException("entity does not exist in a room: " + itemEntity);
        mLocation.remove(itemEntity);
        mPosition.remove(itemEntity);
        mInventoryItem.create(itemEntity).owner = inventoryEntity;
        mHarvestable.remove(itemEntity);
    }
}
