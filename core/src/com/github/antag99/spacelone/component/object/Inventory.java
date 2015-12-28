package com.github.antag99.spacelone.component.object;

import com.github.antag99.retinazer.Component;
import com.github.antag99.retinazer.EntitySet;
import com.github.antag99.spacelone.util.SkipSerialization;

@SkipSerialization
public final class Inventory implements Component {

    /**
     * Items of this inventory.
     */
    public EntitySet items = new EntitySet();
}
