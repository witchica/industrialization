package com.witchica.industrialization.client.screen;

public enum EnergyGeneratorIcon {
    SUN(0, 0),
    RAIN(1, 0),
    MOON(0, 1),
    BLOCKED(1, 1);
    int uvX;
    int uvY;
    EnergyGeneratorIcon(int uvX, int uvY) {
        this.uvX = uvX;
        this.uvY = uvY;
    }
}
