/*
 * Copyright (c) 2020, Trevor <https://github.com/Trevor159>, 2025 Aronson <https://github.com/aronson>
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

class NormalTobRooms extends TobRooms {
    NormalTobRooms() {
        phaseMap = new HashMap<>();
        ImmutableSet<Integer> maiden = ImmutableSet.of(
                NpcID.TOB_MAIDEN_100,
                NpcID.TOB_MAIDEN_70,
                NpcID.TOB_MAIDEN_50,
                NpcID.TOB_MAIDEN_30,
                NpcID.TOB_MAIDEN_DYING_A,
                NpcID.TOB_MAIDEN_DYING_B,
                NpcID.MAIDEN_ELEMENTAL,
                NpcID.MAIDEN_BLOOD_SLUG
        );
        phaseMap.put(TobPhase.Maiden, maiden);
        ImmutableSet<Integer> bloat = ImmutableSet.of(
                NpcID.TOB_BLOAT
        );
        phaseMap.put(TobPhase.Bloat, bloat);
        ImmutableSet<Integer> waves = ImmutableSet.of(
                NpcID.TOB_NYLOCAS_INCOMING_MELEE,
                NpcID.TOB_NYLOCAS_INCOMING_MAGIC,
                NpcID.TOB_NYLOCAS_INCOMING_RANGED,
                NpcID.TOB_NYLOCAS_BIG_INCOMING_MELEE,
                NpcID.TOB_NYLOCAS_BIG_INCOMING_MAGIC,
                NpcID.TOB_NYLOCAS_BIG_INCOMING_RANGED,
                NpcID.TOB_NYLOCAS_FIGHTING_MELEE,
                NpcID.TOB_NYLOCAS_FIGHTING_MAGIC,
                NpcID.TOB_NYLOCAS_FIGHTING_RANGED,
                NpcID.TOB_NYLOCAS_BIG_FIGHTING_MELEE,
                NpcID.TOB_NYLOCAS_BIG_FIGHTING_MAGIC,
                NpcID.TOB_NYLOCAS_BIG_FIGHTING_RANGED
        );
        phaseMap.put(TobPhase.NylocasWaves, waves);
        ImmutableSet<Integer> nylocas = ImmutableSet.of(
                NpcID.NYLOCAS_BOSS_SPAWNING,
                NpcID.NYLOCAS_BOSS_MELEE,
                NpcID.NYLOCAS_BOSS_MAGIC,
                NpcID.NYLOCAS_BOSS_RANGED
        );
        phaseMap.put(TobPhase.NylocasBoss, nylocas);
        ImmutableSet<Integer> sotetseg = ImmutableSet.of(
                NpcID.TOB_SOTETSEG_NONCOMBAT,
                NpcID.TOB_SOTETSEG_COMBAT,
                NpcID.TOB_SOTETSEG_CREEPER
        );
        phaseMap.put(TobPhase.Sotetseg, sotetseg);
        ImmutableSet<Integer> xarpus = ImmutableSet.of(
                NpcID.TOB_XARPUS_COMBAT,
                NpcID.TOB_XARPUS_STATIC,
                NpcID.TOB_XARPUS_FEEDING,
                NpcID.XARPUS_DEATH
        );
        phaseMap.put(TobPhase.Xarpus, xarpus);
        ImmutableSet<Integer> verzikPhase1 = ImmutableSet.of(NpcID.VERZIK_PHASE1);
        phaseMap.put(TobPhase.VerzikPhase1, verzikPhase1);
        ImmutableSet<Integer> verzikPhase2 = ImmutableSet.of(
                NpcID.VERZIK_PHASE1_TO2_TRANSITION,
                NpcID.VERZIK_PHASE2,
                NpcID.VERZIK_NYLOCAS_MAGIC,
                NpcID.VERZIK_NYLOCAS_MELEE,
                NpcID.VERZIK_NYLOCAS_RANGED,
                NpcID.TOB_VERZIK_PHASE2_ARMOUREDNYLOCAS,
                NpcID.TOB_VERZIK_PHASE2_BLOODNYLOCAS
        );
        phaseMap.put(TobPhase.VerzikPhase2, verzikPhase2);
        ImmutableSet<Integer> verzikPhase3 = ImmutableSet.of(
                NpcID.VERZIK_PHASE2_TO3_TRANSITION,
                NpcID.VERZIK_PHASE3,
                NpcID.VERZIK_NYLOCAS_MAGIC,
                NpcID.VERZIK_NYLOCAS_MELEE,
                NpcID.VERZIK_NYLOCAS_RANGED,
                NpcID.VERZIK_WEB_NPC
        );
        phaseMap.put(TobPhase.VerzikPhase3, verzikPhase3);
    }
}
