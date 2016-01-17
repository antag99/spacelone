package com.github.antag99.spacelone.system.object;

import com.github.antag99.retinazer.EntitySet;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.spacelone.component.object.Inventory;
import com.github.antag99.spacelone.component.object.InventoryItem;
import com.github.antag99.spacelone.util.EntityAdapter;

public final class InventorySystem extends EntitySystem {
    private Mapper<InventoryItem> mInventoryItem;
    private Mapper<Inventory> mInventory;

    @Override
    protected void initialize() {
        engine.getFamily(Family.with(Inventory.class)).addListener(new EntityAdapter() {
            @Override
            protected void removed(int entity) {
                EntitySet items = mInventory.get(entity).items;
                for (int i = 0, n = items.size(), v[] = items.getIndices().items; i < n; i++) {
                    mInventoryItem.get(v[i]).owner = -1;
                    engine.destroyEntity(v[i]);
                }
            }
        });

        engine.getFamily(Family.with(InventoryItem.class)).addListener(new EntityAdapter() {
            @Override
            protected void inserted(int entity) {
                int inventoryEntity = mInventoryItem.get(entity).owner;
                if (inventoryEntity == -1)
                    throw new RuntimeException("Cannot add an inventory item without an inventory");
                mInventory.get(inventoryEntity).items.edit().addEntity(entity);
            }

            @Override
            protected void removed(int entity) {
                int inventoryEntity = mInventoryItem.get(entity).owner;
                if (inventoryEntity != -1)
                    mInventory.get(inventoryEntity).items.edit().removeEntity(entity);
            }
        });
    }
}
