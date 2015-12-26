package com.github.antag99.spacelone.system.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.spacelone.component.object.Player;
import com.github.antag99.spacelone.component.object.Position;
import com.github.antag99.spacelone.component.object.Size;
import com.github.antag99.spacelone.system.AssetSystem;

public final class PlayerRendererSystem extends BaseObjectRendererSystem {
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;

    private AssetSystem assetSystem;
    private @SkipWire TextureRegion playerTexture;

    public PlayerRendererSystem() {
        super(Family.with(Player.class), 0f, 0f);
    }

    @Override
    protected void initialize() {
        playerTexture = assetSystem.skin.getRegion("images/white");
    }

    @Override
    protected void renderObject(Batch batch, int viewEntity, int objectEntity) {
        Position position = mPosition.get(objectEntity);
        Size size = mSize.get(objectEntity);

        batch.setColor(Color.FOREST);
        batch.draw(playerTexture, position.x, position.y, size.width, size.height);
    }
}
