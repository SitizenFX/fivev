/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.event.entity.living;

import java.util.Collection;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.eventbus.api.bus.CancellableEventBus;
import net.minecraftforge.eventbus.api.event.RecordEvent;
import net.minecraftforge.eventbus.api.event.characteristic.Cancellable;

/**
 * LivingDropsEvent is fired when an Entity's death causes dropped items to appear.<br>
 * This event is fired whenever an Entity dies and drops items in
 * {@link LivingEntity#die(DamageSource)}.<br>
 * <br>
 * This event is fired via the {@link ForgeEventFactory#onLivingDrops(LivingEntity, DamageSource, Collection, int, boolean)} .<br>
 * <br>
 * @param getSource contains the DamageSource that caused the drop to occur.
 * @param getDrops contains the ArrayList of EntityItems that will be dropped.
 * @param isRecentlyHit determines whether the Entity doing the drop has recently been damaged.
 * <br>
 * This event is {@linkplain Cancellable cancellable}. If this event is cancelled, the Entity does not drop anything.
 */
public record LivingDropsEvent(
        LivingEntity getEntity,
        DamageSource getSource,
        Collection<ItemEntity> getDrops,
        boolean isRecentlyHit
) implements Cancellable, LivingEvent, RecordEvent {
    public static final CancellableEventBus<LivingDropsEvent> BUS = CancellableEventBus.create(LivingDropsEvent.class);
}
