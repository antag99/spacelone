package com.github.antag99.spacelone.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public final class ItemSlot extends Widget {
    private ItemSlotStyle style;
    private TextureRegion itemTexture;

    public ItemSlot(ItemSlotStyle style) {
        setStyle(style);
        setTouchable(Touchable.enabled);
    }

    public ItemSlot(Skin skin) {
        this(skin.get(ItemSlotStyle.class));
    }

    public ItemSlot(Skin skin, String name) {
        this(skin.get(name, ItemSlotStyle.class));
    }

    public ItemSlotStyle getStyle() {
        return style;
    }

    public void setStyle(ItemSlotStyle style) {
        this.style = style;
    }

    public void setItemTexture(TextureRegion itemTexture) {
        this.itemTexture = itemTexture;
    }

    public TextureRegion getItemTexture() {
        return itemTexture;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        float x = getX(), y = getY(), w = getWidth(), h = getHeight();
        batch.setColor(Color.WHITE);
        style.background.draw(batch, x, y, w, h);
        if (itemTexture != null) {
            batch.setColor(Color.WHITE);
            batch.draw(itemTexture,
                    x + style.background.getLeftWidth(),
                    y + style.background.getBottomHeight(),
                    w - style.background.getLeftWidth() - style.background.getRightWidth(),
                    y - style.background.getBottomHeight() - style.background.getTopHeight());
        }
    }

    public static final class ItemSlotStyle {
        public Drawable background;
    }
}
