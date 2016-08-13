package tools.packet;

import client.*;
import client.inventory.*;
import constants.GameConstants;
import constants.ItemConstants;
import constants.ServerConstants;
import handling.Buffstat;
import handling.world.MapleCharacterLook;
import java.util.*;
import java.util.Map.Entry;

import server.MapleItemInformationProvider;
import server.Randomizer;
import server.movement.LifeMovementFragment;
import server.quest.MapleQuest;
import server.shops.MapleShop;
import server.shops.MapleShopItem;
import server.stores.AbstractPlayerStore;
import server.stores.IMaplePlayerShop;
import tools.BitTools;
import tools.DateUtil;
import tools.HexTool;
import tools.KoreanDateUtil;
import tools.Pair;
import tools.StringUtil;
import tools.Triple;
import tools.data.MaplePacketLittleEndianWriter;

public class PacketHelper {

    public static final long FT_UT_OFFSET = 116444592000000000L;
    public static final long MAX_TIME = 150842304000000000L;
    public static final long ZERO_TIME = 94354848000000000L;
    public static final long PERMANENT = 150841440000000000L;

    public static long getKoreanTimestamp(long realTimestamp) {
        return getTime(realTimestamp);
    }

    public static long getTime(long realTimestamp) {
        if (realTimestamp == -1L) { // 00 80 05 BB 46 E6 17 02, 1/1/2079
            return MAX_TIME;
        }
        if (realTimestamp == -2L) { // 00 40 E0 FD 3B 37 4F 01, 1/1/1900
            return ZERO_TIME;
        }
        if (realTimestamp == -3L) {
            return PERMANENT;
        }
        return realTimestamp * 10000L + 116444592000000000L;
    }

    public static long decodeTime(long fakeTimestamp) {
        if (fakeTimestamp == 150842304000000000L) {
            return -1L;
        }
        if (fakeTimestamp == 94354848000000000L) {
            return -2L;
        }
        if (fakeTimestamp == 150841440000000000L) {
            return -3L;
        }
        return (fakeTimestamp - 116444592000000000L) / 10000L;
    }

    public static long getFileTimestamp(long timeStampinMillis, boolean roundToMinutes) {
        if (TimeZone.getDefault().inDaylightTime(new Date())) {
            timeStampinMillis -= 3600000L;
        }
        long time;

        if (roundToMinutes) {
            time = timeStampinMillis / 1000L / 60L * 600000000L;
        } else {
            time = timeStampinMillis * 10000L;
        }
        return time + 116444592000000000L;
    }

    public static void addImageInfo(MaplePacketLittleEndianWriter mplew, byte[] image) {
        mplew.writeInt(image.length);
        mplew.write(image);
    }

