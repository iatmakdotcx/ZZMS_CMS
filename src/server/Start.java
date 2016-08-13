package server;

import client.SkillFactory;
import client.ZZMSEvent;
import constants.GameConstants;
import constants.ServerConfig;
import constants.ServerConstants;
import constants.WorldConstants;
import database.DatabaseConnection;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.channel.MapleDojoRanking;
import handling.channel.MapleGuildRanking;
import handling.farm.FarmServer;
import handling.login.LoginServer;
import handling.world.World;
import handling.world.guild.MapleGuild;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
import server.Timer.BuffTimer;
import server.Timer.CloneTimer;
import server.Timer.EtcTimer;
import server.Timer.EventTimer;
import server.Timer.MapTimer;
import server.Timer.PingTimer;
import server.Timer.WorldTimer;
import server.life.MapleLifeFactory;
import server.life.PlayerNPC;
import server.maps.MapleMapFactory;
import server.quest.MapleQuest;
import tools.MapleAESOFB;

public class Start {

    public static long startTime = System.currentTimeMillis();
    public static final Start Instance = new Start();
    public static AtomicInteger CompletedLoadingThreads = new AtomicInteger(0);

    public void run() throws InterruptedException, IOException {
        long start = System.currentTimeMillis();

        if (ServerConfig.ADMIN_ONLY || ServerConstants.USE_LOCALHOST) {
            System.out.println("Admin Only mode is active.");
        }
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE accounts SET loggedin = 0")) {
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("运行时错误: 无法连接到MySQL数据库服务器 - " + ex);
        }

        System.out.println("正在加载" + ServerConfig.SERVER_NAME + "服务端");
        World.init();
        System.out.println("\r\n主机: " + ServerConfig.IP + ":" + LoginServer.PORT);
        System.out.println("支援游戏版本: " + ServerConstants.MAPLE_TYPE + "的" + ServerConstants.MAPLE_VERSION + "." + ServerConstants.MAPLE_PATCH + "版本" + (ServerConstants.TESPIA ? "测试机" : "") + "用户端");
        System.out.println("主服务端名称: " + WorldConstants.getMainWorld().name());
        System.out.println("");

        if (ServerConstants.MAPLE_TYPE == ServerConstants.MapleType.GLOBAL) {
            boolean encryptionfound = false;
            for (MapleAESOFB.EncryptionKey encryptkey : MapleAESOFB.EncryptionKey.values()) {
                if (("V" + ServerConstants.MAPLE_VERSION).equals(encryptkey.name())) {
                    System.out.println("Packet Encryption: Up-To-Date!");
                    encryptionfound = true;
                    break;
                }
            }
            if (!encryptionfound) {
                System.out.println("无法找到您输入的枫叶版本的数据包加密。使用前面的数据包加密来代替。");
            }
        }
        runThread();
        loadData(false);
        LoginServer.run_startup_configurations();
        ChannelServer.startChannel_Main();
        CashShopServer.run_startup_configurations();
        if (ServerConstants.MAPLE_TYPE == ServerConstants.MapleType.GLOBAL) {
            FarmServer.run_startup_configurations();
        }
        World.registerRespawn();
        Runtime.getRuntime().addShutdownHook(new Thread(new Shutdown()));
        System.out.println("加载地图元素");
        //加载自订地图元素
        MapleMapFactory.loadCustomLife(false);
        //加载玩家NPC
        PlayerNPC.loadAll();
        LoginServer.setOn();
        //System.out.println("Event Script List: " + ServerConfig.getEventList());
        if (ServerConfig.LOG_PACKETS) {
            System.out.println("数据包日志模式已启用");
        }
        if (ServerConfig.USE_FIXED_IV) {
            System.out.println("反抓包功能已启用");
        }
        long now = System.currentTimeMillis() - start;
        long seconds = now / 1000;
        long ms = now % 1000;
        System.out.println("\r\n加载完成, 耗时: " + seconds + "秒" + ms + "毫秒");
        System.gc();
        PingTimer.getInstance().register(System::gc, 1800000); // 每30分钟释放一次        
    }

    public static void runThread() {
        System.out.print("正在加载线程");
        WorldTimer.getInstance().start();
        EtcTimer.getInstance().start();
        MapTimer.getInstance().start();
        CloneTimer.getInstance().start();
        System.out.print(/*"\u25CF"*/".");
        EventTimer.getInstance().start();
        BuffTimer.getInstance().start();
        PingTimer.getInstance().start();
        ZZMSEvent.start();
        System.out.println("完成!\r\n");
    }

    public static void loadData(boolean reload) {
        System.out.println("载入数据(因为数据量大可能比较久而且记忆体消耗会飙升)");
        //加载等级经验
        System.out.println("加载等级经验数据");
        GameConstants.LoadEXP();

        System.out.println("加载排名讯息数据");
        //加载道场拍排名
        MapleDojoRanking.getInstance().load(reload);
        //加载公会排名
        MapleGuildRanking.getInstance().load(reload);
      
        //加载排名
       // RankingWorker.run();

        System.out.println("加载公会数据并清理不存在公会/宠物/机器人");
        //清理已经删除的宠物
        //MaplePet.clearPet();
        //清理已经删除的机器人
        //MapleAndroid.clearAndroid();
        //加载公会并且清理无人公会
        MapleGuild.loadAll();
        //加载家族(家族功能已去掉)
//        MapleFamily.loadAll();
        
        System.out.println("加载任务数据");
        //加载任务讯息
        MapleLifeFactory.loadQuestCounts(reload);
        //加载转存到数据库的任务讯息
        MapleQuest.initQuests(reload);
        
        System.out.println("加载道具数据");
        //加载道具讯息(从WZ)
        MapleItemInformationProvider.getInstance().runEtc(reload);
        //加载道具讯息(从SQL)
        MapleItemInformationProvider.getInstance().runItems(reload);

        System.out.println("加载技能数据");
        //加载技能
        SkillFactory.load(reload);
        
        System.out.println("加载角色卡数据");
        //加载角色卡讯息
        CharacterCardFactory.getInstance().initialize(reload);

        System.out.println("加载商城道具数据");
        //加载商城道具讯息
        //CashItemFactory.getInstance().initialize(reload);
        
        System.out.println("加载掉宝数据");
        //加载掉宝和全域掉宝数据
      //  MapleMonsterInformationProvider.getInstance().load();
        //加载额外的掉宝讯息
      //  MapleMonsterInformationProvider.getInstance().addExtra();

        System.out.println("loadSpeedRuns");
        //?
        SpeedRunner.loadSpeedRuns(reload);
        System.out.println("数据载入完成!\r\n");
    }

    public static class Shutdown implements Runnable {

        @Override
        public void run() {
            ShutdownServer.getInstance().run();
        }
    }

    public static void main(final String args[]) throws InterruptedException, IOException {
        Instance.run();
    }
}
