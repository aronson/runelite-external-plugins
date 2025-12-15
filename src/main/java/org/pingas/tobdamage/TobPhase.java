package org.pingas.tobdamage;

enum TobPhase {
    Maiden,
    Bloat,
    NylocasWaves,
    NylocasBoss,
    Sotetseg,
    Xarpus,
    VerzikPhase1,
    VerzikPhase2,
    VerzikPhase3;

    static String GetPhaseName(TobPhase phase) {
        switch (phase) {
            case Maiden:
                return "Maiden";
            case Bloat:
                return "Bloat";
            case NylocasWaves:
                return "Nylocas Waves";
            case NylocasBoss:
                return "Nylocas Boss";
            case Sotetseg:
                return "Sotetseg";
            case Xarpus:
                return "Xarpus";
            case VerzikPhase1:
                return "Verzik P1";
            case VerzikPhase2:
                return "Verzik P2";
            case VerzikPhase3:
                return "Verzik P3";
        }
        return "";
    }
}
