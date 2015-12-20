package com.github.antag99.spacelone.util;

import com.sudoplay.joise.module.Module;
import com.sudoplay.joise.module.ModuleAutoCorrect;
import com.sudoplay.joise.module.ModuleBias;
import com.sudoplay.joise.module.ModuleBasisFunction.BasisType;
import com.sudoplay.joise.module.ModuleBasisFunction.InterpolationType;
import com.sudoplay.joise.module.ModuleCombiner;
import com.sudoplay.joise.module.ModuleCombiner.CombinerType;
import com.sudoplay.joise.module.ModuleFractal;
import com.sudoplay.joise.module.ModuleFractal.FractalType;
import com.sudoplay.joise.module.ModuleScaleDomain;
import com.sudoplay.joise.module.ModuleSelect;
import com.sudoplay.joise.module.ModuleSphere;
import com.sudoplay.joise.module.ModuleTranslateDomain;

public final class ModuleFactory {

    public static final Module combine(CombinerType type, Module... modules) {
        ModuleCombiner combiner = new ModuleCombiner(type);
        for (int i = 0; i < modules.length; i++)
            combiner.setSource(i, modules[i]);
        return combiner;
    }

    public static final Module combine(CombinerType type, float value, Module module) {
        ModuleCombiner combiner = new ModuleCombiner(type);
        combiner.setSource(0, module);
        combiner.setSource(1, value);
        return combiner;
    }

    public static final Module fractal(long seed, FractalType type,
            BasisType basisType, InterpolationType interpolationType,
            float frequency, int octaves) {
        ModuleFractal module = new ModuleFractal();
        module.setSeed(seed);
        module.setType(type);
        module.setAllSourceBasisTypes(basisType);
        module.setAllSourceInterpolationTypes(interpolationType);
        module.setFrequency(frequency);
        module.setNumOctaves(octaves);
        module.resetAllSources();
        return module;
    }

    public static final Module translate(Module source, Module x, Module y) {
        ModuleTranslateDomain module = new ModuleTranslateDomain();
        module.setSource(source);
        module.setAxisXSource(x);
        module.setAxisYSource(y);
        return module;
    }

    public static final Module scale(Module source, Module x, Module y) {
        ModuleScaleDomain module = new ModuleScaleDomain();
        module.setSource(source);
        module.setScaleX(x);
        module.setScaleY(y);
        return module;
    }

    public static final Module scale(Module source, float x, float y) {
        ModuleScaleDomain module = new ModuleScaleDomain();
        module.setSource(source);
        module.setScaleX(x);
        module.setScaleY(y);
        return module;
    }

    public static final Module sphere(float x, float y, float radius) {
        ModuleSphere module = new ModuleSphere();
        module.setCenterX(x);
        module.setCenterY(y);
        module.setRadius(radius);
        return module;
    }

    public static final Module sphere(float x, float y, Module radius) {
        ModuleSphere module = new ModuleSphere();
        module.setCenterX(x);
        module.setCenterY(y);
        module.setRadius(radius);
        return module;
    }

    public static final Module select(float low, float high, float threshold, Module source) {
        ModuleSelect module = new ModuleSelect();
        module.setControlSource(source);
        module.setLowSource(low);
        module.setHighSource(high);
        module.setThreshold(threshold);
        return module;
    }

    public static final Module autoCorrect(Module source, float low, float high,
            int samples, float sampleScale) {
        ModuleAutoCorrect module = new ModuleAutoCorrect();
        module.setSource(source);
        module.setLow(low);
        module.setHigh(high);
        module.setSamples(samples);
        module.setSampleScale(sampleScale);
        module.calculate();
        return module;
    }

    public static final Module bias(float bias, Module source) {
        ModuleBias module = new ModuleBias();
        module.setSource(source);
        module.setBias(bias);
        return module;
    }
}
