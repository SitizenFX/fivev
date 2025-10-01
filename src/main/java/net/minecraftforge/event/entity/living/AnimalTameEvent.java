/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.event.entity.living;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.eventbus.api.bus.CancellableEventBus;
import net.minecraftforge.eventbus.api.event.RecordEvent;
import net.minecraftforge.eventbus.api.event.characteristic.Cancellable;

/**
 * This event is fired when an {@link Animal} is tamed. <br>
 * It is fired via {@link ForgeEventFactory#onAnimalTame(Animal, Player)}.
 * Forge fires this event for applicable vanilla animals, mods need to fire it themselves.
 * This event is {@linkplain Cancellable cancellable}. If cancelled, taming the animal will fail.
 */
public record AnimalTameEvent(Animal getAnimal, Player getTamer) implements Cancellable, LivingEvent, RecordEvent {
    public static final CancellableEventBus<AnimalTameEvent> BUS = CancellableEventBus.create(AnimalTameEvent.class);

    @Override
    public LivingEntity getEntity() {
        return getAnimal;
    }
}
