package com.github.antag99.spacelone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.antag99.spacelone.util.AreaViewport;

public final class MenuScreen extends ScreenAdapter {
    private Spacelone game;
    private Stage stage;
    private AreaViewport viewport;

    private final class Slot extends ClickListener {
        public final FileHandle directory;

        public Slot(int index) {
            this.directory = game.saveDirectory.child("worlds/" + (index + 1));
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            game.gameScreen.directory = directory;
            game.setScreen(game.gameScreen);
        }
    }

    public MenuScreen(Spacelone game) {
        this.game = game;
        viewport = new AreaViewport();
        stage = new Stage(viewport, game.batch);
        viewport.setWorldArea(500000f);
        viewport.setPixelsPerUnit(1f);

        Table table = new Table();
        table.center();

        for (int i = 0; i < 5; i++) {
            Slot slot = new Slot(i);
            TextButton textButton = new TextButton(
                    slot.directory.exists() ? "World " + (i + 1) : "New World",
                    game.skin, "worldButton");
            textButton.addListener(slot);
            table.add(textButton).row();
        }

        table.setFillParent(true);
        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
