package com.github.antag99.spacelone.component.object;

import com.github.antag99.retinazer.Component;

public final class Harvestor implements Component {
    /** whether the entity is currently harvesting */
    public transient boolean active = false;
    /** entity that is currently being harvested */
    public transient int target = -1;
    /** counter for haversting the current entity */
    public transient float counter = 0f;
}
