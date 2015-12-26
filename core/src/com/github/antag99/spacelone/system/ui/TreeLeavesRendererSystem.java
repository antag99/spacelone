package com.github.antag99.spacelone.system.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;
import com.github.antag99.retinazer.EntitySet;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.spacelone.component.object.Position;
import com.github.antag99.spacelone.component.object.Size;
import com.github.antag99.spacelone.component.object.Tree;
import com.github.antag99.spacelone.system.AssetSystem;

public final class TreeLeavesRendererSystem extends BaseObjectRendererSystem implements Disposable {
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;

    private AssetSystem assetSystem;
    private @SkipWire TextureRegion leavesTexture, seethroughTexture;
    private @SkipWire FrameBuffer leavesBuffer;
    private @SkipWire TextureRegion leavesBufferRegion;
    private @SkipWire int displayWidth = -1, displayHeight = -1;

    public TreeLeavesRendererSystem() {
        super(Family.with(Tree.class), 8f, 8f);
    }

    @Override
    protected void initialize() {
        leavesTexture = assetSystem.skin.getRegion("images/leaves");
        seethroughTexture = assetSystem.skin.getRegion("images/seethrough");
    }

    @Override
    public void dispose() {
        if (leavesBuffer != null)
            leavesBuffer.dispose();
    }

    @Override
    protected void renderView(int viewEntity) {
        if (displayWidth != Gdx.graphics.getWidth() ||
                displayHeight != Gdx.graphics.getHeight()) {
            this.displayWidth = Gdx.graphics.getWidth();
            this.displayHeight = Gdx.graphics.getHeight();

            if (leavesBuffer != null) {
                leavesBuffer.dispose();
            }

            leavesBuffer = new FrameBuffer(Format.RGBA8888, displayWidth, displayHeight, false);
            // leavesBufferRegion = new TextureRegion(leavesBuffer.getColorBufferTexture(),
            // 0, bufferHeight - displayHeight, displayWidth, displayHeight);
            // leavesBufferRegion.flip(false, true);
        }

        leavesBuffer.begin();
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.renderView(viewEntity);
    }

    @Override
    protected void renderObjects(Batch batch, int viewEntity, EntitySet objects) {
        super.renderObjects(batch, viewEntity, objects);

        Position viewPosition = mPosition.get(viewEntity);
        Size viewSize = mSize.get(viewEntity);
        float viewX = viewPosition.x + viewSize.width * 0.5f;
        float viewY = viewPosition.y + viewSize.height * 0.5f;

        batch.flush();
        batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);
        Gdx.gl.glBlendEquation(GL20.GL_FUNC_REVERSE_SUBTRACT);

        batch.draw(seethroughTexture, viewX - 4f, viewY - 4f, 8f, 8f);
        batch.flush();

        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glBlendEquation(GL20.GL_FUNC_ADD);
        batch.end();

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
        Size size = mSize.get(objectEntity);
        batch.setColor(Color.FOREST);
        batch.draw(leavesTexture, position.x - 2f, position.y - 2f, 0f, 0f,
                5f, 5f, 1f, 1f, 0f);
    }
}
