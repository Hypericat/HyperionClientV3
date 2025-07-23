package me.hypericats.hyperionclientv3.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.GetHitboxListener;
import me.hypericats.hyperionclientv3.events.eventData.GetHitboxData;
import me.hypericats.hyperionclientv3.mixinInterface.IEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.CollisionView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import org.spongepowered.include.com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Mixin(Entity.class)
public abstract class EntityMixin implements IEntity {

	@Unique
	private static World world;
	@Unique
	private static Entity entity;

	@Shadow
	private Box boundingBox;

	@Shadow
	@Nullable
	private Entity.@Nullable RemovalReason removalReason;

	@Inject(at = @At("HEAD"), method = {"getBoundingBox"}, cancellable = true)
	private void getBoundingBox(CallbackInfoReturnable<Box> cir) {
		GetHitboxData data = new GetHitboxData((Entity) (Object) this, this.boundingBox);
		EventHandler.onEvent(GetHitboxListener.class, data);

		Box newBox = data.getNewBox();
		if (newBox != null) {
			cir.setReturnValue(newBox);
		}
	}

	public Box getRealBoundingBox() {
		return this.boundingBox;
	}


	// Most/all of code below stolen from carpet-tis

	//#if MC >= 12100
	 @Unique
	 private static final ThreadLocal<Boolean> movementOk = ThreadLocal.withInitial(() -> false);

	 @ModifyVariable(
	 		method = "adjustMovementForCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Lnet/minecraft/world/World;Ljava/util/List;)Lnet/minecraft/util/math/Vec3d;",
	 		at = @At("HEAD"),
	 		argsOnly = true
	 )
	 private static Vec3d optimizedFastEntityMovement_checkMovement(Vec3d movement) {
		 movementOk.set(movement.lengthSquared() >= 16d);
	 	return movement;
	 }
	//#endif

	@WrapOperation(
			method = "findCollisionsForMovement",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;getBlockCollisions(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/lang/Iterable;"
			)
	)
	private static Iterable<VoxelShape>
	optimizedFastEntityMovement_dontUseThatLargeBlockCollisions(World world, Entity entity, Box box, Operation<Iterable<VoxelShape>> original) {
		 if (movementOk.get()) {
			 EntityMixin.entity = entity;
			 EntityMixin.world = world;
			 return Collections.emptyList();
		 }
		 EntityMixin.entity = null;
		 EntityMixin.world = null;
		 return original.call(world, entity, box);
	}

	@ModifyExpressionValue(
			method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/List;)Lnet/minecraft/util/math/Vec3d;",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/List;isEmpty()Z",
					ordinal = 0
			)
	)
	private static boolean optimizedFastEntityMovement_theCollisionsListParameterIsIncompleteSoDontReturnEvenIfItIsEmpty(boolean isEmpty) {
		if (entity != null && world != null) {
			isEmpty = false;
		}
		return isEmpty;
	}

	@ModifyArgs(
			//#if MC >= 11800
			method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/List;)Lnet/minecraft/util/math/Vec3d;",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11800
					target = "Lnet/minecraft/util/shape/VoxelShapes;calculateMaxOffset(Lnet/minecraft/util/math/Direction$Axis;Lnet/minecraft/util/math/Box;Ljava/lang/Iterable;D)D"
			)
			//#if MC < 12105
			, require = 4
			//#endif
	)
	private static void optimizedFastEntityMovement_useTheAxisOnlyBlockCollisionsForSpeed(Args args) {
		if (entity != null && world != null) {
			// Direction.Axis axis, Box box, (Iterable<VoxelShape> | Stream<VoxelShape>) shapes, double maxDist
			Direction.Axis axis = args.get(0);
			Box entityBoundingBox = args.get(1);
			List<VoxelShape>
					entityAndBorderCollisions = args.get(2);
			double maxDist = args.get(3);


			Iterable<VoxelShape>
					//#endif
					blockCollisions = getAxisOnlyBlockCollision(axis, entity, maxDist, world, entityBoundingBox);

			// order: (entity, border), block
			//#if MC >= 11800
			List<VoxelShape> voxelShapeList = Lists.newArrayList();
			voxelShapeList.addAll(entityAndBorderCollisions);
			blockCollisions.forEach(voxelShapeList::add);
			args.set(2, voxelShapeList);
		}
	}

	@Unique
	private static Iterable<VoxelShape> getAxisOnlyBlockCollision(Direction.Axis axis, Entity entity, double movementOnAxis, World world, Box box) {
		Vec3d axisOnlyMovement = switch (axis) {
            case X -> new Vec3d(movementOnAxis, 0.0D, 0.0D);
            case Y -> new Vec3d(0.0D, movementOnAxis, 0.0D);
            case Z -> new Vec3d(0.0D, 0.0D, movementOnAxis);
        };

        return world.getBlockCollisions(entity, box.stretch(axisOnlyMovement));
	}

}