    public static void addStartedQuestInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        boolean newPacket = true;
        mplew.write(newPacket);
        if (newPacket) {
            final List<MapleQuestStatus> started = chr.getStartedQuests();
            mplew.writeShort(started.size());
            for (MapleQuestStatus q : started) {
                mplew.writeShort(q.getQuest().getId());
                mplew.writeShort(0);
                if (q.hasMobKills()) {
                    StringBuilder sb = new StringBuilder();
                    for (Iterator i$ = q.getMobKills().values().iterator(); i$.hasNext();) {
                        int kills = ((Integer) i$.next());
                        sb.append(StringUtil.getLeftPaddedStr(String.valueOf(kills), '0', 3));
                    }
                    mplew.writeMapleAsciiString(sb.toString());
                } else {
                    mplew.writeMapleAsciiString(q.getCustomData() == null ? "" : q.getCustomData());
                }
            }
        } else {
            final List<MapleQuestStatus> started = chr.getStartedQuests();
            mplew.writeShort(started.size());
            for (MapleQuestStatus q : started) {
                mplew.writeShort(q.getQuest().getId());
            }
        }
    }
    /*mplew.write(1);
     final List<MapleQuestStatus> started = chr.getStartedQuests();
     mplew.writeShort(started.size());
     for (MapleQuestStatus q : started) {
     mplew.writeShort(q.getQuest().getId());
     if (q.hasMobKills()) {
     StringBuilder sb = new StringBuilder();
     for (Iterator i$ = q.getMobKills().values().iterator(); i$.hasNext();) {
     int kills = ((Integer) i$.next()).intValue();
     sb.append(StringUtil.getLeftPaddedStr(String.valueOf(kills), '0', 3));
     }
     mplew.writeMapleAsciiString(sb.toString());
     } else {
     mplew.writeMapleAsciiString(q.getCustomData() == null ? "" : q.getCustomData());
     }
     }
     //        addNXQuestInfo(mplew, chr);
     }*/

    public static void addNXQuestInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        mplew.writeShort(0);
        /*
         mplew.writeShort(7);
         mplew.writeMapleAsciiString("1NX5211068");
         mplew.writeMapleAsciiString("1");
         mplew.writeMapleAsciiString("SE20130619");
         mplew.writeMapleAsciiString("20130626060823");
         mplew.writeMapleAsciiString("99NX5533018");
         mplew.writeMapleAsciiString("1");
         mplew.writeMapleAsciiString("1NX1003792");
         mplew.writeMapleAsciiString("1");
         mplew.writeMapleAsciiString("1NX1702337");
         mplew.writeMapleAsciiString("1");
         mplew.writeMapleAsciiString("1NX9102857");
         mplew.writeMapleAsciiString("1");
         mplew.writeMapleAsciiString("SE20130116");
         mplew.writeMapleAsciiString("1");
         */
    }

    public static void addCompletedQuestInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        boolean newPacket = true;
        mplew.write(newPacket);
        if (newPacket) {
            final List<MapleQuestStatus> completed = chr.getCompletedQuests();
            mplew.writeShort(completed.size());
            for (MapleQuestStatus q : completed) {
                mplew.writeShort(q.getQuest().getId());
                mplew.writeShort(0);
                mplew.writeInt(KoreanDateUtil.getQuestTimestamp(q.getCompletionTime()));
                //v139 changed from long to int
            }
        } else {
            final List<MapleQuestStatus> completed = chr.getCompletedQuests();
            mplew.writeShort(completed.size());
            for (MapleQuestStatus q : completed) {
                mplew.writeShort(q.getQuest().getId());
            }
        }
    }
    /*mplew.write(1);
     final List<MapleQuestStatus> completed = chr.getCompletedQuests();
     mplew.writeShort(completed.size());
     for (MapleQuestStatus q : completed) {
     mplew.writeShort(q.getQuest().getId());
     //mplew.writeLong(KoreanDateUtil.getQuestTimestamp(q.getCompletionTime()));
     mplew.writeLong(getTime(q.getCompletionTime()));
     //v139 changed from long to int
     }
     }*/

    public static void addSkillInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        final Map<Skill, SkillEntry> skills = chr.getSkills();
        boolean newPacket = true;
        mplew.write(newPacket);
        if (newPacket) {
            mplew.writeShort(skills.size());
            //技能不知道为什么 错误
            for (Entry<Skill, SkillEntry> skill : skills.entrySet()) {
                if(((Skill) skill.getKey()) != null){
                   mplew.writeInt(((Skill) skill.getKey()).getId());
                   if (((Skill) skill.getKey()).isLinkSkills()) {
                       mplew.writeInt(((SkillEntry) skill.getValue()).teachId);
                   } else if (((Skill) skill.getKey()).isTeachSkills()) {
                       mplew.writeInt(((SkillEntry) skill.getValue()).teachId > 0 ? ((SkillEntry) skill.getValue()).teachId : chr.getId());
                   } else {
                       mplew.writeInt(((SkillEntry) skill.getValue()).skillLevel);
                   }
                   addExpirationTime(mplew, ((SkillEntry) skill.getValue()).expiration);
                   if (((Skill) skill.getKey()).isFourthJob()) {
                       mplew.writeInt(((SkillEntry) skill.getValue()).masterlevel);
                   }
                }
            }
            int v87 = 0;
            mplew.writeShort(v87);
            for (int i = 0; i < v87; i++) {
                mplew.writeInt(0);
                mplew.writeShort(0);
            }
        } else {
            final Map<Integer, Integer> skillsWithoutMax = new LinkedHashMap<>();
            final Map<Integer, Long> skillsWithExpiration = new LinkedHashMap<>();
            final Map<Integer, Byte> skillsWithMax = new LinkedHashMap<>();

            for (final Entry<Skill, SkillEntry> skill : skills.entrySet()) {
                skillsWithoutMax.put(((Skill) skill.getKey()).getId(), ((SkillEntry) skill.getValue()).skillLevel);
                if (((SkillEntry) skill.getValue()).expiration > 0L) {
                    skillsWithExpiration.put(((Skill) skill.getKey()).getId(), ((SkillEntry) skill.getValue()).expiration);
                }
                if (((Skill) skill.getKey()).isFourthJob()) {
                    skillsWithMax.put(((Skill) skill.getKey()).getId(), ((SkillEntry) skill.getValue()).masterlevel);
                }
            }

            int amount = skillsWithoutMax.size();
            mplew.writeShort(amount);
            for (final Entry<Integer, Integer> x : skillsWithoutMax.entrySet()) {
                mplew.writeInt((x.getKey()));
                mplew.writeInt((x.getValue()));
            }
            mplew.writeShort(0);

            amount = skillsWithExpiration.size();
            mplew.writeShort(amount);
            for (final Entry<Integer, Long> x : skillsWithExpiration.entrySet()) {
                mplew.writeInt((x.getKey()));
                mplew.writeLong((x.getValue()));
            }
            mplew.writeShort(0);

            amount = skillsWithMax.size();
            mplew.writeShort(amount);
            for (final Entry<Integer, Byte> x : skillsWithMax.entrySet()) {
                mplew.writeInt((x.getKey()));
                mplew.writeInt((x.getValue()));
            }
            mplew.writeShort(0);
        }
    }

    public static void addCoolDownInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        final List<MapleCoolDownValueHolder> cd = chr.getCooldowns();
        mplew.writeShort(cd.size());
        for (MapleCoolDownValueHolder cooling : cd) {
            mplew.writeInt(cooling.skillId);
            mplew.writeInt((int) (cooling.length + cooling.startTime - System.currentTimeMillis()) / 1000);
        }
    }

    public static void addRocksInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        int[] mapz = chr.getRegRocks();
        for (int i = 0; i < 5; i++) {
            mplew.writeInt(mapz[i]);
        }

        int[] map = chr.getRocks();
        for (int i = 0; i < 10; i++) {
            mplew.writeInt(map[i]);
        }

        int[] maps = chr.getHyperRocks();
        for (int i = 0; i < 13; i++) {
            mplew.writeInt(maps[i]);
        }
    }

    public static void addMiniGameInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        short size = 0;
        mplew.writeShort(size);
        for (int i = 0; i < size; i++) {
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.write(0);
        }
    }

    public static void addRingInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        Triple<List<MapleRing>, List<MapleRing>, List<MapleRing>> aRing = chr.getRings(true);
        List<MapleRing> cRing = aRing.getLeft();
        mplew.writeShort(cRing.size());
        for (MapleRing ring : cRing) {
            mplew.writeInt(ring.getPartnerChrId());
            mplew.writeAsciiString(ring.getPartnerName(), 15);
            mplew.writeLong(ring.getRingId());
            mplew.writeLong(ring.getPartnerRingId());
        }
        List<MapleRing> fRing = aRing.getMid();
        mplew.writeShort(fRing.size());
        for (MapleRing ring : fRing) {
            mplew.writeInt(ring.getPartnerChrId());
            mplew.writeAsciiString(ring.getPartnerName(), 15);
            mplew.writeLong(ring.getRingId());
            mplew.writeLong(ring.getPartnerRingId());
            mplew.writeInt(ring.getItemId());
        }
        List<MapleRing> mRing = aRing.getRight();
        mplew.writeShort(mRing.size());
        int marriageId = 30000;
        for (MapleRing ring : mRing) {
            mplew.writeInt(marriageId);
            mplew.writeInt(chr.getId());
            mplew.writeInt(ring.getPartnerChrId());
            mplew.writeShort(3);
            mplew.writeInt(ring.getItemId());
            mplew.writeInt(ring.getItemId());
            mplew.writeAsciiString(chr.getName(), 15);
            mplew.writeAsciiString(ring.getPartnerName(), 15);
        }
    }

    public static void addMoneyInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        mplew.writeLong(chr.getMeso());
    }

    public static void addInventoryInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        mplew.write(chr.getInventory(MapleInventoryType.EQUIP).getSlotLimit());
        mplew.write(chr.getInventory(MapleInventoryType.USE).getSlotLimit());
        mplew.write(chr.getInventory(MapleInventoryType.SETUP).getSlotLimit());
        mplew.write(chr.getInventory(MapleInventoryType.ETC).getSlotLimit());
        mplew.write(chr.getInventory(MapleInventoryType.CASH).getSlotLimit());
        MapleQuestStatus stat = chr.getQuestNoAdd(MapleQuest.getInstance(122700));
        if ((stat != null) && (stat.getCustomData() != null) && (Long.parseLong(stat.getCustomData()) > System.currentTimeMillis())) {
            mplew.writeLong(getTime(Long.parseLong(stat.getCustomData())));
        } else {
            mplew.writeLong(getTime(-2L));  //00 40 E0 FD 3B 37 4F 01
        }
        mplew.write(0);
        MapleInventory iv = chr.getInventory(MapleInventoryType.EQUIPPED);
        final List<Item> equipped = iv.newList();
        Collections.sort(equipped);
        for (Item item : equipped) {
            if ((item.getPosition() < 0) && (item.getPosition() > -100)) {//普通裝備
                addItemPosition(mplew, item, false, false);
                addItemInfo(mplew, item, chr);
            }
        }
        mplew.writeShort(0);//1
        for (Item item : equipped) {
            if ((item.getPosition() <= -100) && (item.getPosition() > -1000)) {//現金裝備
                addItemPosition(mplew, item, false, false);
                addItemInfo(mplew, item, chr);
            }
        }
        mplew.writeShort(0);//2
        iv = chr.getInventory(MapleInventoryType.EQUIP);
        for (Item item : iv.list()) {//裝備欄
            addItemPosition(mplew, item, false, false);
            addItemInfo(mplew, item, chr);
        }
        mplew.writeShort(0);//3
        for (Item item : equipped) {
            if ((item.getPosition() <= -1000) && (item.getPosition() > -1100)) {//龍魔龍裝備
                addItemPosition(mplew, item, false, false);
                addItemInfo(mplew, item, chr);
            }
        }
        mplew.writeShort(0);//4
        for (Item item : equipped) {
            if ((item.getPosition() <= -1100) && (item.getPosition() > -1200)) {//機甲裝備
                addItemPosition(mplew, item, false, false);
                addItemInfo(mplew, item, chr);
            }
        }
        mplew.writeShort(0);//5
        for (Item item : equipped) {
            if ((item.getPosition() <= -1200) && (item.getPosition() > -1300)) {//機器人的現金裝備
                addItemPosition(mplew, item, false, false);
                addItemInfo(mplew, item, chr);
            }
        }
        mplew.writeShort(0);//6
        for (Item item : equipped) {
            if ((item.getPosition() <= -1300) && (item.getPosition() > -1400)) {//天使破壞者裝備
                addItemPosition(mplew, item, false, false);
                addItemInfo(mplew, item, chr);
            }
        }
        mplew.writeShort(0);//7
        for (Item item : equipped) {
            if ((item.getPosition() <= -1400) && (item.getPosition() > -1500)) {//拼圖
                addItemPosition(mplew, item, false, false);
                addItemInfo(mplew, item, chr);
            }
        }
        mplew.writeShort(0);//8
        for (Item item : equipped) {
            if ((item.getPosition() <= -1500) && (item.getPosition() > -1600)) {//未知[未確認]
                addItemPosition(mplew, item, false, false);
                addItemInfo(mplew, item, chr);
            }
        }
        mplew.writeShort(0);//9
        for (Item item : equipped) {
            if ((item.getPosition() <= -1600) && (item.getPosition() > -1700)) {//獸魔裝備[未確認]
                addItemPosition(mplew, item, false, false);
                addItemInfo(mplew, item, chr);
            }
        }
        mplew.writeShort(0);//10
        for (Item item : equipped) {
            if ((item.getPosition() <= -1700) && (item.getPosition() > -1800)) {//未知[未確認]
                addItemPosition(mplew, item, false, false);
                addItemInfo(mplew, item, chr);
            }
        }
        mplew.writeShort(0);//11
        for (Item item : equipped) {
            if ((item.getPosition() <= -5000) && (item.getPosition() > -5003)) {//圖騰
                addItemPosition(mplew, item, false, false);
                addItemInfo(mplew, item, chr);
            }
        }
        mplew.writeShort(0);//12
        mplew.writeShort(0);//13

        //new143 idk where hai hemmi idk too
        mplew.writeShort(0);//14
        mplew.writeShort(0);//15
        mplew.writeShort(0);//16
        mplew.writeShort(0);//17

        iv = chr.getInventory(MapleInventoryType.USE); // 消耗
        for (Item item : iv.list()) {
            addItemPosition(mplew, item, false, false);
            addItemInfo(mplew, item, chr);
        }
        mplew.write(0);//a
        iv = chr.getInventory(MapleInventoryType.SETUP); // 装饰
        for (Item item : iv.list()) {
            addItemPosition(mplew, item, false, false);
            addItemInfo(mplew, item, chr);
        }
        mplew.write(0);//b
        iv = chr.getInventory(MapleInventoryType.ETC); // 其他
        for (Item item : iv.list()) {
            if (item.getPosition() < 100) {
                addItemPosition(mplew, item, false, false);
                addItemInfo(mplew, item, chr);
            }
        }
        mplew.write(0);//c
        iv = chr.getInventory(MapleInventoryType.CASH); // 特殊(現金欄位)
        for (Item item : iv.list()) {
            addItemPosition(mplew, item, false, false);
            addItemInfo(mplew, item, chr);
        }
        mplew.write(0);//d
        mplew.writeInt(0);
        mplew.writeInt(chr.getExtendedSlots().size());
        for (int i = 0; i < chr.getExtendedSlots().size(); i++) {
            mplew.writeInt(i);
            mplew.writeInt(chr.getExtendedSlot(i));
            for (Item item : chr.getInventory(MapleInventoryType.ETC).list()) {
                if ((item.getPosition() > i * 100 + 100) && (item.getPosition() < i * 100 + 200)) {
                    addItemPosition(mplew, item, false, true);
                    addItemInfo(mplew, item, chr);
                }
            }
            mplew.writeInt(-1);
        }
        mplew.writeZeroBytes(9);//was9
        mplew.writeInt(0); // 185 or 186
    }

    public static void addPotionPotInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        if (chr.getPotionPots() == null) {
            mplew.writeInt(0);
            return;
        }
        mplew.writeInt(chr.getPotionPots().size());
        for (MaplePotionPot p : chr.getPotionPots()) {
            mplew.writeInt(p.getId());
            mplew.writeInt(p.getMaxValue());
            mplew.writeInt(p.getHp());
            mplew.writeInt(0);
            mplew.writeInt(p.getMp());

            mplew.writeLong(PacketHelper.getTime(p.getStartDate()));
            mplew.writeLong(PacketHelper.getTime(p.getEndDate()));
        }
    }

    public static void addCharCreateStats(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        mplew.writeInt(chr.getId());

        mplew.writeLong(0);
        
        mplew.writeAsciiString(chr.getName(), 13);
        mplew.write(MapleJob.is神之子(chr.getJob()) ? 0 : chr.getGender());
//        mplew.write(0); // addCharCreateStats unk
        mplew.write(chr.getSkinColor());
        
        mplew.writeInt(chr.getFace());
        mplew.writeInt(chr.getHair());
        
        mplew.write(-1);//176+
        mplew.write(0);//176+
        mplew.write(0);//176+
        
        mplew.write(chr.getLevel());
        mplew.writeShort(chr.getJob());
        
        chr.getStat().connectData(mplew, chr);
        mplew.writeShort(chr.getRemainingAp());
        
        if (GameConstants.isSeparatedSp(chr.getJob())) { //
            int size = chr.getRemainingSpSize();
            mplew.write(size);
            for (int i = 0; i < chr.getRemainingSps().length; i++) {
                if (chr.getRemainingSp(i) > 0) {
                    mplew.write(i + 1);
                    mplew.writeInt(chr.getRemainingSp(i));
                }
            }
        } else {
            mplew.writeShort(chr.getRemainingSp());
        }
        
        mplew.writeLong(chr.getExp());
        mplew.writeInt(chr.getFame());
        mplew.writeInt(chr.getWeaponPoint()); // 未知国服132新出 位置51  A5 5D 5B 00 |0C AD BA 13  |A8 18 3B 00  |6D 28 3B 00  |E0 1F 3B 00
   
        mplew.writeLong(chr.getGachExp());
        mplew.writeLong(DateUtil.getFileTimestamp(System.currentTimeMillis()));//00 40 E0 FD 3B 37 4F 01
        mplew.writeInt(chr.getMapId());
        mplew.write(chr.getInitialSpawnpoint());
        mplew.writeShort(chr.getSubcategory());
        if (MapleJob.is惡魔(chr.getJob()) || MapleJob.is尖兵(chr.getJob()) || MapleJob.is林芝林(chr.getJob())) {
            mplew.writeInt(chr.getFaceMarking());
        }
        mplew.write(chr.getFatigue());
        
        mplew.writeInt(DateUtil.getTime());//登陆时间?
        for (MapleTrait.MapleTraitType t : MapleTrait.MapleTraitType.values()) {//性向,循環6次
            mplew.writeInt(chr.getTrait(t).getTotalExp());
        }
        //idb - 21
        for (MapleTrait.MapleTraitType t2 : MapleTrait.MapleTraitType.values()) {//性向,循環6次
            mplew.writeShort(0); // today's trait points
        }
        mplew.write(0);
        mplew.writeLong(getTime(-2L));//00 40 E0 FD 3B 37 4F 01
        mplew.writeShort(0);
        mplew.writeInt(0);
        mplew.writeInt(0x58);
        mplew.writeInt(0);
        mplew.write(1);
        
        addPartTimeJob(mplew, MapleCharacter.getPartTime(chr.getId()));
        chr.getCharacterCard().connectData(mplew);
        mplew.writeReversedLong(PacketHelper.getTime(System.currentTimeMillis()));
        mplew.write(0);
        mplew.writeReversedInt(DateUtil.getTime(System.currentTimeMillis()));
    }

    public static void addCharStats(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        mplew.writeInt(chr.getId());
        mplew.writeInt(chr.getId());
        mplew.writeInt(0x2C);// 132  本来是两个 INT  一个是; 0 一个是   本来是1

        mplew.writeAsciiString(chr.getName(), 13);
        
        mplew.write(MapleJob.is神之子(chr.getJob()) ? 0 : chr.getGender());
        mplew.write(chr.getSkinColor());
        
        mplew.writeInt(chr.getFace());
        mplew.writeInt(chr.getHair());
        
        mplew.write(-1);//176+
        mplew.writeShort(0);
        mplew.write(chr.getLevel());
     

       
        mplew.writeShort(chr.getJob());
        chr.getStat().connectData(mplew, chr);//4short 4int
        mplew.writeShort(chr.getRemainingAp());
        //这里未看
        if (GameConstants.isSeparatedSp(chr.getJob())) { //
            int size = chr.getRemainingSpSize();
            mplew.write(size);
            for (int i = 0; i < chr.getRemainingSps().length; i++) {
                if (chr.getRemainingSp(i) > 0) {
                    mplew.write(i + 1);
                    mplew.writeInt(chr.getRemainingSp(i));
                }
            }
        } else {
            mplew.writeShort(chr.getRemainingSp());
        }
        
        
        mplew.writeLong(chr.getExp());
        mplew.writeInt(chr.getFame());
        mplew.writeInt(chr.getWeaponPoint()); // 未知国服132新出 位置51  A5 5D 5B 00 |0C AD BA 13  |A8 18 3B 00  |6D 28 3B 00  |E0 1F 3B 00
       
        mplew.writeLong(chr.getGachExp());
        mplew.writeLong(DateUtil.getFileTimestamp(System.currentTimeMillis()));//00 40 E0 FD 3B 37 4F 01
        mplew.writeInt(chr.getMapId());
        mplew.write(chr.getInitialSpawnpoint());
        mplew.writeShort(chr.getSubcategory());
        if (MapleJob.is惡魔(chr.getJob()) || MapleJob.is尖兵(chr.getJob()) || MapleJob.is林芝林(chr.getJob())) {
            mplew.writeInt(chr.getFaceMarking());
        }
        mplew.write(chr.getFatigue());
        mplew.writeInt(GameConstants.getCurrentDate());
        mplew.write(0);
        for (MapleTrait.MapleTraitType t : MapleTrait.MapleTraitType.values()) {//性向,循環6次
            mplew.writeInt(chr.getTrait(t).getTotalExp());
        }
        //idb - 21
        for (MapleTrait.MapleTraitType t2 : MapleTrait.MapleTraitType.values()) {//性向,循環6次
           mplew.writeShort(0); // today's trait points
        }
        
        
        mplew.writeLong(getTime(-2L));//00 40 E0 FD 3B 37 4F 01
//        mplew.writeInt(chr.getStat().pvpExp);
//        mplew.write(chr.getStat().pvpRank);
//        mplew.writeInt(chr.getBattlePoints());
        mplew.writeInt(chr.getStat().pvpExp);
        mplew.write(chr.getStat().pvpRank);
        mplew.writeInt(chr.getBattlePoints());
        mplew.write(5);
        mplew.write(6);
        mplew.writeInt(0);
        //15位

        addPartTimeJob(mplew, MapleCharacter.getPartTime(chr.getId()));//14位
        chr.getCharacterCard().connectData(mplew);//9位*9个卡槽
        mplew.writeReversedInt(PacketHelper.getTime(System.currentTimeMillis()));
        mplew.writeReversedLong(PacketHelper.getTime(System.currentTimeMillis()));
        

        mplew.write(0x16);  //未知-->常见 c4,16 ,be
    }

    public static void addCharLook(MaplePacketLittleEndianWriter mplew, MapleCharacterLook chr, boolean mega, boolean second) {
        mplew.write(second ? chr.getSecondGender() : chr.getGender());
        mplew.write(second ? chr.getSecondSkinColor() : chr.getSkinColor());
        mplew.writeInt(second ? chr.getSecondFace() : chr.getFace());
        mplew.writeInt(chr.getJob());
        mplew.write(mega ? 0 : 1);
        mplew.writeInt(second ? chr.getSecondHair() : chr.getHair());

        final Map<Byte, Integer> myEquip = new LinkedHashMap<>();
        final Map<Byte, Integer> maskedEquip = new LinkedHashMap<>();
        final Map<Byte, Integer> equip = second ? chr.getSecondEquips(true) : chr.getEquips(true);
        for (final Entry<Byte, Integer> item : equip.entrySet()) {
            if ((item.getKey()) < -127) {
                continue;
            }
            byte pos = (byte) ((item.getKey()) * -1);

            if ((pos < 100) && (myEquip.get(pos) == null)) {
                myEquip.put(pos, item.getValue());
            } else if ((pos > 100) && (pos != 111)) {
                pos = (byte) (pos - 100);
                if (myEquip.get(pos) != null) {
                    maskedEquip.put(pos, myEquip.get(pos));
                }
                myEquip.put(pos, item.getValue());
            } else if (myEquip.get(pos) != null) {
                maskedEquip.put(pos, item.getValue());
            }
        }
        final Map<Byte, Integer> totemEquip = chr.getTotems();

        for (Map.Entry entry : myEquip.entrySet()) {
            int weapon = ((Integer) entry.getValue());
            if (ItemConstants.武器類型(weapon) == (second ? MapleWeaponType.琉 : MapleWeaponType.璃)) {
                continue;
            }
            mplew.write(((Byte) entry.getKey()));
            mplew.writeInt(((Integer) entry.getValue()));
        }
        mplew.write(0xFF);

        for (Map.Entry entry : maskedEquip.entrySet()) {
            mplew.write(((Byte) entry.getKey()));
            mplew.writeInt(((Integer) entry.getValue()));
        }
        mplew.write(0xFF);

        for (Map.Entry entry : totemEquip.entrySet()) {
            mplew.write(((Byte) entry.getKey()));
            mplew.writeInt(((Integer) entry.getValue()));
        }
        mplew.write(0xFF);

        boolean zero = MapleJob.is神之子(chr.getJob());
        Integer cWeapon = equip.get(Byte.valueOf((byte) -111));
        Integer Weapon = equip.get(Byte.valueOf((byte) -11));
        Integer Shield = equip.get(Byte.valueOf((byte) -10));
        mplew.writeInt(cWeapon != null ? cWeapon : 0);
        mplew.writeInt(Weapon != null ? Weapon : 0);
        mplew.writeInt(!zero && Shield != null ? Shield : 0);//盾
        mplew.write(!MapleJob.is双弩精灵(chr.getJob()) ? chr.getElf() : chr.getElf() == 0 ? 1 : 0);// 精靈耳朵
        mplew.writeInt(0); // 寵物[1]
        mplew.writeInt(0); // 寵物[2]
        mplew.writeInt(0); // 寵物[3]
        
//        这个是宠物代码 从121的端搬来的
//         for (int i = 0; i < 3; i++) {
//            if (channelserver) {
//                mplew.writeInt(chr.getPet(i) != null ? chr.getPet(i).getPetItemId() : 0);
//            } else {
//                mplew.writeInt(0);
//            }
//        }
        
        
        if (MapleJob.is惡魔(chr.getJob())) {
            mplew.writeInt(chr.getFaceMarking());
        } else if (MapleJob.is尖兵(chr.getJob())) {
            mplew.writeInt(chr.getFaceMarking());
        } else if (MapleJob.is神之子(chr.getJob())) {
            mplew.write(1);
        } else if (MapleJob.is林芝林(chr.getJob())) {
            mplew.writeInt(chr.getFaceMarking());
            mplew.write(1);
            mplew.writeInt(chr.getEars());
            mplew.write(1);
            mplew.writeInt(chr.getTail());
        }
//        mplew.write(0);//176+
//        mplew.write(0);//176+
//        mplew.writeZeroBytes(5);//186+
    }

    public static void addExpirationTime(MaplePacketLittleEndianWriter mplew, long time) {
        mplew.writeLong(getTime(time));
    }

    public static void addItemPosition(MaplePacketLittleEndianWriter mplew, Item item, boolean trade, boolean bagSlot) {
        if (item == null) {
            mplew.write(0);
            return;
        }
        short pos = item.getPosition();
        if (pos <= -1) {
            pos = (short) (pos * -1);
            if ((pos > 100) && (pos < 1000)) {
                pos = (short) (pos - 100);
            }
        }
        if (bagSlot) {
            mplew.writeInt(pos % 100 - 1);
        } else if ((!trade) && (item.getType() == 1)) {
            mplew.writeShort(pos);
        } else {
            mplew.write(pos);
        }
    }

    public static void addItemInfo(MaplePacketLittleEndianWriter mplew, Item item) {
        addItemInfo(mplew, item, null);
    }

    public static void addItemInfo(final MaplePacketLittleEndianWriter mplew, final Item item, final MapleCharacter chr) {
        mplew.write(item.getPet() != null ? 3 : item.getType());
        mplew.writeInt(item.getItemId());
        boolean hasUniqueId = item.getUniqueId() > 0 && !ItemConstants.类型.結婚戒指(item.getItemId()) && item.getItemId() / 10000 != 166;
        //marriage rings arent cash items so dont have uniqueids, but we assign them anyway for the sake of rings
        mplew.write(hasUniqueId ? 1 : 0);
        if (hasUniqueId) {
            mplew.writeLong(item.getUniqueId());
        }
        if (item.getPet() != null) { // Pet
            addPetItemInfo(mplew, item, item.getPet(), true);
        } else {
            addExpirationTime(mplew, item.getExpiration());
            mplew.writeInt(chr == null ? -1 : chr.getExtendedSlots().indexOf(item.getItemId()));
            if (item.getType() == 1) {
                //mplew.write(0);
                final Equip equip = Equip.calculateEquipStats((Equip) item);
                addEquipStats(mplew, equip);
                addEquipBonusStats(mplew, equip, hasUniqueId);
            } else {
                mplew.writeShort(item.getQuantity());
                mplew.writeMapleAsciiString(item.getOwner());
                mplew.writeShort(item.getFlag());
                //mplew.writeShort(0);
                if (ItemConstants.类型.可充值道具(item.getItemId()) || item.getItemId() / 10000 == 287) {
                    mplew.writeLong(/*(int)*/(item.getInventoryId() <= 0 ? -1 : item.getInventoryId()));
                    //mplew.writeShort(0);
                }
                mplew.writeZeroBytes(19);                
            }
        }
    }

    public static void addEquipStats(MaplePacketLittleEndianWriter mplew, Equip equip) {
        int head = 0;
        if (equip.getStats().size() > 0) {
            for (EquipStat stat : equip.getStats()) {
                head |= stat.getValue();
            }
        }
        mplew.writeInt(head);
        if (head != 0) {
            if (equip.getStats().contains(EquipStat.SLOTS)) {
                mplew.write(equip.getUpgradeSlots());
            }
            if (equip.getStats().contains(EquipStat.LEVEL)) {
                mplew.write(equip.getLevel());
            }
            if (equip.getStats().contains(EquipStat.STR)) {
                mplew.writeShort(equip.getStr());
            }
            if (equip.getStats().contains(EquipStat.DEX)) {
                mplew.writeShort(equip.getDex());
            }
            if (equip.getStats().contains(EquipStat.INT)) {
                mplew.writeShort(equip.getInt());
            }
            if (equip.getStats().contains(EquipStat.LUK)) {
                mplew.writeShort(equip.getLuk());
            }
            if (equip.getStats().contains(EquipStat.MHP)) {
                mplew.writeShort(equip.getHp());
            }
            if (equip.getStats().contains(EquipStat.MMP)) {
                mplew.writeShort(equip.getMp());
            }
            if (equip.getStats().contains(EquipStat.WATK)) {
                mplew.writeShort(equip.getWatk());
            }
            if (equip.getStats().contains(EquipStat.MATK)) {
                mplew.writeShort(equip.getMatk());
            }
            if (equip.getStats().contains(EquipStat.WDEF)) {
                mplew.writeShort(equip.getWdef());
            }
            if (equip.getStats().contains(EquipStat.MDEF)) {
                mplew.writeShort(equip.getMdef());
            }
            if (equip.getStats().contains(EquipStat.ACC)) {
                mplew.writeShort(equip.getAcc());
            }
            if (equip.getStats().contains(EquipStat.AVOID)) {
                mplew.writeShort(equip.getAvoid());
            }
            if (equip.getStats().contains(EquipStat.HANDS)) {
                mplew.writeShort(equip.getHands());
            }
            if (equip.getStats().contains(EquipStat.SPEED)) {
                mplew.writeShort(equip.getSpeed());
            }
            if (equip.getStats().contains(EquipStat.JUMP)) {
                mplew.writeShort(equip.getJump());
            }
            if (equip.getStats().contains(EquipStat.FLAG)) {
                mplew.writeInt(equip.getFlag());
            }
            if (equip.getStats().contains(EquipStat.INC_SKILL)) {
                mplew.write(equip.getIncSkill() > 0 ? 1 : 0);
            }
            if (equip.getStats().contains(EquipStat.ITEM_LEVEL)) {
                mplew.write(Math.max(equip.getBaseLevel(), equip.getEquipLevel())); // Item level
            }
            if (equip.getStats().contains(EquipStat.ITEM_EXP)) {
                mplew.writeLong(equip.getExpPercentage() * 100000); // Item Exp... 10000000 = 100%
            }
            if (equip.getStats().contains(EquipStat.DURABILITY)) {
                mplew.writeInt(equip.getDurability());
            }
            if (equip.getStats().contains(EquipStat.VICIOUS_HAMMER)) {
                mplew.writeShort(equip.getViciousHammer());
                mplew.writeShort(equip.getPlatinumHammer());
            }
            if (equip.getStats().contains(EquipStat.PVP_DAMAGE)) {
                mplew.writeShort(equip.getPVPDamage());
            }
            if (equip.getStats().contains(EquipStat.DOWNLEVEL)) {
                mplew.write(0);
            }
            if (equip.getStats().contains(EquipStat.ENHANCT_BUFF)) {
                mplew.writeShort(equip.getEnhanctBuff());
            }
            if (equip.getStats().contains(EquipStat.DURABILITY_SPECIAL)) {
                mplew.writeInt(equip.getDurability());
            }
            if (equip.getStats().contains(EquipStat.REQUIRED_LEVEL)) {
                mplew.write(equip.getReqLevel());
            }
            if (equip.getStats().contains(EquipStat.YGGDRASIL_WISDOM)) {
                mplew.write(equip.getYggdrasilWisdom());
            }
            if (equip.getStats().contains(EquipStat.FINAL_STRIKE)) {
                mplew.write(equip.getFinalStrike());
            }
            if (equip.getStats().contains(EquipStat.BOSS_DAMAGE)) {
                mplew.write(equip.getBossDamage());
            }
            if (equip.getStats().contains(EquipStat.IGNORE_PDR)) {
                mplew.write(equip.getIgnorePDR());
            }
        } else {
            /*
             *   if ( v3 >= 0 )
             *     v36 = 0;
             *   else
             *     v36 = (unsigned __int8)CInPacket::Decode1(a2);
             */
//            mplew.write(0); //unknown
        }
        addEquipSpecialStats(mplew, equip);
    }

    public static void addEquipSpecialStats(MaplePacketLittleEndianWriter mplew, Equip equip) {
        //int head = equip.getEquipSpecialFlag();
        int head = 0;
        if (equip.getSpecialStats().size() > 0) {
            for (EquipSpecialStat stat : equip.getSpecialStats()) {
                head |= stat.getValue();
            }
        }

        mplew.writeInt(head);
//        System.out.println("mask " + head);

        if (head != 0) {
            if (equip.getSpecialStats().contains(EquipSpecialStat.TOTAL_DAMAGE)) {
                mplew.write(equip.getTotalDamage());
            }
            if (equip.getSpecialStats().contains(EquipSpecialStat.ALL_STAT)) {
                mplew.write(equip.getAllStat());
            }
            if (equip.getSpecialStats().contains(EquipSpecialStat.KARMA_COUNT)) {
                mplew.write(equip.getKarmaCount());
            }
            if (equip.getSpecialStats().contains(EquipSpecialStat.FIRE)) {
                mplew.writeLong(System.currentTimeMillis());
            }
            if (equip.getSpecialStats().contains(EquipSpecialStat.STAR_FORCE)) {
                mplew.write(0);
                mplew.write(equip.getStarForce());
                mplew.write(0);
                mplew.write(0);
            }
            if (equip.isTrace()) {
                mplew.write(1);
            }
        }
    }

    public static void addEquipBonusStats(MaplePacketLittleEndianWriter mplew, Equip equip, boolean hasUniqueId) {
        //擁有者名字
        mplew.writeMapleAsciiString(equip.getOwner());
        //潛能等級 17 = 特殊rare, 18 = 稀有epic, 19 = 罕見unique, 20 = 傳說legendary, potential flags. special grade is 14 but it crashes
        mplew.write(equip.getState(true) > 0 && equip.getState(true) < 17 ? equip.getState(false) | 0x20 : equip.getState(false));
        //裝備星級
        mplew.write(equip.getEnhance());
        //潛在能力
        for (int i = 1; i <= 3; i++) {
            mplew.writeShort(equip.getState(false) > 0 && equip.getState(false) < 17 ? 0 : equip.getPotential(i, false));
        }
        //附加潛能
        for (int i = 1; i <= 3; i++) {
            mplew.writeShort(equip.getState(true) > 0 && equip.getState(true) < 17 ? i == 1 ? equip.getState(true) : 0 : equip.getPotential(i, true));
        }
        //鐵砧
        mplew.writeShort(equip.getFusionAnvil() % 10000);
        //Alien Stone FLAG
        mplew.writeShort(equip.getSocketState());
        //Alien Stone能力(Item.wz/Install/0306.img) > 0 = 安裝, 0 = 空, -1 = 無.
        for (int i = 1; i <= 3; i++) {
            mplew.writeShort(equip.getSocket(i) % 10000);
        }

        if (!hasUniqueId) {
            mplew.writeLong(equip.getInventoryId() <= 0 ? -1 : equip.getInventoryId());
        }
        mplew.writeLong(getTime(-2));
        mplew.writeInt(-1);
        mplew.writeLong(0);
        mplew.writeLong(getTime(-2));
        mplew.writeLong(0);

        mplew.writeShort(0);

        mplew.writeShort(0);//魂武器 : 0
        mplew.writeShort(0);//魂武器 / 100 : 0
        mplew.writeShort(0);//魂武器 / 100 : 0
        //靈魂寶珠
        mplew.writeShort(equip.getSoulName());
        //靈魂捲軸
        mplew.writeShort(equip.getSoulEnchanter());
        //靈魂潛能
        mplew.writeShort(equip.getSoulPotential());
        //突破傷害上限
        //mplew.writeInt(equip.getMaxDamage());
        mplew.writeInt(0);
        //mplew.writeLong(getTime(-2));
    }

    public static void serializeMovementList(MaplePacketLittleEndianWriter lew, List<LifeMovementFragment> moves) {
        lew.write(moves.size());
        for (LifeMovementFragment move : moves) {
            move.serialize(lew);
        }
    }

    public static void addAnnounceBox(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        if ((chr.getPlayerShop() != null) && (chr.getPlayerShop().isOwner(chr)) && (chr.getPlayerShop().getShopType() != 1) && (chr.getPlayerShop().isAvailable())) {
            addInteraction(mplew, chr.getPlayerShop());
        } else {
            mplew.write(0);
        }
    }

    public static void addInteraction(MaplePacketLittleEndianWriter mplew, IMaplePlayerShop shop) {
        mplew.write(shop.getGameType());
        mplew.writeInt(((AbstractPlayerStore) shop).getObjectId());
        mplew.writeMapleAsciiString(shop.getDescription());
        mplew.write(shop.getPassword().length() > 0 ? 1 : 0);
        mplew.write(shop.getItemId() % 10);
        mplew.write(shop.getSize());
        mplew.write(shop.getMaxSize());
        mplew.write(shop.isOpen() ? 0 : 1);
    }
    

    public static void addCharacterInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        long mask = 0xFF_FF_FF_FF_FF_FF_FF_FFL; //FF FF FF FF FF FF DF FF v148+
        mplew.writeLong(mask);
        mplew.write(0);
        for (int i = 0; i < 3; i++) {
            mplew.writeInt(-2);
        }
        mplew.writeShort(0);
        mplew.writeInt(0);
        if ((mask & 1) != 0) {
            addCharStats(mplew, chr); // 角色狀態訊息
            
            mplew.write(chr.getBuddylist().getCapacity()); // 好友上限
            
//  Mak 测试         
//            mplew.write(chr.getBlessOfFairyOrigin() != null); // 精靈的祝福
//            if (chr.getBlessOfFairyOrigin() != null) {
//                mplew.writeMapleAsciiString(chr.getBlessOfFairyOrigin());
//            }
//            mplew.write(chr.getBlessOfEmpressOrigin() != null); // 女皇的祝福
//            if (chr.getBlessOfEmpressOrigin() != null) {
//                mplew.writeMapleAsciiString(chr.getBlessOfEmpressOrigin());
//            }
//            
            mplew.write(0);      
            mplew.write(0);            
//  Mak 测试        
            
            // 終極冒險家訊息
            MapleQuestStatus ultExplorer = chr.getQuestNoAdd(MapleQuest.getInstance(GameConstants.ULT_EXPLORER));
            mplew.write((ultExplorer != null) && (ultExplorer.getCustomData() != null));
            if ((ultExplorer != null) && (ultExplorer.getCustomData() != null)) {
                mplew.writeMapleAsciiString(ultExplorer.getCustomData());
            }

            PacketHelper.UnkFunctin6(mplew);
            
        }
        if ((mask & 2) != 0) {
            addMoneyInfo(mplew, chr);// 楓幣
            mplew.writeInt(chr.getId());
            mplew.writeInt(0); // 小鋼珠
	        mplew.writeZeroBytes(12);
	        mplew.writeInt(chr.getId());
	        mplew.writeZeroBytes(31);
	
	        addPotionPotInfo(mplew, chr);
        }

        if ((mask & 4) != 0) {
            addInventoryInfo(mplew, chr);//道具訊息
        }
        if ((mask & 0x100) != 0) {
            addSkillInfo(mplew, chr);//技能訊息
        }
        if ((mask & 0x8000) != 0) {
            addCoolDownInfo(mplew, chr);//冷卻技能訊息
        }
        if ((mask & 0x200) != 0) {
            addStartedQuestInfo(mplew, chr);//已開始任務訊息
        }
        if ((mask & 0x4000) != 0) {
            addCompletedQuestInfo(mplew, chr);//已完成任務訊息
        }
        if ((mask & 0x400) != 0) {
            addMiniGameInfo(mplew, chr);//小遊戲訊息
        }
        if ((mask & 0x800) != 0) {
            addRingInfo(mplew, chr);//戒指訊息
        }
        if ((mask & 0x1000) != 0) {
            addRocksInfo(mplew, chr);
        }
        if ((mask & 0x40000) != 0) {
            chr.QuestInfoPacket(mplew);//任務數據
        }
        if ((mask & 0x20) != 0) {
            int i1 = 0;
            mplew.writeShort(i1);
            for (int i = 0; i < i1; i++) {
                mplew.writeInt(0);
                addCharLook(mplew, chr, false, false);
            }
        }
        if ((mask & 0x1000) != 0) {
            int i2 = 0;
            mplew.writeInt(i2);
            for (int i = 0; i < i2; i++) {
                mplew.writeInt(0);
                mplew.writeInt(0);
            }
        }
        if ((mask & 0x200000) != 0) {
            if ((chr.getJob() >= 3300) && (chr.getJob() <= 3312)) {
                addJaguarInfo(mplew, chr);//狂豹訊息
            }
        }
        if ((mask & 0x800) != 0) {
            if (MapleJob.is神之子(chr.getJob())) {
                addZeroInfo(mplew, chr);//神之子訊息
            }
        }
        if ((mask & 0x400000) != 0) {
            int v142 = 0;
            mplew.writeShort(v142);//未知
            for (int i = 0; i < v142; i++) {
                mplew.writeShort(0);
                mplew.writeLong(0);
            }
        }
        if ((mask & 0x4000000) != 0) {
            int v143 = 0;
            mplew.writeShort(v143);//未知
            for (int i = 0; i < v143; i++) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
        }
        if ((mask & 0x10000000) != 0 || (mask & 0x20000000) != 0) {
            addStealSkills(mplew, chr);//幻影複製技能訊息 [52+16]Byte
        }
        if ((mask & 0x80000000) != 0) {
            addAbilityInfo(mplew, chr);//角色內在能力訊息
        }
        if ((mask & 0x400000) != 0) {
            int v183 = 0;
            mplew.writeShort(v183);
            for (int i = 0; i < v183; i++) {
                mplew.writeInt(0);
                mplew.writeInt(0);
            }
        }
        if ((mask & 1) != 0) {
            addHonorInfo(mplew, chr);//內在能力聲望訊息
        }
        
        {
        	mplew.writeShort(0);    //????
	        mplew.writeInt(1);
	        mplew.writeInt(0);
	        //or
//	        mplew.writeInt(0);
//	        mplew.writeInt(95000);
        }
        mplew.write(1);
        mplew.writeZeroBytes(16);
        mplew.writeInt(-1);
                
        mplew.writeZeroBytes(16);
        mplew.writeLong(getTime(-2));
        addEvolutionInfo(mplew, chr);//
        mplew.write(0);
        mplew.writeInt(0); 
        mplew.writeLong(getTime(-2));

        mplew.writeInt(0);
        mplew.writeInt(chr.getId());   //user id
        mplew.writeZeroBytes(12);
        mplew.writeLong(getTime(-2));
        

        mplew.writeInt(10);       
 //mak
