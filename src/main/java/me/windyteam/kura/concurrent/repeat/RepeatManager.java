package me.windyteam.kura.concurrent.repeat;

import me.windyteam.kura.concurrent.TaskManager;
import me.windyteam.kura.concurrent.TaskManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by B_312 on 05/01/2021
 */
public class RepeatManager {

    public static RepeatManager instance;

    public static void init() {
        if (instance == null) instance = new RepeatManager();
    }

    public final List<RepeatUnit> repeatUnits = new CopyOnWriteArrayList<>();
    public final List<DelayUnit> delayUnits = new CopyOnWriteArrayList<>();

    public static void update() {
        instance.delayUnits.removeIf(DelayUnit::invoke);
        instance.repeatUnits.removeIf(RepeatUnit::isDead);
        instance.repeatUnits.forEach(it -> {
            if (it.shouldRun()) {
                TaskManager.launch(it::run);
            }
        });
    }

}
