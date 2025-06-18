/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.event.entity.player;

import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.HasResult;
import net.minecraftforge.common.util.Result;
import net.minecraftforge.eventbus.api.bus.EventBus;

import java.util.Optional;

/**
 * This event is fired when the game checks if players can sleep at this time.<br>
 * Failing this check will cause sleeping players to wake up and prevent awake players from sleeping.<br>
 *
 * This event has a result. {@link HasResult}<br>
 *
 * setResult(ALLOW) informs game that player can sleep at this time.<br>
 * setResult(DEFAULT) causes game to check !{@link Level#isDay()} instead.
 */
public final class SleepingTimeCheckEvent extends PlayerEvent implements HasResult {
    public static final EventBus<SleepingTimeCheckEvent> BUS = EventBus.create(SleepingTimeCheckEvent.class);

    private final Optional<BlockPos> sleepingLocation;
    private Result result = Result.DEFAULT;

    public SleepingTimeCheckEvent(Player player, Optional<BlockPos> sleepingLocation) {
        super(player);
        this.sleepingLocation = sleepingLocation;
    }

    /**
     * Note that the sleeping location may be an approximated one.
     * @return The player's sleeping location.
     */
    public Optional<BlockPos> getSleepingLocation() {
        return sleepingLocation;
    }

    @Override
    public Result getResult() {
        return this.result;
    }

    @Override
    public void setResult(Result result) {
        this.result = result;
    }
}
