package com.trafficx.owls.libgdx.utils;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class LabelNum {
    public static int getNum(Label label) {
        return Integer.parseInt(String.valueOf(label.getText().substring(0, label.getText().length() - 3)));
    }

    public static void setNum(Label label, int num) {
        if (num < 0) num = 0;
        label.setText(num + ".00");
    }

    public static void subtractNum(Label label, int num) {
        int temp = Integer.parseInt(String.valueOf(label.getText().substring(0, label.getText().length() - 3))) - num;
        if (temp < 0) temp = 0;
        label.setText(temp + ".00");
    }

    public static void addNum(Label label, int num) {
        int temp = Integer.parseInt(String.valueOf(label.getText().substring(0, label.getText().length() - 3))) + num;
        label.setText(temp + ".00");
    }
}
