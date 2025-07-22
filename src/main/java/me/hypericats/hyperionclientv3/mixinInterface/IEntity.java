package me.hypericats.hyperionclientv3.mixinInterface;

import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public interface IEntity {
    public Box getRealBoundingBox();

    public Vec3d adjustMovementCollision(Vec3d movement);

}
