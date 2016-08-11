/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package constants;

import client.MapleCharacter;
import handling.channel.ChannelServer;
import server.Randomizer;
import server.life.MapleMonster;

/**
 *
 * @author Pungin
 */
public class ZZMSConfig {

    public static byte defaultInventorySlot = 96; //默認道具欄位

    public static int monsterSpawn = 1; //同一個生怪點生怪次數

    public static boolean saveSFLevel = true; //強化爆裝保留星級

    public static boolean maxSkillLevel = true; //默認滿技能等級上限

    public static boolean superiorPotential = true; //尊貴裝允許出尊貴潛能

    public static boolean classBonusEXP = false; //職業隊伍經驗獎勵

    public static boolean levelUpTimesLimit = true; //升級次數限制

    public static boolean 星力損毀保持星階 = true;

    public static void gainMobBFGash(MapleMonster monster, MapleCharacter chr) {
        //獲得類型 1 - 樂豆, 2 - 楓點, 3 - 里程
        int type = 2;
        //系統倍率
        final int caServerrate = ChannelServer.getInstance(chr.getClient().getChannel()).getCashRate();
        //BOSS怪物獲得量
        int cashz = (monster.getStats().isBoss() && monster.getStats().getHPDisplayType() == 0 ? 20 : 0) * caServerrate;
        if (cashz > 0) {
            cashz += Randomizer.nextInt(cashz);
        }
        //通用獲得量
        final int cashModifier = (int) (monster.getStats().getLevel()/ 10);
        if (Randomizer.nextInt(1000) < 50) { //kill nx殺怪得樂豆點、楓點或里程
            chr.modifyCSPoints(type, (int) ((cashz + cashModifier) * (chr.getStat().cashBuff / 100.0) * chr.getCashMod()), true);
        }
    }
}
