package org.pingas.tobdamage;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.gameval.SpotanimID;
import net.runelite.api.kit.KitType;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.text.DecimalFormat;
import java.util.*;


@Slf4j
@PluginDescriptor(name = "TOB Damage Counter", description = "Shows personal and total damage for each room in the theatre of blood", tags = {"counter", "tracker"})
public class TobDamageCounterPlugin extends Plugin {
    private static final Set<Integer> blacklistNPCs = ImmutableSet.of(NpcID.VERZIK_HARD_PILLAR_NPC, NpcID.VERZIK_PILLAR_NPC);
    private static final Set<Integer> SALVE_IDS = ImmutableSet.of(ItemID.SALVE_AMULET_E, ItemID.SALVE_AMULETEI, ItemID.SALVE_AMULET, ItemID.SALVE_AMULETI);
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###.##");
    private static final DecimalFormat DAMAGE_FORMAT = new DecimalFormat("#,###");

    private static final TobPhase[] phases = {
            TobPhase.Maiden,
            TobPhase.Bloat,
            TobPhase.NylocasWaves,
            TobPhase.NylocasBoss,
            TobPhase.Sotetseg,
            TobPhase.Xarpus,
            TobPhase.VerzikPhase1,
            TobPhase.VerzikPhase2,
            TobPhase.VerzikPhase3
    };
    private static final HardTobRooms hardRooms = new HardTobRooms();
    private static final NormalTobRooms normalRooms = new NormalTobRooms();

    // world point they put a player in while they check if he is in a raid
    private static final WorldPoint TEMP_LOCATION = new WorldPoint(3370, 5152, 2);

    private static final int TOB_VAR_STATE = 6440;
    private static final int LOCAL_TOB_ORB_VARB = 6441;
    @Getter
    private final Map<TobPhase, Damage> damageMap = new HashMap<>();
    @Getter
    private TobMode tobMode = null;
    @Inject
    private Client client;
    @Inject
    private ClientThread clientThread;
    @Inject
    private ChatMessageManager chatMessageManager;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private TobDamageOverlay tobDamageOverlay;
    @Inject
    private TobDamageCounterConfig config;
    @Getter
    private boolean inTob;
    //index of local player in tob party
    @Getter
    private int localPlayerIndex;
    @Getter
    private Damage raidDamage;
    private boolean shouldCalc;
    private boolean loggedIn;

    @Provides
    TobDamageCounterConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(TobDamageCounterConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        clientThread.invokeLater(this::calcInTob);
        overlayManager.add(tobDamageOverlay);
    }

    @Override
    protected void shutDown() throws Exception {
        overlayManager.remove(tobDamageOverlay);
        reset();
    }

    private void reset() {
        damageMap.clear();
        inTob = false;
        raidDamage = null;
        shouldCalc = false;
        tobMode = null;
    }

    @Subscribe
    public void onHitsplatApplied(HitsplatApplied hitsplatApplied) {
        if (!inTob) {
            return;
        }

        Actor actor = hitsplatApplied.getActor();

        if (!(actor instanceof NPC)) {
            return;
        }

        NPC npc = (NPC) actor;

        if (blacklistNPCs.contains(npc.getId())) {
            return;
        }

        determineAndUpdateCurrentRoom(npc.getId());

        if (tobMode == null) {
            return;
        }
        tobMode.onHitsplat(hitsplatApplied, npc, damageMap, config);
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned event) {
        // If we're not both in the ToB raid and the final Verzik bat just despawned, don't print status
        if (!(inTob && event.getNpc().getId() == NpcID.VERZIK_DEATH_BAT) &&
                !(inTob && event.getNpc().getId() == NpcID.VERZIK_DEATH_BAT_HARD)) {
            return;
        }

        if (config.showDamageSummary()) {
            // raid over
            for (TobPhase room : phases) {
                printRoomDamage(room, damageMap.get(room));
            }

            printRoomDamage(null, raidDamage);
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGGED_IN && !loggedIn) {
            if (client.getLocalPlayer() != null && client.getLocalPlayer().getWorldLocation().equals(TEMP_LOCATION)) {
                return;
            }

            shouldCalc = true;
        } else if (client.getGameState() == GameState.LOGIN_SCREEN || client.getGameState() == GameState.CONNECTION_LOST) {
            loggedIn = false;
        } else if (client.getGameState() == GameState.HOPPING) {
            reset();
        }
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        if (shouldCalc) {
            calcInTob();
            shouldCalc = false;
            loggedIn = true;
        }
    }

    @Subscribe
    public void onVarbitChanged(VarbitChanged event) {
        boolean tempInTob = getTobState();
        localPlayerIndex = client.getVarbitValue(LOCAL_TOB_ORB_VARB);

        if (tempInTob != inTob) {
            if (loggedIn) {
                if (tempInTob) {
                    initializeTob();
                } else {
                    reset();
                }
            }

            inTob = tempInTob;
        }
    }

    @Subscribe
    public void onGraphicChanged(GraphicChanged event) {
        if (!inTob) {
            return;
        }

        if (event.getActor().hasSpotAnim(SpotanimID.VERZIK_P3_CREEPER_SPOT)) {
            if (config.showLeechMessages()) {
                String chatMessage = new ChatMessageBuilder().append(ChatColorType.HIGHLIGHT).append(event.getActor().getName() + " has leeched and healed Verzik.").build();

                chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.FRIENDSCHATNOTIFICATION).runeLiteFormattedMessage(chatMessage).build());
            }

