package com.github.antag99.spacelone.util;

import com.github.antag99.spacelone.command.Command;

public class RunnableCommand extends Command {
    public Runnable runnable;

    public RunnableCommand(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public float apply(Handle entity, float time) {
        runnable.run();
        return time;
    }

    @Override
    public void restart() {
    }
}
