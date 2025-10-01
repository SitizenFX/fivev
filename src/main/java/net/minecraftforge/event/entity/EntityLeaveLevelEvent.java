/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.event.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.LevelCallback;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.bus.EventBus;
import net.minecraftforge.eventbus.api.event.RecordEvent;

/**
 * This event is fired whenever an {@link Entity} leaves a {@link Level}.
 * This event is fired whenever an entity is removed from the level in {@link LevelCallback#onTrackingEnd(Object)}.
 * <p>
 * This event is fired on the {@linkplain MinecraftForge#EVENT_BUS main Forge event bus} on both logical sides.
 **/
public record EntityLeaveLevelEvent(Entity getEntity, Level getLevel) implements RecordEvent, EntityEvent {
    public static final EventBus<EntityLeaveLevelEvent> BUS = EventBus.create(EntityLeaveLevelEvent.class);

    /**
     * {@return the level the entity is set to leave}
     */
    public Level getLevel() {
        return getLevel;
    }
}