            if (event.getActor() instanceof Player) {
                damageMap.get(tobMode.currentPhase).addLeech((Player) event.getActor());
            }
        }
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged event) {
        if (!inTob || !(event.getActor() instanceof Player)) {
            return;
        }

        Player p = (Player) event.getActor();

        Actor interacting = p.getInteracting();
        if (!(interacting instanceof NPC)) {
            return;
        }

        NPC npc = (NPC) interacting;
        int npcId = npc.getId();
        if ((npcId != NpcID.TOB_BLOAT_HARD && npcId != NpcID.TOB_BLOAT)
                || p.getAnimation() == -1) {
            return;
        }

        determineAndUpdateCurrentRoom(npcId);

        int amulet_id = p.getPlayerComposition().getEquipmentId(KitType.AMULET);

        if (SALVE_IDS.contains(amulet_id)) {
            return;
        }

        if (config.showLeechMessages()) {
            Integer leechCount = damageMap.get(tobMode.currentPhase).getLeechCounts().get(p);
            if (leechCount == null) {
                String chatMessage = new ChatMessageBuilder().append(ChatColorType.HIGHLIGHT).append(p.getName() + " is leeching and is not attacking with a salve.").build();

                chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.FRIENDSCHATNOTIFICATION).runeLiteFormattedMessage(chatMessage).build());
            }
        }

        if (event.getActor() instanceof Player) {
            damageMap.get(tobMode.currentPhase).addLeech((Player) event.getActor());
        }
    }

    private void determineAndUpdateCurrentRoom(int npcID) {
        // If we're in ToB but don't know if it's normal or hard mode, hunt for the condition
        if (tobMode == null) {
            // Seek a case where it does match to set the phase
            for (TobPhase phase : phases) {
                Set<Integer> hardNpcIDs = hardRooms.phaseMap.get(phase);
                Set<Integer> normalNpcIDs = normalRooms.phaseMap.get(phase);
                if (hardNpcIDs.contains(npcID)) {
                    tobMode = new HardTobMode();
                } else if (normalNpcIDs.contains(npcID)) {
                    tobMode = new NormalTobMode();
                }
                if (tobMode != null) {
                    tobMode.currentPhase = phase;
                }
            }
            if (tobMode == null) {
                return;
            }
        }
        // Return early if this NPC's type is on the mode matcher for the current room
        if (tobMode.currentPhase != null && tobMode.getNpcIDs().contains(npcID)) {
            return;
            // Search if this NPC was in another phase (which the party now must be in if this NPC is present)
        } else {
            for (TobPhase phase : phases) {
                // Seek a case where it does match to set the phase
                if (!hardRooms.phaseMap.get(phase).contains(npcID)
                        && (!normalRooms.phaseMap.get(phase).contains(npcID))) {
                    continue;
                }
                tobMode.currentPhase = phase;
                return;
            }
        }

        if (tobMode != null) {
            tobMode.currentPhase = null;
        }
        log.warn("NPC ID not handled: {}", npcID);
    }

    private void calcInTob() {
        if (client.getGameState() != GameState.LOGGED_IN) {
            return;
        }

        boolean tempInTob = getTobState();

        if (tempInTob != inTob) {
            if (!tempInTob) {
                reset();
            } else {
                initializeTob();
            }

            inTob = tempInTob;
        }
    }

    private void initializeTob() {
        for (TobPhase phase : TobPhase.values()) {
            damageMap.put(phase, new Damage());
        }
    }

    private boolean getTobState() {
        return client.getVarbitValue(TOB_VAR_STATE) == 2 || client.getVarbitValue(TOB_VAR_STATE) == 3;
    }

    private void printRoomDamage(TobPhase phase, Damage damage) {
        int totalDamage = damage.getTotalDamage();
        int personalDamage = damage.getPersonalDamage();

        ChatMessageBuilder builder = new ChatMessageBuilder()
                .append(ChatColorType.NORMAL)
                .append("Total " +
                        (phase != null
                                ? TobPhase.GetPhaseName(phase)
                                : "raid")
                        + " damage: ")
                .append(ChatColorType.HIGHLIGHT)
                .append(DAMAGE_FORMAT.format(totalDamage));
        if (personalDamage > 0 && totalDamage > 0) {
            double percentage = personalDamage / (totalDamage / 100.0);
            builder
                    .append(ChatColorType.NORMAL)
                    .append(", Personal damage: ")

                    .append(ChatColorType.HIGHLIGHT)
                    .append(DAMAGE_FORMAT.format(personalDamage))
                    .append(ChatColorType.NORMAL)

                    .append(" (")
                    .append(ChatColorType.HIGHLIGHT)
                    .append(DECIMAL_FORMAT.format(percentage))
                    .append(ChatColorType.NORMAL)
                    .append("%)");
        }
        String chatMessage = builder.build();

        chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.FRIENDSCHATNOTIFICATION).runeLiteFormattedMessage(chatMessage).build());
    }

    @Data
    class Damage {
        private int personalDamage = 0;
        private int totalDamage = 0;
        private int totalHealing = 0;
        private Map<Player, Integer> leechCounts = new HashMap<>();

        void addDamage(int damage, boolean isLocalPlayer) {
            if (this != raidDamage) {
                if (raidDamage == null) {
                    raidDamage = new Damage();
                }
                raidDamage.addDamage(damage, isLocalPlayer);
            }

            totalDamage += damage;


            if (isLocalPlayer) {
                personalDamage += damage;
            }
        }

        void addLeech(Player player) {
            leechCounts.merge(player, 1, Integer::sum);
        }

        void addHealing(int amount) {
            totalHealing += amount;
        }
    }
}
