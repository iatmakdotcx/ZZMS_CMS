package client;

import handling.Buffstat;
import java.io.Serializable;
import server.Randomizer;

public enum MapleDisease implements Serializable, Buffstat {

    封印(MapleBuffStat.SEAL, 120),
    黑暗(MapleBuffStat.DARKNESS, 121),
    虛弱(MapleBuffStat.WEAKEN, 122),
    昏迷(MapleBuffStat.STUN, 123),
    詛咒(MapleBuffStat.CURSE, 124),
    中毒(MapleBuffStat.POISON, 125),
    緩慢(MapleBuffStat.SLOWNESS, 126),
    誘惑(MapleBuffStat.SEDUCE, 128),
    混亂(MapleBuffStat.REVERSE_DIRECTION, 132),
    不死化(MapleBuffStat.ZOMBIFY, 133),
    無法使用藥水(MapleBuffStat.POTION_CURSE, 134),
    影子(MapleBuffStat.SHADOW, 135), //receiving damage/moving
    致盲(MapleBuffStat.BLINDNESS, 136),
    冰凍(MapleBuffStat.FREEZE, 137),
    裝備潛能無效化(MapleBuffStat.DISABLE_POTENTIAL, 138),
    變身(MapleBuffStat.MORPH, 172),
    龍捲風(MapleBuffStat.TORNADO_CURSE, 173),
    FLAG(MapleBuffStat.PVP_FLAG, 799); // PVP - Capture the Flag
    // 127 = 1 snow?
    // 129 = turn?
    // 131 = poison also, without msg
    // 133, become undead?..50% recovery?
    // 0x100 is disable skill except buff
    private static final long serialVersionUID = 0L;
    private final int buffstat;
    private final int first;
    private final int disease;

    private MapleDisease(MapleBuffStat buffstat, int disease) {
        this.buffstat = buffstat.getValue();
        this.first = buffstat.getPosition();
        this.disease = disease;
    }

    @Override
    public int getPosition() {
        return first;
    }

    @Override
    public int getValue() {
        return buffstat;
    }

    public int getDisease() {
        return disease;
    }

    public static MapleDisease getRandom() {
        while (true) {
            for (MapleDisease dis : MapleDisease.values()) {
                if (Randomizer.nextInt(MapleDisease.values().length) == 0) {
                    return dis;
                }
            }
        }
    }

    public static MapleDisease getBySkill(final int skill) {
        for (MapleDisease d : MapleDisease.values()) {
            if (d.getDisease() == skill) {
                return d;
            }
        }
        return null;
    }
}
