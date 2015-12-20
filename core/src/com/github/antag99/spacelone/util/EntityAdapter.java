package com.github.antag99.spacelone.util;

import com.github.antag99.retinazer.EntityListener;
import com.github.antag99.retinazer.EntitySet;

public abstract class EntityAdapter implements EntityListener {
    @Override
    public final void inserted(EntitySet entities) {
        int[] items = entities.getIndices().items;
        for (int i = 0, n = entities.size(); i < n; i++) {
            inserted(items[i]);
        }
    }

    @Override
    public final void removed(EntitySet entities) {
        int[] items = entities.getIndices().items;
        for (int i = 0, n = entities.size(); i < n; i++) {
            removed(items[i]);
        }
    }

    protected void inserted(int entity) {
    }

    protected void removed(int entity) {
    }
}
