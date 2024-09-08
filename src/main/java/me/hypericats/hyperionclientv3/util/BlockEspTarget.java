package me.hypericats.hyperionclientv3.util;

import me.hypericats.hyperionclientv3.enums.EspColorType;
import me.hypericats.hyperionclientv3.enums.EspType;
import net.minecraft.block.Block;

public class BlockEspTarget {
    private Block blockType;
    private double maxRange;
    private EspType espType;
    private EspColorType espColorType;
    private double extraBoxSize;
    private int innerBoxAlpha;
    private int outerBoxAlpha;
    private int tracerAlpha;
    private boolean renderOuterBox;
    private boolean renderInnerBox;
    private int defaultColor;
    private boolean enabled;

    private BlockEspTarget(builder builder) {
        this.blockType = builder.blockType;
        this.maxRange = builder.maxRange;;
        this.espType = builder.espType;
        this.espColorType = builder.espColorType;
        this.extraBoxSize = builder.extraBoxSize;
        this.innerBoxAlpha = builder.innerBoxAlpha;
        this.outerBoxAlpha = builder.outerBoxAlpha;
        this.tracerAlpha = builder.tracerAlpha;
        this.renderOuterBox = builder.renderOuterBox;
        this.renderInnerBox = builder.renderInnerBox;
        this.defaultColor = builder.defaultColor;
        this.enabled = builder.enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Block getBlockType() {
        return blockType;
    }

    public void setBlockType(Block blockType) {
        this.blockType = blockType;
    }

    public double getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(double maxRange) {
        this.maxRange = maxRange;
    }

    public EspType getEspType() {
        return espType;
    }

    public void setEspType(EspType espType) {
        this.espType = espType;
    }

    public EspColorType getEspColorType() {
        return espColorType;
    }

    public void setEspColorType(EspColorType espColorType) {
        this.espColorType = espColorType;
    }

    public double getExtraBoxSize() {
        return extraBoxSize;
    }

    public void setExtraBoxSize(double extraBoxSize) {
        this.extraBoxSize = extraBoxSize;
    }

    public int getInnerBoxAlpha() {
        return innerBoxAlpha;
    }

    public void setInnerBoxAlpha(int innerBoxAlpha) {
        this.innerBoxAlpha = innerBoxAlpha;
    }

    public int getOuterBoxAlpha() {
        return outerBoxAlpha;
    }

    public void setOuterBoxAlpha(int outerBoxAlpha) {
        this.outerBoxAlpha = outerBoxAlpha;
    }

    public int getTracerAlpha() {
        return tracerAlpha;
    }

    public void setTracerAlpha(int tracerAlpha) {
        this.tracerAlpha = tracerAlpha;
    }

    public boolean isRenderOuterBox() {
        return renderOuterBox;
    }

    public void setRenderOuterBox(boolean renderOuterBox) {
        this.renderOuterBox = renderOuterBox;
    }

    public boolean isRenderInnerBox() {
        return renderInnerBox;
    }

    public void setRenderInnerBox(boolean renderInnerBox) {
        this.renderInnerBox = renderInnerBox;
    }

    public int getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(int defaultColor) {
        this.defaultColor = defaultColor;
    }

    //Builder
    public static class builder {
        private final Block blockType;

        private double maxRange = 150d;
        private EspType espType = EspType.BOX;
        private EspColorType espColorType = EspColorType.STATIC;
        private double extraBoxSize = 0d;
        private int innerBoxAlpha = 30;
        private int outerBoxAlpha = 255;
        private int tracerAlpha = 255;
        private boolean renderOuterBox = true;
        private boolean renderInnerBox = true;
        private int defaultColor = -1;
        private boolean enabled = true;

        public builder(Block blockType) {
            this.blockType = blockType;
        }

        public builder maxRange(double maxRange) {
            this.maxRange = maxRange;
            return this;
        }

        public builder espType(EspType espType) {
            this.espType = espType;
            return this;
        }

        public builder espColorType(EspColorType espColorType) {
            this.espColorType = espColorType;
            return this;
        }

        public builder extraBoxSize(double extraBoxSize) {
            this.extraBoxSize = extraBoxSize;
            return this;
        }

        public builder innerBoxAlpha(int innerBoxAlpha) {
            this.innerBoxAlpha = innerBoxAlpha;
            return this;
        }

        public builder outerBoxAlpha(int outerBoxAlpha) {
            this.outerBoxAlpha = outerBoxAlpha;
            return this;
        }

        public builder tracerAlpha(int tracerAlpha) {
            this.tracerAlpha = tracerAlpha;
            return this;
        }

        public builder renderOuterBox(boolean renderOuterBox) {
            this.renderOuterBox = renderOuterBox;
            return this;
        }

        public builder renderInnerBox(boolean renderInnerBox) {
            this.renderInnerBox = renderInnerBox;
            return this;
        }

        public builder defaultColor(int defaultColor) {
            this.defaultColor = defaultColor;
            return this;
        }
        public builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public BlockEspTarget build() {
            return new BlockEspTarget(this);
        }
    }

}
