package com.ellanserver.lootinadvancementcompat;

import java.util.Map;

final class BastionLootTables {

    private static final Map<String, String> CRITERIA = Map.of(
            "minecraft:chests/bastion_bridge", "loot_bastion_bridge",
            "minecraft:chests/bastion_hoglin_stable", "loot_bastion_hoglin_stable",
            "minecraft:chests/bastion_other", "loot_bastion_other",
            "minecraft:chests/bastion_treasure", "loot_bastion_treasure"
    );

    private BastionLootTables() {
    }

    static String criterionFor(String lootTable) {
        if (lootTable == null) {
            return null;
        }
        return CRITERIA.get(lootTable);
    }
}
