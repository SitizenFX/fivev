/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.event.entity.living;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.bus.CancellableEventBus;
import net.minecraftforge.eventbus.api.event.MutableEvent;
import net.minecraftforge.eventbus.api.event.characteristic.Cancellable;

/**
 * LivingFallEvent is fired when an Entity is set to be falling.<br>
 * This event is fired whenever an Entity is set to fall in
 * {@link LivingEntity#causeFallDamage(double, float, DamageSource)}.<br>
 * <br>
 * This event is fired via the {@link net.minecraftforge.event.ForgeEventFactory#onLivingFall(LivingEntity, double, float)}.<br>
 * <br>
 * {@link #distance} contains the distance the Entity is to fall. If this event is canceled, this value is set to 0.0F.
 * <br>
 * This event is {@linkplain Cancellable cancellable}. If this event is canceled, the Entity does not fall.
 **/
public final class LivingFallEvent extends MutableEvent implements Cancellable, LivingEvent {
    public static final CancellableEventBus<LivingFallEvent> BUS = CancellableEventBus.create(LivingFallEvent.class);

    private final LivingEntity entity;
    private double distance;
    private float damageMultiplier;

    public LivingFallEvent(LivingEntity entity, double distance, float damageMultiplier) {
        this.entity = entity;
        this.distance = distance;
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    public LivingEntity getEntity() {
        return entity;
    }

    public double getDistance() { return this.distance; }
    public void setDistance(double distance) { this.distance = distance; }
    public float getDamageMultiplier() { return damageMultiplier; }
    public void setDamageMultiplier(float damageMultiplier) { this.damageMultiplier = damageMultiplier; }
}
