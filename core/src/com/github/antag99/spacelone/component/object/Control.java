package com.github.antag99.spacelone.component.object;

import com.github.antag99.retinazer.Component;
import com.github.antag99.spacelone.util.SkipSerialization;

@SkipSerialization
public final class Control implements Component {
    public boolean moveLeft, wasMoveLeft;
    public boolean moveRight, wasMoveRight;
    public boolean moveUp, wasMoveUp;
    public boolean moveDown, wasMoveDown;
    public boolean harvest, wasHarvest;
}
