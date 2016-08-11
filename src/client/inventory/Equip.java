package client.inventory;

import constants.EventConstants;
import constants.GameConstants;
import constants.ItemConstants;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import server.MapleItemInformationProvider;
import server.Randomizer;

public class Equip extends Item implements Serializable {

    public static enum ScrollResult {

        SUCCESS,
        FAIL,
        CURSE
    }
    public static final long ARMOR_RATIO = 350000L;
    public static final long WEAPON_RATIO = 700000L;
    //charm: -1 = has not been initialized yet, 0 = already been worn, >0 = has teh charm exp
    private byte state = 0, bonusState = 0, oldState = 0, upgradeSlots = 0, level = 0, vicioushammer = 0, platinumhammer = 0, enhance = 0, reqLevel = 0, yggdrasilWisdom = 0, bossDamage = 0, ignorePDR = 0, totalDamage = 0, allStat = 0, karmaCount = -1, fire = -1, starforce;
    private short str = 0, dex = 0, _int = 0, luk = 0, hp = 0, mp = 0, watk = 0, matk = 0, wdef = 0, mdef = 0, acc = 0, avoid = 0, hands = 0, speed = 0, jump = 0, charmExp = 0, pvpDamage = 0, enhanctBuff = 0, soulname, soulenchanter, soulpotential;
    private int durability = -1, incSkill = -1, potential1 = 0, potential2 = 0, potential3 = 0, bonuspotential1 = 0, bonuspotential2 = 0, bonuspotential3 = 0, fusionAnvil = 0, socket1 = 0, socket2 = 0, socket3 = 0, soulskill, limitBreak = 0;
    private long itemEXP = 0;
    private boolean finalStrike = false;
    private boolean trace = false;
    private int failCount = 0;
    private MapleRing ring = null;
    private MapleAndroid android = null;
    private List<EquipStat> stats = new LinkedList();
    private List<EquipSpecialStat> specialStats = new LinkedList();

    public Equip(int id, short position, byte flag) {
        super(id, position, (short) 1, flag);
    }

    public Equip(int id, short position, int uniqueid, short flag) {
        super(id, position, (short) 1, flag, uniqueid);
    }

    @Override
    public Item copy() {
        return copyTo(new Equip(getItemId(), getPosition(), getUniqueId(), getFlag()));
    }

    @Override
    public byte getType() {
        return 1;
    }

    public byte getUpgradeSlots() {
        return upgradeSlots;
    }

    public short getStr() {
        return str;
    }

    public short getDex() {
        return dex;
    }

    public short getInt() {
        return _int;
    }

    public short getLuk() {
        return luk;
    }

    public short getHp() {
        return hp;
    }

    public short getMp() {
        return mp;
    }

    public short getWatk() {
        return watk;
    }

    public short getMatk() {
        return matk;
    }

    public short getWdef() {
        return wdef;
    }

    public short getMdef() {
        return mdef;
    }

    public short getAcc() {
        return acc;
    }

    public short getAvoid() {
        return avoid;
    }

    public short getHands() {
        return hands;
    }

    public short getSpeed() {
        return speed;
    }

    public short getJump() {
        return jump;
    }

    public void setStr(short str) {
        if (str < 0) {
            str = 0;
        }
        this.str = str;
    }

    public void setDex(short dex) {
        if (dex < 0) {
            dex = 0;
        }
        this.dex = dex;
    }

    public void setInt(short _int) {
        if (_int < 0) {
            _int = 0;
        }
        this._int = _int;
    }

    public void setLuk(short luk) {
        if (luk < 0) {
            luk = 0;
        }
        this.luk = luk;
    }

    public void setHp(short hp) {
        if (hp < 0) {
            hp = 0;
        }
        this.hp = hp;
    }

    public void setMp(short mp) {
        if (mp < 0) {
            mp = 0;
        }
        this.mp = mp;
    }

    public void setWatk(short watk) {
        if (watk < 0) {
            watk = 0;
        }
        this.watk = watk;
    }

