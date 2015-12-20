package com.github.antag99.spacelone.system.ui;

import static com.badlogic.gdx.math.MathUtils.ceil;
import static com.badlogic.gdx.math.MathUtils.floor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.spacelone.component.Room;
import com.github.antag99.spacelone.component.object.Location;
import com.github.antag99.spacelone.component.type.FloorTexture;
import com.github.antag99.spacelone.component.ui.Renderer;
import com.github.antag99.spacelone.component.ui.View;
import com.github.antag99.spacelone.system.AssetSystem;
import com.github.antag99.spacelone.system.IdSystem;

public final class RendererSystem extends EntityProcessorSystem {
    private AssetSystem assetSystem;
    private IdSystem idSystem;
    private @SkipWire TextureRegion groundTexture;
    private Mapper<Location> mLocation;
    private Mapper<Renderer> mRenderer;
    private Mapper<View> mView;
    private Mapper<FloorTexture> mFloorTexture;
    private Mapper<Room> mRoom;

    public RendererSystem() {
        super(Family.with(Renderer.class, View.class));
    }

    @Override
    protected void initialize() {
        groundTexture = assetSystem.skin.getRegion("images/ground");
    }

    @Override
    protected void process(int entity) {
        Batch batch = mRenderer.get(entity).batch;
        OrthographicCamera camera = mView.get(entity).camera;

        batch.setProjectionMatrix(camera.combined);
        batch.setColor(Color.WHITE);
        batch.begin();

        int world = mLocation.get(entity).room;
        Room room = mRoom.get(world);
        int startX = floor(camera.position.x - camera.viewportWidth * 0.5f * camera.zoom);
        int startY = floor(camera.position.y - camera.viewportHeight * 0.5f * camera.zoom);
        int endX = ceil(camera.position.x + camera.viewportWidth * 0.5f * camera.zoom);
        int endY = ceil(camera.position.y + camera.viewportHeight * 0.5f * camera.zoom);

        if (startX < 0)
            startX = 0;
        if (startY < 0)
            startY = 0;
        if (endX > room.floor.getWidth())
            endX = room.floor.getWidth();
        if (endY > room.floor.getHeight())
            endY = room.floor.getHeight();
        if (startX > endX)
            startX = endX;
        if (startY > endY)
            startY = endY;

        int ground = idSystem.getEntity("ground");

        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
                int terrain = room.terrain.get(i, j);
                if (terrain == ground)
                    batch.draw(groundTexture, i, j, 1f, 1f);

                int floor = room.floor.get(i, j);
                FloorTexture floorTexture = mFloorTexture.get(floor);
                if (floorTexture != null)
                    batch.draw(floorTexture.texture, i, j, 1f, 1f);
            }
        }

        batch.end();
    }
}
