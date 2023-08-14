package com.trafficx.owls.libgdx.screens;

import android.content.Intent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.trafficx.owls.WebViewActivity;
import com.trafficx.owls.libgdx.MainLibgdx;
import com.trafficx.owls.libgdx.actors.Reel;
import com.trafficx.owls.libgdx.utils.LabelNum;

public class GameScreen implements Screen {

    public static final float SCREEN_WIDTH = MainLibgdx.SCREEN_WIDTH;
    public static final float SCREEN_HEIGHT = MainLibgdx.SCREEN_HEIGHT;

    //Viewport
    private final MainLibgdx main;
    private Viewport viewportUI;
    private Viewport viewportSlot;
    private Skin skin;
    private Stage stageUI;
    private Stage stageSlot;
    private Table mainTable;
    private Table table;

    //Table
    private Image infoButton;
    private Image minusButton;
    private Label labelBet;
    private Image plusButton;
    private Image maxBetButton;
    private Image spinButton;
    private Image autoGameButton;
    private Image stopAutoGameButton;
    private Label labelBalance;

    //Game
    private static final int MAX_BET = 300;
    private static final int MIN_BET = 1;
    private int timesWon = 0;
    private Array<Reel> reels = new Array<Reel>();
    private int elementsInSlotWidth = 5;
    private int elementsPerSlotHeight = 3;
    private float reelWidth = 208;
    private float reelHeight = 578;
    private float reelX = 340;
    private float reelY = 226;
    private float gapX = 50;
    private float reelSpeed = 4000;
    private float slotWidth = reelWidth * elementsInSlotWidth + gapX * (elementsInSlotWidth - 1);
    private float slotHeight = reelHeight;
    private int slotX = (int) reelX;
    private int slotY = (int) reelY;
    private float delayBetweenSpins = 0.2f;
    private Timer autoGameTimer;
    private int completedTasksCount = 0;


    public GameScreen(MainLibgdx main) {
        this.main = main;
    }

    public void show() {
        showCameraAndStage();

        skin = new Skin(Gdx.files.internal("skin.json"));


        mainTable = new Table();
        mainTable.setBackground(skin.getDrawable("back"));
        mainTable.setFillParent(true);

        table = new Table();
        table.align(Align.bottomLeft);

        infoButton = new Image(skin, "b_info_button");
        infoButton.setScaling(Scaling.fit);
        table.add(infoButton).padLeft(33.0f).padBottom(39.0f).align(Align.bottomLeft);

        minusButton = new Image(skin, "b_minus_button");
        minusButton.setScaling(Scaling.fit);
        table.add(minusButton).padLeft(340.0f).padBottom(33.0f).align(Align.bottomLeft);

        labelBet = new Label("1.00", skin);
        labelBet.setAlignment(Align.center);
        table.add(labelBet).padBottom(25.0f).align(Align.bottom).minWidth(228.0f);

        plusButton = new Image(skin, "b_plus_button");
        plusButton.setScaling(Scaling.fit);
        table.add(plusButton).padBottom(33.0f).align(Align.bottomLeft);

        maxBetButton = new Image(skin, "b_max_bet_button");
        maxBetButton.setScaling(Scaling.fit);
        table.add(maxBetButton).padLeft(42.0f).padBottom(13.0f).align(Align.bottomLeft);

        spinButton = new Image(skin, "b_spin_button");
        spinButton.setScaling(Scaling.fit);
        table.add(spinButton).padLeft(-10.0f).padBottom(9.0f).align(Align.bottomLeft);


        Stack stack = new Stack();
        stopAutoGameButton = new Image(skin, "b_stop_auto_game_button");
        stopAutoGameButton.setVisible(false);
        stopAutoGameButton.setScaling(Scaling.fit);
        stack.addActor(stopAutoGameButton);

        autoGameButton = new Image(skin, "b_auto_game_button");
        autoGameButton.setScaling(Scaling.fit);
        stack.addActor(autoGameButton);
        table.add(stack).padLeft(-11.0f).padRight(33.0f).padBottom(13.0f).align(Align.bottomLeft);

        labelBalance = new Label("301.00", skin);
        labelBalance.setAlignment(Align.center);
        table.add(labelBalance).padBottom(25.0f).align(Align.bottom).minWidth(340.0f);
        mainTable.add(table).minWidth(1920.0f).minHeight(1080.0f);
        stageUI.addActor(mainTable);

        setClickListeners();
        createReels();

    }

