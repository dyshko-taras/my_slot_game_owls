package com.trafficx.owls.libgdx.actors;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

public class Reel extends Group {
    public Array<Image> elements = new Array<>();
    private float elementHeight;
    private float reelSpeed;
    private int elementsPerSlotHeight;
    private float x;
    private float y;
    private float width;
    private int currentPosition;

    public Reel(int elementsPerSlotHeight, float x, float y, float width, float height, Array<Image> elementImages, float speed) {
        this.elementsPerSlotHeight = elementsPerSlotHeight;
        this.x = x;
        this.y = y;
        this.width = width;
        this.reelSpeed = speed;

        elementHeight = height / elementsPerSlotHeight;

        elements.addAll(elementImages);

        for (Image element : elements) {
            this.addActor(element);
        }
        shuffleAndArrangeElements();
        setBounds(x, y, width, height);
    }

    public void spin() {
        setPosition(x, y);
//        float totalDistance = elementHeight * (elements.size - elementsPerSlotHeight);
        float totalDistance = elementHeight * (currentPosition - elementsPerSlotHeight);
        float duration = totalDistance / reelSpeed;
        addAction(Actions.moveBy(0, -totalDistance, duration));
        shuffleAndArrangeElements();
    }

//    private void shuffleAndArrangeElements() {
//        elements.shuffle();
//        for (int i = 0; i < elements.size; i++) {
//            Image element = elements.get(i);
//            element.setPosition((width - element.getWidth()) / 2, i * elementHeight);
//        }
//    }

    private void shuffleAndArrangeElements() {
        elements.shuffle();
        currentPosition = 0;
        for (int i = 0; i < elements.size; i++) {
            elements.get(i).setPosition((width - elements.get(i).getWidth()) / 2, currentPosition * elementHeight);
            if (isLongImage(i)) {
                currentPosition += 3;
            } else {
                currentPosition++;
            }
        }
    }

    public boolean isLongImage(int i) {
        return elements.get(i).toString().equals("Image: s_12") ||
                elements.get(i).toString().equals("Image: s_13") ||
                elements.get(i).toString().equals("Image: s_14") ||
                elements.get(i).toString().equals("Image: s_15");
    }

    public Array<Image> getElements() {
        return elements;
    }
}
