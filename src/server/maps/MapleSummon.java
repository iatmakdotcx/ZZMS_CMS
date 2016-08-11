/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package server.maps;

import java.awt.Point;

import client.MapleCharacter;
import client.MapleClient;
import client.SkillFactory;
import constants.GameConstants;
import client.anticheat.CheatingOffense;
import server.MapleStatEffect;
import tools.packet.CField.SummonPacket;

public class MapleSummon extends AnimatedMapleMapObject {

    private final int ownerid, skillLevel, ownerLevel, skill;
    private MapleMap map; //required for instanceMaps
    private short hp;
    private boolean changedMap = false;
    private SummonMovementType movementType;
    // Since player can have more than 1 summon [Pirate] 
    // Let's put it here instead of cheat tracker
    private int lastSummonTickCount;
    private byte Summon_tickResetCount;
    private long Server_ClientSummonTickDiff;
    private long lastAttackTime;
    private boolean isControl = false;
    private boolean isScream = false;
    private int SummonTime;
    private boolean isfaceleft;
    private long SummonStratTime;
    private int linkmonid = 0;

    public MapleSummon(final MapleCharacter owner, final MapleStatEffect skill, final Point pos, final SummonMovementType movementType) {
        this(owner, skill.getSourceId(), skill.getLevel(), pos, movementType);
    }

    public MapleSummon(final MapleCharacter owner, final int sourceid, final int level, final Point pos, final SummonMovementType movementType) {
        super();
        this.ownerid = owner.getId();
        this.ownerLevel = owner.getLevel();
        this.skill = sourceid;
        this.map = owner.getMap();
        this.skillLevel = level;
        this.movementType = movementType;
        this.SummonStratTime = System.currentTimeMillis();
        setPosition(pos);
        this.isfaceleft = owner.isFacingLeft();

        if (!isPuppet()) { // Safe up 12 bytes of data, since puppet doesn't attack.
            lastSummonTickCount = 0;
            Summon_tickResetCount = 0;
            Server_ClientSummonTickDiff = 0;
            lastAttackTime = 0;
        }
    }

    @Override
    public final void sendSpawnData(final MapleClient client) {
    }

    @Override
    public final void sendDestroyData(final MapleClient client) {
        client.getSession().write(SummonPacket.removeSummon(this, false));
    }

    public final void updateMap(final MapleMap map) {
        this.map = map;
    }

    public int getSummonTime() {
        return SummonTime;
    }

    public int SummonTime(int bufftime) {
        SummonTime = bufftime - (int) (System.currentTimeMillis() - SummonStratTime);
        return SummonTime;
    }

    public final MapleCharacter getOwner() {
        return map.getCharacterById(ownerid);
    }

    public final int getOwnerId() {
        return ownerid;
    }

    public boolean setControl(boolean ss) {//灵魂统治开关
        return this.isControl = ss;
    }

    public void setLinkmonid(int ss) {
        this.linkmonid = ss;
    }

    public int getLinkmonid() {
        return this.linkmonid;
    }

    public boolean getControl() {
        return isControl;
    }

    public boolean setScream(boolean ss) {
        return this.isScream = ss;
    }

    public boolean getScream() {
        return isScream;
    }

    public boolean isfacingleft() {
        return isfaceleft;
    }

    public final int getOwnerLevel() {
        return ownerLevel;
    }

    public final int getSkill() {
        return skill;
    }

    public final short getHP() {
        return hp;
    }

    public final void addHP(final short delta) {
        this.hp += delta;
    }

    public boolean is替身术() {
        switch (skill) {
            case 3221014:
            case 4341006:
            case 33111003:
                return true;
        }
        return is天使召唤兽();
    }

    public boolean is天使召唤兽() {
        return GameConstants.isAngel(skill);
    }

