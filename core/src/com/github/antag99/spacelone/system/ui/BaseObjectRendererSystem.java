package com.github.antag99.spacelone.system.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.github.antag99.retinazer.EntitySet;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.FamilyConfig;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.retinazer.util.Mask;
import com.github.antag99.spacelone.system.RoomSystem;

public abstract class BaseObjectRendererSystem extends BaseRendererSystem {
    private RoomSystem roomSystem;

    private @SkipWire FamilyConfig objectFamilyConfig;
    private @SkipWire Family objectFamily;
    private @SkipWire float maxObjectWidth;
    private @SkipWire float maxObjectHeight;
    private @SkipWire Mask tmpMask = new Mask();

    /**
     * @param objectFamily
     *            the family of objects to render.
     * @param maxObjectWidth
     *            maximum width of an object to be rendered, used as view edge margin.
     * @param maxObjectHeight
     *            maximum height of an object to be rendered, used as view edge margin.
     */
    public BaseObjectRendererSystem(FamilyConfig objectFamily,
            float maxObjectWidth, float maxObjectHeight) {
        this.objectFamilyConfig = objectFamily;
        this.maxObjectWidth = maxObjectWidth;
        this.maxObjectHeight = maxObjectHeight;
    }

    @Override
    public void setup() {
        objectFamily = engine.getFamily(objectFamilyConfig);

        super.setup();
    }

    @Override
    protected final void render(Batch batch, int viewEntity, int roomEntity,
            int startX, int startY, int endX, int endY) {
        EntitySet objects = roomSystem.getEntities(roomEntity,
                startX - maxObjectWidth, startY - maxObjectHeight,
                endX - startX + maxObjectWidth * 2, endY - startY + maxObjectHeight * 2);
        tmpMask.set(objects.getMask());
        tmpMask.and(objectFamily.getEntities().getMask());
        objects.edit().clear();
        objects.edit().addEntities(tmpMask);

        renderObjects(batch, viewEntity, objects);
    }

    protected void renderObjects(Batch batch, int viewEntity, EntitySet objects) {
        for (int i = 0, n = objects.size(), items[] = objects.getIndices().items; i < n; i++) {
            renderObject(batch, viewEntity, items[i]);
        }
    }

    protected abstract void renderObject(Batch batch, int viewEntity, int objectEntity);
}
