package com.example.familymap.Client;

import android.graphics.Color;
public class Colors extends Color {
    private float color;

    public Colors(String eventType) {
        color = Math.abs(eventType.hashCode() % 360);
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getColor() {
        return color;
    }
}