    public void setMatk(short matk) {
        if (matk < 0) {
            matk = 0;
        }
        this.matk = matk;
    }

    public void setWdef(short wdef) {
        if (wdef < 0) {
            wdef = 0;
        }
        this.wdef = wdef;
    }

    public void setMdef(short mdef) {
        if (mdef < 0) {
            mdef = 0;
        }
        this.mdef = mdef;
    }

    public void setAcc(short acc) {
        if (acc < 0) {
            acc = 0;
        }
        this.acc = acc;
    }

    public void setAvoid(short avoid) {
        if (avoid < 0) {
            avoid = 0;
        }
        this.avoid = avoid;
    }

    public void setHands(short hands) {
        if (hands < 0) {
            hands = 0;
        }
        this.hands = hands;
    }

    public void setSpeed(short speed) {
        if (speed < 0) {
            speed = 0;
        }
        this.speed = speed;
    }

    public void setJump(short jump) {
        if (jump < 0) {
            jump = 0;
        }
        this.jump = jump;
    }

    public void setUpgradeSlots(byte upgradeSlots) {
        this.upgradeSlots = upgradeSlots;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public byte getViciousHammer() {
        return vicioushammer;
    }

    public void setViciousHammer(byte ham) {
        vicioushammer = ham;
    }
    
    public byte getPlatinumHammer() {
        return platinumhammer;
    }

    public void setPlatinumHammer(byte ham) {
        platinumhammer = ham;
    }
    
    public byte getHammer() {
        return (byte) (vicioushammer + platinumhammer);
    }

    public long getItemEXP() {
        return itemEXP;
    }

    public void setItemEXP(long itemEXP) {
        if (itemEXP < 0) {
            itemEXP = 0;
        }
        this.itemEXP = itemEXP;
    }

    public long getEquipExp() {
        if (itemEXP <= 0) {
            return 0;
        }
        //aproximate value
        if (ItemConstants.类型.武器(getItemId())) {
            return itemEXP / WEAPON_RATIO;
        } else {
            return itemEXP / ARMOR_RATIO;
        }
    }

    public long getEquipExpForLevel() {
        if (getEquipExp() <= 0) {
            return 0;
        }
        long expz = getEquipExp();
        for (int i = getBaseLevel(); i <= GameConstants.getMaxLevel(getItemId()); i++) {
            if (expz >= GameConstants.getExpForLevel(i, getItemId())) {
                expz -= GameConstants.getExpForLevel(i, getItemId());
            } else { //for 0, dont continue;
                break;
            }
        }
        return expz;
    }

    public long getExpPercentage() {
        if (getEquipLevel() < getBaseLevel() || getEquipLevel() > GameConstants.getMaxLevel(getItemId()) || GameConstants.getExpForLevel(getEquipLevel(), getItemId()) <= 0) {
            return 0;
        }
        return getEquipExpForLevel() * 100 / GameConstants.getExpForLevel(getEquipLevel(), getItemId());
    }

    public int getEquipLevel() {
        int fixLevel = 0;
        Map<String, Integer> equipStats = MapleItemInformationProvider.getInstance().getEquipStats(getItemId());
        if (equipStats.containsKey("fixLevel")) {
            fixLevel = equipStats.get("fixLevel");
        }

        if (GameConstants.getMaxLevel(getItemId()) <= 0) {
            return fixLevel;
        }

        int levelz = getBaseLevel() + fixLevel;
        if (getEquipExp() <= 0) {
            return levelz;
        }
        long expz = getEquipExp();
        for (int i = levelz; (GameConstants.getStatFromWeapon(getItemId()) == null ? (i <= GameConstants.getMaxLevel(getItemId())) : (i < GameConstants.getMaxLevel(getItemId()))); i++) {
            if (expz >= GameConstants.getExpForLevel(i, getItemId())) {
                levelz++;
                expz -= GameConstants.getExpForLevel(i, getItemId());
            } else { //for 0, dont continue;
                break;
            }
        }
        return levelz;
    }

    public int getBaseLevel() {
        return (GameConstants.getStatFromWeapon(getItemId()) == null ? 1 : 0);
    }

    @Override
    public void setQuantity(short quantity) {
        if (quantity < 0 || quantity > 1) {
            throw new RuntimeException("Setting the quantity to " + quantity + " on an equip (itemid: " + getItemId() + ")");
        }
        super.setQuantity(quantity);
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(final int dur) {
        durability = dur;
    }

    public short getEnhanctBuff() {
        return enhanctBuff;
    }

    public void setEnhanctBuff(short enhanctBuff) {
        this.enhanctBuff = enhanctBuff;
    }

    public byte getReqLevel() {
        return reqLevel;
    }

    public void setReqLevel(byte reqLevel) {
        this.reqLevel = reqLevel;
    }

    public byte getYggdrasilWisdom() {
        return yggdrasilWisdom;
    }

    public void setYggdrasilWisdom(byte yggdrasilWisdom) {
        this.yggdrasilWisdom = yggdrasilWisdom;
    }

    public boolean getFinalStrike() {
        return finalStrike;
    }

    public void setFinalStrike(boolean finalStrike) {
        this.finalStrike = finalStrike;
    }

    public byte getBossDamage() {
        return bossDamage;
    }

    public void setBossDamage(byte bossDamage) {
        this.bossDamage = bossDamage;
    }

    public byte getIgnorePDR() {
        return ignorePDR;
    }

    public void setIgnorePDR(byte ignorePDR) {
        this.ignorePDR = ignorePDR;
    }

    public byte getTotalDamage() {
        return totalDamage;
    }

    public void setTotalDamage(byte totalDamage) {
        this.totalDamage = totalDamage;
    }

    public byte getAllStat() {
        return allStat;
    }

    public void setAllStat(byte allStat) {
        this.allStat = allStat;
    }

    public void setFailCount(int value) {
        failCount = value;
    }

    public int getFailCount() {
        return failCount;
    }

    public boolean isTrace() {
        return trace;
    }

    public void setTrace(boolean value) {
        trace = value;
    }

    public byte getKarmaCount() {
        return karmaCount;
    }

    public void setKarmaCount(byte karmaCount) {
        this.karmaCount = karmaCount;
    }

    public int getMaxDamage() {
        return ItemConstants.类型.武器(getItemId()) ? ItemConstants.getMaxDamageLimitBreak(getItemId()) : 0;
    }

    public int getLimitBreak() {
        return limitBreak;
    }

    public void setLimitBreak(int lb) {
        limitBreak = lb;
    }

    public byte getEnhance() {
        return enhance;
    }

    public void setEnhance(final byte en) {
        enhance = en;
    }

    public int getPotential(int num, boolean isBonus) {
        switch (num) {
            case 1:
                if (isBonus) {
                    return bonuspotential1;
                } else {
                    return potential1;
                }
            case 2:
                if (isBonus) {
                    return bonuspotential2;
                } else {
                    return potential2;
                }
            case 3:
                if (isBonus) {
                    return bonuspotential3;
                } else {
                    return potential3;
                }
        }
        return 0;
    }

    public void setPotential(int en, int num, boolean isBonus) {
        switch (num) {
            case 1:
                if (isBonus) {
                    bonuspotential1 = en;
                } else {
                    potential1 = en;
                }
                break;
            case 2:
                if (isBonus) {
                    bonuspotential2 = en;
                } else {
                    potential2 = en;
                }
                break;
            case 3:
                if (isBonus) {
                    bonuspotential3 = en;
                } else {
                    potential3 = en;
                }
                break;
        }
    }

    public int getFusionAnvil() {
        return fusionAnvil;
    }

    public void setFusionAnvil(final int en) {
        fusionAnvil = en;
    }

    public byte getOldState() {
        return oldState;
    }

    public void setOldState(final byte en) {
        oldState = en;
    }

    public byte getState(boolean bonus) {
        if (bonus) {
            return bonusState;
        } else {
            return state;
        }
    }

    public void setState(final byte en, boolean bonus) {
        if (bonus) {
            bonusState = en;
        } else {
            state = en;
        }
    }

    public void updateState(boolean bonus) {
        int ret = 0;
        int v1;
        int v2;
        int v3;
        if (!bonus) {
            v1 = potential1;
            v2 = potential2;
            v3 = potential3;
        } else {
            v1 = bonuspotential1;
            v2 = bonuspotential2;
            v3 = bonuspotential3;
        }
        if (v1 >= 40000 || v2 >= 40000 || v3 >= 40000) {
            ret = 20;//英雄初心者
        } else if (v1 >= 30000 || v2 >= 30000 || v3 >= 30000) {
            ret = 19;//罕見
        } else if (v1 >= 20000 || v2 >= 20000 || v3 >= 20000) {
            ret = 18;//稀有
        } else if (v1 >= 1 || v2 >= 1 || v3 >= 1) {
            ret = 17;//特殊
        } else if (v1 == -20 || v2 == -20 || v3 == -20 || v1 == -4 || v2 == -4 || v3 == -4) {
            ret = 4;//未鑒定傳說
        } else if (v1 == -19 || v2 == -19 || v3 == -19 || v1 == -3 || v2 == -3 || v3 == -3) {
            ret = 3;//未鑒定罕見
        } else if (v1 == -18 || v2 == -18 || v3 == -18 || v1 == -2 || v2 == -2 || v3 == -2) {
            ret = 2;//未鑒定稀有
        } else if (v1 == -17 || v2 == -17 || v3 == -17 || v1 == -1 || v2 == -1 || v3 == -1) {
            ret = 1;//未鑒定特殊
        } else if (v1 < 0 || v2 < 0 || v3 < 0) {
            return;
        }

        setState((byte) ret, bonus);
    }

    public short getSoulName() {
        return soulname;
    }

    public void setSoulName(final short soulname) {
        this.soulname = soulname;
    }

    public short getSoulEnchanter() {
        return soulenchanter;
    }

    public void setSoulEnchanter(final short soulenchanter) {
        this.soulenchanter = soulenchanter;
    }

    public short getSoulPotential() {
        return soulpotential;
    }

    public void setSoulPotential(final short soulpotential) {
        this.soulpotential = soulpotential;
    }

    public int getSoulSkill() {
        return soulskill;
    }

    public void setSoulSkill(final int skillid) {
        this.soulskill = skillid;
    }

    public byte getFire() {
        return fire;
    }

    public void setFire(byte fire) {
        this.fire = fire;
    }

    public byte getStarForce() {
        return starforce;
    }

    public void setStarForce(byte starforce) {
        this.starforce = starforce;
    }

    public void resetPotential_Fuse(boolean half, int potentialState, boolean bonus) { //maker skill - equip first receive
        //no legendary, 0.16% chance unique, 4% chance epic, else rare
        potentialState = -potentialState;
        if (Randomizer.nextInt(100) < 4) {
            potentialState -= Randomizer.nextInt(100) < 4 ? 2 : 1;
        }
        setPotential(potentialState, 1, bonus);
        setPotential((Randomizer.nextInt(half ? 5 : 10) == 0 ? potentialState : 0), 2, bonus); //1/10 chance of 3 line
        setPotential(0, 3, bonus); //just set it theoretically
        updateState(bonus);
    }

    public void resetPotential(boolean bonus) {
        resetPotential(0, bonus);
    }

    public void resetPotential(int state, boolean bonus) {
        resetPotential(state, false, bonus);
    }

    public void resetPotential(boolean fullLine, boolean bonus) {
        resetPotential(0, fullLine, bonus);
    }

    public void resetPotential(int state, boolean fullLine, boolean bonus) {
        final int rank;
        switch (state) {
            case 1:
                rank = -17;
                break;
            case 2:
                rank = -18;
                break;
            case 3:
                rank = -19;
                break;
            case 4:
                rank = -20;
                break;
            default:
                rank = Randomizer.nextInt(100) < 4 ? (Randomizer.nextInt(100) < 4 ? -19 : -18) : -17;
        }
        fullLine = getState(bonus) != 0 && getPotential(3, bonus) != 0 ? true : fullLine;
        setPotential(rank, 1, bonus);
        setPotential(Randomizer.nextInt(10) <= 1 || fullLine ? rank : 0, 2, bonus); //1/10 chance of 3 line
        setPotential(0, 3, bonus); //just set it theoretically
        updateState(bonus);
    }

    public void renewPotential(int rate, int type, int toLock) {
        int miracleRate = 1;
        if (EventConstants.DoubleMiracleTime) {
            miracleRate *= 2;
        }

        boolean bonus = ItemConstants.方塊.CubeType.附加潛能.check(type);

        boolean threeLine = getPotential(3, bonus) > 0;

        int rank = Randomizer.nextInt(100) < rate * miracleRate ? 1 : 0;
        if (ItemConstants.方塊.CubeType.等級下降.check(type)) {
            if (rank == 0) {
                rank = Randomizer.nextInt(100) < (rate + 20) * miracleRate ? -1 : 0;
            }
        }

        if (ItemConstants.方塊.CubeType.前兩條相同.check(type)) {
            type -= Randomizer.nextInt(10) <= 5 ? ItemConstants.方塊.CubeType.前兩條相同.getValue() : 0;
        }

        if (getState(bonus) + rank < 17 || getState(bonus) + rank > (!ItemConstants.方塊.CubeType.傳說.check(type) ? !ItemConstants.方塊.CubeType.罕見.check(type) ? !ItemConstants.方塊.CubeType.稀有.check(type) ? 17 : 18 : 19 : 20)) {
            rank = 0;
        }

        setState((byte) (getState(bonus) + rank - 16), bonus);

        if (toLock != 0 && toLock <= 3) {
            setPotential(-(toLock * 100000 + getPotential(toLock, bonus)), 1, bonus);
        } else {
            setPotential(-getState(bonus), 1, bonus);
        }

        if (ItemConstants.方塊.CubeType.調整潛能條數.check(type)) {
            setPotential(Randomizer.nextInt(10) <= 2 ? -getState(bonus) : 0, 2, bonus);
        } else if (threeLine) {
            setPotential(-getState(bonus), 2, bonus);
        } else {
            setPotential(0, 2, bonus);
        }

        setPotential(-type, 3, bonus);

        if (ItemConstants.方塊.CubeType.洗後無法交易.check(type)) {
            setFlag((short) (getFlag() | ItemFlag.UNTRADABLE.getValue()));
        }

        updateState(bonus);
    }

    public int getIncSkill() {
        return incSkill;
    }

    public void setIncSkill(int inc) {
        incSkill = inc;
    }

    public short getCharmEXP() {
        return charmExp;
    }

    public short getPVPDamage() {
        return pvpDamage;
    }

    public void setCharmEXP(short s) {
        charmExp = s;
    }

    public void setPVPDamage(short p) {
        pvpDamage = p;
    }

    public MapleRing getRing() {
        if (!ItemConstants.类型.特效戒指(getItemId()) || getUniqueId() <= 0) {
            return null;
        }
        if (ring == null) {
            ring = MapleRing.loadFromDb(getUniqueId(), getPosition() < 0);
        }
        return ring;
    }

    public void setRing(MapleRing ring) {
        this.ring = ring;
    }

    public MapleAndroid getAndroid() {
        if (getItemId() / 10000 != 166 || getUniqueId() <= 0) {
            return null;
        }
        if (android == null) {
            android = MapleAndroid.loadFromDb(getItemId(), getUniqueId());
        }
        return android;
    }

    public void setAndroid(MapleAndroid ad) {
        android = ad;
    }

    public short getSocketState() {
        int flag = 0;
        if (socket1 > 0 || socket2 > 0 || socket3 > 0) { // Got empty sockets show msg 
            flag |= SocketFlag.DEFAULT.getValue();
        }
        if (socket1 > 0) {
            flag |= SocketFlag.SOCKET_BOX_1.getValue();
        }
        if (socket2 > 0) {
            flag |= SocketFlag.SOCKET_BOX_2.getValue();
        }
        if (socket3 > 0) {
            flag |= SocketFlag.SOCKET_BOX_3.getValue();
        }
        if (socket1 > 1) {
            flag |= SocketFlag.USED_SOCKET_1.getValue();
        }
        if (socket2 > 1) {
            flag |= SocketFlag.USED_SOCKET_2.getValue();
        }
        if (socket3 > 1) {
            flag |= SocketFlag.USED_SOCKET_3.getValue();
        }
        return (short) flag;
    }

    public int getSocket(int num) {
        switch (num) {
            case 1:
                return socket1;
            case 2:
                return socket2;
            case 3:
                return socket3;
        }
        return 0;
    }

    public void setSocket(int socket, int num) {
        switch (num) {
            case 1:
                socket1 = socket;
                break;
            case 2:
                socket2 = socket;
                break;
            case 3:
                socket3 = socket;
                break;
        }
    }

    public List<EquipStat> getStats() {
        return stats;
    }

    public List<EquipSpecialStat> getSpecialStats() {
        return specialStats;
    }

    public static Equip calculateEquipStats(Equip eq) {
        eq.getStats().clear();
        eq.getSpecialStats().clear();
        if (eq.getUpgradeSlots() > 0) {
            eq.getStats().add(EquipStat.SLOTS);
        }
        if (eq.getLevel() > 0) {
            eq.getStats().add(EquipStat.LEVEL);
        }
        if (eq.getStr() > 0) {
            eq.getStats().add(EquipStat.STR);
        }
        if (eq.getDex() > 0) {
            eq.getStats().add(EquipStat.DEX);
        }
        if (eq.getInt() > 0) {
            eq.getStats().add(EquipStat.INT);
        }
        if (eq.getLuk() > 0) {
            eq.getStats().add(EquipStat.LUK);
        }
        if (eq.getHp() > 0) {
            eq.getStats().add(EquipStat.MHP);
        }
        if (eq.getMp() > 0) {
            eq.getStats().add(EquipStat.MMP);
        }
        if (eq.getWatk() > 0) {
            eq.getStats().add(EquipStat.WATK);
        }
        if (eq.getMatk() > 0) {
            eq.getStats().add(EquipStat.MATK);
        }
        if (eq.getWdef() > 0) {
            eq.getStats().add(EquipStat.WDEF);
        }
        if (eq.getMdef() > 0) {
            eq.getStats().add(EquipStat.MDEF);
        }
        if (eq.getAcc() > 0) {
            eq.getStats().add(EquipStat.ACC);
        }
        if (eq.getAvoid() > 0) {
            eq.getStats().add(EquipStat.AVOID);
        }
        if (eq.getHands() > 0) {
            eq.getStats().add(EquipStat.HANDS);
        }
        if (eq.getSpeed() > 0) {
            eq.getStats().add(EquipStat.SPEED);
        }
        if (eq.getJump() > 0) {
            eq.getStats().add(EquipStat.JUMP);
        }
        if (eq.getFlag() > 0) {
            eq.getStats().add(EquipStat.FLAG);
        }
        if (eq.getIncSkill() > 0) {
            eq.getStats().add(EquipStat.INC_SKILL);
        }
        if (eq.getEquipLevel() > 0) {
            eq.getStats().add(EquipStat.ITEM_LEVEL);
        }
        if (eq.getItemEXP() > 0) {
            eq.getStats().add(EquipStat.ITEM_EXP);
        }
        if (eq.getDurability() > -1) {
            eq.getStats().add(EquipStat.DURABILITY);
        }
        if (eq.getViciousHammer() > 0 || eq.getPlatinumHammer() > 0) {
            eq.getStats().add(EquipStat.VICIOUS_HAMMER);
        }
        if (eq.getPVPDamage() > 0) {
            eq.getStats().add(EquipStat.PVP_DAMAGE);
        }
        if (eq.getEnhanctBuff() > 0) {
            eq.getStats().add(EquipStat.ENHANCT_BUFF);
        }
        if (eq.getReqLevel() > 0) {
            eq.getStats().add(EquipStat.REQUIRED_LEVEL);
        }
        if (eq.getYggdrasilWisdom() > 0) {
            eq.getStats().add(EquipStat.YGGDRASIL_WISDOM);
        }
        if (eq.getFinalStrike()) {
            eq.getStats().add(EquipStat.FINAL_STRIKE);
        }
        if (eq.getBossDamage() > 0) {
            eq.getStats().add(EquipStat.BOSS_DAMAGE);
        }
        if (eq.getIgnorePDR() > 0) {
            eq.getStats().add(EquipStat.IGNORE_PDR);
        }
        //SPECIAL STATS:
        if (eq.getTotalDamage() > 0) {
            eq.getSpecialStats().add(EquipSpecialStat.TOTAL_DAMAGE);
        }
        if (eq.getAllStat() > 0) {
            eq.getSpecialStats().add(EquipSpecialStat.ALL_STAT);
        }
        eq.getSpecialStats().add(EquipSpecialStat.KARMA_COUNT);
        if (eq.getFire() > 0) {
            eq.getSpecialStats().add(EquipSpecialStat.FIRE);
        }
        if (eq.getStarForce() > 0) {
            eq.getSpecialStats().add(EquipSpecialStat.STAR_FORCE);
        }
        return (Equip) eq.copy();
    }

    public Equip copyTo(Equip ret) {
        //力量
        ret.str = str;
        //敏捷
        ret.dex = dex;
        //智力
        ret._int = _int;
        //幸運
        ret.luk = luk;
        //HP
        ret.hp = hp;
        //MP
        ret.mp = mp;
        //魔攻
        ret.matk = matk;
        //魔防
        ret.mdef = mdef;
        //物攻
        ret.watk = watk;
        //物防
        ret.wdef = wdef;
        //命中
        ret.acc = acc;
        //迴避
        ret.avoid = avoid;
        //靈敏度
        ret.hands = hands;
        //移動速度
        ret.speed = speed;
        //跳躍力
        ret.jump = jump;
        //裝備星級
        ret.enhance = enhance;
        //可使用捲軸次數
        ret.upgradeSlots = upgradeSlots;
        //已使用捲軸次數
        ret.level = level;
        //道具經驗
        ret.itemEXP = itemEXP;
        //耐久
        ret.durability = durability;
        //黃金鐵鎚次數
        ret.vicioushammer = vicioushammer;
        //白金鐵鎚次數
        ret.platinumhammer = platinumhammer;
        //潛能等級
        ret.state = state;
        //潛能1
        ret.potential1 = potential1;
        //潛能2
        ret.potential2 = potential2;
        //潛能3
        ret.potential3 = potential3;
        //附加潛能等級
        ret.bonusState = bonusState;
        //附加潛能1
        ret.bonuspotential1 = bonuspotential1;
        //附加潛能2
        ret.bonuspotential2 = bonuspotential2;
        //附加潛能3
        ret.bonuspotential3 = bonuspotential3;
        //鐵砧
        ret.fusionAnvil = fusionAnvil;
        //星岩1
        ret.socket1 = socket1;
        //星岩2
        ret.socket2 = socket2;
        //星岩3
        ret.socket3 = socket3;
        // 突破上限(台服已無)
        ret.limitBreak = limitBreak;
        //魅力經驗值
        ret.charmExp = charmExp;
        //大亂鬥傷害
        ret.pvpDamage = pvpDamage;
        //裝備技能
        ret.incSkill = incSkill;
        //星力強化狀態
        ret.enhanctBuff = enhanctBuff;
        //裝備需求等級減少
        ret.reqLevel = reqLevel;
        //世界之樹的祝福 [20485XX]
        ret.yggdrasilWisdom = yggdrasilWisdom;
        //最後武力 技能 [2048600]
        ret.finalStrike = finalStrike;
        //BOSS傷
        ret.bossDamage = bossDamage;
        //無視怪物防禦
        ret.ignorePDR = ignorePDR;
        //總傷
        ret.totalDamage = totalDamage;
        //全屬性%
        ret.allStat = allStat;
        //剪刀次數
        ret.karmaCount = karmaCount;
        //星火
        ret.fire = fire;
        //靈魂寶珠
        ret.soulname = soulname;
        //靈魂捲軸
        ret.soulenchanter = soulenchanter;
        //靈魂寶珠潛能
        ret.soulpotential = soulpotential;
        //靈魂技能
        ret.soulskill = soulskill;
        //星之力
        ret.starforce = starforce;

        //從 XX 贈送
        ret.setGiftFrom(getGiftFrom());
        //製作者
        ret.setOwner(getOwner());
        //數量
        ret.setQuantity(getQuantity());
        //剩餘使用時間
        ret.setExpiration(getExpiration());

        ret.stats = stats;
        ret.specialStats = specialStats;
        return ret;
    }

    public Item inheritance(Equip oldEquip) {
        if (ItemFlag.UNTRADABLE.check(oldEquip.getFlag())) {
            addFlag((short) ItemFlag.UNTRADABLE.getValue());
        }
        oldEquip.enhanctBuff = (short) ((enhanctBuff | oldEquip.enhanctBuff) - EquipStat.EnhanctBuff.EQUIP_MARK.getValue());
        oldEquip.charmExp = charmExp;
        oldEquip.setFlag(getFlag());
        oldEquip.setOwner(getOwner());
        return oldEquip.copyTo(this);
    }

    public Item reset(Equip newEquip, boolean prefectReset) {
        newEquip.setQuantity(getQuantity());
        newEquip.setExpiration(getExpiration());
        newEquip.upgradeSlots += platinumhammer;
        newEquip.platinumhammer = platinumhammer;
        newEquip.charmExp = charmExp;
        newEquip.enhanctBuff = enhanctBuff;
        newEquip.soulenchanter = soulenchanter;

        newEquip.state = state;
        newEquip.potential1 = potential1;
        newEquip.potential2 = potential2;
        newEquip.potential3 = potential3;
        newEquip.bonusState = bonusState;
        newEquip.bonuspotential1 = bonuspotential1;
        newEquip.bonuspotential2 = bonuspotential2;
        newEquip.bonuspotential3 = bonuspotential3;
        newEquip.fusionAnvil = fusionAnvil;
        newEquip.socket1 = socket1;
        newEquip.socket2 = socket2;
        newEquip.socket3 = socket3;
        newEquip.limitBreak = limitBreak;

        if  (prefectReset) {
            //去除無法交易狀態
            if (ItemFlag.UNTRADABLE.check(getFlag())) {
                newEquip.setFlag((short) (getFlag() - ItemFlag.UNTRADABLE.getValue()));
            }
            //去除楓方塊狀態，否則回真後無法交易裝備又不能剪刀，需要穿一次裝才能剪刀(這個是台版BUG)
            if (ItemFlag.MAPLE_CUBE.check(getFlag())) {
                newEquip.setFlag((short) (getFlag() - ItemFlag.MAPLE_CUBE.getValue()));
            }
        } else {
            newEquip.soulname = soulname;
            newEquip.soulpotential = soulpotential;
            newEquip.soulskill = soulskill;
            newEquip.karmaCount = karmaCount;
            newEquip.fire = fire;
        }

        return newEquip.copyTo(this);
    }
}
