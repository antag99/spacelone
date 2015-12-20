package com.github.antag99.spacelone.system.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.spacelone.component.object.Control;

public final class ControlSystem extends EntityProcessorSystem {
    private Mapper<Control> mControl;

    public ControlSystem() {
        super(Family.with(Control.class));
    }

    @Override
    protected void process(int entity) {
        Control control = mControl.get(entity);
        control.wasMoveLeft = control.moveLeft;
        control.wasMoveRight = control.moveRight;
        control.wasMoveUp = control.moveUp;
        control.wasMoveDown = control.moveDown;
        control.moveLeft = Gdx.input.isKeyPressed(Keys.A);
        control.moveRight = Gdx.input.isKeyPressed(Keys.D);
        control.moveUp = Gdx.input.isKeyPressed(Keys.W);
        control.moveDown = Gdx.input.isKeyPressed(Keys.S);
    }
}
