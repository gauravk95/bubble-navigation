package com.gauravk.bubblenavigation;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

@SuppressWarnings("unused")
class BubbleToggleItem {

    private Drawable icon;
    private Drawable shape;
    private String title = "";
    private int colorActive = Color.BLUE;
    private int colorInactive = Color.BLACK;

    private float titleSize;

    BubbleToggleItem() {
    }

    Drawable getIcon() {
        return icon;
    }

    void setIcon(Drawable icon) {
        this.icon = icon;
    }

    Drawable getShape() {
        return shape;
    }

    void setShape(Drawable shape) {
        this.shape = shape;
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    int getColorActive() {
        return colorActive;
    }

    void setColorActive(int colorActive) {
        this.colorActive = colorActive;
    }

    public int getColorInactive() {
        return colorInactive;
    }

    public void setColorInactive(int colorInactive) {
        this.colorInactive = colorInactive;
    }

    public float getTitleSize() {
        return titleSize;
    }

    public void setTitleSize(float titleSize) {
        this.titleSize = titleSize;
    }

}
