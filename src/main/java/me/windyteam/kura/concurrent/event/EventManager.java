package me.windyteam.kura.concurrent.event;

import me.windyteam.kura.concurrent.task.ObjectTask;
import me.windyteam.kura.concurrent.TaskManager;
import me.windyteam.kura.concurrent.TaskManager;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Author B_312
 * Created on 8/19/2021
 */
public class EventManager {

    private final List<SubscribedUnit> registeredUnit = new CopyOnWriteArrayList<>();

    public void register(Object owner) {
        unregister(owner);
        Method[] methods = owner.getClass().getDeclaredMethods();
        if (methods.length != 0) {
            for (Method method : methods) {
                if (method.isAnnotationPresent(Listener.class)) {
                    Listener annotation = method.getAnnotation(Listener.class);
                    if (method.getParameterCount() == 1) {
                        if (!method.isAccessible()) method.setAccessible(true);
                        ObjectTask task = it -> {
                            try {
                                method.invoke(owner, it);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        };

                        SubscribedUnit unit = new SubscribedUnit(owner, method.getParameterTypes()[0], task, annotation.priority(), annotation.priority() == Priority.PARALLEL);
                        registeredUnit.add(unit);
                    }
                }
            }
            registeredUnit.sort(Comparator.comparing(SubscribedUnit::getPriority));
            Collections.reverse(registeredUnit);
        }
    }

    public void unregister(Object owner) {
        registeredUnit.removeIf(it -> it.getOwner().equals(owner));
    }

    public void post(Object event) {
        Class<?> eventClass = event.getClass();
        TaskManager.runBlocking(content -> {
            for (SubscribedUnit unit : registeredUnit) {
                if (eventClass.equals(unit.getEventClass())) {
                    if (unit.isParallel()) content.launch(() -> unit.getTask().invoke(event));
                    else unit.getTask().invoke(event);
                }
            }
        });
    }

    public boolean isRegistered(Object owner) {
        return registeredUnit.stream().anyMatch(it -> it.getOwner().equals(owner));
    }

}
