package com.github.antag99.spacelone.component.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.github.antag99.retinazer.Component;

public final class Fade implements Component {
    public Color color = new Color();
    public float duration = 0f;
    public Interpolation interpolation = Interpolation.linear;
    public float counter = 0f;
    public Color init = new Color();

    public Fade color(Color color) {
        this.color.set(color);
        return this;
    }

    public Fade color(float r, float g, float b) {
        this.color.set(r, g, b, 1f);
        return this;
    }

    public Fade color(float r, float g, float b, float a) {
        this.color.set(r, g, b, a);
        return this;
    }

    public Fade duration(float duration) {
        this.duration = duration;
        return this;
    }

    public Fade interpolation(Interpolation interpolation) {
        this.interpolation = interpolation;
        return this;
    }
}
