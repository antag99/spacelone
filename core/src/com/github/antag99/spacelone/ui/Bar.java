package com.github.antag99.spacelone.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public final class Bar extends Widget {
    private float value;
    private BarStyle style;

    public Bar(Skin skin) {
        this(skin.get(BarStyle.class));
    }

    public Bar(Skin skin, String name) {
        this(skin.get(name, BarStyle.class));
    }

    public Bar(BarStyle style) {
        setStyle(style);
    }

    public BarStyle getStyle() {
        return style;
    }

    public void setStyle(BarStyle style) {
        if (style == null)
            throw new IllegalArgumentException("style must not be null");

        this.style = style;
    }

    public void setValue(float value) {
        if (value < 0f || value > 1f)
            throw new IllegalArgumentException("value must be between 0 and 1: " + value);
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        float x = getX();
        float y = getY();
        float width = getWidth();
        float height = getHeight();

        float leftPad = style.background.getLeftWidth();
        float rightPad = style.background.getRightWidth();
        float bottomPad = style.background.getBottomHeight();
        float topPad = style.background.getTopHeight();

        float fillWidth = (width - leftPad - rightPad) * value;
        style.background.draw(batch, x, y, width, height);
        style.foreground.draw(batch, x + leftPad, y + bottomPad,
                fillWidth, height - topPad - bottomPad);
    }

    public static final class BarStyle {
        public Drawable background;
        public Drawable foreground;
    }
}
