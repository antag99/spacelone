package com.github.antag99.spacelone.util;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Viewport that scales the world size to a fixed area, preserving the aspect
 * ratio. Note that this may result in insane world bounds when the window is
 * resized, so it works best for mobile games that have fixed bounds.
 */
public class AreaViewport extends Viewport {
    private float pixelsPerUnit = 1f;
    private float worldArea = 800f * 600f;

    public AreaViewport() {
        this(new OrthographicCamera());
    }

    public AreaViewport(Camera camera) {
        setCamera(camera);
    }

    @Override
    public void update(int screenWidth, int screenHeight, boolean centerCamera) {
        float scale = (float) Math.sqrt(worldArea / (screenWidth * screenHeight));
        setScreenBounds(0, 0, screenWidth, screenHeight);
        setWorldSize(screenWidth * (1f / pixelsPerUnit) * scale,
                screenHeight * (1f / pixelsPerUnit) * scale);
        apply(centerCamera);
    }

    /**
     * @return pixels per world unit; defaults to {@code 1f}.
     */
    public float getPixelsPerUnit() {
        return pixelsPerUnit;
    }

    /**
     * @param pixelsPerUnit
     *            pixels per world unit; defaults to {@code 1f}.
     * @return {@code this} for chaining calls.
     */
    public AreaViewport setPixelsPerUnit(float pixelsPerUnit) {
        this.pixelsPerUnit = pixelsPerUnit;
        return this;
    }

    /**
     * @return area of the world after scaling; defaults to {@code 800 * 600}.
     */
    public float getWorldArea() {
        return worldArea;
    }

    /**
     * @param worldArea
     *            area of the world after scaling; defaults to {@code 800 * 600}.
     * @return {@code this} for chaining calls.
     */
    public AreaViewport setWorldArea(float worldArea) {
        this.worldArea = worldArea;
        return this;
    }
}
