package com.github.antag99.spacelone.system;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.github.antag99.retinazer.EntityListener;
import com.github.antag99.retinazer.EntitySet;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.spacelone.component.object.Acting;

public final class ActorSystem extends EntitySystem {
    private Mapper<Acting> mActing;

    @SkipWire
    private Group root;

    public ActorSystem(Group root) {
        this.root = root;
    }

    public Group getRoot() {
        return root;
    }

    @Override
    protected void initialize() {
        engine.getFamily(Family.with(Acting.class)).addListener(new EntityListener() {
            @Override
            public void inserted(EntitySet entities) {
                int[] items = entities.getIndices().items;
                for (int i = 0, n = entities.size(); i < n; i++) {
                    root.addActor(mActing.get(items[i]).actor);
                }
            }

            @Override
            public void removed(EntitySet entities) {
                int[] items = entities.getIndices().items;
                for (int i = 0, n = entities.size(); i < n; i++) {
                    root.removeActor(mActing.get(items[i]).actor);
                }
            }
        });
    }
}
