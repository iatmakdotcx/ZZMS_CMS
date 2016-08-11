package client;

import constants.GameConstants;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import provider.MapleData;
import provider.MapleDataTool;
import server.MapleStatEffect;
import server.Randomizer;
import server.life.Element;
import tools.Pair;

public class Skill implements Comparator<Skill> {

    private String name = "";
    private final List<MapleStatEffect> effects = new ArrayList<>();
    private List<MapleStatEffect> pvpEffects = null;
    private List<Integer> animation = null;
    private final List<Pair<String, Integer>> requiredSkill = new ArrayList<>();
    private Element element = Element.NEUTRAL;
    private final int id;
    private int animationTime = 0, masterLevel = 0, maxLevel = 0, delay = 0, trueMax = 0, eventTamingMob = 0, skillTamingMob = 0, skillType = 0; //4 is alert
    private boolean invisible = false, chargeskill = false, timeLimited = false, combatOrders = false, pvpDisabled = false, magic = false, casterMove = false, pushTarget = false, pullTarget = false;
    private boolean isBuffSkill = false;
    private boolean isSummonSkill = false;
    private boolean notRemoved = false;
    private int hyper = 0;
    private int reqLev = 0;
    private int maxDamageOver = 2147483647;
    private int fixLevel;
    private int vehicleID;
    private boolean petPassive = false;
    private int setItemReason;
    private int setItemPartsCount;

