/*
 * Copyright (c) 2025, Aronson <https://github.com/aronson>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.pingas.tobdamage;

import com.google.common.collect.ImmutableSet;
import net.runelite.api.gameval.NpcID;
import java.util.HashMap;

class HardTobRooms extends TobRooms
{
    HardTobRooms() {
        phaseMap = new HashMap<>();
        ImmutableSet<Integer> maiden = ImmutableSet.of(
                NpcID.TOB_MAIDEN_100_HARD,
                NpcID.TOB_MAIDEN_70_HARD,
                NpcID.TOB_MAIDEN_50_HARD,
                NpcID.TOB_MAIDEN_30_HARD,
                NpcID.TOB_MAIDEN_DYING_A_HARD,
                NpcID.TOB_MAIDEN_DYING_B_HARD,
                NpcID.MAIDEN_ELEMENTAL_HARD,
                NpcID.MAIDEN_BLOOD_SLUG_HARD
        );
        phaseMap.put(TobPhase.Maiden, maiden);
        ImmutableSet<Integer> bloat = ImmutableSet.of(
                NpcID.TOB_BLOAT_HARD
        );
        phaseMap.put(TobPhase.Bloat, bloat);
        ImmutableSet<Integer> waves = ImmutableSet.of(
                NpcID.TOB_NYLOCAS_INCOMING_MELEE_HARD,
                NpcID.TOB_NYLOCAS_INCOMING_MAGIC_HARD,
                NpcID.TOB_NYLOCAS_INCOMING_RANGED_HARD,
                NpcID.TOB_NYLOCAS_BIG_INCOMING_MELEE_HARD,
                NpcID.TOB_NYLOCAS_BIG_INCOMING_MAGIC_HARD,
                NpcID.TOB_NYLOCAS_BIG_INCOMING_RANGED_HARD,
                NpcID.TOB_NYLOCAS_FIGHTING_MELEE_HARD,
                NpcID.TOB_NYLOCAS_FIGHTING_MAGIC_HARD,
                NpcID.TOB_NYLOCAS_FIGHTING_RANGED_HARD,
                NpcID.TOB_NYLOCAS_BIG_FIGHTING_MELEE_HARD,
                NpcID.TOB_NYLOCAS_BIG_FIGHTING_MAGIC_HARD,
                NpcID.TOB_NYLOCAS_BIG_FIGHTING_RANGED_HARD,
                NpcID.NYLOCAS_MINIBOSS_SPAWNING_HARD,
                NpcID.NYLOCAS_MINIBOSS_MELEE_HARD,
                NpcID.NYLOCAS_MINIBOSS_MAGIC_HARD,
                NpcID.NYLOCAS_MINIBOSS_RANGED_HARD
        );
        phaseMap.put(TobPhase.NylocasWaves, waves);
        ImmutableSet<Integer> nylocas = ImmutableSet.of(
                NpcID.NYLOCAS_BOSS_SPAWNING_HARD,
                NpcID.NYLOCAS_BOSS_MELEE_HARD,
                NpcID.NYLOCAS_BOSS_MAGIC_HARD,
                NpcID.NYLOCAS_BOSS_RANGED_HARD
        );
        phaseMap.put(TobPhase.NylocasBoss, nylocas);
        ImmutableSet<Integer> sotetseg = ImmutableSet.of(
                NpcID.TOB_SOTETSEG_COMBAT_HARD,
                NpcID.TOB_SOTETSEG_CREEPER_HARD,
                NpcID.TOB_SOTETSEG_NONCOMBAT_HARD
        );
        phaseMap.put(TobPhase.Sotetseg, sotetseg);
        ImmutableSet<Integer> xarpus = ImmutableSet.of(
                NpcID.TOB_XARPUS_COMBAT_HARD,
                NpcID.TOB_XARPUS_STATIC_HARD,
                NpcID.TOB_XARPUS_FEEDING_HARD,
                NpcID.XARPUS_DEATH_HARD
        );
        phaseMap.put(TobPhase.Xarpus, xarpus);
        ImmutableSet<Integer> verzikPhase1 = ImmutableSet.of(
                NpcID.VERZIK_PHASE1_HARD
        );
        phaseMap.put(TobPhase.VerzikPhase1, verzikPhase1);
        ImmutableSet<Integer> verzikPhase2 = ImmutableSet.of(
                NpcID.VERZIK_PHASE1_TO2_TRANSITION_HARD,
                NpcID.VERZIK_PHASE2_HARD,
                NpcID.VERZIK_NYLOCAS_MAGIC_HARD,
                NpcID.VERZIK_NYLOCAS_MELEE_HARD,
                NpcID.VERZIK_NYLOCAS_RANGED_HARD,
                NpcID.TOB_VERZIK_PHASE2_ARMOUREDNYLOCAS_HARD,
                NpcID.TOB_VERZIK_PHASE2_BLOODNYLOCAS_HARD
        );
        phaseMap.put(TobPhase.VerzikPhase2, verzikPhase2);
        ImmutableSet<Integer> verzikPhase3 = ImmutableSet.of(
                NpcID.VERZIK_PHASE2_TO3_TRANSITION_HARD,
                NpcID.VERZIK_PHASE3_HARD,
                NpcID.VERZIK_NYLOCAS_MAGIC_HARD,
                NpcID.VERZIK_NYLOCAS_MELEE_HARD,
                NpcID.VERZIK_NYLOCAS_RANGED_HARD,
                NpcID.VERZIK_WEB_NPC_HARD
        );
        phaseMap.put(TobPhase.VerzikPhase3, verzikPhase3);
    }
}
