package com.trafficx.owls.libgdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.trafficx.owls.libgdx.screens.GameScreen;

public class MainLibgdx extends Game {
    public AndroidApplication androidApplication;

    public static float SCREEN_WIDTH = 1920;
    public static float SCREEN_HEIGHT = 1080;

    public SpriteBatch batch;

    public MainLibgdx(AndroidApplication androidApplication) {
        this.androidApplication = androidApplication;
    }

    public MainLibgdx() {
    }

    public void create() {
        initAssets();
        this.setScreen(new GameScreen(this));
    }

    public void render() {
        super.render();
    }

    public void resize(int width, int height) {
        super.resize(width, height);
    }

    public void dispose() {
        batch.dispose();
    }


    public void initAssets() {
        batch = new SpriteBatch();
    }
}
