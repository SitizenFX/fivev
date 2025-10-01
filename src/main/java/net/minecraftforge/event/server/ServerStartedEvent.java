/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.event.server;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.eventbus.api.bus.EventBus;

/**
 * Called after {@link ServerStartingEvent} when the server is available and ready to play.
 *
 * @author cpw
 */
public record ServerStartedEvent(MinecraftServer getServer) implements ServerLifecycleEvent {
    public static final EventBus<ServerStartedEvent> BUS = EventBus.create(ServerStartedEvent.class);
}
