package me.hypericats.hyperionclientv3.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import org.joml.Matrix4f;

public class RenderUtil {
    public static void fillWithBorder(DrawContext context, int x1, int y1, int x2, int y2, int innerColor, int borderColor, int borderThickness) {
        fillWithBorder(context, x1, y1, x2, y2, innerColor, borderColor, borderThickness, false);
    }
    public static void fillWithBorder(DrawContext context, int x1, int y1, int x2, int y2, int innerColor, int borderColor, int borderThickness, boolean ignoreBottom) {
        context.fill(x1, y1, x2, y2, innerColor);

        context.fill(x1, y1, x2, y1 + borderThickness, borderColor);
        context.fill(x2 - borderThickness, y2 - borderThickness, x2, y1 + borderThickness, borderColor);
        context.fill(x1, y2 - borderThickness, x1 + borderThickness, y1 + borderThickness, borderColor);

        if (!ignoreBottom)
            context.fill(x1, y2 - borderThickness, x2, y2, borderColor);
    }
    public static void drawSolidBox(Box bb, MatrixStack matrixStack)
    {
        float minX = (float)bb.minX;
        float minY = (float)bb.minY;
        float minZ = (float)bb.minZ;
        float maxX = (float)bb.maxX;
        float maxY = (float)bb.maxY;
        float maxZ = (float)bb.maxZ;

        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

        bufferBuilder.vertex(matrix, minX, minY, minZ);
        bufferBuilder.vertex(matrix, maxX, minY, minZ);
        bufferBuilder.vertex(matrix, maxX, minY, maxZ);
        bufferBuilder.vertex(matrix, minX, minY, maxZ);

        bufferBuilder.vertex(matrix, minX, maxY, minZ);
        bufferBuilder.vertex(matrix, minX, maxY, maxZ);
        bufferBuilder.vertex(matrix, maxX, maxY, maxZ);
        bufferBuilder.vertex(matrix, maxX, maxY, minZ);

        bufferBuilder.vertex(matrix, minX, minY, minZ);
        bufferBuilder.vertex(matrix, minX, maxY, minZ);
        bufferBuilder.vertex(matrix, maxX, maxY, minZ);
        bufferBuilder.vertex(matrix, maxX, minY, minZ);

        bufferBuilder.vertex(matrix, maxX, minY, minZ);
        bufferBuilder.vertex(matrix, maxX, maxY, minZ);
        bufferBuilder.vertex(matrix, maxX, maxY, maxZ);
        bufferBuilder.vertex(matrix, maxX, minY, maxZ);

        bufferBuilder.vertex(matrix, minX, minY, maxZ);
        bufferBuilder.vertex(matrix, maxX, minY, maxZ);
        bufferBuilder.vertex(matrix, maxX, maxY, maxZ);
        bufferBuilder.vertex(matrix, minX, maxY, maxZ);

        bufferBuilder.vertex(matrix, minX, minY, minZ);
        bufferBuilder.vertex(matrix, minX, minY, maxZ);
        bufferBuilder.vertex(matrix, minX, maxY, maxZ);
        bufferBuilder.vertex(matrix, minX, maxY, minZ);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }
    public static void drawOutlinedBox(Box box, MatrixStack matrices) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);

        float minX = (float) box.minX;
        float minY = (float) box.minY;
        float minZ = (float) box.minZ;
        float maxX = (float) box.maxX;
        float maxY = (float) box.maxY;
        float maxZ = (float) box.maxZ;

        bufferBuilder.vertex(matrix, minX, minY, minZ);
        bufferBuilder.vertex(matrix, maxX, minY, minZ);

        bufferBuilder.vertex(matrix, maxX, minY, minZ);
        bufferBuilder.vertex(matrix, maxX, minY, maxZ);

        bufferBuilder.vertex(matrix, maxX, minY, maxZ);
        bufferBuilder.vertex(matrix, minX, minY, maxZ);

        bufferBuilder.vertex(matrix, minX, minY, maxZ);
        bufferBuilder.vertex(matrix, minX, minY, minZ);

        bufferBuilder.vertex(matrix, minX, minY, minZ);
        bufferBuilder.vertex(matrix, minX, maxY, minZ);

        bufferBuilder.vertex(matrix, maxX, minY, minZ);
        bufferBuilder.vertex(matrix, maxX, maxY, minZ);

        bufferBuilder.vertex(matrix, maxX, minY, maxZ);
        bufferBuilder.vertex(matrix, maxX, maxY, maxZ);

        bufferBuilder.vertex(matrix, minX, minY, maxZ);
        bufferBuilder.vertex(matrix, minX, maxY, maxZ);

        bufferBuilder.vertex(matrix, minX, maxY, minZ);
        bufferBuilder.vertex(matrix, maxX, maxY, minZ);

        bufferBuilder.vertex(matrix, maxX, maxY, minZ);
        bufferBuilder.vertex(matrix, maxX, maxY, maxZ);

        bufferBuilder.vertex(matrix, maxX, maxY, maxZ);
        bufferBuilder.vertex(matrix, minX, maxY, maxZ);

        bufferBuilder.vertex(matrix, minX, maxY, maxZ);
        bufferBuilder.vertex(matrix, minX, maxY, minZ);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public static boolean isBetween(int cordX, int cordY, int x1, int y1, int x2, int y2) {
        int i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }
        return cordX < x1 && cordX > x2 && cordY < y1 && cordY > y2;
    }

}
