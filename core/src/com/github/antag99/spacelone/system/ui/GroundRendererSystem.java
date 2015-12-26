package com.github.antag99.spacelone.system.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.spacelone.component.Room;
import com.github.antag99.spacelone.system.AssetSystem;
import com.github.antag99.spacelone.system.IdSystem;

public final class GroundRendererSystem extends BaseRendererSystem {
    private IdSystem idSystem;
    private Mapper<Room> mRoom;

    private AssetSystem assetSystem;
    private @SkipWire TextureRegion groundTexture;

    @Override
    protected void initialize() {
        groundTexture = assetSystem.skin.getRegion("images/ground");
    }

    @Override
    protected void render(Batch batch, int viewEntity, int roomEntity,
            int startX, int startY, int endX, int endY) {
        Room room = mRoom.get(roomEntity);
        int groundEntity = idSystem.getEntity("ground");

        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
                int terrainEntity = room.terrain.get(i, j);
                if (terrainEntity == groundEntity)
                    batch.draw(groundTexture, i, j, 1f, 1f);
            }
        }
    }
}
