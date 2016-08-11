package client;

public enum MapleJob {
//冒险家
    初心者(0),
    战士(100),
    剑客(110),
    勇士(111),
    英雄(112),
    准骑士(120),
    騎士(121),
    圣骑士(122),
    枪战士(130),
    龙骑士(131),
    黑騎士(132),
    魔法师(200),
    火毒法师(210),
    火毒巫师(211),
    火毒魔导士(212),
    冰雷法师(220),
    冰雷巫师(221),
    冰雷魔导士(222),
    僧侶(230),
    祭司(231),
    主教(232),
    弓箭手(300),
    猎人(310),
    射手(311),
    神射手(312),
    弩弓手(320),
    游侠(321),
    箭神(322),
    飞侠(400),
    刺客(410),
    无影人(411),
    隐士(412),
    侠客(420),
    独行客(421),
    侠盗(422),
    见习刀客(430),
    双刀客(431),
    双刀侠(432),
    血刀(433),
    暗影双刀(434),
    海盜(500),
    砲手(501),
    龙的传人1转(508),
    拳手(510),
    斗士(511),
    冲锋队长(512),
    槍手(520),
    大副(521),
    船长(522),
    火炮手2转(530),
    毁灭炮手3转(531),
    神炮王(532),
    龙的传人2转(570),
    龙的传人3转(571),
    龙的传人4转(572),
    运营员(900),
    管理员(800),
/*    SUPERGM(910),//台服無*/
    //骑士团
    骑士团初心者(1000),
    魂骑士1转(1100),
    魂骑士2转(1110),
    魂骑士3转(1111),
    魂骑士4转(1112),
    炎术士1转(1200),
    炎术士2转(1210),
    炎术士3转(1211),
    炎术士4转(1212),
    风灵使者1转(1300),
    风灵使者2转(1310),
    风灵使者3转(1311),
    风灵使者4转(1312),
    夜行者1转(1400),
    夜行者2转(1410),
    夜行者3转(1411),
    夜行者4转(1412),
    奇袭者1转(1500),
    奇袭者2转(1510),
    奇袭者3转(1511),
    奇袭者4转(1512),
    //英雄
    英雄初心者(2000),
    龙初心(2001),
    双弩精灵(2002),
    幻影(2003),
    夜光法师(2004),
    隱月(2005),
    战神1转(2100),
    战神2转(2110),
    战神3转(2111),
    战神4转(2112),
    龙神1转(2200),
    龙神2转(2210),
    龙神3转(2211),
    龙神4转(2212),
    龙神5转(2213),
    龙神6转(2214),
    龙神7转(2215),
    龙神8转(2216),
    龙神9转(2217),
    龙神10转(2218),
    双弩精灵1转(2300),
    双弩精灵2转(2310),
    双弩精灵3转(2311),
    双弩精灵4转(2312),
    幻影俠盜1轉(2400),
    幻影俠盜2轉(2410),
    幻影俠盜3轉(2411),
    幻影俠盜4轉(2412),
    隱月1轉(2500),
    隱月2轉(2510),
    隱月3轉(2511),
    隱月4轉(2512),
    夜光1轉(2700),
    夜光2轉(2710),
    夜光3轉(2711),
    夜光4轉(2712),
    //反抗者
    反抗的基础(3000),
    恶魔猎手(3001),
    尖兵(3002),
    恶魔猎手1转(3100),
    恶魔猎手2转(3110),
    恶魔猎手3转(3111),
    惡魔殺手4轉(3112),
    惡魔復仇者1轉(3101),
    惡魔復仇者2轉(3120),
    惡魔復仇者3轉(3121),
    惡魔復仇者4轉(3122),
    唤灵法师1转(3200),
    唤灵法师2转(3210),
    唤灵法师3转(3211),
    唤灵法师4转(3212),
    豹弩游侠1转(3300),
    豹弩游侠2转(3310),
    豹弩游侠3转(3311),
    豹弩游侠4转(3312),
    机械师1转(3500),
    机械师2转(3510),
    机械师3转(3511),
    机械师4转(3512),
    尖兵1转(3600),
    尖兵2转(3610),
    尖兵3转(3611),
    尖兵4转(3612),
    劍豪(4001),
    陰陽師(4002),
    劍豪1轉(4100),
    劍豪2轉(4110),
    劍豪3轉(4111),
    劍豪4轉(4112),
    陰陽師1轉(4200),
    陰陽師2轉(4210),
    陰陽師3轉(4211),
    陰陽師4轉(4212),
    米哈逸(5000),
    米哈逸1轉(5100),
    米哈逸2轉(5110),
    米哈逸3轉(5111),
    米哈逸4轉(5112),
    //狂龙战士的基础
    狂龙战士的基础(6000),
    爆莉萌天使的基础(6001),
    狂龙战士1转(6100),
    狂龙战士2转(6110),
    狂龙战士3转(6111),
    狂龙战士4转(6112),
    爆莉萌天使1转(6500),
    爆莉萌天使2转(6510),
    爆莉萌天使3转(6511),
    爆莉萌天使4转(6512),
    ADDITIONAL_SKILLS(9000),  //未知
    神之子的基本(10000),
    神之子1转(10100),
    神之子2转(10110),
    神之子3转(10111),
    神之子(10112),
    林芝林(11000),
    林芝林1转(11200),
    林芝林2转(11210),
    林芝林3转(11211),
    林芝林4转(11212),
    皮卡啾(13000),
    皮卡啾1轉(13100),
    超能力者(14000),
    超能力者1转(14200),
    超能力者2转(14210),
    超能力者3转(14211),
    超能力者4转(14212),
    未知(999999),
    ;
    private final int jobid;

