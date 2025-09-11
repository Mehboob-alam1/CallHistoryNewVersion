package com.easyranktools.easyranktools.item;


public class ItemStyle {
    private boolean free;
    private int image;

    public ItemStyle(int i, boolean z) {
        this.image = i;
        this.free = z;
    }

    public int getImage() {
        return this.image;
    }

    public boolean isFree() {
        return this.free;
    }
}
