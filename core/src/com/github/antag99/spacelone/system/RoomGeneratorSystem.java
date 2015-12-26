package com.github.antag99.spacelone.system;

import static com.github.antag99.spacelone.util.ModuleFactory.combine;
import static com.github.antag99.spacelone.util.ModuleFactory.fractal;
import static com.github.antag99.spacelone.util.ModuleFactory.select;
import static com.github.antag99.spacelone.util.ModuleFactory.sphere;
import static com.github.antag99.spacelone.util.ModuleFactory.translate;
import static com.sudoplay.joise.module.ModuleBasisFunction.BasisType.SIMPLEX;
import static com.sudoplay.joise.module.ModuleBasisFunction.InterpolationType.QUINTIC;
import static com.sudoplay.joise.module.ModuleFractal.FractalType.FBM;

import java.util.Random;

import com.badlogic.gdx.math.MathUtils;
import com.github.antag99.retinazer.EntitySystem;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.spacelone.component.Room;
import com.github.antag99.spacelone.system.type.ContentSystem;
import com.github.antag99.spacelone.util.IntMatrix;
import com.sudoplay.joise.module.Module;
import com.sudoplay.joise.module.ModuleCombiner.CombinerType;

public final class RoomGeneratorSystem extends EntitySystem {
    private RoomSystem roomSystem;
    private IdSystem idSystem;
    private ContentSystem contentSystem;
    private Mapper<Room> mRoom;

    private static final int ROOM_SIZE = 1024;
    private static final int ROOM_PADDING = 32;
    private static final int EDGE_DISTORT = 20;
    private static final int EDGE_FREQ = 120;

    public void fillMatrix(IntMatrix matrix, int terrain, Module module) {
        for (int i = 0, ii = matrix.getWidth(); i < ii; i++) {
            for (int j = 0, jj = matrix.getHeight(); j < jj; j++) {
                if (module.get(i, j) == 1f) {
                    matrix.set(i, j, terrain);
                }
            }
        }
    }

    private int createTree(int roomEntity, float x, float y) {
        int treeEntity = contentSystem.createObject(roomEntity, idSystem.getEntity("tree"), x, y);
        return treeEntity;
    }

    public int generateRoom(int worldEntity) {
        int roomEntity = roomSystem.createRoom(worldEntity, ROOM_SIZE, ROOM_SIZE);

        Room room = mRoom.get(roomEntity);
        room.spawnPosition.set(ROOM_SIZE * 0.5f, ROOM_SIZE * 0.5f);

        float centerX = ROOM_SIZE * 0.5f;
        float centerY = ROOM_SIZE * 0.5f;
        float radius = ROOM_SIZE * 0.5f - ROOM_PADDING;
        Random random = new Random();
        Module sphere = sphere(centerX, centerY, radius);
        Module offX = fractal(random.nextLong(), FBM, SIMPLEX, QUINTIC, 1f / EDGE_FREQ, 8);
        offX = combine(CombinerType.MULT, EDGE_DISTORT, offX);
        Module offY = fractal(random.nextLong(), FBM, SIMPLEX, QUINTIC, 1f / EDGE_FREQ, 8);
        offY = combine(CombinerType.MULT, EDGE_DISTORT, offY);
        Module ground = select(0f, 1f, MathUtils.FLOAT_ROUNDING_ERROR, translate(sphere, offX, offY));
        fillMatrix(room.terrain, idSystem.getEntity("ground"), ground);

        // XXX *just a test*
        int groundEntity = idSystem.getEntity("ground");
        int density = 16;
        int treeY = density;

        while (treeY < room.height) {
            int treeX = treeY % (density * 2) < density ? density - (density / 2 + density % 2) : density * 2;

            while (treeX < room.width) {
                if (room.terrain.get(treeX, treeY) == groundEntity) {
                    createTree(roomEntity, treeX, treeY);
                }
                treeX += density;
            }

            treeY += density;
        }

        return roomEntity;
    }
}
