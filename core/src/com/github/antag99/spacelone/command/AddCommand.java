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
package com.github.antag99.spacelone.command;

import com.github.antag99.retinazer.Component;
import com.github.antag99.retinazer.Mapper;
import com.github.antag99.spacelone.util.Handle;

public final class AddCommand extends Command {
    public Component component;

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public float apply(Handle entity, float time) {
        Mapper mapper = entity.getEngine().getMapper(component.getClass());
        Component existingComponent = mapper.get(entity.get());
        if (existingComponent == null) {
            mapper.add(entity.get(), component);
        } else if (existingComponent != component) {
            return 0f;
        }
        return time;
    }

    @Override
    public void restart() {
    }

    @Override
    public void reset() {
        super.reset();
        component = null;
    }
}