    public Skill(final int id) {
        super();
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Skill loadFromData(final int id, final MapleData data, final MapleData delayData) {
        Skill ret = new Skill(id);

        final int skillType = MapleDataTool.getInt("skillType", data, -1);
        final String elem = MapleDataTool.getString("elemAttr", data, null);
        if (elem != null) {
            ret.element = Element.getFromChar(elem.charAt(0));
        }
        ret.skillType = skillType;
        ret.invisible = MapleDataTool.getInt("invisible", data, 0) > 0;
        ret.notRemoved = (MapleDataTool.getInt("notRemoved", data, 0) > 0);
        ret.timeLimited = MapleDataTool.getInt("timeLimited", data, 0) > 0;
        ret.combatOrders = MapleDataTool.getInt("combatOrders", data, 0) > 0;
        ret.fixLevel = MapleDataTool.getInt("fixLevel", data, 0);
        ret.masterLevel = MapleDataTool.getInt("masterLevel", data, 0);
        ret.eventTamingMob = MapleDataTool.getInt("eventTamingMob", data, 0);
        ret.skillTamingMob = MapleDataTool.getInt("skillTamingMob", data, 0);
        ret.vehicleID = MapleDataTool.getInt("vehicleID", data, 0);
        ret.hyper = MapleDataTool.getInt("hyper", data, 0);
        ret.reqLev = MapleDataTool.getInt("reqLev", data, 0);

        ret.petPassive = (MapleDataTool.getInt("petPassive", data, 0) > 0);
        ret.setItemReason = MapleDataTool.getInt("setItemReason", data, 0);
        ret.setItemPartsCount = MapleDataTool.getInt("setItemPartsCount", data, 0);
        final MapleData inf = data.getChildByPath("info");
        if (inf != null) {
            ret.pvpDisabled = MapleDataTool.getInt("pvp", inf, 1) <= 0;
            ret.magic = MapleDataTool.getInt("magicDamage", inf, 0) > 0;
            ret.casterMove = MapleDataTool.getInt("casterMove", inf, 0) > 0;
            ret.pushTarget = MapleDataTool.getInt("pushTarget", inf, 0) > 0;
            ret.pullTarget = MapleDataTool.getInt("pullTarget", inf, 0) > 0;
        }
        final MapleData effect = data.getChildByPath("effect");
        boolean isBuff;
        if (skillType == 2) {
            isBuff = true;
        } else if (skillType == 3) { //final attack
            ret.animation = new ArrayList<>();
            ret.animation.add(0);
            isBuff = effect != null;
            switch (id) {
                case 20040216:
                case 20040217:
                case 20040219:
                case 20040220:
                case 20041239:
                    isBuff = true;
            }
        } else {
            MapleData action_ = data.getChildByPath("action");
            final MapleData hit = data.getChildByPath("hit");
            final MapleData ball = data.getChildByPath("ball");

            boolean action = false;
            if (action_ == null) {
                if (data.getChildByPath("prepare/action") != null) {
                    action_ = data.getChildByPath("prepare/action");
                    action = true;
                }
            }
            isBuff = effect != null && hit == null && ball == null;
            if (action_ != null) {
                String d;
                if (action) { //prepare
                    d = MapleDataTool.getString(action_, null);
                } else {
                    d = MapleDataTool.getString("0", action_, null);
                }
                if (d != null) {
                    isBuff |= d.equals("alert2");
                    final MapleData dd = delayData.getChildByPath(d);
                    if (dd != null) {
                        for (MapleData del : dd) {
                            ret.delay += Math.abs(MapleDataTool.getInt("delay", del, 0));
                        }
                        if (ret.delay > 30) {
                            ret.delay = (int) Math.round(ret.delay * 11.0 / 16.0);
                            ret.delay -= (ret.delay % 30);
                        }
                    }
                    if (SkillFactory.getDelay(d) != null) { //this should return true always
                        ret.animation = new ArrayList<>();
                        ret.animation.add(SkillFactory.getDelay(d));
                        if (!action) {
                            for (MapleData ddc : action_) {
                                if (!MapleDataTool.getString(ddc, d).equals(d)) {
                                    String c = MapleDataTool.getString(ddc);
                                    if (SkillFactory.getDelay(c) != null) {
                                        ret.animation.add(SkillFactory.getDelay(c));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            switch (id) { //TODO 添加新的BUFF技能
                case 1076: // 奧茲的火牢術屏障
                case 2111002: // 末日烈焰
                case 2111003: // 致命毒霧
                case 2301002: // 群體治癒
                case 2321001: // 核爆術
                case 4301004: // 雙刃旋
                case 12111005: // 火牢術屏障
                case 14111006: // 毒炸彈
                case 22161003: // 聖療之光
                case 32121006: // 魔法屏障
                case 36121007: // 時空膠囊
                case 100001266:
                    isBuff = false;
                    break;
                case 93: // 潛在開放(冒險家)
                case 1004: // 怪物騎乘
                case 1026: // 飛翔
                case 1101013: // 鬥氣集中
                case 1121016: // 魔防消除
                case 1210016: // 祝福護甲
                case 1111002:
                case 1111007:
                case 1211009:
                case 1220013:
                case 1311007:
                case 1320009:
                case 2101010: // 燎原之火
                case 2120010:
                case 2121009:
                case 2201009: // 寒冰迅移
                case 2220010:
                case 2221009:
                case 2311006:
                case 2320011:
                case 2321010:
                case 3120006:
                case 3121002:
                case 3220005:
                case 3221002:
                case 4111001:
                case 4111009:
                case 4211003:
                case 4221013:
                case 4321000:
                case 4331003:
                case 4341002:
                case 5001005:
                case 5110001:
                case 5111005:
                case 5111007:
                case 5120011:
                case 5120012:
                case 5121003:
                case 5121009:
                case 5121015:
                case 5211001:
                case 5211002:
                case 5211006:
                case 5211007:
                case 5211009:
                case 5220002:
                case 5220011:
                case 5220012:
                case 5311004:
                case 5311005:
                case 5320007:
                case 5321003:
                case 5321004:
                case 5721066: // 千斤墜
                case 5081023: // 追影連擊
                case 5701013: // 真氣流貫
                case 5711024: // 天地無我
                case 5721000: // 楓葉祝福
                case 5701005:
                case 5711001:
                case 5711011:
                case 5220014://dice2 cosair
                case 5720005:
                case 5721002:
                case 9001004:
                case 9101004:
                case 10000093:
                case 10001004:
                case 10001026:
                case 11101022: // 沉月
                case 11111022: // 旭日
                case 11121012: // 雙重力量（旭日）
                case 11121011: // 雙重力量（沉月）
                case 12000022: //元素:火焰
                case 12101023: //火之書
                case 12101024: //燃燒
                case 13111005:
                case 13001022: //元素： 風暴
                case 13101023: //快速之箭
                case 13101024: //妖精援助
                case 13111023: //阿爾法
                case 13121004: //風之祈禱
                case 13121005: //會心之眼
                case 13120008: //極限阿爾法
                case 13121053: //守護者榮耀
                case 13121054: //風暴使者
                case 14111007:
                case 14001021: //元素 : 闇黑
                case 14001022: //急速
                case 14001023: //黑暗面
                case 14001027: //暗影蝙蝠
                case 15001003:
                case 15100004:
                case 15101006:
                case 15111002:
                case 15111005:
                case 15111006:
                case 15111011:
                case 15001022: //元素： 雷電
                case 15101022: //致命快打
                case 15111022: //疾風
                case 15111023: //渦流
                case 15111024: //磁甲
                case 15121005: //最終極速
                case 15121004: // 引雷
                case 20000093:
                case 20001004:
                case 20001026:
                case 20010093:
                case 20011004:
                case 20011026:
                case 20020093:
                case 20021026:
                case 20031209:
                case 20031210:
                case 21000000:
                case 21101003:
                case 22121001:
                case 22131001:
                case 22131002:
                case 22141002:
                case 22151002:
                case 22151003:
                case 22161002:
                case 22161004:
                case 22171000:
                case 22171004:
                case 22181000:
                case 22181003:
                case 22181004:
                case 24101005:
                case 24111002:
                case 24121008:
                case 24121009:
                case 27001004: // 擴充魔力 - Mana Well
                case 27100003: // 黑暗祝福 - Black Blessing
                case 27101004: // 極速詠唱 - Booster
                case 27101202: // 黑暗之眼 - Pressure Void
                case 27111004: // 魔力護盾 - Shadow Shell
                case 27111005: // 光暗之盾 - Dusk Guard
                case 27111006: // 團隊精神 - Photic Meditation
                case 27110007: // 光暗轉換
                case 27121005: // 黑暗強化 - Dark Crescendo
                case 27121006: // 黑暗魔心 - Arcane Pitch
                case 30000093:
                case 30001026:
                case 30010093:
                case 30011026:
                case 31121005:
                case 32001003:
                case 32101003:
                case 32110000:
                case 32110007:
                case 32110008:
                case 32110009:
                case 32111005:
                case 32111006:
                case 32120000:
                case 32120001:
                case 32121003:
                case 32121017: // 黑色光環
                case 32121018: // 減益效果光環
                case 32111012: // 藍色繩索
                case 32101009: // 紅色光環
                case 32001016: // 黃色光環
                case 32100010: // 死神契約I
                case 32110017: // 死神契約II
                case 32120019: // 死神契約III
                case 32111016: // 黑暗閃電       
                case 33101006:
                case 33111003:
                case 35001001:
                case 35001002:
                case 35101005:
                case 35101007:
                case 35101009:
                case 35111001:
                case 35111002:
                case 35111004:
                case 35111005:
                case 35111009:
                case 35111010:
                case 35111011:
                case 35111013:
                case 35120000:
                case 35120014:
                case 35121003:
                case 35121005:
                case 35121006:
                case 35121009:
                case 35121010:
                case 35121013:
                case 36111006:
                case 40011186: // 劍豪初心者拔刀術
                case 41001001: // 拔刀術
                case 41110008: // 拔刀術‧心體技
                case 41101003: // 武神招來
                case 41110006: // 柳身
                case 41101005: // 秘劍‧隼
                case 41121002: // 一閃
                case 41121003: // 剛健
                case 41121005: // 曉月勇者
                case 41121014: // 疾風五月雨刃
                case 41121015: // 制敵之先
                case 41121054: // 劍神護佑
                case 41121053: // 紋櫻的祝福
                case 42100010:
                case 42101002:
                case 42101004:
                case 42111006:
                case 42121008:
                case 50001214:
                case 51101003:
                case 51111003:
                case 51111004:
                case 51121004:
                case 51121005:
                case 60001216:
                case 60001217:
                case 61101002:
                case 61111008:
                case 61120007:
                case 61120008:
                case 61120011:
                case 80001000:
                case 80001089:
                case 80001427:
                case 80001428:
                case 80001430:
                case 80001432:
                case 5111010:
                case 1221014:
                case 1310016:
                case 1321014:
                case 2120012:
                case 2220013:
                case 2320012:
                case 3101004:
                case 3111011:
                case 3201004:
                case 3211012:
                case 4341052:
                case 5100015:
                case 5220019:
                case 5221015:
                case 5720012:
                case 5721003:
                case 1121053://传说冒险家
                case 1221053:
                case 1321053:
                case 2121053:
                case 2221053:
                case 2321053:
                case 3121053:
                case 3221053:
                case 3321053:
                case 4121053:
                case 4221053:
                case 5121053:
                case 5221053://传说冒险家
                case 31201003://恶魔复仇者技能
                case 31211003:
                case 31211004:
                case 31221004:
                case 31221054://恶魔复仇者技能
                case 31221053://自由之墙
                case 32121053:
                case 33121053:
                case 31121053:
                case 35121053://自由之墙
                case 24121053://英雄奥斯
                case 23121053:
                case 27121053:
                case 25121132:
                case 21121053:
                case 22171053://英雄奥斯
                case 80001140:
                case 20050286:
                case 25111209:
                case 25111211:
                case 25121209:
                case 14110030:
                case 1221009:
                case 5121052:
                case 51121052:
                case 14121004:
                case 14121052:
                case 35121054://金属机甲:悬浮
                case 100001005:
                case 110001005:
                case 35121055:
                case 33001007:
                case 131001015: // 迷你啾出動
                case 2321052://天堂之門
                case 131001001://皮卡啾攻擊
                case 131001002://皮卡啾攻擊
                case 131001003://皮卡啾攻擊
                case 131001101://皮卡啾攻擊
                case 131001102://皮卡啾攻擊
                case 131001103://皮卡啾攻擊
                case 131002000://皮卡啾攻擊
                case 131001000://皮卡啾攻擊
                case 131001010: // 超烈焰溜溜球
                case 131001113://電吉他
                    isBuff = true;
            }
            if (GameConstants.isAngel(id)/* || GameConstants.isSummon(id)*/) {
                isBuff = false;
            }
        }
        ret.chargeskill = data.getChildByPath("keydown") != null;

        final MapleData level = data.getChildByPath("common");
        if (level != null) {
            ret.maxLevel = MapleDataTool.getInt("maxLevel", level, 1); //10 just a failsafe, shouldn't actually happens
            ret.trueMax = ret.maxLevel + (ret.combatOrders ? 2 : 0);
            for (int i = 1; i <= ret.trueMax; i++) {
                ret.effects.add(MapleStatEffect.loadSkillEffectFromData(level, id, isBuff, i, "x", ret.notRemoved));
            }
            ret.maxDamageOver = MapleDataTool.getInt("MDamageOver", level, 999999);
        } else {
            for (final MapleData leve : data.getChildByPath("level")) {
                ret.effects.add(MapleStatEffect.loadSkillEffectFromData(leve, id, isBuff, Byte.parseByte(leve.getName()), null, ret.notRemoved));
            }
            ret.maxLevel = ret.effects.size();
            ret.trueMax = ret.effects.size();
        }
        final MapleData level2 = data.getChildByPath("PVPcommon");
        if (level2 != null) {
            ret.pvpEffects = new ArrayList<>();
            for (int i = 1; i <= ret.trueMax; i++) {
                ret.pvpEffects.add(MapleStatEffect.loadSkillEffectFromData(level2, id, isBuff, i, "x", ret.notRemoved));
            }
        }
        final MapleData reqDataRoot = data.getChildByPath("req");
        if (reqDataRoot != null) {
            for (final MapleData reqData : reqDataRoot.getChildren()) {
                ret.requiredSkill.add(new Pair<>(reqData.getName(), MapleDataTool.getInt(reqData, 1)));
            }
        }
        ret.animationTime = 0;
        if (effect != null) {
            for (final MapleData effectEntry : effect) {
                ret.animationTime += MapleDataTool.getIntConvert("delay", effectEntry, 0);
            }
        }
        ret.isBuffSkill = isBuff;
        switch (id) {
            case 27000207:
            case 27001100:
            case 27001201:
                ret.masterLevel = ret.maxLevel;
        }

        ret.isSummonSkill = (data.getChildByPath("summon") != null);
        return ret;
    }

    public MapleStatEffect getEffect(final int level) {
        if (effects.size() < level) {
            if (effects.size() > 0) { //incAllskill
                return effects.get(effects.size() - 1);
            }
            return null;
        } else if (level <= 0) {
            return effects.get(0);
        }
        return effects.get(level - 1);
    }

    public MapleStatEffect getPVPEffect(final int level) {
        if (pvpEffects == null) {
            return getEffect(level);
        }
        if (pvpEffects.size() < level) {
            if (pvpEffects.size() > 0) { //incAllskill
                return pvpEffects.get(pvpEffects.size() - 1);
            }
            return null;
        } else if (level <= 0) {
            return pvpEffects.get(0);
        }
        return pvpEffects.get(level - 1);
    }

    public int getSkillType() {
        return skillType;
    }

    public List<Integer> getAllAnimation() {
        return animation;
    }

    public int getAnimation() {
        if (animation == null) {
            return -1;
        }
        return (animation.get(Randomizer.nextInt(animation.size())));
    }

    public boolean isPVPDisabled() {
        return pvpDisabled;
    }

    public boolean isChargeSkill() {
        return chargeskill;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public boolean hasRequiredSkill() {
        return requiredSkill.size() > 0;
    }

    public List<Pair<String, Integer>> getRequiredSkills() {
        return requiredSkill;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getTrueMax() {
        return trueMax;
    }

    public boolean combatOrders() {
        return combatOrders;
    }

    public boolean canBeLearnedBy(int job) {
        int skillForJob = id / 10000;
        return MapleJob.getJobGrade(skillForJob) <= MapleJob.getJobGrade(job) && MapleJob.isSameJob(job, skillForJob);
    }

    public boolean isTimeLimited() {
        return timeLimited;
    }

    public boolean isFourthJob() {
        if (isHyper()) {
            return true;
        }

        switch (id) { // I guess imma make an sql table to store these, so that we could max them all out.
            case 1120012:
            case 1320011:
            case 3110014:
            case 4320005:
            case 4340010:
            case 4340012:
            case 5120011:
            case 5120012:
            case 5220012:
            case 5220014:
            case 5321006:
            case 5720008:
            case 21120011:
            case 21120014:
            case 22171004:
            case 22181004:
            case 23120011:
            case 23120013:
            case 23121008:
            case 33120010:
            case 33121005:
                return false;
        }

        switch (this.id / 10000) {
            case 2312:
            case 2412:
            case 2217:
            case 2218:
            case 2512:
            case 2712:
            case 3122:
                return true;
            case 3612:
                return getMasterLevel() >= 10;
            case 6112:
            case 6512:
                return true;
            case 10100:
                return this.id == 101000101;
            case 10110:
                return (this.id == 101100101) || (this.id == 101100201);
            case 10111:
                return (this.id == 101110102) || (this.id == 101110200) || (this.id == 101110203);
            case 10112:
                return (this.id == 101120104) || (this.id == 101120204);
        }
        if ((getMaxLevel() <= 15 && !invisible && getMasterLevel() <= 0)) {
            return false;
        }
        //龍魔技能
        if (id / 10000 >= 2210 && id / 10000 < 3000) {
            return ((id / 10000) % 10) >= 7 || getMasterLevel() > 0;
        }
        //影武技能
        if (id / 10000 >= 430 && id / 10000 <= 434) {
            return ((id / 10000) % 10) == 4 || getMasterLevel() > 0;
        }
        if (this.id == 40020002) {
            return true;
        }
        //冒險家
        return ((id / 10000) % 10) == 2 && id < 90000000 && !isBeginnerSkill();
    }

    public Element getElement() {
        return element;
    }

    public int getvehicleID() {
        return vehicleID;
    }

    public int getAnimationTime() {
        return animationTime;
    }

    public int getMasterLevel() {
        return masterLevel;
    }

    public int getDelay() {
        return delay;
    }

    public int getTamingMob() {
        return eventTamingMob;
    }

    public int getSkillTamingMob() {
        return eventTamingMob;
    }

    public boolean isBeginnerSkill() {
        int jobId = id / 10000;
        return MapleJob.isBeginner(jobId);
    }

    public boolean isMagic() {
        return magic;
    }

    public boolean isHyper() {
        //return hyper > 0;
        return (hyper > 0) && (reqLev > 0);
    }

    public int getHyper() {
        return hyper;
    }

    public int getReqLevel() {
        return reqLev;
    }

    public int getMaxDamageOver() {
        return maxDamageOver;
    }

    public boolean isMovement() {
        return casterMove;
    }

    public boolean isPush() {
        return pushTarget;
    }

    public boolean isBuffSkill() {
        return this.isBuffSkill;
    }

    public boolean isSummonSkill() {
        return this.isSummonSkill;
    }

    public boolean isPull() {
        return pullTarget;
    }

    public boolean isAdminSkill() {
        int jobId = id / 10000;
        return MapleJob.is管理員(jobId);
    }

    public boolean isInnerSkill() {
        int jobId = id / 10000;
        return jobId == 7000;
    }

    public boolean isSpecialSkill() {
        int jobId = id / 10000;
        switch(jobId) {
            case 7000:
            case 7100:
            case 8000:
            case 9000:
            case 9100:
            case 9200:
            case 9201:
            case 9202:
            case 9203:
            case 9204:
            case 9500:
                return true;
            default:
                return false;
        }
    }

    public boolean isPetPassive() {
        return this.petPassive;
    }

    public int getSetItemReason() {
        return this.setItemReason;
    }

    public int geSetItemPartsCount() {
        return this.setItemPartsCount;
    }

    @Override
    public int compare(Skill o1, Skill o2) {
        return (Integer.valueOf(o1.getId()).compareTo(o2.getId()));
    }

    public boolean isTeachSkills() {
        switch (this.id) {
            case 110:
            case 1214:
            case 20021110:
            case 20030204:
            case 20040218:
            case 30010112:
            case 30010241:
            case 30020233:
            case 50001214:
            case 60000222:
            case 60011219:
            case 110000800:
            case 10000255:
            case 10000256:
            case 10000257:
            case 10000258:
            case 10000259:
            case 100000271:
                return true;
        }
        return false;
    }

    //TODO 連結技能
    public boolean isLinkSkills() {
        switch (this.id) {
            case 80000000:
            case 80000001:
            case 80000002:
            case 80000005:
            case 80000006:
            case 80000047:
            case 80000050:
            case 80001040:
            case 80001140:
            case 80001151:
            case 80001155:
            case 80000169://九死一生
            case 80010006:
            case 80000070:
            case 80000066:
            case 80000067:
            case 80000068:
            case 80000069:
            case 80000055:
                return true;
        }
        return false;
    }

    public boolean isLinkedAttackSkill() {
        return GameConstants.isLinkedAttackSkill(id);
    }

    public int getFixLevel() {
        return fixLevel;
    }
}
