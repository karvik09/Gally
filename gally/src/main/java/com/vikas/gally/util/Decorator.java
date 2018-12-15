package com.vikas.gally.util;

import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.StyleRes;

public class Decorator {
    private String title;
    private int maxSelection;
    private int tickColor;

    private Decorator(Builder builder) {
        this.title = builder.title;
        this.maxSelection = builder.maxSelection;
        this.tickColor = builder.tickColor;
    }

    private Decorator() {
    }


    @ColorRes
    public int getTickColor() {
        return tickColor;
    }

    public void setTickColor(@ColorRes int tickColor) {
        this.tickColor = tickColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMaxSelection() {
        return maxSelection;
    }

    public void setMaxSelection(int maxSelection) {
        this.maxSelection = maxSelection;
    }

    public static class Builder{
        private String title;
        private int maxSelection;
        private int tickColor;


        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }
        public Builder setMaxSelection(int maxSelection){
            this.maxSelection = maxSelection;
            return this;
        }

        public Builder setTickColor(@ColorRes int tickColor) {
            this.tickColor = tickColor;
            return this;
        }

        public Decorator build() {
            return new Decorator(this);
        }
    }
}
