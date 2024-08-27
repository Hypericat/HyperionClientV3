package me.hypericats.hyperionclientv3.events.eventData;

import me.hypericats.hyperionclientv3.event.EventData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AddStatusEffectData extends EventData {
    public StatusEffectInstance getStatusEffect() {
        return getArg(0);
    }
    public Entity getSourceEntity() {
        return getArg(1);
    }
    public AddStatusEffectData(StatusEffectInstance statusEffect, Entity sourceEntity) {
        super(statusEffect, sourceEntity);
    }
}
