package me.hypericats.hyperionclientv3.moduleOptions;

public class NumberOption<T extends Number> extends ModuleOption<T> {
    private final Class<?> numberClass;
    public NumberOption(boolean shouldSave, String name, T value) {
        super(shouldSave, name, value);
        this.numberClass = value.getClass();
    }
    public void setValue(Number value) {
        if (numberClass == Integer.class) {
            this.value = (T) (Object) value.intValue();
            return;
        }
        if (numberClass == Short.class) {
            this.value = (T) (Object) value.shortValue();
            return;
        }
        if (numberClass == Byte.class) {
            this.value = (T) (Object) value.byteValue();
            return;
        }
        if (numberClass == Long.class) {
            this.value = (T) (Object) value.longValue();
            return;
        }
        if (numberClass == Float.class) {
            this.value = (T) (Object) value.floatValue();
            return;
        }
        if (numberClass == Double.class) {
            this.value = (T) (Object) value.doubleValue();
            return;
        }
    }
    public T getValue() {
        if (numberClass == Integer.class) {
            return (T) (Object) value.intValue();
        }
        if (numberClass == Short.class) {
            return (T) (Object) value.shortValue();
        }
        if (numberClass == Byte.class) {
            return (T) (Object) value.byteValue();
        }
        if (numberClass == Long.class) {
            return (T) (Object) value.longValue();
        }
        if (numberClass == Float.class) {
            return (T) (Object) value.floatValue();
        }
        if (numberClass == Double.class) {
            return (T) (Object) value.doubleValue();
        }
        return null;
    }
}