//		List<int,String> acceptStr = new ArrayList<int,String>();
//		acceptStr.add("accept=0;date=16/08/06");
//		acceptStr.add("check1=0;cDate=16/08/10");
//        if ((mask & 0x4000) != 0) {
//            mplew.writeShort(acceptStr.size());
//            for (int i = 0; i < acceptStr.size(); ++i) {
//                mplew.writeMapleAsciiString(acceptStr.get(i));
//            }
//        }
        
        mplew.writeShort(2);
        mplew.writeInt(1);
        mplew.writeMapleAsciiString("accept=0;date=16/08/08");
        mplew.writeInt(9);
        mplew.writeMapleAsciiString("check1=0;cDate=16/08/12");        
///c测试写        
        
        mplew.writeInt(0);
        mplew.writeShort(0);
        mplew.write(new byte[]{0, 1, 0});
        mplew.writeLong(1);
        mplew.writeInt(100);
        mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
        mplew.writeInt(0);//
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.write(0);
        if ((mask & 0x2000) != 0) {
            addCoreAura(mplew, chr);
        }
        mplew.writeInt(0);
        mplew.writeInt(chr.getClient().getAccID());
        mplew.writeInt(chr.getId());
        addRedLeafInfo(mplew, chr);
        
//        mplew.writeInt(chr.getId());
//        mplew.writeLong(0);
//        mplew.writeLong(getTime(-2L));
//        mplew.writeInt(30);
    }
    
    public static void UnkFunction(final MaplePacketLittleEndianWriter mplew) {
        mplew.writeInt(0);
        for (int i = 0; i < 3; i++) {
            mplew.writeInt(0);
        }
    }

    public static void UnkFunction2(final MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        mplew.writeInt(chr.getId());
        for (int i = 0; i < 3; i++) {
            mplew.writeInt(0);
            mplew.writeInt(0);
        }
    }

    public static void UnkFunction3(final MaplePacketLittleEndianWriter mplew) {
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeLong(0);
        mplew.writeLong(0);
        mplew.writeLong(0);
    }

    public static void UnkFunction4(final MaplePacketLittleEndianWriter mplew) {
        mplew.write(0);
        mplew.writeInt(0);
        mplew.write(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeMapleAsciiString(""); // char name??
    }

    public static void UnkFunction5(final MaplePacketLittleEndianWriter mplew) {
        int result = 0;
        mplew.writeInt(result);
        for (int i = 0; i < result; i++) {
            mplew.writeInt(0);
            mplew.writeMapleAsciiString("");
        }
    }

    public static void UnkFunctin6(final MaplePacketLittleEndianWriter mplew) {
        int v7 = 2;
        do {
            mplew.writeInt(0);
            while (true) {
                int res = 255;
                mplew.write(res);
                if (res == 255) {
                    break;
                }
                mplew.writeInt(0);
            }
            v7 += 36;
        } while (v7 < 74);
    }

    public static int getSkillBook(final int i) {
        switch (i) {
            case 1:
            case 2:
                return 4;
            case 3:
                return 3;
            case 4:
                return 2;
        }
        return 0;
    }

    public static void addAbilityInfo(final MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        final List<InnerSkillValueHolder> skills = chr.getInnerSkills();
        mplew.writeShort(skills.size());
        for (int i = 0; i < skills.size(); ++i) {
            mplew.write(i + 1); // key
            mplew.writeInt(skills.get(i).getSkillId()); //d 7000000 id ++, 71 = char cards
            mplew.write(skills.get(i).getSkillLevel()); // level
            mplew.write(skills.get(i).getRank()); //rank, C, B, A, and S
        }

    }

    public static void addHonorInfo(final MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        mplew.writeInt(chr.getHonorLevel()); //之前是聲望等級honor lvl
        mplew.writeInt(chr.getHonourExp()); //之前是聲望經驗值,現在是聲望honor exp
    }

    public static void addEvolutionInfo(final MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        mplew.writeShort(0);
        mplew.writeShort(0);
    }

    public static void addCoreAura(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        MapleCoreAura aura = chr.getCoreAura();
//        mplew.writeInt(aura.getId()); //nvr change//176-
        //mplew.writeInt(chr.getId());
        mplew.writeInt(0);
 
        int level = chr.getSkillLevel(80001151) > 0 ? chr.getSkillLevel(80001151) : chr.getSkillLevel(1214);
        mplew.writeInt(level);
        
      //  mplew.writeInt(aura.getExpire());//timer
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(aura.getAtt());//wep att
        mplew.writeInt(aura.getDex());//dex
        
        mplew.writeInt(aura.getLuk());//luk
        mplew.writeInt(aura.getMagic());//magic att
        mplew.writeInt(aura.getInt());//int
        mplew.writeInt(aura.getStr());//str
        
        mplew.writeInt(0);
        mplew.writeInt(aura.getTotal());//max
        mplew.writeInt(0);
        mplew.writeInt(0);
        
        mplew.writeLong(getTime(System.currentTimeMillis() + 86400000L));
        mplew.write(MapleJob.is蒼龍俠客(chr.getJob()) && MapleJob.is林芝林(chr.getJob()) ? 1 : 0);
        mplew.write(1);
    }

    public static void addStolenSkills(MaplePacketLittleEndianWriter mplew, MapleCharacter chr, int jobNum, boolean writeJob) {
        if (writeJob) {
            mplew.writeInt(jobNum);
        }
        int count = 0;
        if (chr.getStolenSkills() != null) {
            for (Pair<Integer, Boolean> sk : chr.getStolenSkills()) {
                if (MapleJob.getJobGrade(sk.left / 10000) == jobNum) {
                    mplew.writeInt(sk.left);
                    count++;
                    if (count >= GameConstants.getNumSteal(jobNum)) {
                        break;
                    }
                }
            }
        }
        while (count < GameConstants.getNumSteal(jobNum)) { //for now?
            mplew.writeInt(0);
            count++;
        }
    }

    public static void addChosenSkills(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        for (int i = 1; i <= 4; i++) {
            boolean found = false;
            if (chr.getStolenSkills() != null) {
                for (Pair<Integer, Boolean> sk : chr.getStolenSkills()) {
                    if (MapleJob.getJobGrade(sk.left / 10000) == i && sk.right) {
                        mplew.writeInt(sk.left);
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                mplew.writeInt(0);
            }
        }
    }

    public static void addStealSkills(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr) {
        for (int i = 1; i <= 4; i++) {
            addStolenSkills(mplew, chr, i, false); // 52
        }
        addChosenSkills(mplew, chr); // 16
    }

    public static void addMonsterBookInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        if (chr.getMonsterBook().getSetScore() > 0) {
            chr.getMonsterBook().writeFinished(mplew);
        } else {
            chr.getMonsterBook().writeUnfinished(mplew);
        }

        mplew.writeInt(chr.getMonsterBook().getSet());
    }

    public static void addPetItemInfo(MaplePacketLittleEndianWriter mplew, Item item, MaplePet pet, boolean active) {
        if (item == null) {
            mplew.writeLong(PacketHelper.getKoreanTimestamp((long) (System.currentTimeMillis() * 1.5)));
        } else {
            addExpirationTime(mplew, item.getExpiration() <= System.currentTimeMillis() ? -1L : item.getExpiration());
        }
        mplew.writeInt(-1);
        mplew.writeAsciiString(pet.getName(), 13);
        mplew.write(pet.getLevel());
        mplew.writeShort(pet.getCloseness());
        mplew.write(pet.getFullness());
        if (item == null) {
            mplew.writeLong(PacketHelper.getKoreanTimestamp((long) (System.currentTimeMillis() * 1.5)));
        } else {
            addExpirationTime(mplew, item.getExpiration() <= System.currentTimeMillis() ? -1L : item.getExpiration());
        }
        mplew.writeShort(0);
        mplew.writeShort(pet.getFlags());
        mplew.writeInt((pet.getPetItemId() == 5000054) && (pet.getSecondsLeft() > 0) ? pet.getSecondsLeft() : 0);
        mplew.writeShort(pet.isCanPickup() ? 0 : 2);
        mplew.write(active ? 0 : pet.getSummoned() ? pet.getSummonedValue() : 0);
        mplew.writeInt(active ? pet.getBuffSkill() : 0);
        mplew.writeInt(0); // 顏色
        mplew.writeShort(0);
        mplew.writeShort(0); // 訓練箱子技能
        mplew.writeInt(0);
        mplew.writeInt(-1);
        mplew.writeShort(100);
    }

    public static void addShopInfo(MaplePacketLittleEndianWriter mplew, MapleShop shop, MapleClient c) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        mplew.writeInt(1);
        mplew.write(0);
        mplew.writeInt(GameConstants.getCurrentDate());
        mplew.write(shop.getRanks().size() > 0 ? 1 : 0);
        if (shop.getRanks().size() > 0) {
            mplew.write(shop.getRanks().size());
            for (Pair s : shop.getRanks()) {
                mplew.writeInt(((Integer) s.left));
                mplew.writeMapleAsciiString((String) s.right);
            }
        }
        mplew.writeShort(shop.getItems().size() + c.getPlayer().getRebuy().size());
        for (MapleShopItem item : shop.getItems()) {
            addShopItemInfo(mplew, item, shop, ii, null, c.getPlayer());
        }
        //short buyable, short quantity, int itemId, int price, short slot, int reqItem, int reqItemQ, byte rank, int category, int minLevel, int expiration, boolean potential
        byte rank = 0;
        for (Item i : c.getPlayer().getRebuy()) {
            addShopItemInfo(mplew, new MapleShopItem(i, rank), shop, ii, i, c.getPlayer());
            rank++;
        }
    }

    /*
     * Categories:
     * 0 - 標題
     * 1 - 装备
     * 2 - 消耗
     * 3 - 装饰
     * 4 - 其他
     * 5 - 配方
     * 6 - 卷軸
     * 7 - 特殊
     * 8 - 七週年
     * 9 - 紐扣
     * 10 - 入場券
     * 11 - 材料
     * 12 - 新楓之谷
     * 13 - 運動會
     * 14 - 楓核心
     * 80 - 喬
     * 81 - 海麗蜜
     * 82 - 小龍
     * 83 - 李卡司
     */
    public static void addShopItemInfo(MaplePacketLittleEndianWriter mplew, MapleShopItem item, MapleShop shop, MapleItemInformationProvider ii, Item i, MapleCharacter chr) {
        mplew.writeInt(item.getItemId());
        mplew.writeInt(item.getPrice());
        mplew.write(ServerConstants.SHOP_DISCOUNT);//打折
        mplew.writeInt(item.getReqItem());//貨幣道具
        mplew.writeInt(item.getReqItemQ());//消耗貨幣數量
        mplew.writeInt(0);//點數道具
        mplew.writeInt(0);//消耗點數數量
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeMapleAsciiString("");
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(item.getExpiration());//使用時限(單位分鐘)
        mplew.writeInt(item.getMinLevel());//購買等級限制
        mplew.writeShort(0);
        mplew.writeShort(0);
        mplew.writeInt(0);//未知
        mplew.writeLong(getTime(-2L));
        mplew.writeLong(getTime(-1L));
        mplew.writeInt(item.getCategory());
        mplew.write(0);//未知
        mplew.writeMapleAsciiString("1900010100");
        mplew.writeMapleAsciiString("2079010100");
        mplew.writeInt(ItemConstants.类型.装备(item.getItemId()) && item.hasPotential() ? 1 : 0);
        mplew.writeInt(0);//允許購買次數
        mplew.write(0);//未知
        if (!ItemConstants.类型.可充值道具(item.getItemId())) {
            int slotMax = ii.getSlotMax(item.getItemId());
            int quantity = item.getQuantity() == 0 ? slotMax : item.getQuantity();
            mplew.writeShort(quantity); //購買數量
            mplew.writeShort(quantity > 1 ? 1 : item.getBuyable() == 0 ? slotMax : item.getBuyable()); //可購買數量
        } else {
            mplew.writeAsciiString("333333");
            mplew.writeShort(BitTools.doubleToShortBits(Math.max(ii.getPrice(item.getItemId()), 0)));
            mplew.writeShort(ii.getSlotMax(item.getItemId()));
        }
        mplew.write(i == null ? 0 : 1);
        if (i != null) {
            addItemInfo(mplew, i);
        }
        if (shop.getRanks().size() > 0) {
            mplew.write(item.getRank() >= 0 ? 1 : 0);
            if (item.getRank() >= 0) {
                mplew.write(item.getRank());
            }
        }
        for (int j = 0; j < 4; j++) {
            mplew.writeInt(0); //red leaf high price probably
        }
        int idarr[] = new int[]{9410165, 9410166, 9410167, 9410168, 9410198};
        for (int k = 0; k < 5; k++) {
            mplew.writeInt(idarr[k]);
            mplew.writeInt(chr.getFriendShipPoints()[k]);
        }
    }

    public static void addJaguarInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        mplew.write(chr.getIntNoRecord(GameConstants.JAGUAR));
        for (int i = 0; i < 5; i++) {
            mplew.writeInt(0);
        }
    }

    public static void addAdventurerInfo(MaplePacketLittleEndianWriter mplew) {
        mplew.write(HexTool.getByteArrayFromHexString("2A 00 98 46 07 00 63 6F 75 6E 74 3D 30 10 47 06 00 76 61 6C 32 3D 30 16 69 00 00 99 46 07 00 63 6F 75 6E 74 3D 30 09 47 03 00 44 3D 32 6A 36 1D 00 6C 61 73 74 47 61 6D 65 3D 31 34 2F 30 32 2F 31 39 3B 53 6E 57 41 74 74 65 6E 64 3D 30 CD 33 0B 00 62 6F 72 6E 3D 31 34 30 32 30 34 8B 45 00 00 17 69 00 00 9A 46 1A 00 63 6F 75 6E 74 30 3D 31 3B 63 6F 75 6E 74 31 3D 31 3B 63 6F 75 6E 74 32 3D 31 0A 47 03 00 45 3D 31 22 47 17 00 63 6F 6D 70 3D 31 3B 69 3D 32 33 30 30 30 30 30 30 30 30 30 30 30 30 FA 46 20 00 63 6F 75 6E 74 3D 35 3B 74 69 6D 65 3D 32 30 31 33 2F 31 32 2F 31 34 20 30 38 3A 33 32 3A 31 32 12 47 40 00 4D 4C 3D 30 3B 4D 4D 3D 30 3B 4D 41 3D 30 3B 4D 42 3D 30 3B 4D 43 3D 30 3B 4D 44 3D 30 3B 4D 45 3D 30 3B 4D 46 3D 30 3B 4D 47 3D 30 3B 4D 48 3D 30 3B 4D 49 3D 30 3B 4D 4A 3D 30 3B 4D 4B 3D 30 CB 36 1B 00 6D 41 74 74 65 6E 64 3D 30 3B 6C 61 73 74 47 61 6D 65 3D 31 34 2F 30 32 2F 31 39 1C 1E 13 00 64 72 61 77 3D 30 3B 6C 6F 73 65 3D 30 3B 77 69 6E 3D 30 18 69 00 00 23 47 09 00 62 41 74 74 65 6E 64 3D 30 63 47 20 00 64 74 3D 31 33 2F 31 32 2F 32 34 3B 64 3D 32 30 31 33 31 32 32 34 3B 69 3D 33 30 30 30 30 30 30 D7 33 05 00 73 6E 32 3D 30 F7 33 04 00 30 33 3D 31 19 69 00 00 B4 46 07 00 63 6F 75 6E 74 3D 30 23 7F 1F 00 6C 61 73 74 44 65 63 54 69 6D 65 3D 32 30 31 34 2F 30 32 2F 31 39 20 30 35 3A 34 31 3A 31 32 2C 47 07 00 4C 6F 67 69 6E 3D 31 64 47 04 00 41 51 3D 31 85 46 17 00 31 3D 30 3B 32 3D 30 3B 33 3D 30 3B 34 3D 30 3B 35 3D 30 3B 36 3D 30 B5 46 07 00 63 6F 75 6E 74 3D 30 0D 47 14 00 65 54 69 6D 65 3D 31 32 2F 31 32 2F 33 31 2F 30 30 2F 30 30 1D 47 06 00 73 74 65 70 3D 30 B6 46 07 00 63 6F 75 6E 74 3D 30 55 67 05 00 76 61 6C 3D 30 16 47 31 00 52 48 3D 30 3B 47 54 3D 30 3B 57 4D 3D 30 3B 46 41 3D 30 3B 45 43 3D 30 3B 43 48 3D 30 3B 4B 44 3D 30 3B 49 4B 3D 30 3B 50 44 3D 30 3B 50 46 3D 30 31 15 04 00 64 63 3D 30 87 46 1E 00 52 47 3D 30 3B 53 4D 3D 30 3B 41 4C 50 3D 30 3B 44 42 3D 30 3B 43 44 3D 30 3B 4D 48 3D 30 14 69 13 00 73 66 3D 30 3B 6D 74 3D 30 3B 61 6C 3D 31 3B 69 64 3D 30 42 34 0C 00 52 6F 6C 6C 50 65 72 44 61 79 3D 30 9F 46 1C 00 69 6E 64 65 78 3D 31 3B 6C 61 73 74 52 3D 31 33 2F 30 39 2F 32 35 3B 73 6E 31 3D 30 24 C8 0A 00 53 74 61 67 65 4B 65 79 3D 30 C3 33 07 00 63 6F 75 6E 74 3D 30 53 0C 07 00 72 65 73 65 74 3D 31 A0 46 05 00 6E 75 6D 3D 30 00 00"));
    }

    public static void addZeroInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        short mask = 0;
        mplew.writeShort(mask);
        if ((mask & 1) != 0) {
            mplew.write(0); //bool
        }
        if ((mask & 2) != 0) {
            mplew.writeInt(0);
        }
        if ((mask & 4) != 0) {
            mplew.writeInt(0);
        }
        if ((mask & 8) != 0) {
            mplew.write(0);
        }
        if ((mask & 10) != 0) {
            mplew.writeInt(0);
        }
        if ((mask & 20) != 0) {
            mplew.writeInt(0);
        }
        if ((mask & 40) != 0) {
            mplew.writeInt(0);
        }
        if (mask < 0) {
            mplew.writeInt(0);
        }
        if ((mask & 100) != 0) {
            mplew.writeInt(0);
        }
    }

    public static void addAdventurerInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        mplew.write(HexTool.getByteArrayFromHexString("14 00 0D 47 14 00 65 54 69 6D 65 3D 31 32 2F 31 32 2F 33 31 2F 30 30 2F 30 30 B6 46 07 00 63 6F 75 6E 74 3D 30 87 46 1E 00 52 47 3D 30 3B 53 4D 3D 30 3B 41 4C 50 3D 30 3B 44 42 3D 30 3B 43 44 3D 30 3B 4D 48 3D 30 16 47 31 00 52 48 3D 30 3B 47 54 3D 30 3B 57 4D 3D 30 3B 46 41 3D 30 3B 45 43 3D 30 3B 43 48 3D 30 3B 4B 44 3D 30 3B 49 4B 3D 30 3B 50 44 3D 30 3B 50 46 3D 30 9F 46 1C 00 69 6E 64 65 78 3D 31 3B 6C 61 73 74 52 3D 31 34 2F 30 33 2F 32 36 3B 73 6E 31 3D 30 A0 46 05 00 6E 75 6D 3D 30 40 47 3A 00 63 6F 75 6E 74 3D 30 3B 61 67 6F 3D 35 3B 64 6F 31 3D 30 3B 64 6F 32 3D 30 3B 64 61 69 6C 79 46 50 3D 30 3B 6C 61 73 74 44 61 74 65 3D 32 30 31 34 30 33 32 38 3B 46 50 3D 30 10 47 06 00 76 61 6C 32 3D 30 9A 46 1A 00 63 6F 75 6E 74 30 3D 31 3B 63 6F 75 6E 74 31 3D 31 3B 63 6F 75 6E 74 32 3D 31 22 47 17 00 63 6F 6D 70 3D 31 3B 69 3D 32 33 30 30 30 30 30 30 30 30 30 30 30 30 0A 47 03 00 45 3D 31 5B 46 0C 00 52 65 74 75 72 6E 55 73 65 72 3D 31 12 47 40 00 4D 4C 3D 30 3B 4D 4D 3D 30 3B 4D 41 3D 30 3B 4D 42 3D 30 3B 4D 43 3D 30 3B 4D 44 3D 30 3B 4D 45 3D 30 3B 4D 46 3D 30 3B 4D 47 3D 30 3B 4D 48 3D 30 3B 4D 49 3D 30 3B 4D 4A 3D 30 3B 4D 4B 3D 30 FA 46 20 00 63 6F 75 6E 74 3D 35 3B 74 69 6D 65 3D 32 30 31 34 2F 30 33 2F 32 36 20 30 37 3A 30 38 3A 33 37 23 47 09 00 62 41 74 74 65 6E 64 3D 30 B4 46 07 00 63 6F 75 6E 74 3D 30 85 46 17 00 31 3D 30 3B 32 3D 30 3B 33 3D 30 3B 34 3D 30 3B 35 3D 30 3B 36 3D 30 2C 47 07 00 4C 6F 67 69 6E 3D 31 64 47 04 00 41 51 3D 30 B5 46 07 00 63 6F 75 6E 74 3D 30"));
    }

    public static void addBeastTamerInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        int beast = MapleJob.is林芝林(chr.getJob()) ? 1 : 0;
        String ears = Integer.toString(chr.getEars());
        String tail = Integer.toString(chr.getTail());

        mplew.write(HexTool.getByteArrayFromHexString("28 00 10 47 06 00 76 61 6C 32 3D 30 16 69 00 00 6A 36 1D 00 6C 61 73 74 47 61 6D 65 3D 31 34 2F 30 33 2F 30 36 3B 53 6E 57 41 74 74 65 6E 64 3D 30 8B 45 00 00 CD 33 0B 00 62 6F 72 6E 3D 31 34 30 33 30 36 9A 46 1A 00 63 6F 75 6E 74 30 3D 31 3B 63 6F 75 6E 74 31 3D 31 3B 63 6F 75 6E 74 32 3D 31 17 69 00 00 0A 47 03 00 45 3D 31 22 47 17 00 63 6F 6D 70 3D 31 3B 69 3D 32 33 30 30 30 30 30 30 30 30 30 30 30 30 FA 46 20 00 63 6F 75 6E 74 3D 35 3B 74 69 6D 65 3D 32 30 31 34 2F 30 33 2F 30 36 20 30 31 3A 30 39 3A 32 35 12 47 40 00 4D 4C 3D 30 3B 4D 4D 3D 30 3B 4D 41 3D 30 3B 4D 42 3D 30 3B 4D 43 3D 30 3B 4D 44 3D 30 3B 4D 45 3D 30 3B 4D 46 3D 30 3B 4D 47 3D 30 3B 4D 48 3D 30 3B 4D 49 3D 30 3B 4D 4A 3D 30 3B 4D 4B 3D 30 CB 36 1B 00 6D 41 74 74 65 6E 64 3D 30 3B 6C 61 73 74 47 61 6D 65 3D 31 34 2F 30 33 2F 30 36 1C 1E 13 00 64 72 61 77 3D 30 3B 6C 6F 73 65 3D 30 3B 77 69 6E 3D 30 18 69 00 00 23 47 09 00 62 41 74 74 65 6E 64 3D 30 D7 33 05 00 73 6E 32 3D 30 F7 33 04 00 30 33 3D 31 19 69 00 00 B4 46 07 00 63 6F 75 6E 74 3D 30 23 7F 1F 00 6C 61 73 74 44 65 63 54 69 6D 65 3D 32 30 31 34 2F 30 33 2F 30 36 20 30 36 3A 35 39 3A 32 32 2C 47 07 00 4C 6F 67 69 6E 3D 31 64 47 04 00 41 51 3D 30 85 46 17 00 31 3D 30 3B 32 3D 30 3B 33 3D 30 3B 34 3D 30 3B 35 3D 30 3B 36 3D 30 B5 46 07 00 63 6F 75 6E 74 3D 30 0D 47 14 00 65 54 69 6D 65 3D 31 32 2F 31 32 2F 33 31 2F 30 30 2F 30 30 1D 47 06 00 73 74 65 70 3D 30 B6 46 07 00 63 6F 75 6E 74 3D 30 55 67 05 00 76 61 6C 3D 30 16 47 31 00 52 48 3D 30 3B 47 54 3D 30 3B 57 4D 3D 30 3B 46 41 3D 30 3B 45 43 3D 30 3B 43 48 3D 30 3B 4B 44 3D 30 3B 49 4B 3D 30 3B 50 44 3D 30 3B 50 46 3D 30 31 15 04 00 64 63 3D 30 87 46 1E 00 52 47 3D 30 3B 53 4D 3D 30 3B 41 4C 50 3D 30 3B 44 42 3D 30 3B 43 44 3D 30 3B 4D 48 3D 30 14 69 13 00 73 66 3D 30 3B 6D 74 3D 30 3B 61 6C 3D 31 3B 69 64 3D 30 42 34 0C 00 52 6F 6C 6C 50 65 72 44 61 79 3D 30 9F 46 1C 00 69 6E 64 65 78 3D 31 3B 6C 61 73 74 52 3D 31 34 2F 30 33 2F 30 36 3B 73 6E 31 3D 30 8D E6 0E 00 6D 6F 76 69 65 3D 31 3B 74 75 74 6F 3D 31 A4 E7 2B 00"));
        mplew.writeAsciiString("bTail=" + beast + ";");
        mplew.writeAsciiString("bEar=" + beast + ";");
        mplew.writeAsciiString("TailID=" + tail + ";");
        mplew.writeAsciiString("EarID=" + ears);
        mplew.write(HexTool.getByteArrayFromHexString("24 C8 0A 00 53 74 61 67 65 4B 65 79 3D 30 C3 33 07 00 63 6F 75 6E 74 3D 30 53 0C 07 00 72 65 73 65 74 3D 31 A0 46 05 00 6E 75 6D 3D 30 00 00"));
    }

    public static void addFarmInfo(MaplePacketLittleEndianWriter mplew, MapleClient c, int idk) {
        mplew.writeMapleAsciiString(c.getFarm().getName());
        mplew.writeInt(c.getFarm().getWaru());
        mplew.writeInt(c.getFarm().getLevel());
        mplew.writeInt(c.getFarm().getExp());
        mplew.writeInt(c.getFarm().getAestheticPoints());
        mplew.writeInt(0); //gems 

        mplew.write((byte) idk);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(1);
    }

    public static void addRedLeafInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        int idarr[] = new int[]{9410165, 9410166, 9410167, 9410168, 9410198};
        //mplew.writeInt(chr.getClient().getAccID());
        //mplew.writeInt(chr.getId());
        int size = 5;
        mplew.writeInt(size);
        mplew.writeInt(0);
        for (int i = 0; i < 5; i++) {
            mplew.writeInt(idarr[i]);
            mplew.writeInt(chr.getFriendShipPoints()[i]);
        }
    }

    public static void addLuckyLogoutInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        mplew.writeInt(0);
        for (int i = 0; i < 3; i++) {
            mplew.writeInt(0);
        }
    }

    public static void addPartTimeJob(MaplePacketLittleEndianWriter mplew, PartTimeJob parttime) {
        mplew.write(parttime.getJob());
        if (parttime.getJob() > 0 && parttime.getJob() <= 5) {
            mplew.writeReversedLong(parttime.getTime());
        } else {
            mplew.writeReversedLong(getTime(-2));
        }
        mplew.writeInt(parttime.getReward());
        mplew.write(parttime.getReward() > 0);
    }

    public static void addSpawnPlayerBuffStat(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        Map<MapleBuffStat, Object[]> statups = new LinkedHashMap();

        // 鬥氣集中
        if (chr.getBuffedValue(MapleBuffStat.COMBO) != null) {
            statups.put(MapleBuffStat.COMBO,
            new Object[]{
                chr.getBuffedValue(MapleBuffStat.COMBO).byteValue()
            });
        }
        // 預設Buff
        statups.put(MapleBuffStat.CHAR_BUFF, new Object[]{-1});
        // 預設Buff
        statups.put(MapleBuffStat.MOUNT_MORPH, new Object[]{(byte) 0});
        // 預設Buff
        statups.put(MapleBuffStat.DIVINE_FORCE_AURA, new Object[]{(short) 0, 0});
        // 預設Buff
        statups.put(MapleBuffStat.DIVINE_SPEED_AURA, new Object[]{(short) 0, 0});
        // 預設Buff
        statups.put(MapleBuffStat.NEW_AURA, new Object[]{(short) 0, 0});
        // 預設Buff
        statups.put(MapleBuffStat.ENERGY_CHARGE, null);
        // 預設Buff - 内容猜填
        statups.put(MapleBuffStat.DEFAULTBUFF3, new Object[]{(short) 0, 0});
        // 預設Buff - 内容猜填
        statups.put(MapleBuffStat.DEFAULTBUFF4, new Object[]{(short) 0, 0});
        // 預設Buff - 内容猜填
        statups.put(MapleBuffStat.DEFAULTBUFF5, new Object[]{(short) 0, 0});
        // 預設Buff
        statups.put(MapleBuffStat.DASH_SPEED, null);
        // 預設Buff
        statups.put(MapleBuffStat.DASH_JUMP, null);
        // 預設Buff
        statups.put(MapleBuffStat.MONSTER_RIDING, null);
        // 預設Buff
        statups.put(MapleBuffStat.SPEED_INFUSION, null);
        // 預設Buff
        statups.put(MapleBuffStat.HOMING_BEACON, null);
        // 預設Buff
        statups.put(MapleBuffStat.DEFAULTBUFF1, null);
        // 預設Buff
        statups.put(MapleBuffStat.DEFAULTBUFF2, null);
        // 抵禦致命異常狀態(如 元素適應(火、毒), 元素適應(雷、冰), 聖靈守護)
        if (chr.getBuffedValue(MapleBuffStat.ABNORMAL_BUFF_RESISTANCES) != null) {
            statups.put(MapleBuffStat.ABNORMAL_BUFF_RESISTANCES,
            new Object[]{
                chr.getBuffedValue(MapleBuffStat.ABNORMAL_BUFF_RESISTANCES).shortValue(),
                chr.getTrueBuffSource(MapleBuffStat.ABNORMAL_BUFF_RESISTANCES)
            });
        }
        // 飛天騎乘
        if (chr.getBuffedValue(MapleBuffStat.SOARING) != null) {
            statups.put(MapleBuffStat.SOARING,
            new Object[]{
                chr.getBuffedValue(MapleBuffStat.SOARING).shortValue(),
                chr.getTrueBuffSource(MapleBuffStat.SOARING)
            });
        }

        // ---------寫入玩家身上剩餘未處理的的Buff
//        chr.getAllBuffs().forEach((mbsvh) -> {
//            for (MapleBuffStat mb : mbsvh.statup.keySet()) {
//                if (!statups.containsKey(mb)) {
//                    statups.put(mb, null);
//                }
//            }
//        });

        writeBuffMask(mplew, statups);

        statups.values().stream().filter((value) -> !(value == null || value.length == 0)).forEach((value) -> {
            for (Object i : value) {
                if (i instanceof Byte) {
                    mplew.write((Byte) i);
                } else if (i instanceof Short) {
                    mplew.writeShort((Short) i);
                } else if (i instanceof Integer) {
                    mplew.writeInt((Integer) i);
                } else if (i instanceof Long) {
                    mplew.writeLong((Long) i);
                }
            }
        });

        mplew.write(0); // unk
        mplew.write(0); // unk
        mplew.write(0); // unk

        statups.keySet().forEach((stat) -> {
            switch (stat) {
                case DIVINE_FORCE_AURA:
                case DIVINE_SPEED_AURA:
                case NEW_AURA:
                case ABNORMAL_BUFF_RESISTANCES:
                case DEFAULTBUFF3: //猜測
                case DEFAULTBUFF4: //猜測
                case DEFAULTBUFF5: //猜測
                    mplew.write(0);
            }
        });

        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0); // for

        mplew.writeInt(0);

        int CHAR_MAGIC_SPAWN = Randomizer.nextInt();

