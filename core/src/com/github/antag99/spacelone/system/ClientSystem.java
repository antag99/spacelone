package com.github.antag99.spacelone.system;

import com.badlogic.gdx.files.FileHandle;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.spacelone.GameScreen;
import com.github.antag99.spacelone.component.World;
import com.github.antag99.spacelone.component.ui.Renderer;
import com.github.antag99.spacelone.component.ui.View;
import com.github.antag99.spacelone.system.ui.GameHUDSystem;

public final class ClientSystem extends EntitySystem {
    private @SkipWire GameScreen gameScreen;

    private WorldSystem worldSystem;
    private GameHUDSystem gameHUDSystem;
    private Mapper<World> mWorld;
    private Mapper<View> mView;
    private Mapper<Renderer> mRenderer;

    public ClientSystem(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public int startGame(FileHandle directory) {
        int world = !directory.exists() ? worldSystem.createWorld(directory)
                : worldSystem.loadWorld(directory);
        int player = mWorld.get(world).localPlayer;
        mView.create(player).camera = gameScreen.getCamera();
        mRenderer.create(player).batch = gameScreen.getBatch();
        gameHUDSystem.setPlayer(player);
        return world;
    }

    public void stopGame(int world) {
        worldSystem.saveWorld(world);
        worldSystem.destroyWorld(world);
    }
}
