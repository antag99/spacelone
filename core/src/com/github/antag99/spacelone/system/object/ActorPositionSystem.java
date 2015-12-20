package com.github.antag99.spacelone.system.object;

import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.spacelone.component.object.Acting;
import com.github.antag99.spacelone.component.object.Position;

public final class ActorPositionSystem extends EntityProcessorSystem {
    private Mapper<Acting> mActing;
    private Mapper<Position> mPosition;

    public ActorPositionSystem() {
        super(Family.with(Acting.class, Position.class));
    }

    @Override
    protected void process(int entity) {
        Acting acting = mActing.get(entity);
        Position position = mPosition.get(entity);

        acting.actor.setPosition(position.x, position.y);
    }
}
