package server.commands;

import client.MapleCharacter;
import client.MapleClient;
import constants.GameConstants;
import handling.channel.ChannelServer;
import java.util.Arrays;
import scripting.NPCScriptManager;
import server.life.MapleMonster;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.maps.SavedLocationType;
import tools.StringUtil;
import tools.packet.CWvsContext;

/**
 *
 * @author Emilyx3
 */
public class PlayerCommand {

    public static PlayerGMRank getPlayerLevelRequired() {
        return PlayerGMRank.NORMAL;
    }

    public static class 怪物 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleMonster mob = null;
            for (final MapleMapObject monstermo : c.getPlayer().getMap().getMapObjectsInRange(c.getPlayer().getPosition(), 100000, Arrays.asList(MapleMapObjectType.MONSTER))) {
                mob = (MapleMonster) monstermo;
                if (mob.isAlive()) {
                    c.getPlayer().dropMessage(-11, "怪物: " + mob.toString());
                    break; //only one
                }
            }
            if (mob == null) {
                c.getPlayer().dropMessage(-11, "沒找到任何怪物");
            }
            return 1;
        }
    }

    public static class 解卡 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.removeClickedNPC();
            NPCScriptManager.getInstance().dispose(c);
            c.getSession().write(CWvsContext.enableActions());
            c.getPlayer().dropMessage(-11, "解卡成功");
            return 1;
        }
    }

    public static class 查看 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            c.getPlayer().showMessage(10, "如下是你在伺服器上的訊息，如不不正確請與管理員聯繫");
            c.getPlayer().showPlayerStats(false);
            c.removeClickedNPC();
            NPCScriptManager.getInstance().dispose(c);
            c.getSession().write(CWvsContext.enableActions());
            return 1;
        }
    }

    public static class GM extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            for (final ChannelServer cserv : ChannelServer.getAllInstances()) {
                cserv.broadcastGMMessage(tools.packet.CField.multiChat("[管理幫幫忙] " + c.getPlayer().getName(), StringUtil.joinStringFrom(splitted, 1), 4));
            }
            c.getPlayer().dropMessage(5, "訊息發送成功");
            return 1;
        }
    }

    /*遊戲活動相關
     public static class Event extends CommandExecute {

     @Override
     public int execute(MapleClient c, String[] splitted) {
     if (c.getPlayer().isInBlockedMap() || c.getPlayer().hasBlockedInventory()) {
     c.getPlayer().dropMessage(5, "You may not use this command here.");
     return 0;
     }
     NPCScriptManager.getInstance().start(c, 9000000, null);
     return 1;
     }
     }

     public static class JoinEvent extends CommandExecute {

     @Override
     public int execute(MapleClient c, String[] splitted) {
     c.getChannelServer().warpToEvent(c.getPlayer());
     return 1;
     }
     }

     public static class SpawnBomb extends CommandExecute {

     @Override
     public int execute(MapleClient c, String[] splitted) {
     if (c.getPlayer().getMapId() != 109010100) {
     c.getPlayer().dropMessage(5, "You may only spawn bomb in the event map.");
     return 0;
     }
     if (!c.getChannelServer().bombermanActive()) {
     c.getPlayer().dropMessage(5, "You may not spawn bombs yet.");
     return 0;
     }
     c.getPlayer().getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9300166), c.getPlayer().getPosition());
     return 1;
     }
     }

     //大概是賽跑活動的指令
     public static class JoinRace extends CommandExecute {

     @Override
     public int execute(MapleClient c, String[] splitted) {
     if (c.getPlayer().getEntryNumber() < 1) {
     if (c.getPlayer().getMapId() == 100000000) {
     if (c.getChannelServer().getWaiting() || c.getPlayer().isGM()) { //TOD: test
     c.getPlayer().setEntryNumber(c.getChannelServer().getCompetitors() + 1);
     c.getChannelServer().setCompetitors(c.getChannelServer().getCompetitors() + 1);
     SkillFactory.getSkill(c.getPlayer().getGender() == 1 ? 80001006 : 80001005).getEffect(1).applyTo(c.getPlayer());
     c.getPlayer().dropMessage(-11, "You have successfully joined the race! Your entry number is " + c.getPlayer().getEntryNumber() + ".");
     c.getPlayer().dropMessage(1, "If you cancel the mount buff, you will automatically leave the race.");
     } else {
     c.getPlayer().dropMessage(-11, "There is no event currently taking place.");
     return 0;
     }
     } else {
     c.getPlayer().dropMessage(-11, "You are not at Henesys.");
     return 0;
     }
     } else {
     c.getPlayer().dropMessage(-11, "You have already joined this race.");
     return 0;
     }
     return 1;
     }
     }

     public static class Rules extends CommandExecute {

     @Override
     public int execute(MapleClient c, String[] splitted) {
     if (c.getChannelServer().getWaiting() || c.getChannelServer().getRace()) {
     c.getPlayer().dropMessage(-11, "The Official Rules and Regulations of the Great Victoria Island Race:");
     c.getPlayer().dropMessage(-11, "-------------------------------------------------------------------------------------------");
     c.getPlayer().dropMessage(-11, "To win you must race from Henesys all the way to Henesys going Eastward.");
     c.getPlayer().dropMessage(-11, "Rule #1: No cheating. You can't use any warping commands, or you'll be disqualified.");
     c.getPlayer().dropMessage(-11, "Rule #2: You may use any form of transportation. This includes Teleport, Flash Jump and Mounts.");
     c.getPlayer().dropMessage(-11, "Rule #3: You are NOT allowed to kill any monsters in your way. They are obstacles.");
     c.getPlayer().dropMessage(-11, "Rule #4: You may start from anywhere in Henesys, but moving on to the next map before the start won't work.");
     } else {
     c.getPlayer().dropMessage(-11, "There is no event currently taking place.");
     return 0;
     }
     return 1;
     }
     }
     */
    public static class 卡圖 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (c.getPlayer().卡图 == c.getPlayer().getMapId() && c.getPlayer().getMapId() / 1000000 != 4) {
                c.getPlayer().changeMap(100000000, 0);
            } else {
                c.getPlayer().dropMessage(1, "你並沒有卡圖啊。");
            }
            c.getPlayer().卡图 = 0;
            return 1;
        }
    }

    public static class 獲取貓頭鷹 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (c.getPlayer().haveItem(2310000) || !c.getPlayer().canHold(2310000)) {
                c.getPlayer().dropMessage(1, "道具欄空間不足或者已經有貓頭鷹了了。");
            } else {
                c.getPlayer().gainItem(2310000, 1, splitted[0] + " 指令獲取");
            }
            return 1;
        }
    }

    public static class 地圖掉寶 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.removeClickedNPC();
            NPCScriptManager.getInstance().start(c, 9010000, "MonsterDrops");
            return 1;
        }
    }
    
    public static class FM extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            for (int i : GameConstants.blockedMaps) {
                if (c.getPlayer().getMapId() == i) {
                    c.getPlayer().dropMessage(5, "你不能在這個地圖使用指令。");
                    return 0;
                }
            }
            if (c.getPlayer().hasBlockedInventory() || c.getPlayer().getMap().getSquadByMap() != null || c.getPlayer().getEventInstance() != null || c.getPlayer().getMap().getEMByMap() != null || c.getPlayer().getMapId() >= 990000000/* || FieldLimitType.VipRock.check(c.getPlayer().getMap().getFieldLimit())*/) {
                c.getPlayer().dropMessage(5, "你不能在這個地圖使用指令。");
                return 0;
            }
            if ((c.getPlayer().getMapId() >= 680000210 && c.getPlayer().getMapId() <= 680000502) || (c.getPlayer().getMapId() / 1000 == 980000 && c.getPlayer().getMapId() != 980000000) || (c.getPlayer().getMapId() / 100 == 1030008) || (c.getPlayer().getMapId() / 100 == 922010) || (c.getPlayer().getMapId() / 10 == 13003000)) {
                c.getPlayer().dropMessage(5, "你不能在這個地圖使用指令。");
                return 0;
            }
            c.getPlayer().saveLocation(SavedLocationType.FREE_MARKET, c.getPlayer().getMap().getReturnMap().getId());
            MapleMap map = c.getChannelServer().getMapFactory().getMap(910000000);
            c.getPlayer().changeMap(map, map.getPortal(0));
            return 1;
        }
    }
    
}
