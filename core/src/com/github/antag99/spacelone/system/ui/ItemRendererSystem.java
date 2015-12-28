package com.github.antag99.spacelone.system.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.spacelone.component.object.Colored;
import com.github.antag99.spacelone.component.object.Item;
import com.github.antag99.spacelone.component.object.Position;
import com.github.antag99.spacelone.component.object.Size;
import com.github.antag99.spacelone.component.type.ItemTexture;

public final class ItemRendererSystem extends BaseObjectRendererSystem {
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;
    private Mapper<ItemTexture> mItemTexture;
    private Mapper<Colored> mColored;

    public ItemRendererSystem() {
        super(Family.with(Item.class, ItemTexture.class), 5f, 5f);
    }

    @Override
    protected void renderObject(Batch batch, int viewEntity, int objectEntity) {
        Position position = mPosition.get(objectEntity);
        Size size = mSize.get(objectEntity);
        Color color = mColored.get(objectEntity).color;
        ItemTexture itemTexture = mItemTexture.get(objectEntity);
        batch.setColor(color);
        batch.draw(itemTexture.texture, position.x, position.y, size.width, size.height);
    }
}