//IDA ->  for ( i = 0; i < 8; ++i ) {
        mplew.writeZeroBytes(8);
        mplew.write(1);
        mplew.writeInt(CHAR_MAGIC_SPAWN);//1

        mplew.writeZeroBytes(8);
        mplew.write(1);
        mplew.writeInt(CHAR_MAGIC_SPAWN);//2

        mplew.writeZeroBytes(10);
        mplew.write(1);
        mplew.writeInt(CHAR_MAGIC_SPAWN);//3

        mplew.writeShort(0);
        int buffSrc = chr.getBuffSource(MapleBuffStat.MONSTER_RIDING);
        if (buffSrc > 0) {
            Item c_mount = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -118);
            Item mount = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -18);
            if ((GameConstants.getMountItem(buffSrc, chr) == 0) && (c_mount != null) && (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -119) != null)) {
                mplew.writeInt(c_mount.getItemId());
            } else if ((GameConstants.getMountItem(buffSrc, chr) == 0) && (mount != null) && (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -19) != null)) {
                mplew.writeInt(mount.getItemId());
            } else {
                mplew.writeInt(GameConstants.getMountItem(buffSrc, chr));
            }
            mplew.writeInt(buffSrc);
        } else {
            mplew.writeInt(0);
            mplew.writeInt(0);
        }
        mplew.write(1);
        mplew.writeInt(CHAR_MAGIC_SPAWN);//4
        mplew.writeLong(0L);
        mplew.write(1);
        mplew.writeInt(CHAR_MAGIC_SPAWN);//5
        mplew.write(0);
        mplew.writeInt(Randomizer.nextInt());
        mplew.writeZeroBytes(10);
        mplew.write(1);
        mplew.writeInt(CHAR_MAGIC_SPAWN);//6
        mplew.writeZeroBytes(16);
        mplew.write(1);
        mplew.writeInt(CHAR_MAGIC_SPAWN);//7
        mplew.writeZeroBytes(10);
        mplew.write(1);
        mplew.writeInt(CHAR_MAGIC_SPAWN);//8
