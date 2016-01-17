package com.github.antag99.spacelone.system.object;

import java.util.UUID;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.IntArray;
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
import com.github.antag99.spacelone.component.object.HeldItem;
import com.github.antag99.spacelone.component.object.Hotbar;
import com.github.antag99.spacelone.component.object.Inventory;
import com.github.antag99.spacelone.component.object.InventoryItem;
import com.github.antag99.spacelone.component.object.Location;
import com.github.antag99.spacelone.component.object.Movement;
import com.github.antag99.spacelone.component.object.Player;
import com.github.antag99.spacelone.component.object.Position;
import com.github.antag99.spacelone.component.object.Size;
import com.github.antag99.spacelone.component.object.Velocity;
import com.github.antag99.spacelone.system.RoomSystem;
import com.github.antag99.spacelone.system.WorldSystem;
import com.github.antag99.spacelone.system.type.PrefabSystem;
import com.github.antag99.spacelone.util.UUIDUtils;

public final class PlayerSystem extends EntitySystem {
    public static final float PLAYER_WIDTH = 0.8f;
    public static final float PLAYER_HEIGHT = 0.8f;
    public static final int HOTBAR_SIZE = 10;

    private RoomSystem roomSystem;
    private WorldSystem worldSystem;
    private PrefabSystem prefabSystem;

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
    private Mapper<Inventory> mInventory;
    private Mapper<InventoryItem> mInventoryItem;
    private Mapper<HeldItem> mHeldItem;
    private Mapper<Hotbar> mHotbar;

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
        mMovement.create(player).speed = 8f;
        mHarvestor.create(player);
        mCollision.create(player);
        mControl.create(player);
        mInventory.create(player);
        mHeldItem.create(player).item = prefabSystem.createItem(prefabSystem.air);
        int[] hotbarItems = new int[HOTBAR_SIZE];
        for (int i = 0; i < HOTBAR_SIZE; i++) {
            hotbarItems[i] = prefabSystem.createItem(prefabSystem.air);
            mInventoryItem.create(hotbarItems[i]).owner = player;
        }
        mHotbar.create(player).items = hotbarItems;
        return player;
    }

    public int loadPlayer(int worldEntity, UUID playerUuid) {
        World world = mWorld.get(worldEntity);
        FileHandle playerFile = world.directory.child("players/" + UUIDUtils.toHexString(playerUuid));
        if (!playerFile.exists())
            throw new IllegalArgumentException("player does not exist");
        Input input = new Input(playerFile.read());
        UUID roomUuid = world.kryo.readObject(input, UUID.class);
        int roomEntity = worldSystem.getOrLoadRoom(worldEntity, roomUuid);
        int playerEntity = roomSystem.createEntity(roomEntity);
        worldSystem.loadEntity(worldEntity, playerEntity, input);
        mPlayer.create(playerEntity).uuid = playerUuid;
        mControl.create(playerEntity);
        world.players.edit().addEntity(playerEntity);

        mInventory.create(playerEntity);
        int itemCount = input.readInt(true);
        IntArray itemsByIndex = new IntArray();
        for (int i = 0; i < itemCount; i++) {
            int entity = engine.createEntity();
            mInventoryItem.create(entity).owner = playerEntity;
            worldSystem.loadEntity(worldEntity, entity, input);
            itemsByIndex.add(entity);
        }

        int heldItem = engine.createEntity();
        mInventoryItem.create(heldItem).owner = playerEntity;
        worldSystem.loadEntity(worldEntity, heldItem, input);
        mHeldItem.create(playerEntity).item = heldItem;
        Hotbar hotbar = mHotbar.create(playerEntity);
        hotbar.items = new int[HOTBAR_SIZE];
        for (int i = 0; i < HOTBAR_SIZE; i++) {
            hotbar.items[i] = itemsByIndex.get(input.readInt(true));
        }
        input.close();
        return playerEntity;
    }

    public void savePlayer(int playerEntity) {
        Player player = mPlayer.get(playerEntity);
        Room room = mRoom.get(mLocation.get(playerEntity).room);
        int worldEntity = room.world;
        World world = mWorld.get(worldEntity);
        FileHandle playerFile = world.directory.child("players/" + UUIDUtils.toHexString(player.uuid));
        Output output = new Output(playerFile.write(false));
        world.kryo.writeObject(output, room.uuid);
        worldSystem.saveEntity(worldEntity, playerEntity, output);

        Inventory inventory = mInventory.get(playerEntity);
        output.writeInt(inventory.items.size(), true);
        IntArray itemsByIndex = inventory.items.getIndices();
        for (int i = 0, n = inventory.items.size(), v[] = itemsByIndex.items; i < n; i++) {
            worldSystem.saveEntity(worldEntity, v[i], output);
        }

        worldSystem.saveEntity(worldEntity, mHeldItem.get(playerEntity).item, output);
        Hotbar hotbar = mHotbar.get(playerEntity);
        for (int i = 0; i < HOTBAR_SIZE; i++) {
            output.writeInt(itemsByIndex.indexOf(hotbar.items[i]), true);
        }
        output.close();
    }

    public void destroyPlayer(int player) {
        Room room = mRoom.get(mLocation.get(player).room);
        World world = mWorld.get(room.world);
        world.players.edit().removeEntity(player);
        engine.destroyEntity(player);
    }
}
