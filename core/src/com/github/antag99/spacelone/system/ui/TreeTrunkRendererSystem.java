package com.github.antag99.spacelone.system.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.spacelone.component.object.Position;
import com.github.antag99.spacelone.component.object.Size;
import com.github.antag99.spacelone.component.object.Tree;
import com.github.antag99.spacelone.system.AssetSystem;

public final class TreeTrunkRendererSystem extends BaseObjectRendererSystem {
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;

    private AssetSystem assetSystem;
    private @SkipWire TextureRegion trunkTexture;

    public TreeTrunkRendererSystem() {
        super(Family.with(Tree.class), 5f, 5f);
    }

    @Override
    protected void initialize() {
        trunkTexture = assetSystem.skin.getRegion("images/trunk");
    }

    @Override
    protected void renderObject(Batch batch, int viewEntity, int objectEntity) {
        Position position = mPosition.get(objectEntity);
        Size size = mSize.get(objectEntity);
        batch.setColor(Color.BROWN);
        batch.draw(trunkTexture,
                position.x - size.width * 0.5f,
                position.y - size.height * 0.5f,
                size.width, size.height);
    }
}
