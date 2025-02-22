package me.windyteam.kura.concurrent.decentralization;

import me.windyteam.kura.concurrent.task.EventTask;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("ALL")
public class DecentralizedEvent<T extends EventData> {

    private final List<EventTask<T>> listeners = new CopyOnWriteArrayList<>();

    public void register(EventTask<? extends EventData> action) {
        if (listeners.stream().anyMatch(it -> it.equals(action))) return;
        listeners.add((EventTask<T>) action);
    }

    public void unregister(EventTask<? extends EventData> action)  {
        listeners.removeIf(it -> it.equals(action));
    }

    public void post(T data) {
        listeners.forEach(it -> {
            it.invoke(data);
        });
    }

}
