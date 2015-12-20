package com.github.antag99.spacelone.system.ui;

import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.spacelone.component.Room;
import com.github.antag99.spacelone.component.object.Location;
import com.github.antag99.spacelone.component.object.Position;
import com.github.antag99.spacelone.component.object.Size;
import com.github.antag99.spacelone.component.ui.View;

public final class ViewPositionSystem extends EntityProcessorSystem {
    private Mapper<Location> mLocation;
    private Mapper<Room> mRoom;
    private Mapper<View> mView;
    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;

    public ViewPositionSystem() {
        super(Family.with(View.class, Position.class, Size.class));
    }

    @Override
    protected void process(int entity) {
        Room room = mRoom.get(mLocation.get(entity).room);
        View view = mView.get(entity);
        Position position = mPosition.get(entity);
        Size size = mSize.get(entity);

        float x = position.x + size.width * 0.5f;
        float y = position.y + size.height * 0.5f;

        float w = view.camera.viewportWidth * view.camera.zoom;
        float h = view.camera.viewportHeight * view.camera.zoom;

        if (x - w / 2 < 0f)
            x = w / 2;
        if (x + w / 2 > room.width)
            x = room.width - w / 2;

        if (y - h / 2 < 0f)
            y = h / 2;
        if (y + h / 2 > room.height)
            y = room.height - h / 2;

        view.camera.position.x = x;
        view.camera.position.y = y;
        view.camera.update();
    }
}
