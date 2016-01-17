package com.github.antag99.spacelone.system.object;

import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.spacelone.component.object.Hotbar;
import com.github.antag99.spacelone.component.object.InventoryItem;
import com.github.antag99.spacelone.component.object.Location;
import com.github.antag99.spacelone.component.object.Position;
import com.github.antag99.spacelone.component.type.Harvestable;

public final class HotbarSystem extends EntitySystem {
    private Mapper<Location> mLocation;
    private Mapper<Hotbar> mHotbar;
    private Mapper<InventoryItem> mInventoryItem;
    private Mapper<Position> mPosition;
    private Mapper<Harvestable> mHarvestable;

}
