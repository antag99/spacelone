package com.github.antag99.spacelone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.esotericsoftware.kryo.Kryo;
import com.github.antag99.retinazer.Engine;
import com.github.antag99.retinazer.EngineConfig;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.spacelone.system.ActorSystem;
import com.github.antag99.spacelone.system.AssetSystem;
import com.github.antag99.spacelone.system.ClientSystem;
import com.github.antag99.spacelone.system.DeltaSystem;
import com.github.antag99.spacelone.system.IdSystem;
import com.github.antag99.spacelone.system.RoomGeneratorSystem;
import com.github.antag99.spacelone.system.RoomSystem;
import com.github.antag99.spacelone.system.ScriptSystem;
import com.github.antag99.spacelone.system.WorldSystem;
import com.github.antag99.spacelone.system.object.ActorColorSystem;
import com.github.antag99.spacelone.system.object.ActorPositionSystem;
import com.github.antag99.spacelone.system.object.ActorSizeSystem;
import com.github.antag99.spacelone.system.object.CollisionSystem;
import com.github.antag99.spacelone.system.object.ControlSystem;
import com.github.antag99.spacelone.system.object.FadeSystem;
import com.github.antag99.spacelone.system.object.HarvestedSystem;
import com.github.antag99.spacelone.system.object.HarvestorSystem;
import com.github.antag99.spacelone.system.object.MovementSystem;
import com.github.antag99.spacelone.system.object.PlayerSystem;
import com.github.antag99.spacelone.system.object.ResourceSystem;
import com.github.antag99.spacelone.system.object.SpatialSystem;
import com.github.antag99.spacelone.system.object.VelocitySystem;
import com.github.antag99.spacelone.system.type.ContentSystem;
import com.github.antag99.spacelone.system.type.FloorTextureRefSystem;
import com.github.antag99.spacelone.system.type.ItemTextureRefSystem;
import com.github.antag99.spacelone.system.type.ObjectTextureRefSystem;
import com.github.antag99.spacelone.system.ui.FloorRendererSystem;
import com.github.antag99.spacelone.system.ui.GroundRendererSystem;
import com.github.antag99.spacelone.system.ui.PlayerRendererSystem;
import com.github.antag99.spacelone.system.ui.TreeLeavesRendererSystem;
import com.github.antag99.spacelone.system.ui.TreeTrunkRendererSystem;
import com.github.antag99.spacelone.system.ui.ViewPositionSystem;
import com.github.antag99.spacelone.util.AreaViewport;
import com.github.antag99.spacelone.util.DependencyConfig;
import com.github.antag99.spacelone.util.DependencyResolver;

public final class GameScreen extends ScreenAdapter {
    private static final float MAX_DELTA_TIME = 1f / 20f;
    private static final int PARTITION_WIDTH = 32;
    private static final int PARTITION_HEIGHT = 32;

    private Spacelone game;
    private Engine engine;
    private Kryo kryo;
    private Stage stage;
    private AreaViewport viewport;
    private OrthographicCamera camera;
    private InputMultiplexer inputMultiplexer;
    private int world;

    public FileHandle directory;

    public GameScreen(Spacelone game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new AreaViewport(camera);
        viewport.setPixelsPerUnit(16f);
        viewport.setWorldArea(50000f);
        stage = new Stage(viewport, game.batch);

        kryo = new Kryo();
        engine = new Engine(new EngineConfig()
                .addWireResolver(new DependencyResolver(new DependencyConfig()
                        .addDependency(kryo)))
                .addSystem(new DeltaSystem(MAX_DELTA_TIME))
                .addSystem(new AssetSystem(game.skin))
                .addSystem(new ScriptSystem())
                .addSystem(new IdSystem())
                .addSystem(new ContentSystem())
                .addSystem(new ItemTextureRefSystem(game.skin.getAtlas()))
                .addSystem(new FloorTextureRefSystem(game.skin.getAtlas()))
                .addSystem(new ObjectTextureRefSystem(game.skin.getAtlas()))
                .addSystem(new PlayerSystem())
                .addSystem(new WorldSystem())
                .addSystem(new RoomSystem())
                .addSystem(new RoomGeneratorSystem())
                .addSystem(new HarvestorSystem())
                .addSystem(new HarvestedSystem())
                .addSystem(new ResourceSystem())
                .addSystem(new ControlSystem())
                .addSystem(new MovementSystem())
                .addSystem(new VelocitySystem())
                .addSystem(new SpatialSystem(PARTITION_WIDTH, PARTITION_HEIGHT))
                .addSystem(new CollisionSystem())
                .addSystem(new ViewPositionSystem())
                .addSystem(new GroundRendererSystem())
                .addSystem(new FloorRendererSystem())
                .addSystem(new TreeTrunkRendererSystem())
                .addSystem(new PlayerRendererSystem())
                .addSystem(new TreeLeavesRendererSystem())
                .addSystem(new ClientSystem(this))
                .addSystem(new FadeSystem())
                .addSystem(new ActorColorSystem())
                .addSystem(new ActorPositionSystem())
                .addSystem(new ActorSizeSystem())
                .addSystem(new ActorSystem(stage.getRoot())));
        engine.wire(this);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        // inputMultiplexer.addProcessor(engine.getSystem(KeyboardSystem.class));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
        world = engine.getSystem(ClientSystem.class).startGame(directory);
    }

    @Override
    public void hide() {
        engine.getSystem(ClientSystem.class).stopGame(world);
        engine.reset();
    }

    @Override
    public void dispose() {
        for (EntitySystem system : engine.getSystems()) {
            if (system instanceof Disposable) {
                ((Disposable) system).dispose();
            }
        }
    }

    public Batch getBatch() {
        return game.batch;
    }

    public Skin getSkin() {
        return game.skin;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        engine.getSystem(DeltaSystem.class).setDeltaTime(deltaTime);
        engine.update();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
