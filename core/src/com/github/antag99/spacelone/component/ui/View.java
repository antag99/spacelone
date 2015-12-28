package com.github.antag99.spacelone.component.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.github.antag99.retinazer.Component;
import com.github.antag99.spacelone.util.SkipSerialization;

@SkipSerialization
public final class View implements Component {
    public OrthographicCamera camera;
}
