package com.github.antag99.spacelone.component.object;

import com.github.antag99.retinazer.Component;
import com.github.antag99.spacelone.util.SkipSerialization;

/**
 * This component is added to objects that are harvested; note that you must
 * use the {@link RoomObject} component to get properties of the harvested
 * object via it's type.
 */
@SkipSerialization
public final class Harvested implements Component {
    public int harvestor = -1;
}
