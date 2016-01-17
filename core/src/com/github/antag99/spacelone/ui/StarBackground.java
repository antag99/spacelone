package com.github.antag99.spacelone.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sudoplay.joise.module.ModuleBasisFunction.BasisType;
import com.sudoplay.joise.module.ModuleBasisFunction.InterpolationType;
import com.sudoplay.joise.module.ModuleFractal;

public final class StarBackground {
    private ModuleFractal baseScale;
    private ModuleFractal maxScale;
    private ModuleFractal offsetX;
    private ModuleFractal offsetY;
    private ModuleFractal rotation;
    private float counter;
    private float density;
    private float starWidth;
    private float starHeight;
    private TextureRegion starTexture;

    public StarBackground(long seed, float density,
            float starWidth, float starHeight,
            TextureRegion starTexture) {
        this.starWidth = starWidth;
        this.starHeight = starHeight;

        baseScale = new ModuleFractal();
        baseScale.setSeed(seed);
        baseScale.setAllSourceBasisTypes(BasisType.GRADIENT);
        baseScale.setAllSourceInterpolationTypes(InterpolationType.QUINTIC);
        baseScale.setNumOctaves(8);
        baseScale.setFrequency(density);
        baseScale.resetAllSources();

        maxScale = new ModuleFractal();
        maxScale.setSeed(seed);
        maxScale.setAllSourceBasisTypes(BasisType.GRADIENT);
        maxScale.setAllSourceInterpolationTypes(InterpolationType.QUINTIC);
        maxScale.setNumOctaves(8);
        maxScale.setFrequency(density);
        maxScale.resetAllSources();

        offsetX = new ModuleFractal();
        offsetX.setSeed(seed);
        offsetX.setAllSourceBasisTypes(BasisType.GRADIENT);
        offsetX.setAllSourceInterpolationTypes(InterpolationType.QUINTIC);
        offsetX.setNumOctaves(8);
        offsetX.setFrequency(density);
        offsetX.resetAllSources();

        offsetY = new ModuleFractal();
        offsetY.setSeed(seed);
        offsetY.setAllSourceBasisTypes(BasisType.GRADIENT);
        offsetY.setAllSourceInterpolationTypes(InterpolationType.QUINTIC);
        offsetY.setNumOctaves(8);
        offsetY.setFrequency(density);
        offsetY.resetAllSources();

        rotation = new ModuleFractal();
        rotation.setSeed(seed);
        rotation.setAllSourceBasisTypes(BasisType.GRADIENT);
        rotation.setAllSourceInterpolationTypes(InterpolationType.QUINTIC);
        rotation.setNumOctaves(8);
        rotation.setFrequency(density);
        rotation.resetAllSources();

        this.density = density;
        this.starTexture = starTexture;
    }

    public void update(float delta) {
        counter += delta / 3f;
    }

    public void draw(Batch batch, float x, float y, float width, float height) {
        float d = density;
        for (float i = x - x % d - d, ii = x + width + d; i < ii; i += d) {
            for (float j = y - y % d - d, jj = y + height + d; j < jj; j += d) {
                float sampleX = i;
                float sampleY = j;
                float starMaxScale = ((float) maxScale.get(sampleX, sampleY) + 1f) / 2f;
                float starScale = ((((float) baseScale.get(sampleX, sampleY) + 1f) + counter) % (starMaxScale * 2f));
                if (starScale > starMaxScale)
                    starScale = starMaxScale * 2f - starScale;
                float starX = i + (float) offsetX.get(sampleX, sampleY) * (d - starWidth * 0.5f);
                float starY = j + (float) offsetY.get(sampleX, sampleY) * (d - starHeight * 0.5f);
                float starRotation = (float) rotation.get(sampleX, sampleY) * 180f;
                batch.setColor(1f, 1f, 0.5f, 1f);
                batch.draw(starTexture,
                        starX - starWidth * 0.5f, starY - starHeight * 0.5f,
                        starWidth * 0.5f, starHeight * 0.5f,
                        starWidth, starHeight, starScale, starScale, starRotation);
            }
        }
    }
}
