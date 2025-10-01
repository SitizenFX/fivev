/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.event.entity.player;

import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.bus.CancellableEventBus;
import net.minecraftforge.eventbus.api.bus.EventBus;
import net.minecraftforge.eventbus.api.event.MutableEvent;
import net.minecraftforge.eventbus.api.event.RecordEvent;
import net.minecraftforge.eventbus.api.event.characteristic.Cancellable;
import org.jspecify.annotations.NullMarked;

/**
 * PlayerXpEvent is fired whenever an event involving player experience occurs. <br>
 * If a method utilizes this {@link net.minecraftforge.eventbus.api.Event} as its parameter, the method will
 * receive every child event of this class.<br>
 * <br>
 * All children of this event are fired on the {@link MinecraftForge#EVENT_BUS}.
 */
public sealed interface PlayerXpEvent extends PlayerEvent {
    /**
     * This event is fired after the player collides with an experience orb, but before the player has been given the experience.
     * It can be cancelled, and no further processing will be done.
     */
    @NullMarked
    record PickupXp(Player getEntity, ExperienceOrb getOrb) implements Cancellable, PlayerXpEvent, RecordEvent {
        public static final CancellableEventBus<PickupXp> BUS = CancellableEventBus.create(PickupXp.class);
    }

    /**
     * This event is fired when the player's experience changes through the {@link Player#giveExperiencePoints(int)} method.
     * It can be cancelled, and no further processing will be done.
     */
    @NullMarked
    final class XpChange extends MutableEvent implements Cancellable, PlayerXpEvent {
        public static final CancellableEventBus<XpChange> BUS = CancellableEventBus.create(XpChange.class);

        private final Player player;
        private int amount;

        public XpChange(Player player, int amount) {
            this.player = player;
            this.amount = amount;
        }

        @Override
        public Player getEntity() {
            return this.player;
        }

        public int getAmount() {
            return this.amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }

    /**
     * This event is fired when the player's experience level changes through the {@link Player#giveExperienceLevels(int)} method.
     * It can be cancelled, and no further processing will be done.
     */
    @NullMarked
    final class LevelChange extends MutableEvent implements Cancellable, PlayerXpEvent {
        public static final CancellableEventBus<LevelChange> BUS = CancellableEventBus.create(LevelChange.class);

        private final Player player;
        private int levels;

        public LevelChange(Player player, int levels) {
            this.player = player;
            this.levels = levels;
        }

        @Override
        public Player getEntity() {
            return this.player;
        }

        public int getLevels() {
            return this.levels;
        }

        public void setLevels(int levels) {
            this.levels = levels;
        }
    }
}
