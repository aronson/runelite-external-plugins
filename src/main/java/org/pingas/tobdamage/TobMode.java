package org.pingas.tobdamage;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.gameval.NpcID;

import java.util.Map;
import java.util.Set;

import java.util.stream.Stream;

abstract class TobMode {
    Set<Integer> maidenSpawns;
    Set<Integer> verzikIDs;

    @Getter
    TobPhase currentPhase;
    @Getter
    TobRooms rooms;

    ImmutableSet<Integer> getNpcIDs() {
        return rooms.phaseMap.get(currentPhase);
    }

    void onHitsplat(HitsplatApplied hitsplatApplied, NPC npc, Map<TobPhase, TobDamageCounterPlugin.Damage> damageMap, TobDamageCounterConfig config) {
        Hitsplat hitsplat = hitsplatApplied.getHitsplat();

        if (currentPhase == null) {
            return;
        }

        // Filter out other NPCs (not Maiden, Verzik) for MVP?
        if (config.showMVPDamage()) {
            // Guard clause for maiden spawns
            if (currentPhase == TobPhase.Maiden && maidenSpawns.contains(npc.getId())) {
                return;
            }

            // Guard clause for Verzik crabs/web/etc.
            if ((Stream.of(TobPhase.VerzikPhase1, TobPhase.VerzikPhase2, TobPhase.VerzikPhase3)
                    .anyMatch(tobPhase -> currentPhase == tobPhase))
                    && !verzikIDs.contains(npc.getId())) {
                return;
            }
        }

        // Damage done by player
        if (hitsplat.isMine()) {
            damageMap.get(currentPhase).addDamage(hitsplat.getAmount(), true);
            // Damage done by allies
        } else if (hitsplat.isOthers()) {
            damageMap.get(currentPhase).addDamage(hitsplat.getAmount(), false);
            // Healing done by party
        } else if (hitsplat.getHitsplatType() == HitsplatID.HEAL) {
            damageMap.get(currentPhase).addHealing(hitsplat.getAmount());
        }
    }
}

class HardTobMode extends TobMode {
    HardTobMode() {
        maidenSpawns = ImmutableSet.of(NpcID.MAIDEN_BLOOD_SLUG_HARD, NpcID.MAIDEN_ELEMENTAL_HARD);
        verzikIDs = ImmutableSet.of(NpcID.VERZIK_INITIAL_HARD, NpcID.VERZIK_PHASE2_HARD, NpcID.VERZIK_PHASE3_HARD);
        rooms = new HardTobRooms();
    }
}

class NormalTobMode extends TobMode {
    NormalTobMode() {
        maidenSpawns = ImmutableSet.of(NpcID.MAIDEN_ELEMENTAL_HARD);
        verzikIDs = ImmutableSet.of(NpcID.VERZIK_INITIAL, NpcID.VERZIK_PHASE2, NpcID.VERZIK_PHASE3);
        rooms = new NormalTobRooms();
    }
}
