package com.github.antag99.spacelone.system;

import com.github.antag99.retinazer.EntitySystem;

public final class RoomGeneratorSystem extends EntitySystem {
    private RoomSystem roomSystem;
    // private Mapper<Room> mRoom;

    public int generateRoom(int worldEntity) {
        int roomEntity = roomSystem.createRoom(worldEntity, 1024, 1024);
        return roomEntity;
    }
}
