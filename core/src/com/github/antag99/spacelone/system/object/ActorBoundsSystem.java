package com.github.antag99.spacelone.system.object;

import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.spacelone.component.object.Acting;
import com.github.antag99.spacelone.component.object.Position;
import com.github.antag99.spacelone.component.object.Size;

public final class ActorBoundsSystem extends EntityProcessorSystem {
    private Mapper<Acting> mActing;
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;

    public ActorBoundsSystem() {
        super(Family.with(Acting.class, Position.class, Size.class));
    }

    @Override
    protected void process(int entity) {
        Acting acting = mActing.get(entity);
        Position position = mPosition.get(entity);
        Size size = mSize.get(entity);
        acting.actor.setPosition(position.x - size.width * 0.5f,
                position.y - size.height * 0.5f);
        acting.actor.setSize(size.width, size.height);
        acting.actor.setOrigin(size.width * 0.5f, size.height * 0.5f);
    }
}
