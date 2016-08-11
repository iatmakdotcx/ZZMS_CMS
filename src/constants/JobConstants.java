package constants;

import server.ServerProperties;

public class JobConstants {

    public static final boolean enableJobs = true;
    // UI.wz/Login.img/RaceSelect_new/order
    public static final int jobOrder = 01;

    public enum LoginJob {
//龙神
//尖兵
//战神
//林之灵
//双弩精灵
//暗影双刀
//狂龙战士
//龙的传人
        反抗者(0),
        冒险家(1),
        骑士团(2),
        战神(3),
        龙神(4),
        双弩精灵(5),
        恶魔(6),
        幻影(7),
        暗影双刀(8),
        米哈尔(9),
        夜光法师(10),
        狂龙战士(11),
        爆莉萌天使(12),
        火炮手(13),
        尖兵(14),
        神之子(15),
        隐月(16),
        品克缤(17),
        超能力者(18),
        龙的传人(19),
        剑豪(20),
        阴阳师(21),
        林芝林(22),;
        private final int jobType;
        private final boolean enableCreate = true;

        private LoginJob(int jobType) {
            this.jobType = jobType;
        }

        public int getJobType() {
            return jobType;
        }

        public boolean enableCreate() {
            return Boolean.valueOf(ServerProperties.getProperty("JobEnableCreate" + jobType, String.valueOf(enableCreate)));
        }

        public void setEnableCreate(boolean info) {
            if (info == enableCreate) {
                ServerProperties.removeProperty("JobEnableCreate" + jobType);
                return;
            }
            ServerProperties.setProperty("JobEnableCreate" + jobType, String.valueOf(info));
        }
    }
}
