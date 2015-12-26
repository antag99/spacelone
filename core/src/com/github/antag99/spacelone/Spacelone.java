package com.github.antag99.spacelone;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.esotericsoftware.minlog.Log;

public final class Spacelone extends Game {
    private Spacelone() {
    }

    public static final Spacelone INSTANCE = new Spacelone();

    public Batch batch;
    public Skin skin;
    public GameScreen gameScreen;
    public MenuScreen menuScreen;
    public FileHandle saveDirectory;

    @Override
    public void create() {
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("skin.json"));

        saveDirectory = Gdx.files.local("save/");

        gameScreen = new GameScreen(this);
        menuScreen = new MenuScreen(this);

        setScreen(menuScreen);
    }

    @Override
    public void dispose() {
        super.dispose();

        batch.dispose();
        gameScreen.dispose();
        menuScreen.dispose();
        skin.dispose();
    }

    public static final String TAG = "blockabout";

    public static final void trace(String format, Object... args) {
        Log.trace(TAG, String.format(format, args));
    }

    public static final void debug(String format, Object... args) {
        Log.debug(TAG, String.format(format, args));
    }

    public static final void info(String format, Object... args) {
        Log.info(TAG, String.format(format, args));
    }

    public static final void warn(String format, Object... args) {
        Log.warn(TAG, String.format(format, args));
    }

    public static final void error(String format, Object... args) {
        Log.error(TAG, String.format(format, args));
    }

    public static final void error(String message, Throwable ex) {
        Log.error(TAG, message, ex);
    }
}