    private MapleJob(int id) {
        this.jobid = id;
    }

    public int getId() {
        return this.jobid;
    }

    public static String getName(MapleJob mjob) {
        return mjob.name();
    }

    public static MapleJob getById(int id) {
        for (MapleJob l : values()) {
            if (l.getId() == id) {
                return l;
            }
        }
        return 未知;
    }

    public static boolean isExist(int id) {
        for (MapleJob job : values()) {
            if (job.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public static boolean is冒險家(final int job) {
        return job / 1000 == 0;
    }

    public static boolean is英雄(final int job) {
        return job / 10 == 11;
    }

    public static boolean is聖騎士(final int job) {
        return job / 10 == 12;
    }

    public static boolean is黑騎士(final int job) {
        return job / 10 == 13;
    }

    public static boolean is大魔導士_火毒(final int job) {
        return job / 10 == 21;
    }

    public static boolean is大魔導士_冰雷(final int job) {
        return job / 10 == 22;
    }

    public static boolean is主教(final int job) {
        return job / 10 == 23;
    }

    public static boolean is箭神(final int job) {
        return job / 10 == 31;
    }

    public static boolean is神射手(final int job) {
        return job / 10 == 32;
    }

    public static boolean is夜使者(final int job) {
        return job / 10 == 41;
    }

    public static boolean is暗影神偷(final int job) {
        return job / 10 == 42;
    }

    public static boolean is影武者(final int job) {
        return job / 10 == 43; // sub == 1 && job == 400
    }

    public static boolean is拳霸(final int job) {
        return job / 10 == 51;
    }

    public static boolean is槍神(final int job) {
        return job / 10 == 52;
    }

    public static boolean is重砲指揮官(final int job) {
        return job / 10 == 53 || job == 501 || job == 1;
    }

    public static boolean is蒼龍俠客(final int job) {
        return job / 10 == 57 || job == 508;
    }

    public static boolean is管理員(final int job) {
        return job == 800 || job == 900 || job == 910;
    }

    public static boolean is皇家騎士團(final int job) {
        return job / 1000 == 1;
    }

    public static boolean is聖魂劍士(final int job) {
        return job / 100 == 11;
    }

    public static boolean is烈焰巫師(final int job) {
        return job / 100 == 12;
    }

    public static boolean is破風使者(final int job) {
        return job / 100 == 13;
    }

    public static boolean is暗夜行者(final int job) {
        return job / 100 == 14;
    }

    public static boolean is閃雷悍將(final int job) {
        return job / 100 == 15;
    }

    public static boolean is英雄團(final int job) {
        return job / 1000 == 2;
    }

    public static boolean is狂狼勇士(final int job) {
        return job / 100 == 21 || job == 2000;
    }

    public static boolean is龍魔導士(final int job) {
        return job / 100 == 22 || job == 2001;
    }

    public static boolean is双弩精灵(final int job) {
        return job / 100 == 23 || job == 2002;
    }

    public static boolean is幻影俠盜(final int job) {
        return job / 100 == 24 || job == 2003;
    }

    public static boolean is夜光(final int job) {
        return job / 100 == 27 || job == 2004;
    }

    public static boolean is隱月(int job) {
        return job / 100 == 25 || job == 2005;
    }

    public static boolean is末日反抗軍(final int job) {
        return job / 1000 == 3;
    }

    public static boolean is惡魔(final int job) {
        return is惡魔殺手(job) || is惡魔復仇者(job) || job == 3001;
    }

    public static boolean is惡魔殺手(final int job) {
        return job / 10 == 311 || job == 3100;
    }

    public static boolean is惡魔復仇者(int job) {
        return job / 10 == 312 || job == 3101;
    }

    public static boolean is煉獄巫師(final int job) {
        return job / 100 == 32;
    }

    public static boolean is狂豹獵人(final int job) {
        return job / 100 == 33;
    }

    public static boolean is機甲戰神(final int job) {
        return job / 100 == 35;
    }

    public static boolean is尖兵(final int job) {
        return job / 100 == 36 || job == 3002;
    }

    public static boolean is曉の陣(int job) {
        return job / 1000 == 4;
    }

    public static boolean is劍豪(int job) {
        return job / 100 == 41 || job == 4001;
    }

    public static boolean is陰陽師(int job) {
        return job / 100 == 42 || job == 4002;
    }

    public static boolean is米哈逸(final int job) {
        return job / 100 == 51 || job == 5000;
    }

    public static boolean is諾巴(final int job) {
        return job / 1000 == 6;
    }

    public static boolean is凱撒(final int job) {
        return job / 100 == 61 || job == 6000;
    }

    public static boolean is天使破壞者(final int job) {
        return job / 100 == 65 || job == 6001;
    }

    public static boolean is神之子(int job) {
        return job == 10000 || job == 10100 || job == 10110 || job == 10111 || job == 10112;
    }

    public static boolean is林芝林(final int job) {
        return job / 100 == 112 || job == 11000;
    }

    public static boolean is皮卡啾(final int job) {
        return job / 100 == 131 || job == 13000;
    }

    public static boolean is凱內西斯(final int job) {
        return job / 100 == 142 || job == 14000;
    }

    public static boolean is劍士(final int job) {
        return getJobBranch(job) == 1;
    }

    public static boolean is法師(final int job) {
        return getJobBranch(job) == 2;
    }

    public static boolean is弓箭手(final int job) {
        return getJobBranch(job) == 3;
    }

    public static boolean is盜賊(final int job) {
        return getJobBranch(job) == 4 || getJobBranch(job) == 6;
    }

    public static boolean is海盜(final int job) {
        return getJobBranch(job) == 5 || getJobBranch(job) == 6;
    }

    public static short getBeginner(final short job) {
        if (job % 1000 < 10) {
            return job;
        }
        switch (job / 100) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 8:
            case 9:
                return (short) 初心者.getId();
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                return (short) 骑士团初心者.getId();
            case 20:
                return (short) 英雄初心者.getId();
            case 21:
                return (short) 英雄初心者.getId();
            case 22:
                return (short) 龙初心.getId();
            case 23:
                return (short) 双弩精灵.getId();
            case 24:
                return (short) 幻影.getId();
            case 25:
                return (short) 隱月.getId();
            case 27:
                return (short) 夜光法师.getId();
            case 31:
                return (short) 恶魔猎手.getId();
            case 36:
                return (short) 尖兵.getId();
            case 30:
            case 32:
            case 33:
            case 35:
                return (short) 反抗的基础.getId();
            case 40:
            case 41:
                return (short) 劍豪.getId();
            case 42:
                return (short) 陰陽師.getId();
            case 50:
            case 51:
                return (short) 米哈逸.getId();
            case 60:
            case 61:
                return (short) 狂龙战士的基础.getId();
            case 65:
                return (short) 爆莉萌天使的基础.getId();
            case 100:
            case 101:
                return (short) 神之子的基本.getId();
            case 110:
            case 112:
                return (short) 林芝林.getId();
            case 130:
            case 131:
                return (short) 皮卡啾.getId();
            case 140:
            case 142:
                return (short) 超能力者.getId();
        }
        return (short) 初心者.getId();
    }

    public static boolean isNotMpJob(int job) {
        return is惡魔(job) || is天使破壞者(job) || is神之子(job) || is陰陽師(job) || is凱內西斯(job);
    }

    public static boolean isBeginner(final int job) {
        return getJobGrade(job) == 0;
    }

    public static boolean isSameJob(int job, int job2) {
        int jobNum = getJobGrade(job);
        int job2Num = getJobGrade(job2);
        // 對初心者判斷
        if (jobNum == 0 || job2Num == 0) {
            return getBeginner((short) job) == getBeginner((short) job2);
        }

        // 初心者過濾掉后, 對職業群進行判斷
        if (getJobGroup(job) != getJobGroup(job2)) {
            return false;
        }

        // 代碼特殊的單獨判斷
        if (MapleJob.is管理員(job) || MapleJob.is管理員(job)) {
            return MapleJob.is管理員(job2) && MapleJob.is管理員(job2);
        } else if (MapleJob.is重砲指揮官(job) || MapleJob.is重砲指揮官(job)) {
            return MapleJob.is重砲指揮官(job2) && MapleJob.is重砲指揮官(job2);
        } else if (MapleJob.is蒼龍俠客(job) || MapleJob.is蒼龍俠客(job)) {
            return MapleJob.is蒼龍俠客(job2) && MapleJob.is蒼龍俠客(job2);
        } else if (MapleJob.is惡魔復仇者(job) || MapleJob.is惡魔復仇者(job)) {
            return MapleJob.is惡魔復仇者(job2) && MapleJob.is惡魔復仇者(job2);
        }

        // 對一轉分支判斷(如 战士 跟 黑騎)
        if (jobNum == 1 || job2Num == 1) {
            return job / 100 == job2 / 100;
        }

        return job / 10 == job2 / 10;
    }

    public static int getJobGroup(int job) {
        return job / 1000;
    }

    public static int getJobBranch(int job) {
        if (job / 100 == 27) {
            return 2;
        } else {
            return job % 1000 / 100;
        }
    }

    public static int getJobBranch2nd(int job) {
        if (job / 100 == 27) {
            return 2;
        } else {
            return job % 1000 / 100;
        }
    }

    public static int getJobGrade(int jobz) {
        int job = (jobz % 1000);
        if (job / 10 == 0) {
            return 0; //beginner
        } else if (job / 10 % 10 == 0) {
            return 1;
        } else {
            return job % 10 + 2;
        }
    }
}
