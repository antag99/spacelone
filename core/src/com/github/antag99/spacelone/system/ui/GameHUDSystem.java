package com.github.antag99.spacelone.system.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.spacelone.GameScreen;
import com.github.antag99.spacelone.component.object.HeldItem;
import com.github.antag99.spacelone.component.object.Hotbar;
import com.github.antag99.spacelone.component.object.Inventory;
import com.github.antag99.spacelone.ui.Bar;
import com.github.antag99.spacelone.ui.ItemSlot;
import com.github.antag99.spacelone.util.AreaViewport;

public final class GameHUDSystem extends EntitySystem implements InputProcessor {

    private @SkipWire int player;
    private @SkipWire GameHUD gameHUD;
    private @SkipWire int screenWidth, screenHeight;

    private Mapper<Inventory> mInventory;
    private Mapper<Hotbar> mHotbar;
    private Mapper<HeldItem> mHeldItem;

    public GameHUDSystem(GameScreen gameScreen) {
        gameHUD = new GameHUD(gameScreen);
    }

    private final class GameHUD {
        private GameScreen gameScreen;
        private Stage stage;
        private AreaViewport viewport;
        private Table table;
        private Bar healthBar;
        private Bar experienceBar;
        private ItemSlot[] hotbarSlots;

        public GameHUD(GameScreen screen) {
            gameScreen = screen;
            viewport = new AreaViewport();
            stage = new Stage(viewport, gameScreen.getBatch());
            table = new Table(gameScreen.getSkin());
            table.setRound(false);
            table.align(Align.left | Align.right | Align.top);

            healthBar = new Bar(gameScreen.getSkin(), "health");
            healthBar.setValue(1f);

            experienceBar = new Bar(gameScreen.getSkin(), "experience");
            experienceBar.setValue(1f);

            Table hotbarTable = new Table();
            hotbarSlots = new ItemSlot[8];
            for (int i = 0; i < hotbarSlots.length; i++) {
                final int index = i;
                hotbarSlots[i] = new ItemSlot(gameScreen.getSkin(), "hotbar");
                hotbarSlots[i].addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Hotbar hotbar = mHotbar.get(player);
                        HeldItem heldItem = mHeldItem.get(player);
                        int oldHeldItem = heldItem.item;
                        heldItem.item = hotbar.items[index];
                        hotbar.items[index] = oldHeldItem;
                        event.handle();
                    }
                });
                hotbarTable.add(hotbarSlots[i]).size(50f).pad(2f);
            }
            table.add(healthBar).expandX().height(10f).fill(0.8f, 1f).space(2f).row();
            table.add(experienceBar).expandX().height(10f).fill(0.8f, 1f).space(2f).row();
            table.add(hotbarTable).expandX().height(50f).fill(0.8f, 1f).space(2f).row();
            table.setFillParent(true);
            stage.addActor(table);
        }
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    @Override
    protected void update() {
        if (Gdx.graphics.getWidth() != screenWidth ||
                Gdx.graphics.getHeight() != screenHeight) {
            screenWidth = Gdx.graphics.getWidth();
            screenHeight = Gdx.graphics.getHeight();
            gameHUD.viewport.update(screenWidth, screenHeight, true);
        }

        gameHUD.stage.act();
        gameHUD.stage.draw();
    }

    @Override
    public boolean keyDown(int keycode) {
        return gameHUD.stage.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return gameHUD.stage.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return gameHUD.stage.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return gameHUD.stage.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return gameHUD.stage.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return gameHUD.stage.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return gameHUD.stage.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        return gameHUD.stage.scrolled(amount);
    }
}
