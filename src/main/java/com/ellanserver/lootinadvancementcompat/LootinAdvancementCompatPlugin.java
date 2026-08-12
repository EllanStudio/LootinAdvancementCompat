package com.ellanserver.lootinadvancementcompat;

import com.github.sachin.lootin.api.LootinInventoryOpenEvent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.loot.Lootable;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class LootinAdvancementCompatPlugin extends JavaPlugin implements Listener {

    private static final NamespacedKey WAR_PIGS = NamespacedKey.minecraft("nether/loot_bastion");

    private List<NamespacedKey> lootTableKeys;
    private boolean missingAdvancementLogged;
    private boolean unknownCriterionLogged;

    @Override
    public void onEnable() {
        Plugin lootin = getServer().getPluginManager().getPlugin("Lootin");
        if (lootin == null || !lootin.isEnabled()) {
            getLogger().severe("Lootin is not enabled; disabling compatibility bridge.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Lootin historically misspells this PDC key. Keep the corrected spelling
        // as a forward-compatible fallback for maintained forks.
        lootTableKeys = List.of(
                new NamespacedKey(lootin, "lotttable"),
                new NamespacedKey(lootin, "loottable")
        );

        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Enabled lightweight War Pigs compatibility for Lootin containers.");
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onLootinInventoryOpen(LootinInventoryOpenEvent event) {
        String lootTable = readLootTable(event.getLootable());
        String criterion = BastionLootTables.criterionFor(lootTable);
        if (criterion == null) {
            return;
        }

        Player player = event.getPlayer();
        Advancement advancement = Bukkit.getAdvancement(WAR_PIGS);
        if (advancement == null) {
            if (!missingAdvancementLogged) {
                missingAdvancementLogged = true;
                getLogger().warning("Vanilla advancement minecraft:nether/loot_bastion is unavailable.");
            }
            return;
        }

        AdvancementProgress progress = player.getAdvancementProgress(advancement);
        if (progress.isDone()) {
            return;
        }

        if (!progress.getRemainingCriteria().contains(criterion)) {
            if (!unknownCriterionLogged) {
                unknownCriterionLogged = true;
                getLogger().warning("War Pigs criterion is unavailable for loot table " + lootTable
                        + "; no advancement was granted.");
            }
            return;
        }

        if (progress.awardCriteria(criterion)) {
            getLogger().info("Restored War Pigs advancement trigger for " + player.getName()
                    + " using " + lootTable + '.');
        }
    }

    private String readLootTable(Lootable lootable) {
        if (!(lootable instanceof PersistentDataHolder holder)) {
            return null;
        }

        PersistentDataContainer data = holder.getPersistentDataContainer();
        for (NamespacedKey key : lootTableKeys) {
            String value = data.get(key, PersistentDataType.STRING);
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }
}
