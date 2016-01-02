package com.github.antag99.spacelone.system.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntArray;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.spacelone.component.object.Position;
import com.github.antag99.spacelone.component.object.Tree;
import com.github.antag99.spacelone.system.AssetSystem;

public final class TreeLeavesRendererSystem extends BaseObjectRendererSystem implements Disposable {
    private static final Color LEAVES_COLOR = new Color(Color.FOREST).mul(1.0f, 1.0f, 1.0f, 1.0f);
    private static final Color LEAVES_EDGE_COLOR = new Color(Color.FOREST).mul(0.8f, 0.8f, 0.8f, 1.0f);
    private static final float SEETHROUGH_RADIUS = 3f;
    private static final float SEETHROUGH_ALPHA = 0.9f;

    private Mapper<Position> mPosition;

    private AssetSystem assetSystem;
    private @SkipWire TextureRegion leavesTexture, leavesEdgeTexture, seethroughTexture;
    private @SkipWire FrameBuffer leavesBuffer;
    private @SkipWire int displayWidth = -1, displayHeight = -1;
    private @SkipWire EarClippingTriangulator triangulator = new EarClippingTriangulator();

    // Slightly hacky; first leaf edges are rendered, then leaves.
    private @SkipWire boolean renderLeaves = false;

    public TreeLeavesRendererSystem() {
        super(Family.with(Tree.class), 8f, 8f);
    }

    @Override
    protected void initialize() {
        leavesTexture = assetSystem.skin.getRegion("images/leaves");
        leavesEdgeTexture = assetSystem.skin.getRegion("images/leaves_edge");
        seethroughTexture = assetSystem.skin.getRegion("images/seethrough");
    }

    @Override
    public void dispose() {
        if (leavesBuffer != null)
            leavesBuffer.dispose();
    }

    private void prepareFrameBuffers() {
        if (displayWidth != Gdx.graphics.getWidth() ||
                displayHeight != Gdx.graphics.getHeight()) {
            this.displayWidth = Gdx.graphics.getWidth();
            this.displayHeight = Gdx.graphics.getHeight();

            if (leavesBuffer != null) {
                leavesBuffer.dispose();
            }

            leavesBuffer = new FrameBuffer(Format.RGBA8888, displayWidth, displayHeight, false);
        }
    }

    private void renderLeaves(Batch batch, int viewEntity, IntArray objects) {
        batch.begin();
        renderLeaves = false;
        super.renderObjects(batch, viewEntity, objects);
        renderLeaves = true;
        super.renderObjects(batch, viewEntity, objects);
        batch.end();
    }

    @Override
    protected void renderObjects(Batch batch, int viewEntity, IntArray objects) {
        batch.end();

        prepareFrameBuffers();
        leavesBuffer.begin();
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderLeaves(batch, viewEntity, objects);

        Position viewPosition = mPosition.get(viewEntity);

        Gdx.gl.glBlendEquation(GL20.GL_FUNC_REVERSE_SUBTRACT);
        batch.begin();
        batch.setColor(1f, 1f, 1f, 1f - SEETHROUGH_ALPHA);
        batch.draw(seethroughTexture,
                viewPosition.x - SEETHROUGH_RADIUS,
                viewPosition.y - SEETHROUGH_RADIUS,
                SEETHROUGH_RADIUS / 0.5f, SEETHROUGH_RADIUS / 0.5f);
        batch.end();
        Gdx.gl.glBlendEquation(GL20.GL_FUNC_ADD);
        leavesBuffer.end();
        batch.getProjectionMatrix().setToOrtho2D(0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setColor(Color.WHITE);
        batch.begin();
        batch.draw(leavesBuffer.getColorBufferTexture(), 0, 0,
                leavesBuffer.getWidth(), leavesBuffer.getHeight(),
                0, 0, leavesBuffer.getWidth(), leavesBuffer.getHeight(),
                false, true);
    }

    @Override
    protected void renderObject(Batch batch, int viewEntity, int objectEntity) {
        Position position = mPosition.get(objectEntity);

        if (!renderLeaves) {
            batch.setColor(LEAVES_EDGE_COLOR);
            batch.draw(leavesEdgeTexture,
                    position.x - 2.5f, position.y - 2.5f, 0f, 0f,
                    5f, 5f, 1f, 1f, 0f);
        } else {
            batch.setColor(LEAVES_COLOR);
            batch.draw(leavesTexture,
                    position.x - 2.5f, position.y - 2.5f, 0f, 0f,
                    5f, 5f, 1f, 1f, 0f);
        }
    }
}
