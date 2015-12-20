package com.github.antag99.spacelone.system.object;

import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.spacelone.component.object.Control;
import com.github.antag99.spacelone.component.object.Movement;
import com.github.antag99.spacelone.component.object.Velocity;

public final class MovementSystem extends EntityProcessorSystem {
    private Mapper<Control> mControl;
    private Mapper<Velocity> mVelocity;
    private Mapper<Movement> mMovement;

    public MovementSystem() {
        super(Family.with(Control.class, Velocity.class, Movement.class));
    }

    @Override
    protected void process(int entity) {
        Control control = mControl.get(entity);
        Velocity velocity = mVelocity.get(entity);
        Movement movement = mMovement.get(entity);

        boolean moveLeft = control.moveLeft && !control.moveRight;
        boolean moveRight = control.moveRight && !control.moveLeft;
        boolean moveUp = control.moveUp && !control.moveDown;
        boolean moveDown = control.moveDown && !control.moveUp;
        float speed = movement.speed;

        if (moveLeft)
            velocity.x = -speed;
        else if (moveRight)
            velocity.x = speed;
        else
            velocity.x = 0f;

        if (moveDown)
            velocity.y = -speed;
        else if (moveUp)
            velocity.y = speed;
        else
            velocity.y = 0f;
    }
}
