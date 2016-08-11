package handling.login;

import client.MapleJob;
import constants.GameConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import tools.Triple;

public class LoginInformationProvider {

    public static enum JobInfoFlag {

        脸型(0x1),
        发型(0x2),
        脸饰(0x4),
        耳朵(0x8),
        尾巴(0x10),
        帽子(0x20),
        衣服(0x40),
        裤裙(0x80),
        披风(0x100),
        鞋子(0x200),
        手套(0x400),
        武器(0x800),
        副手(0x1000),
        ;
        private final int value;

        private JobInfoFlag(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public boolean check(int x) {
            return (value & x) != 0;
        }
    }

    public static enum JobType {

        冒险家(-1, MapleJob.初心者.getId(), 100000000, JobInfoFlag.裤裙.getValue()),
        反抗者(0, MapleJob.反抗的基础.getId(), 931000000),
        冒險家(1, MapleJob.初心者.getId(), 4000000),
        骑士团(2, MapleJob.骑士团初心者.getId(), 130030000, JobInfoFlag.披风.getValue()),
        战神(3, MapleJob.英雄初心者.getId(), 914000000, JobInfoFlag.裤裙.getValue()),
        龙神(4, MapleJob.龙初心.getId(), 900010000, JobInfoFlag.裤裙.getValue()),
        双弩精灵(5, MapleJob.双弩精灵.getId(), 910150000),
        恶魔(6, MapleJob.恶魔猎手.getId(), 927000000, JobInfoFlag.脸饰.getValue() | JobInfoFlag.副手.getValue()), //927000000
        幻影(7, MapleJob.幻影.getId(), 915000000, JobInfoFlag.披风.getValue()),
        暗影双刀(8, MapleJob.初心者.getId(), 103050900),
        米哈尔(9, MapleJob.米哈逸.getId(), 913070000, JobInfoFlag.裤裙.getValue()),//TODO 出生劇情修正
        夜光法师(10, MapleJob.夜光法师.getId(), 101000000, JobInfoFlag.披风.getValue()),//TODO 出生劇情Ellinia atm
        狂龙战士(11, MapleJob.狂龙战士的基础.getId(), 400000000),//TODO 出生劇情
        爆莉萌天使(12, MapleJob.爆莉萌天使的基础.getId(), 940011000),//TODO 出生劇情400000000 - 940011000 - town now
        火炮手(13, MapleJob.初心者.getId(), 0),//TODO 出生劇情
        尖兵(14, MapleJob.尖兵.getId(), 931050920, JobInfoFlag.脸饰.getValue()),//TODO 出生劇情 931060089
        神之子(15, MapleJob.神之子.getId(), 100000000, JobInfoFlag.披风.getValue()),//TODO 出生劇情 321000000 = zero starter map
        隐月(16, MapleJob.隱月.getId(), 910000000, JobInfoFlag.裤裙.getValue() | JobInfoFlag.披风.getValue()),//TODO 出生劇情
        皮卡啾(17, MapleJob.皮卡啾1轉.getId(), 927030090),
        超能力者(18, MapleJob.超能力者1转.getId(), 331001100),//331001100
        龙的传人(19, MapleJob.初心者.getId(), 552000050, JobInfoFlag.裤裙.getValue()),//TODO 出生劇情End map for tutorial
        劍豪(20, MapleJob.劍豪.getId(), 807100010, JobInfoFlag.帽子.getValue() | JobInfoFlag.手套.getValue()),
        阴阳师(21, MapleJob.陰陽師.getId(), 807100110, JobInfoFlag.帽子.getValue() | JobInfoFlag.手套.getValue()),
        林芝林(22, MapleJob.林芝林.getId(), 866100000, JobInfoFlag.脸饰.getValue() | JobInfoFlag.耳朵.getValue() | JobInfoFlag.尾巴.getValue());//TODO 出生劇情
        public int type, id, map;
        public int flag = JobInfoFlag.脸型.getValue() | JobInfoFlag.发型.getValue() | JobInfoFlag.衣服.getValue() | JobInfoFlag.鞋子.getValue() | JobInfoFlag.武器.getValue();

        private JobType(int type, int id, int map) {
            this.type = type;
            this.id = id;
            this.map = map;
        }

        private JobType(int type, int id, int map, int flag) {
            this.type = type;
            this.id = id;
            this.map = map;
            this.flag |= flag;
        }
        
        public static JobType getByType(int g) {
            if (g == JobType.火炮手.type) {
                return JobType.冒險家;
            }
            for (JobType e : JobType.values()) {
                if (e.type == g) {
                    return e;
                }
            }
            return null;
        }

