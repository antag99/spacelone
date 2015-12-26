/*******************************************************************************
 * Copyright (C) 2015 Anton Gustafsson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.github.antag99.spacelone.system.object;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.github.antag99.retinazer.EntityProcessorSystem;
import com.github.antag99.retinazer.EntitySet;
import com.github.antag99.retinazer.Family;
import com.github.antag99.retinazer.FamilyConfig;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.retinazer.SkipWire;
import com.github.antag99.spacelone.component.Room;
import com.github.antag99.spacelone.component.object.Position;
import com.github.antag99.spacelone.component.object.Size;
import com.github.antag99.spacelone.util.OverlapListener;

public final class OverlapSystem extends EntityProcessorSystem {
    private Mapper<Room> mRoom;

    private Mapper<Position> mPosition;
    private Mapper<Size> mSize;

    private static final class ListenerData {
        Family a, b;
        OverlapListener listener;
    }

    @SkipWire
    private Array<ListenerData> listeners = new Array<ListenerData>();

    public OverlapSystem() {
        super(Family.with(Room.class));
    }

    public final class OverlapListenerBuilder {
        private OverlapListener listener;
        private Family a = engine.getFamily(new FamilyConfig()), b = a;

        private OverlapListenerBuilder(OverlapListener listener) {
            this.listener = listener;
        }

        public OverlapListenerBuilder a(FamilyConfig config) {
            a = engine.getFamily(config);
            return this;
        }

        public OverlapListenerBuilder b(FamilyConfig config) {
            b = engine.getFamily(config);
            return this;
        }

        public OverlapListenerBuilder add() {
            ListenerData listenerData = new ListenerData();
            listenerData.a = a;
            listenerData.b = b;
            listenerData.listener = listener;
            listeners.add(listenerData);
            return this;
        }

        public OverlapListenerBuilder remove() {
            int index = 0;
            while (index < listeners.size) {
                if (listeners.get(index).listener == listener) {
                    listeners.removeIndex(index);
                } else {
                    index++;
                }
            }
            return this;
        }
    }

    public OverlapListenerBuilder overlapListener(OverlapListener listener) {
        return new OverlapListenerBuilder(listener);
    }

    @Override
    protected void process(int entity) {
        Room room = mRoom.get(entity);

        for (EntitySet set : room.partitions.values()) {
            IntArray indices = set.getIndices();
            int[] items = indices.items;

            for (int i = 0, n = indices.size; i < n; i++) {
                int a = items[i];
                Position aPosition = mPosition.get(a);
                Size aSize = mSize.get(a);

                for (int j = 0; j < i; j++) {
                    int b = items[j];
                    Position bPosition = mPosition.get(b);
                    Size bSize = mSize.get(b);

                    float aX = aPosition.x;
                    float aY = aPosition.y;
                    float aW = aSize.width;
                    float aH = aSize.height;

                    float bX = bPosition.x;
                    float bY = bPosition.y;
                    float bW = bSize.width;
                    float bH = bSize.height;

                    if (aX + aW > bX && aY + aH > bY && bX + bW > aX && bY + bH > aY) {
                        Object[] l = listeners.items;
                        for (int ii = 0, nn = listeners.size; ii < nn; ii++) {
                            ListenerData d = (ListenerData) l[ii];

                            if (d.a.getEntities().contains(a) && d.b.getEntities().contains(b)) {
                                d.listener.onCollison(a, b);
                            } else if (d.a.getEntities().contains(b) && d.b.getEntities().contains(a)) {
                                d.listener.onCollison(b, a);
                            }
                        }
                    }
                }
            }
        }
    }
}
