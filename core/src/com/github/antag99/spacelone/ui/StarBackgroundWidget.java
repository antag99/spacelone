package com.github.antag99.spacelone.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public final class StarBackgroundWidget extends Widget {
    private StarBackground starBackground;

    public StarBackgroundWidget(long seed, float density, float starWidth, float starHeight, TextureRegion starTexture) {
        this.starBackground = new StarBackground(seed, density, starWidth, starHeight, starTexture);
    }

    @Override
    public void act(float delta) {
        starBackground.update(delta);

        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (clipBegin()) {
            starBackground.draw(batch, getX(), getY(), getWidth(), getHeight());
            clipEnd();
        }
    }
}
