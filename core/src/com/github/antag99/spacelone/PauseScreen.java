package com.github.antag99.spacelone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.antag99.spacelone.ui.StarBackground;
import com.github.antag99.spacelone.util.AreaViewport;

public final class PauseScreen extends ScreenAdapter {
    private Spacelone game;
    private Stage stage;
    private AreaViewport viewport;

    public PauseScreen(final Spacelone game) {
        this.game = game;
        viewport = new AreaViewport();
        viewport.setWorldArea(500000f);
        viewport.setPixelsPerUnit(1f);
        stage = new Stage(viewport, game.batch);
        StarBackground bg = new StarBackground(0L, 50f, 20f, 20f,
                game.skin.getRegion("images/star"));
        bg.setFillParent(true);
        stage.addActor(bg);

        Table table = new Table(game.skin);
        table.center().top();
        table.add("GAME PAUSED", "title");
        table.row();

        TextButton resumeButton = new TextButton("RESUME", game.skin, "menu");
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.gameScreen);
            }
        });
        table.add(resumeButton);
        table.row();

        TextButton quitButton = new TextButton("SAVE AND QUIT", game.skin, "menu");
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.menuScreen);
            }
        });
        table.add(quitButton);
        table.row();

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

        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            game.setScreen(game.gameScreen);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
