/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server.buffs.buffclasses.beasttamer;

import client.MapleBuffStat;
import client.MapleJob;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;

/**
 *
 * @author Maple
 */
public class BeastTamerBuff extends AbstractBuffClass {
    
    public BeastTamerBuff() {
        buffs = new int[]{  
            110001501, // 召喚熊熊Bear Mode
            110001502, // 召喚雪豹Snow Leopard Mode
            110001503, // 召喚雀鷹Hawk Mode
            110001504, // 召喚貓咪Cat Mode
            112001009,  // 集中打擊Bear Assault
            112111000, // 艾卡飛行
        };
    }
    
    @Override
    public boolean containsJob(int job) {
        return MapleJob.is林芝林(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff,int skill) {
        switch (skill) {
            case 112000012:
                eff.statups.put(MapleBuffStat.INDIE_BOOSTER, eff.info.get(MapleStatInfo.indieBooster));
                break;
            default:
//                System.out.println("BeastTamer skill not coded: " + skill);
                break;
        }
    }
}