//}
    }

    public static <E extends Buffstat> void writeSingleMask(MaplePacketLittleEndianWriter mplew, E statup) {
        writeSingleMask(mplew, statup, GameConstants.MAX_BUFFSTAT);
    }

    public static <E extends Buffstat> void writeSingleMobMask(MaplePacketLittleEndianWriter mplew, E statup) {
        writeSingleMask(mplew, statup, GameConstants.MAX_MOBSTAT);
    }

    public static <E extends Buffstat> void writeSingleMask(MaplePacketLittleEndianWriter mplew, E statup, int maxMask) {
        for (int i = 0; i < maxMask; i++) {
            mplew.writeInt(i == statup.getPosition() ? statup.getValue() : 0);
        }
    }

    public static <E extends Buffstat> void writeMask(MaplePacketLittleEndianWriter mplew, Collection<E> statups) {
        writeMask(mplew, statups, GameConstants.MAX_BUFFSTAT);
    }

    public static <E extends Buffstat> void writeMobMask(MaplePacketLittleEndianWriter mplew, Collection<E> statups) {
        writeMask(mplew, statups, GameConstants.MAX_MOBSTAT);
    }

    public static <E extends Buffstat> void writeMask(MaplePacketLittleEndianWriter mplew, Collection<E> statups, int maxMask) {
        int[] mask = new int[maxMask];
        for (Buffstat statup : statups) {
            mask[(statup.getPosition())] |= statup.getValue();
        }
        for (int i = 0; i < mask.length; i++) {
            mplew.writeInt(mask[i]);
        }
    }

    public static <E extends Buffstat> void writeBuffMask(MaplePacketLittleEndianWriter mplew, Collection<Pair<E, Integer>> statups) {
        int[] mask = new int[GameConstants.MAX_BUFFSTAT];
        for (Pair statup : statups) {
            mask[(((Buffstat) statup.left).getPosition())] |= ((Buffstat) statup.left).getValue();
        }
        for (int i = 0; i < mask.length; i++) {
            mplew.writeInt(mask[i]);
        }
    }

    public static <E extends Buffstat, F extends Object> void writeBuffMask(MaplePacketLittleEndianWriter mplew, Map<E, F> statups) {
        int[] mask = new int[GameConstants.MAX_BUFFSTAT];
        for (Buffstat statup : statups.keySet()) {
            mask[(statup.getPosition())] |= statup.getValue();
        }
        for (int i = 0; i < mask.length; i++) {
            mplew.writeInt(mask[i]);
        }
    }
}
