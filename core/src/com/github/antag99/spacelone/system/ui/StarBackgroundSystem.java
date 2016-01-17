package com.github.antag99.spacelone.system.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.spacelone.system.AssetSystem;
import com.github.antag99.spacelone.system.DeltaSystem;
import com.github.antag99.spacelone.ui.StarBackground;

public final class StarBackgroundSystem extends BaseRendererSystem {
    private AssetSystem assetSystem;
    private DeltaSystem deltaSystem;
    private @SkipWire StarBackground starBackground;

    @Override
    protected void initialize() {
        starBackground = new StarBackground(0L, 0.8f, 0.5f, 0.5f,
                assetSystem.skin.getRegion("images/star"));
    }

    @Override
    protected void render(Batch batch, int viewEntity, int roomEntity,
            int startX, int startY, int endX, int endY) {
        starBackground.update(deltaSystem.getDeltaTime());
        starBackground.draw(batch,
                startX, startY,
                endX - startX,
                endY - startY);
    }
}
