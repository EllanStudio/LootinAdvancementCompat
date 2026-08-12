package com.github.sachin.lootin.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.loot.Lootable;

/**
 * Compile-only declaration of Lootin's public event.
 *
 * This class is intentionally kept outside the main source set and is not
 * included in the plugin JAR. The real class is provided by Lootin at runtime.
 */
public abstract class LootinInventoryOpenEvent extends Event implements Cancellable {

    protected LootinInventoryOpenEvent() {
    }

    public abstract Lootable getLootable();

    public abstract Player getPlayer();
}
