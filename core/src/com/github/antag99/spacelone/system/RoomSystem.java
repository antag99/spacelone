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
import com.github.antag99.spacelone.component.object.Location;
import com.github.antag99.spacelone.component.object.Position;
import com.github.antag99.spacelone.component.object.Size;
import com.github.antag99.spacelone.system.object.SpatialSystem;
import com.github.antag99.spacelone.system.type.ContentSystem;
import com.github.antag99.spacelone.util.IntMatrix;
import com.github.antag99.spacelone.util.UUIDUtils;

public final class RoomSystem extends EntitySystem {
    private Kryo kryo;
    private IdSystem idSystem;
    private ContentSystem contentSystem;
    private WorldSystem worldSystem;
    private SpatialSystem spatialSystem;
    private Mapper<World> mWorld;
    private Mapper<Room> mRoom;
    private Mapper<Location> mLocation;
    private Mapper<Id> mId;
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;

    @Override
    protected void initialize() {
        // Note that only room data is serialized here - no entities
        kryo.register(Room.class, new Serializer<Room>() {
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
        for (int i = 0, n = input.readInt(); i < n; i++) {
            worldSystem.loadEntity(worldEntity, input);
        }
        mRoom.add(roomEntity, room);
        input.close();
        return roomEntity;
    }

    public void saveRoom(int roomEntity) {
        Room room = mRoom.get(roomEntity);
        World world = mWorld.get(room.world);
        Output output = new Output(world.directory.child("rooms/" +
                UUIDUtils.toHexString(room.uuid)).write(false));
        world.kryo.writeObject(output, room);
        output.writeInt(room.entities.size());
        for (int i = 0, n = room.entities.size(), items[] = room.entities.getIndices().items; i < n; i++) {
            worldSystem.saveEntity(items[i], output);
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

    public EntitySet getEntities(int roomEntity, float x, float y, float width, float height) {
        int partitionWidth = spatialSystem.getPartitionWidth();
        int partitionHeight = spatialSystem.getPartitionHeight();

        Room room = mRoom.get(roomEntity);
        int minX = MathUtils.floor(x / partitionWidth);
        int maxX = MathUtils.ceil((x + width) / partitionWidth);
        int minY = MathUtils.floor(y / partitionHeight);
        int maxY = MathUtils.ceil((y + height) / partitionHeight);

        tmpEntities.edit().clear();
        EntitySet set = tmpEntities;

        for (int i = minX; i < maxX; i++) {
            for (int j = minY; j < maxY; j++) {
                EntitySet partition = room.partitions.get(key.set(i, j));
                if (partition == null) {
                    continue;
                }

                for (int k = 0, n = partition.size(), items[] = partition.getIndices().items; k < n; k++) {
                    int entity = items[k];
                    Position position = mPosition.get(entity);
                    Size size = mSize.get(entity);

                    if (position.x + size.width > x && position.y + size.height > y &&
                            x + width > position.x && y + height > position.y) {
                        set.edit().addEntity(entity);
                    }
                }
            }
        }

        return set;
    }
}
