package com.github.antag99.spacelone.system;

import com.badlogic.gdx.utils.IntArray;
import com.github.antag99.retinazer.Component;
import com.github.antag99.retinazer.EntityListener;
import com.github.antag99.retinazer.EntitySet;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.SkipWire;

public abstract class BaseRefSystem<T extends Component, R extends Component> extends EntitySystem {
    private @SkipWire Mapper<T> mT;
    private @SkipWire Mapper<R> mR;

    private @SkipWire Class<T> t;
    private @SkipWire Class<R> r;

    public BaseRefSystem(Class<T> componentType, Class<R> componentRefType) {
        t = componentType;
        r = componentRefType;
    }

    protected abstract void initialize(T component, R ref);

    @Override
    protected void setup() {
        super.setup();

        mT = engine.getMapper(t);
        mR = engine.getMapper(r);

        engine.getFamily(Family.with(r).exclude(t)).addListener(new EntityListener() {
            @Override
            public void inserted(EntitySet entities) {
                IntArray indices = entities.getIndices();
                for (int i = 0, n = indices.size, items[] = indices.items; i < n; i++) {
                    T component = mT.create(items[i]);
                    R ref = mR.get(items[i]);
                    initialize(component, ref);
                }
            }

            @Override
            public void removed(EntitySet entities) {
            }
        });
    }
}
