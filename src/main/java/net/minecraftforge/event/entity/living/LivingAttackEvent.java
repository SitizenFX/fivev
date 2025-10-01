/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.event.entity.living;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeHooks;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.bus.CancellableEventBus;
import net.minecraftforge.eventbus.api.event.RecordEvent;
import net.minecraftforge.eventbus.api.event.characteristic.Cancellable;

/**
 * LivingAttackEvent is fired when a living Entity is attacked. <br>
 * This event is fired whenever an Entity is attacked in
 * {@link LivingEntity#hurt(DamageSource, float)} and
 * {@link Player#hurt(DamageSource, float)}. <br>
 * <br>
 * This event is fired via the {@link ForgeHooks#onLivingAttack(LivingEntity, DamageSource, float)}.<br>
 * <br>
 * This event is {@linkplain Cancellable cancellable}.<br>
 * If this event is canceled, the Entity does not take attack damage.
 *
 * @param getSource the source of the attack
 * @param getAmount the amount of damage dealt to the entity
 */
public record LivingAttackEvent(LivingEntity getEntity, DamageSource getSource, float getAmount)
        implements Cancellable, LivingEvent, RecordEvent {
    public static final CancellableEventBus<LivingAttackEvent> BUS = CancellableEventBus.create(LivingAttackEvent.class);
}
