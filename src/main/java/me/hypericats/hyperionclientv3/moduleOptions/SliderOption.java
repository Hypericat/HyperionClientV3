package me.hypericats.hyperionclientv3.moduleOptions;

public class SliderOption<T extends Number> extends ModuleOption<T> {
    private T maxValue;
    private T minValue;
    private T sliderSnap;
    public SliderOption(boolean shouldSave, String name, T value, T maxValue, T minValue, T sliderSnap) {
        super(shouldSave, name, value);
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.sliderSnap = sliderSnap;
    }

    public T getMaxValue() {
        return maxValue;
    }

    public T getMinValue() {
        return minValue;
    }

    public T getSliderSnap() {
        return sliderSnap;
    }

    public void setMaxValue(T maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinValue(T minValue) {
        this.minValue = minValue;
    }

    public void setSliderSnap(T sliderSnap) {
        this.sliderSnap = sliderSnap;
    }

    @Override
    public void setValue(Object value) {
        super.setValue(value, false);
    }
}
