package com.github.antag99.spacelone.component;

import com.badlogic.gdx.files.FileHandle;
import com.esotericsoftware.kryo.Kryo;
import com.github.antag99.retinazer.Component;
import com.github.antag99.retinazer.EntitySet;

public final class World implements Component {
    public int startRoom;
    public int localPlayer;

    public EntitySet rooms = new EntitySet();
    public EntitySet players = new EntitySet();
    public FileHandle directory;
    public Kryo kryo;
}
