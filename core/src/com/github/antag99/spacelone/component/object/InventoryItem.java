package com.github.antag99.spacelone.component.object;

import com.github.antag99.retinazer.Component;
import com.github.antag99.spacelone.util.SkipSerialization;

@SkipSerialization
public final class InventoryItem implements Component {

    /**
     * Inventory of this item.
     */
    public int inventory = -1;
}
