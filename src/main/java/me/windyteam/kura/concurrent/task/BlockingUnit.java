package me.windyteam.kura.concurrent.task;

import me.windyteam.kura.concurrent.thread.BlockingContent;
import me.windyteam.kura.concurrent.thread.BlockingContent;

public class BlockingUnit implements Runnable {

    private final VoidTask task;
    private final BlockingContent content;

    public BlockingUnit(VoidTask task, BlockingContent content) {
        this.task = task;
        this.content = content;
    }

    @Override
    public void run() {
        try {
            task.invoke();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        content.count();
        content.countDown();
    }

}