    public boolean isMultiAttack() {//TODO 召唤兽是否是一次性攻击
        switch (skill) {
            case 2111010:
                return false;
        }
        return (skill == 61111002) || (skill == 35111002) || (skill == 35121003) || ((skill != 33101008) && (skill < 35000000)) || (skill == 35111001) || (skill == 35111009) || (skill == 35111010);
    }

    public boolean is神箭幻影() {
        return skill == 3221014;
    }

    public boolean is灵魂助力() {
        return skill == 1301013;
    }

    public boolean is分身召唤() {
        return (skill == 4341006) || (skill == 14111024);
    }

    public boolean is机械磁场() {
        return skill == 35111002;
    }

    public boolean is战法重生() {
        return skill == 32111006;
    }

    public boolean is影子蝙蝠() {
        return skill == 14000027;
    }

    public boolean isMultiSummon() {
        return (skill == 5211014) || (skill == 32111006) || (skill == 33101008);
    }

    public boolean isSummon() {
        return (is天使召唤兽()) || (SkillFactory.getSkill(skill).isSummonSkill());
    }

    public final SummonMovementType getMovementType() {
        return movementType;
    }

    public final boolean isPuppet() {
        switch (skill) {
            case 3111002:
            case 3211002:
            case 3120012:
            case 3220012:
            case 13111004:
            case 4341006:
            case 33111003:
                return true;
        }
        return isAngel();
    }

    public final boolean isAngel() {
        return GameConstants.isAngel(skill);
    }

    public final boolean isGaviota() {
        return skill == 5211002;
    }

    public final boolean isBeholder() {
        return skill == 1321007;
    }

    public final int getSkillLevel() {
        return skillLevel;
    }

    public final int getSummonType() {
        if (isAngel()) {
            return 2;
        } else if ((skill != 33111003 && skill != 3120012 && skill != 3220012 && isPuppet()) || skill == 33101008 || skill == 35111002) {
            return 0;
        }
        switch (skill) {
            case 1321007:
                return 2; //buffs and stuff
            case 35111001: //satellite.
            case 35111009:
            case 35111010:
            case 42111003: // Kishin Shoukan
                return 3; //attacks what you attack
            case 35121009: //bots n. tots
                return 5; //sub summons
            case 35121003:
                return 6; //charge
            case 4111007: // test
            case 4211007: //dark flare
            case 14111010: //dark flare
                return 7; //attacks what you get hit by
            case 42101001: // Shikigami Charm
                return 8;
            case 14000027:
                return 0;
        }
        return 1;
    }

    @Override
    public final MapleMapObjectType getType() {
        return MapleMapObjectType.SUMMON;
    }

    public final void CheckSummonAttackFrequency(final MapleCharacter chr, final int tickcount) {
        final int tickdifference = (tickcount - lastSummonTickCount);
        if (tickdifference < SkillFactory.getSummonData(skill).delay) {
            chr.getCheatTracker().registerOffense(CheatingOffense.FAST_SUMMON_ATTACK);
        }
        final long STime_TC = System.currentTimeMillis() - tickcount;
        final long S_C_Difference = Server_ClientSummonTickDiff - STime_TC;
        if (S_C_Difference > 500) {
            chr.getCheatTracker().registerOffense(CheatingOffense.FAST_SUMMON_ATTACK);
        }
        Summon_tickResetCount++;
        if (Summon_tickResetCount > 4) {
            Summon_tickResetCount = 0;
            Server_ClientSummonTickDiff = STime_TC;
        }
        lastSummonTickCount = tickcount;
    }

    public final void CheckPVPSummonAttackFrequency(final MapleCharacter chr) {
        final long tickdifference = (System.currentTimeMillis() - lastAttackTime);
        if (tickdifference < SkillFactory.getSummonData(skill).delay) {
            chr.getCheatTracker().registerOffense(CheatingOffense.FAST_SUMMON_ATTACK);
        }
        lastAttackTime = System.currentTimeMillis();
    }

    public final boolean isChangedMap() {
        return changedMap;
    }

    public final void setChangedMap(boolean cm) {
        this.changedMap = cm;
    }
}
