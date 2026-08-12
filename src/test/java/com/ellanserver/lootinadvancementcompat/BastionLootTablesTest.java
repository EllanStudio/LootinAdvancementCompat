package com.ellanserver.lootinadvancementcompat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

final class BastionLootTablesTest {

    @Test
    void mapsEveryVanillaBastionLootTable() {
        assertEquals("loot_bastion_bridge",
                BastionLootTables.criterionFor("minecraft:chests/bastion_bridge"));
        assertEquals("loot_bastion_hoglin_stable",
                BastionLootTables.criterionFor("minecraft:chests/bastion_hoglin_stable"));
        assertEquals("loot_bastion_other",
                BastionLootTables.criterionFor("minecraft:chests/bastion_other"));
        assertEquals("loot_bastion_treasure",
                BastionLootTables.criterionFor("minecraft:chests/bastion_treasure"));
    }

    @Test
    void ignoresUnrelatedOrMissingLootTables() {
        assertNull(BastionLootTables.criterionFor("minecraft:chests/ancient_city"));
        assertNull(BastionLootTables.criterionFor(null));
    }
}
