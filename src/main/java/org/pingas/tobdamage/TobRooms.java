package org.pingas.tobdamage;

import com.google.common.collect.ImmutableSet;
import java.util.Map;

abstract class TobRooms {
    Map<TobPhase, ImmutableSet<Integer>> phaseMap;
}
