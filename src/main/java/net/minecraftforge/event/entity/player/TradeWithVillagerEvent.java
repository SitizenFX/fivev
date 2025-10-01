/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.minecraftforge.event.entity.player;

import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.eventbus.api.bus.EventBus;
import net.minecraftforge.eventbus.api.event.RecordEvent;
import net.minecraftforge.fml.LogicalSide;
import org.jetbrains.annotations.ApiStatus;

/**
 * Fired when a player trades with an {@link AbstractVillager}.
 *
 * <p>This event is fired only on the {@linkplain LogicalSide#SERVER logical server}.</p>
 *
 * @param getMerchantOffer the {@link MerchantOffer} selected by the player to trade with
 * @param getAbstractVillager the villager the player traded with
 */
public record TradeWithVillagerEvent(
        Player getEntity,
        MerchantOffer getMerchantOffer,
        AbstractVillager getAbstractVillager
) implements RecordEvent, PlayerEvent {
    public static final EventBus<TradeWithVillagerEvent> BUS = EventBus.create(TradeWithVillagerEvent.class);

    @ApiStatus.Internal
    public TradeWithVillagerEvent {}
}
