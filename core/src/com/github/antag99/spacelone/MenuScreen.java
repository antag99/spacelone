package com.github.antag99.spacelone;

import java.util.Arrays;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.github.antag99.spacelone.ui.StarBackground;
import com.github.antag99.spacelone.util.AreaViewport;

public final class MenuScreen extends ScreenAdapter {
    private Spacelone game;
    private Stage stage;
    private AreaViewport viewport;
    private Array<SaveSlot> saveSlots = new Array<>();
    private List<SaveSlot> saveSlotList;
    private ScrollPane saveScrollPane;
    private TextButton createButton;
    private TextButton playButton;
    private TextButton deleteButton;
    private TextButton exitButton;

    private static final class SaveSlot {
        public FileHandle directory;

        @Override
        public String toString() {
            return directory.name();
        }
    }

    public MenuScreen(final Spacelone game) {
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

        saveSlotList = new List<>(game.skin, "save");
        saveScrollPane = new ScrollPane(saveSlotList, game.skin, "save");
        saveScrollPane.setScrollingDisabled(true, false);
        saveScrollPane.setFadeScrollBars(false);
        saveScrollPane.setScrollbarsOnTop(true);
        saveScrollPane.setVariableSizeKnobs(false);
        table.add("SPACELONE", "title");
        table.row();
        table.add(saveScrollPane).expand().fill().pad(0f, 20f, 0f, 20f);
        table.row();

        createButton = new TextButton("CREATE NEW WORLD", game.skin, "menu");
        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.getTextInput(new TextInputListener() {
                    @Override
                    public void input(final String text) {
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                FileHandle worldFile = game.saveDirectory.child("worlds").child(text);
                                game.gameScreen.directory = worldFile;
                                game.setScreen(game.gameScreen);
                            }
                        });
                    }

                    @Override
                    public void canceled() {
                    }
                }, "Create New World", "", "Enter World Name");
            }
        });

        table.add(createButton);
        table.row();

        playButton = new TextButton("PLAY SELECTED WORLD", game.skin, "menu");
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int index = saveSlotList.getSelectedIndex();
                if (index == -1)
                    return;
                SaveSlot saveSlot = saveSlots.get(index);
                game.gameScreen.directory = saveSlot.directory;
                game.setScreen(game.gameScreen);
            }
        });

        deleteButton = new TextButton("DELETE SELECTED WORLD", game.skin, "menu");
        deleteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int index = saveSlotList.getSelectedIndex();
                if (index == -1)
                    return;
                SaveSlot saveSlot = saveSlots.get(index);
                saveSlot.directory.deleteDirectory();
                refreshSaveList();
            }
        });

        exitButton = new TextButton("EXIT GAME", game.skin, "menu");
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        table.add(playButton);
        table.row();
        table.add(deleteButton);
        table.row();
        table.add(exitButton);
        table.row();

        table.setFillParent(true);
        stage.addActor(table);

        refreshSaveList();
    }

    private void refreshSaveList() {
        FileHandle[] worldFiles = game.saveDirectory.child("worlds").list();
        Arrays.sort(worldFiles, new Comparator<FileHandle>() {
            @Override
            public int compare(FileHandle o1, FileHandle o2) {
                return Long.compare(o2.child("world.json").lastModified(),
                        o1.child("world.json").lastModified());
            }
        });
        saveSlots.clear();
        for (FileHandle worldFile : worldFiles) {
            SaveSlot saveSlot = new SaveSlot();
            saveSlot.directory = worldFile;
            saveSlots.add(saveSlot);
        }
        saveSlotList.setItems(saveSlots);
        playButton.setDisabled(saveSlots.size == 0);
        deleteButton.setDisabled(saveSlots.size == 0);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.setScrollFocus(saveScrollPane);
        refreshSaveList();
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
