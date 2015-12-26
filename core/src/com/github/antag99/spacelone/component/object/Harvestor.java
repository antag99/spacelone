package com.github.antag99.spacelone.component.object;

import com.github.antag99.retinazer.Component;

public final class Harvestor implements Component {
    /** whether the entity is currently harvesting */
    public boolean active;
    /** entity that is currently being harvested */
    public int target;
    /** counter for haversting the current entity */
    public float counter;
}
