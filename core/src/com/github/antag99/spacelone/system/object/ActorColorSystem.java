package com.github.antag99.spacelone.system.object;

import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.spacelone.component.object.Acting;
import com.github.antag99.spacelone.component.object.Colored;

public final class ActorColorSystem extends EntityProcessorSystem {
    private Mapper<Acting> mActing;
    private Mapper<Colored> mColored;

    public ActorColorSystem() {
        super(Family.with(Acting.class, Colored.class));
    }

    @Override
    protected void process(int entity) {
        Acting acting = mActing.get(entity);
        Colored colored = mColored.get(entity);

        acting.actor.setColor(colored.color);
    }
}
