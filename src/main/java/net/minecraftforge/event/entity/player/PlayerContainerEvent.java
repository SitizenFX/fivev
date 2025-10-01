/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.event.entity.player;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.eventbus.api.bus.EventBus;
import net.minecraftforge.eventbus.api.event.InheritableEvent;

public sealed interface PlayerContainerEvent extends PlayerEvent, InheritableEvent {
    EventBus<PlayerContainerEvent> BUS = EventBus.create(PlayerContainerEvent.class);

    AbstractContainerMenu getContainer();

    record Open(Player getEntity, AbstractContainerMenu getContainer) implements PlayerContainerEvent {
        public static final EventBus<Open> BUS = EventBus.create(Open.class);
    }

    record Close(Player getEntity, AbstractContainerMenu getContainer) implements PlayerContainerEvent {
        public static final EventBus<Close> BUS = EventBus.create(Close.class);
    }
}
