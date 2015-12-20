package com.github.antag99.spacelone.system;

import java.io.IOException;
import java.util.UUID;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.antag99.retinazer.Component;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.spacelone.component.Name;
import com.github.antag99.spacelone.component.Room;
import com.github.antag99.spacelone.component.Solid;
import com.github.antag99.spacelone.component.World;
import com.github.antag99.spacelone.component.object.Colored;
import com.github.antag99.spacelone.component.object.Control;
import com.github.antag99.spacelone.component.object.Location;
import com.github.antag99.spacelone.component.object.Movement;
import com.github.antag99.spacelone.component.object.Player;
import com.github.antag99.spacelone.component.object.Position;
import com.github.antag99.spacelone.component.object.Size;
import com.github.antag99.spacelone.component.object.Velocity;
import com.github.antag99.spacelone.system.object.PlayerSystem;
import com.github.antag99.spacelone.util.UUIDUtils;

public final class WorldSystem extends EntitySystem {
    private RoomSystem roomSystem;
    private RoomGeneratorSystem roomGeneratorSystem;
    private PlayerSystem playerSystem;
    private Mapper<World> mWorld;
    private Mapper<Room> mRoom;
    private Mapper<Location> mLocation;
    private Mapper<Player> mPlayer;

    private Kryo kryo;
    private @SkipWire Array<Component> tmpComponents = new Array<Component>(Component.class);
    private @SkipWire Array<Mapper<?>> serializableComponents = new Array<Mapper<?>>(Mapper.class);

    public int createWorld(FileHandle directory) {
        int worldEntity = engine.createEntity();
        World world = mWorld.create(worldEntity);
        world.directory = directory;
        world.kryo = kryo;

        int roomEntity = roomGeneratorSystem.generateRoom(worldEntity);
        world.startRoom = roomEntity;

        int playerEntity = playerSystem.createPlayer(roomEntity);
        world.localPlayer = playerEntity;

        saveWorld(worldEntity);

        return worldEntity;
    }

    public int loadWorld(FileHandle directory) {
        int worldEntity = engine.createEntity();

        World world = mWorld.create(worldEntity);
        world.directory = directory;
        world.kryo = kryo; // TODO: Write out ID mappings for compatibility

        JsonValue value = new JsonReader().parse(directory.child("world.json"));
        UUID startRoomUuid = UUIDUtils.fromHexString(value.getString("startRoom"));
        UUID localPlayerUuid = UUIDUtils.fromHexString(value.getString("localPlayer"));
        world.startRoom = roomSystem.loadRoom(worldEntity, startRoomUuid);
        world.localPlayer = playerSystem.loadPlayer(worldEntity, localPlayerUuid);
        return worldEntity;
    }

    public void saveWorld(int worldEntity) {
        World world = mWorld.get(worldEntity);

        for (int roomEntity : world.rooms.getIndices().toArray()) {
            roomSystem.saveRoom(roomEntity);
        }

        for (int playerEntity : world.players.getIndices().toArray()) {
            playerSystem.savePlayer(playerEntity);
        }

        try {
            JsonWriter writer = new JsonWriter(world.directory.child("world.json").writer(false));
            writer.object()
                    .set("startRoom", UUIDUtils.toHexString(mRoom.get(world.startRoom).uuid))
                    .set("localPlayer", UUIDUtils.toHexString(mPlayer.get(world.localPlayer).uuid))
                    .pop();
            writer.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void destroyWorld(int worldEntity) {
        World world = mWorld.get(worldEntity);

        for (int playerEntity : world.players.getIndices().toArray()) {
            playerSystem.destroyPlayer(playerEntity);
        }

        for (int roomEntity : world.rooms.getIndices().toArray()) {
            roomSystem.destroyRoom(roomEntity);
        }

        engine.destroyEntity(worldEntity);
    }

    public int getOrLoadRoom(int worldEntity, UUID roomUuid) {
        World world = mWorld.get(worldEntity);
        for (int roomEntity : world.rooms.getIndices().toArray()) {
            if (mRoom.get(roomEntity).uuid.equals(roomUuid)) {
                return roomEntity;
            }
        }
        return roomSystem.loadRoom(worldEntity, roomUuid);
    }

    @Override
    protected void initialize() {
        registerComponentType(Position.class);
        registerComponentType(Velocity.class);
        registerComponentType(Size.class);
        registerComponentType(Name.class);
        registerComponentType(Player.class);
        registerComponentType(Solid.class);
        registerComponentType(Colored.class);
        registerComponentType(Movement.class);
        registerComponentType(Control.class);
    }

    public void registerComponentType(Class<? extends Component> componentType) {
        serializableComponents.add(engine.getMapper(componentType));
    }

    public void saveEntity(int entity, Output output) {
        int roomEntity = mLocation.get(entity).room;
        World world = mWorld.get(mRoom.get(roomEntity).world);
        Array<Component> components = tmpComponents;
        Mapper<?>[] mappers = serializableComponents.items;
        for (int i = 0, n = serializableComponents.size; i < n; i++) {
            Component component = mappers[i].get(entity);
            if (component != null)
                components.add(component);
        }
        output.writeInt(components.size, true);
        for (Component component : components) {
            world.kryo.writeClassAndObject(output, component);
        }
        components.clear();
    }

    @SuppressWarnings("unchecked")
    public int loadEntity(int worldEntity, Input input) {
        World world = mWorld.get(worldEntity);
        int entity = engine.createEntity();
        for (int i = 0, n = input.readInt(true); i < n; i++) {
            Component component = (Component) world.kryo.readClassAndObject(input);
            ((Mapper<Component>) engine.getMapper(component.getClass())).add(entity, component);
        }
        return entity;
    }
}
