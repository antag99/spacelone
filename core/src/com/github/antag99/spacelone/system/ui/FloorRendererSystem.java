package com.github.antag99.spacelone.system.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.spacelone.component.Room;
import com.github.antag99.spacelone.component.type.FloorTexture;

public final class FloorRendererSystem extends BaseRendererSystem {
    private Mapper<Room> mRoom;
    private Mapper<FloorTexture> mFloorTexture;

    @Override
    protected void render(Batch batch, int viewEntity, int roomEntity,
            int startX, int startY, int endX, int endY) {
        Room room = mRoom.get(roomEntity);

        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
                int floorEntity = room.floor.get(i, j);
                FloorTexture floorTexture = mFloorTexture.get(floorEntity);
                if (floorTexture != null)
                    batch.draw(floorTexture.texture, i, j, 1f, 1f);
            }
        }
    }
}