        public static JobType getById(int g) {
            if (g == JobType.冒險家.id) {
                return JobType.冒險家;
            }
            if (g == 508) {
                return JobType.龙的传人;
            }
            for (JobType e : JobType.values()) {
                if (e.id == g) {
                    return e;
                }
            }
            return null;
        }
    }
    private final static LoginInformationProvider instance = new LoginInformationProvider();
    protected final List<String> ForbiddenName = new ArrayList<>();
    protected final Map<String, String> Curse = new HashMap();
    //gender, val, job
    protected final Map<Triple<Integer, Integer, Integer>, List<Integer>> makeCharInfo = new HashMap<>();
    //0 = eyes 1 = hair 2 = haircolor 3 = skin 4 = top 5 = bottom 6 = shoes 7 = weapon

    public static LoginInformationProvider getInstance() {
        return instance;
    }

    protected LoginInformationProvider() {
        final MapleDataProvider prov = MapleDataProviderFactory.getDataProvider("/Etc.wz");
        MapleData nameData = prov.getData("ForbiddenName.img");
        for (final MapleData data : nameData.getChildren()) {
            ForbiddenName.add(MapleDataTool.getString(data));
        }
        nameData = prov.getData("Curse.img"); 
        for (final MapleData data : nameData.getChildren()) {
            String[] curse = MapleDataTool.getString(data).split(",");
            Curse.put(curse[0], curse[1]);
            ForbiddenName.add(curse[0]);
        }
        final MapleData infoData = prov.getData("MakeCharInfo.img");
        for (MapleData dat : infoData) {
            if (!dat.getName().matches("^\\d+$") && !dat.getName().equals("000_1")) {
                continue;
            }
            //System.out.println("讀取創建道具認證訊息:" + dat.getName());
            final int type;
            if (dat.getName().equals("000_1")) {
                type = JobType.暗影双刀.type;
            } else {
                JobType jobType = JobType.getById(Integer.parseInt(dat.getName()));
                int job = Integer.parseInt(dat.getName());
                if (jobType == null) {
                    
                    System.err.println("读取创建职业道具讯息错误, 职业不存在" + MapleJob.getById(job).name() + "(" + job +")");
                    continue;
                }
                type = JobType.getById(job).type;
            }
            for (MapleData d : dat) {
                int gender;
                if (d.getName().startsWith("female")) {
                    gender = 1;
                } else if (d.getName().startsWith("male")) {
                    gender = 0;
                } else {
                    continue;
                }

                for (MapleData da : d) {
                    Triple<Integer, Integer, Integer> key = new Triple<>(gender, Integer.parseInt(da.getName()), type);
                    List<Integer> our = makeCharInfo.get(key);
                    if (our == null) {
                        our = new ArrayList<>();
                        makeCharInfo.put(key, our);
                    }
                    for (MapleData dd : da) {
                        if (dd.getName().equalsIgnoreCase("color")) {
                            for (MapleData dda : dd) {
                                for (MapleData ddd : dda) {
                                    our.add(MapleDataTool.getInt(ddd, -1));
                                }
                            }
                        } else if (!dd.getName().equalsIgnoreCase("name")) {
                            our.add(MapleDataTool.getInt(dd, -1));
                        }
                    }
                }
            }
        }


        final MapleData uA = infoData.getChildByPath("UltimateAdventurer");
        for (MapleData dat : uA) {
            final Triple<Integer, Integer, Integer> key = new Triple<>(-1, Integer.parseInt(dat.getName()), JobType.冒险家.type);
            List<Integer> our = makeCharInfo.get(key);
            if (our == null) {
                our = new ArrayList<>();
                makeCharInfo.put(key, our);
            }
            for (MapleData d : dat) {
                our.add(MapleDataTool.getInt(d, -1));
            }
        }
    }

    public static boolean isExtendedSpJob(int jobId) {
        return GameConstants.isSeparatedSp(jobId);
    }

    public final boolean isForbiddenName(final String in) {
        for (final String name : ForbiddenName) {
            if (in.toLowerCase().contains(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public String getCurseMsg(String in) {
        for (Map.Entry<String, String> entry : Curse.entrySet()) {
            in = in.replaceAll("(?i)" + entry.getKey(), entry.getValue());
        }
        return in;
    }

    public final boolean isEligibleItem(final int gender, final int val, final int job, final int item) {
        if (item < 0) {
            return false;
        }
        final Triple<Integer, Integer, Integer> key = new Triple<>(gender, val, job);
        final List<Integer> our = makeCharInfo.get(key);
        if (our == null) {
            return false;
        }
        return our.contains(item);
    }
}
