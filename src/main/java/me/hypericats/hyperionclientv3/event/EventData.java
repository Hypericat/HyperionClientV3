package me.hypericats.hyperionclientv3.event;

import me.hypericats.hyperionclientv3.util.NullBool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventData {
    private List<Object> objects = new ArrayList<>();
    private NullBool cancelled = NullBool.NULL;
    public EventData(Object obj) {
        this(new Object[]{obj});
    }
    public EventData() {

    }
    public EventData(Object obj1, Object obj2) {
        this(new Object[]{obj1, obj2});
    }
    public EventData(Object obj1, Object obj2, Object obj3) {
        this(new Object[]{obj1, obj2, obj3});
    }
    public EventData(Object obj1, Object obj2, Object obj3, Object obj4) {
        this(new Object[]{obj1, obj2, obj3, obj4});
    }
    public EventData(Object obj1, Object obj2, Object obj3, Object obj4, Object obj5) {
        this(new Object[]{obj1, obj2, obj3, obj4, obj5});
    }
    public EventData(Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
        this(new Object[]{obj1, obj2, obj3, obj4, obj5, obj6});
    }
    public EventData(Object[] objs) {
        objects.addAll(Arrays.asList(objs));
    }
    public EventData(List<Object> objects) {
        this(objects.toArray());
    }
    public<T> T getArg(int index) {
        return (T) (this.objects.get(index));
    }
    public int getArgCount() {
        return objects.size();
    }
    public void cancel() {
        cancelled = NullBool.TRUE;
    }
    public boolean isCancelled() {
        return cancelled.toBool();
    }

    public void setCancelled(boolean cancelled) {
        if (cancelled) {
            this.cancelled = NullBool.TRUE;
            return;
        }
        this.cancelled = NullBool.FALSE;
    }

    public NullBool isCancelledAsNullBool() {
        return cancelled;
    }
}
