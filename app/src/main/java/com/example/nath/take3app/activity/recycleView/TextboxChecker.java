package com.example.nath.take3app.activity.recycleView;

/**
 * Created by nath on 24-Oct-17.
 */

public class TextboxChecker {
    String text;
    Integer qty;
    private boolean selected;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
