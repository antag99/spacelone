package com.github.antag99.spacelone.system.object;

import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.spacelone.component.object.Colored;
import com.github.antag99.spacelone.component.object.Fade;
import com.github.antag99.spacelone.system.DeltaSystem;
import com.github.antag99.spacelone.util.EntityAdapter;

public final class FadeSystem extends EntityProcessorSystem {
    private DeltaSystem deltaSystem;
    private Mapper<Colored> mColored;
    private Mapper<Fade> mFade;

    public FadeSystem() {
        super(Family.with(Colored.class, Fade.class));
    }

    @Override
    protected void initialize() {
        engine.getFamily(Family.with(Colored.class, Fade.class)).addListener(new EntityAdapter() {
            @Override
            protected void inserted(int entity) {
                Colored colored = mColored.get(entity);
                Fade fade = mFade.get(entity);
                fade.counter = 0f;
                fade.init.set(colored.color);
            }
        });
    }

    @Override
    protected void process(int entity) {
        float deltaTime = deltaSystem.getDeltaTime();
        Colored colored = mColored.get(entity);
        Fade fade = mFade.get(entity);
        fade.counter += deltaTime;
        if (fade.counter > fade.duration) {
            fade.counter = fade.duration;
            mFade.remove(entity);
        }
        float alpha = fade.interpolation.apply(fade.counter / fade.duration);
        colored.color.r = fade.init.r + (fade.color.r - fade.init.r) * alpha;
        colored.color.g = fade.init.g + (fade.color.g - fade.init.g) * alpha;
        colored.color.b = fade.init.b + (fade.color.b - fade.init.b) * alpha;
        colored.color.a = fade.init.a + (fade.color.a - fade.init.a) * alpha;
    }
}
