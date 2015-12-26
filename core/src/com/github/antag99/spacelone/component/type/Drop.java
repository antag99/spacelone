package com.github.antag99.spacelone.component.type;

import com.github.antag99.retinazer.Component;

public final class Drop implements Component {
    public String type;
    public int minAmount;
    public int maxAmount;

    public Drop type(String type) {
        this.type = type;
        return this;
    }

    public Drop amount(int minAmount, int maxAmount) {
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        return this;
    }
}
