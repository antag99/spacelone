package com.github.antag99.spacelone.component.object;

import com.badlogic.gdx.graphics.Color;
import com.github.antag99.retinazer.Component;

public final class Colored implements Component {
    public Color color = new Color();

    public Colored color(Color color) {
        this.color.set(color);
        return this;
    }

    public Colored color(float r, float g, float b) {
        this.color.set(r, g, b, 1f);
        return this;
    }

    public Colored color(float r, float g, float b, float a) {
        this.color.set(r, g, b, a);
        return this;
    }
}
