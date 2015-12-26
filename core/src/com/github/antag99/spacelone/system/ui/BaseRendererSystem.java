package com.github.antag99.spacelone.system.ui;

import static com.badlogic.gdx.math.MathUtils.ceil;
import static com.badlogic.gdx.math.MathUtils.floor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.spacelone.component.Room;
import com.github.antag99.spacelone.component.object.Location;
import com.github.antag99.spacelone.component.ui.Renderer;
import com.github.antag99.spacelone.component.ui.View;

public abstract class BaseRendererSystem extends EntityProcessorSystem {
    private Mapper<Renderer> mRenderer;
    private Mapper<View> mView;
    private Mapper<Location> mLocation;
    private Mapper<Room> mRoom;

    public BaseRendererSystem() {
        super(Family.with(Renderer.class, View.class));
    }

    @Override
    protected final void process(int entity) {
        renderView(entity);
    }

    protected void renderView(int viewEntity) {
        Batch batch = mRenderer.get(viewEntity).batch;
        OrthographicCamera camera = mView.get(viewEntity).camera;

        int roomEntity = mLocation.get(viewEntity).room;
        Room room = mRoom.get(roomEntity);

        int startX = floor(camera.position.x - camera.viewportWidth * 0.5f * camera.zoom);
        int startY = floor(camera.position.y - camera.viewportHeight * 0.5f * camera.zoom);
        int endX = ceil(camera.position.x + camera.viewportWidth * 0.5f * camera.zoom);
        int endY = ceil(camera.position.y + camera.viewportHeight * 0.5f * camera.zoom);

        if (startX < 0)
            startX = 0;
        if (startY < 0)
            startY = 0;

        if (endX > room.width)
            endX = room.width;
        if (endY > room.height)
            endY = room.height;

        batch.setProjectionMatrix(camera.combined);
        batch.setColor(Color.WHITE);
        batch.begin();
        render(batch, viewEntity, roomEntity, startX, startY, endX, endY);
        batch.end();
    }

    /**
     * Renders the visible parts of a room.
     *
     * @param batch
     *            the batch to use for rendering.
     * @param viewEntity
     *            the entity that is seeing these parts of the room.
     * @param roomEntity
     *            the room.
     * @param startX
     *            the first visible grid position.
     * @param startY
     *            the first visible grid position.
     * @param endX
     *            the last visible grid position + 1.
     * @param endY
     *            the last visible grid position + 1.
     */
    protected abstract void render(Batch batch, int viewEntity, int roomEntity,
            int startX, int startY, int endX, int endY);
}