    private void setClickListeners() {
        minusButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                LabelNum.subtractNum(labelBet, MIN_BET);
            }
        });

        plusButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (LabelNum.getNum(labelBet) < LabelNum.getNum(labelBalance)) {
                    LabelNum.addNum(labelBet, MIN_BET);
                }
            }
        });

        maxBetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (LabelNum.getNum(labelBet) < LabelNum.getNum(labelBalance)) {
                    LabelNum.setNum(labelBet, Math.min(LabelNum.getNum(labelBalance), MAX_BET));
                }
            }
        });

        spinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                spinButton.setTouchable(Touchable.disabled);
                startSpinning();
            }
        });

        autoGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                autoGameButton.setVisible(false);
                stopAutoGameButton.setVisible(true);

                autoGameTimer = new Timer();
                autoGameTimer.scheduleTask(new Timer.Task() {
                    @Override
                    public void run() {
                        startSpinning();
                    }
                }, 1, 2.5f);
            }
        });

        stopAutoGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                autoGameButton.setVisible(true);
                stopAutoGameButton.setVisible(false);

                if (autoGameTimer != null) {
                    autoGameTimer.clear();
                    autoGameTimer = null;
                }
            }
        });

        infoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Intent intent = new Intent(main.androidApplication, WebViewActivity.class);
                main.androidApplication.startActivity(intent);
            }
        });
    }


    public void render(float delta) {
        renderCamera();
    }

    public void resize(int width, int height) {
        resizeCamera(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    public void dispose() {
        stageUI.dispose();
        skin.dispose();
    }

    /////Camera
    private void showCameraAndStage() {
        viewportUI = new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT);
        stageUI = new Stage(viewportUI);
        Gdx.input.setInputProcessor(stageUI);

        viewportSlot = new StretchViewport(slotWidth, slotHeight);
        viewportSlot.setScreenPosition(slotX, slotY);
        stageSlot = new Stage(viewportSlot);
    }

    private void renderCamera() {
        ScreenUtils.clear(Color.BLACK);
        viewportUI.apply();
        stageUI.act();
        stageUI.draw();

        viewportSlot.apply();
        stageSlot.act();
        stageSlot.draw();
    }

    private void resizeCamera(int width, int height) {
        viewportUI.update(width, height, true);

        viewportSlot.update((int) (slotWidth * (width / SCREEN_WIDTH)), (int) (slotHeight * (height / SCREEN_HEIGHT)), true);
        viewportSlot.setScreenPosition((int) (slotX * (width / SCREEN_WIDTH)), (int) (slotY * (height / SCREEN_HEIGHT)));
    }
    ////////

    private Array<Image> generateSlotImages() {
        Array<Image> slotIcons = new Array<Image>();
        for (int i = 1; i <= 15; i++) {
            Image image = new Image(skin, "s_" + i);
            image.setScaling(Scaling.fit);
            slotIcons.add(image);
        }
        return slotIcons;
    }

    private void createReels() {
        for (int i = 0; i < elementsInSlotWidth; i++) {
            Reel reel = new Reel(elementsPerSlotHeight, i * (reelWidth + gapX), 0, reelWidth, reelHeight, generateSlotImages(), reelSpeed);
            reels.add(reel);
            stageSlot.addActor(reel);
        }
    }

    private void startSpinning() {
        minusBalance();
//        for (Reel reel : reels) {
//            reel.spin();
//        }
        for (int i = 0; i < reels.size; i++) {
            int finalIndex = i;
            float delay = i * delayBetweenSpins;

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    reels.get(finalIndex).spin();
                    checkForWin(reels.get(finalIndex));
                    addBalance();
                    LabelNum.setNum(labelBet, Math.min(LabelNum.getNum(labelBalance), LabelNum.getNum(labelBet)));
                    completedTasksCount++;
                    if (completedTasksCount == reels.size) {
                        completedTasksCount = 0;
                        spinButton.setTouchable(Touchable.enabled);
                    }
                }
            }, delay);
        }
    }

    private void checkForWin(Reel reel) {

        if (reel.isLongImage(reel.elements.size - 1)) {
            timesWon++;
            System.out.println("You won!");
        } else {
            System.out.println("You lost!");
        }
    }

    private void minusBalance() {
        LabelNum.subtractNum(labelBalance, LabelNum.getNum(labelBet));
    }

    private void addBalance() {
        LabelNum.addNum(labelBalance, LabelNum.getNum(labelBet) * timesWon);
        System.out.println("Profit: " + LabelNum.getNum(labelBet) * timesWon);
        timesWon = 0;
    }

}
