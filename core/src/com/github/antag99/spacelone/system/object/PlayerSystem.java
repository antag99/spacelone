package com.github.antag99.spacelone.system.object;

import java.util.UUID;

import com.badlogic.gdx.files.FileHandle;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.spacelone.component.Room;
import com.github.antag99.spacelone.component.Solid;
import com.github.antag99.spacelone.component.World;
import com.github.antag99.spacelone.component.object.Collision;
import com.github.antag99.spacelone.component.object.Control;
import com.github.antag99.spacelone.component.object.Harvestor;
import com.github.antag99.spacelone.component.object.Location;
import com.github.antag99.spacelone.component.object.Movement;
import com.github.antag99.spacelone.component.object.Player;
import com.github.antag99.spacelone.component.object.Position;
import com.github.antag99.spacelone.component.object.Size;
import com.github.antag99.spacelone.component.object.Velocity;
import com.github.antag99.spacelone.system.RoomSystem;
import com.github.antag99.spacelone.system.WorldSystem;
import com.github.antag99.spacelone.util.UUIDUtils;

public final class PlayerSystem extends EntitySystem {
    public static final float PLAYER_WIDTH = 0.8f;
    public static final float PLAYER_HEIGHT = 0.8f;

    private Kryo kryo;
    private RoomSystem roomSystem;
    private WorldSystem worldSystem;
    private Mapper<Room> mRoom;
    private Mapper<Player> mPlayer;
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;
    private Mapper<Velocity> mVelocity;
    private Mapper<Solid> mSolid;
    private Mapper<World> mWorld;
    private Mapper<Location> mLocation;
    private Mapper<Control> mControl;
    private Mapper<Movement> mMovement;
    private Mapper<Harvestor> mHarvestor;
    private Mapper<Collision> mCollision;

    @Override
    protected void initialize() {
        kryo.register(UUID.class, new Serializer<UUID>() {
            @Override
            public UUID read(Kryo kryo, Input input, Class<UUID> type) {
                return new UUID(input.readLong(), input.readLong());
            }

            @Override
            public void write(Kryo kryo, Output output, UUID object) {
                output.writeLong(object.getMostSignificantBits());
                output.writeLong(object.getLeastSignificantBits());
            }
        });
    }

    public int createPlayer(int roomEntity) {
        int player = roomSystem.createEntity(roomEntity);
        Room room = mRoom.get(roomEntity);
        World world = mWorld.get(room.world);
        world.players.edit().addEntity(player);
        mPlayer.create(player).uuid = UUID.randomUUID();
        mPosition.create(player).xy(mRoom.get(roomEntity).spawnPosition);
        mSize.create(player).set(PLAYER_WIDTH, PLAYER_HEIGHT);
        mVelocity.create(player);
        mSolid.create(player);
        mControl.create(player);
        mMovement.create(player).speed = 8f;
        mHarvestor.create(player);
        mCollision.create(player);

        return player;
    }

    public int loadPlayer(int worldEntity, UUID playerUuid) {
        World world = mWorld.get(worldEntity);
        FileHandle playerFile = world.directory.child("players/" + UUIDUtils.toHexString(playerUuid));

        if (!playerFile.exists())
            throw new IllegalArgumentException("player does not exist");

        Input input = new Input(playerFile.read());
        UUID roomUuid = world.kryo.readObject(input, UUID.class);
        int player = worldSystem.loadEntity(worldEntity, input);
        System.out.println("load player " + player);
        world.players.edit().addEntity(player);
        mLocation.create(player).room = worldSystem.getOrLoadRoom(worldEntity, roomUuid);
        input.close();
        return player;
    }

    public void savePlayer(int playerEntity) {
        Player player = mPlayer.get(playerEntity);
        Room room = mRoom.get(mLocation.get(playerEntity).room);
        World world = mWorld.get(room.world);
        FileHandle playerFile = world.directory.child("players/" + UUIDUtils.toHexString(player.uuid));
        Output output = new Output(playerFile.write(false));
        world.kryo.writeObject(output, room.uuid);
        worldSystem.saveEntity(playerEntity, output);
        output.close();
    }

    public void destroyPlayer(int player) {
        Room room = mRoom.get(mLocation.get(player).room);
        World world = mWorld.get(room.world);
        world.players.edit().removeEntity(player);
        engine.destroyEntity(player);
    }
}
