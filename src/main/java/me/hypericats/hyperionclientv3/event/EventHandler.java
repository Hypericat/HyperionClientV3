package me.hypericats.hyperionclientv3.event;

import me.hypericats.hyperionclientv3.util.ChatUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventHandler {
    private static final Map<Object,List<Listener>> listeners = new HashMap<>();
    private static final List<Pair<Class<? extends Listener>, Listener>> removeNext = new ArrayList<>();
    //Use a hashmap to store the listeners that should be called and use the type to sort them
    public static<L extends Listener> void onEvent(Class<L> listenerType, EventData data) {
        initKey(listenerType);
        for (Listener l : listeners.get(listenerType)) {
            l.onEvent(data);
        }
        removeNext();
    }
    private static void initKey(Object key) {
        if (listeners.containsKey(key) && listeners.get(key) != null && listeners.get(key) instanceof ArrayList<?>) return;
        listeners.put(key, new ArrayList<>());
    }
    public static<L extends Listener> void register(Class<L> listenerType, Listener listener) {
        initKey(listenerType);
        listeners.get(listenerType).add(listener);
    }
    public static<L extends Listener> void unregister(Class<L> listenerType, Listener listener) {
        initKey(listenerType);
        listeners.get(listenerType).remove(listener);
    }
    public static<L extends Listener> void unregisterNext(Class<L> listenerType, Listener listener) {
        removeNext.add(new Pair<>(listenerType, listener));
    }
    private static void removeNext() {
        if (removeNext.isEmpty()) return;
        for (Pair<Class<? extends Listener>, Listener> pair : removeNext) {
            unregister(pair.getLeft(), pair.getRight());
        }
        removeNext.clear();
    }
}
