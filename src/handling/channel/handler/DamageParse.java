package handling.channel.handler;

import client.MapleBuffStat;
import client.MapleCharacter;
import client.MapleJob;
import client.MonsterStatus;
import client.MonsterStatusEffect;
import client.PlayerStats;
import client.Skill;
import client.SkillFactory;
import client.anticheat.CheatTracker;
import client.anticheat.CheatingOffense;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import server.MapleStatEffect;
import server.Randomizer;
import server.life.Element;
import server.life.ElementalEffectiveness;
import server.life.MapleMonster;
import server.life.MapleMonsterStats;
import server.maps.MapleMap;
import server.maps.MapleMapItem;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.AttackPair;
import tools.Pair;
import tools.StringUtil;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.SkillPacket;

public class DamageParse {

    @SuppressWarnings("empty-statement")
    public static void applyAttack(AttackInfo attack, Skill theSkill, MapleCharacter player, int attackCount, double maxDamagePerMonster, MapleStatEffect effect, AttackType attack_type) {
        if (!player.isAlive()) {
            player.getCheatTracker().registerOffense(CheatingOffense.ATTACKING_WHILE_DEAD);
            return;
        }
        if ((attack.real) && (GameConstants.getAttackDelay(attack.skill, theSkill) >= 100)) {
            player.getCheatTracker().checkAttack(attack.skill, attack.lastAttackTickCount);
        }
        if (attack.skill != 0) {
            if (player.isShowInfo()) {
                int display = attack.display & 0x7F;
                player.showMessage(6, "[技能攻擊]使用技能[" + attack.skill + "]進行攻擊，攻擊動作:0x" + Integer.toHexString(display).toUpperCase() + "(" + display + ")");
            }
            if (effect == null) {
                player.getClient().getSession().write(CWvsContext.enableActions());
                return;
            }
            if (GameConstants.isMulungSkill(attack.skill)) {
                if (player.getMapId() / 10000 != 92502) {
                    return;
                }
                if (player.getMulungEnergy() < 10000) {
                    return;
                }
                player.mulung_EnergyModify(false);
            } else if (GameConstants.isPyramidSkill(attack.skill)) {
                if (player.getMapId() / 1000000 != 926) {
                    return;
                }

                if ((player.getPyramidSubway() == null) || (!player.getPyramidSubway().onSkillUse(player))) {
                    return;
                }
            } else if (GameConstants.isInflationSkill(attack.skill)) {
                if (player.getBuffedValue(MapleBuffStat.GIANT_POTION) == null) {
                    return;
                }
            } else if ((attack.targets > effect.getMobCount()) && (attack.skill != 1211002) && (attack.skill != 1220010)) {
                player.getCheatTracker().registerOffense(CheatingOffense.MISMATCHING_BULLETCOUNT);
                if (player.isShowErr()) {
                    player.dropMessage(-5, "物理怪物數量檢測 => 封包解析次數: " + attack.targets + " 伺服器設置次數: " + effect.getMobCount());
                }
                return;
            }
        }
        boolean useAttackCount = !GameConstants.is不檢測次數(attack.skill);

        if ((attack.hits > 0) && (attack.targets > 0)) {
            if (!player.getStat().checkEquipDurabilitys(player, -1)) {
                player.dropMessage(5, "An item has run out of durability but has no inventory room to go to.");
                return;
            }
        }
        int totDamage = 0;
        MapleMap map = player.getMap();

        if (player.isEquippedSoulWeapon() && attack.skill == player.getEquippedSoulSkill()) {
            player.checkSoulState(true);
        }

        if (attack.skill == 4211006) {
            for (AttackPair oned : attack.allDamage) {
                if (oned.attack == null) {
                    MapleMapObject mapobject = map.getMapObject(oned.objectid, MapleMapObjectType.ITEM);

                    if (mapobject != null) {
                        MapleMapItem mapitem = (MapleMapItem) mapobject;
                        mapitem.getLock().lock();
                        try {
                            if (mapitem.getMeso() > 0) {
                                if (mapitem.isPickedUp()) {
                                    return;
                                }
                                map.removeMapObject(mapitem);
                                map.broadcastMessage(CField.explodeDrop(mapitem.getObjectId()));
                                mapitem.setPickedUp(true);
                            } else {
                                player.getCheatTracker().registerOffense(CheatingOffense.ETC_EXPLOSION);
                                return;
                            }
                        } finally {
                            mapitem.getLock().unlock();
                        }
                    } else {
                        player.getCheatTracker().registerOffense(CheatingOffense.EXPLODING_NONEXISTANT);
                        return;
                    }
                }
            }
        }
        int totDamageToOneMonster = 0;
        long hpMob = 0L;
        PlayerStats stats = player.getStat();

        int CriticalDamage = stats.passive_sharpeye_percent();
        int ShdowPartnerAttackPercentage = 0;
        if ((attack_type == AttackType.RANGED_WITH_SHADOWPARTNER) || (attack_type == AttackType.NON_RANGED_WITH_MIRROR)) {
            MapleStatEffect shadowPartnerEffect = player.getStatForBuff(MapleBuffStat.SHADOWPARTNER);
            if (shadowPartnerEffect != null) {
                ShdowPartnerAttackPercentage += shadowPartnerEffect.getX();
            }
            attackCount /= 2;
        }
        ShdowPartnerAttackPercentage *= (CriticalDamage + 100) / 100;
        if (attack.skill == 4221014 || attack.skill == 4221016) {
            ShdowPartnerAttackPercentage *= 30;
        }

        if (attack.skill == 3120017) {
            effect.getMonsterStati().clear();
            if (player.getmod() == 3) {
                effect.getMonsterStati().put(MonsterStatus.POISON, 1);
            }
        }

        double maxDamagePerHit = 0.0D;
        for (AttackPair oned : attack.allDamage) {
            MapleMonster monster = map.getMonsterByOid(oned.objectid);
            if (player.isShowInfo()) {
                player.showMessage(6, "[攻擊]怪物:" + monster);
            }
            if (player.getBuffedValue(MapleBuffStat.QUIVER_KARTRIGE) != null && attack.skill != 95001000 && attack.skill != 3101009 && attack.skill != 3120017) {
                player.handleQuiverKartrige(player, monster.getObjectId());
            }
            if (attack.skill == 65111007 || attack.skill == 31221014) {
                if (monster != null && effect != null && effect.makeChanceResult()) {
                    //TODO 修复灵魂吸取 效果
                    player.getMap().broadcastMessage(player, SkillPacket.DrainSoul(player, monster.getObjectId(), null, 2, effect.getBulletCount(), attack.skill, 0, true), true);
                }
            }
            if ((monster != null) && (monster.getLinkCID() <= 0)) {
                totDamageToOneMonster = 0;
                hpMob = monster.getMobMaxHp();
                MapleMonsterStats monsterstats = monster.getStats();
                int fixeddmg = monsterstats.getFixedDamage();
                boolean Tempest = (monster.getStatusSourceID(MonsterStatus.FREEZE) == 21120006) || (attack.skill == 21120006) || (attack.skill == 1221011);

                if (!Tempest) {
                    maxDamagePerHit = CalculateMaxWeaponDamagePerHit(player, monster, attack, theSkill, effect, maxDamagePerMonster, CriticalDamage);
//                    if (((!monster.isBuffed(MonsterStatus.DAMAGE_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.MAGIC_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.MAGIC_DAMAGE_REFLECT))) || (attack.skill == 3221007) || (attack.skill == 23121003) || (((player.getJob() < 3200) || (player.getJob() > 3212)) && (!monster.isBuffed(MonsterStatus.DAMAGE_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.WEAPON_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.WEAPON_DAMAGE_REFLECT)))) {
//                        maxDamagePerHit = CalculateMaxWeaponDamagePerHit(player, monster, attack, theSkill, effect, maxDamagePerMonster, CriticalDamage);
//                    } else {
//                        maxDamagePerHit = 1.0D;
//                    }
                }
                byte overallAttackCount = 0;

                int criticals = 0;
                for (Pair eachde : oned.attack) {
                    Integer eachd = (Integer) eachde.left;
                    overallAttackCount = (byte) (overallAttackCount + 1);
                    if (((Boolean) eachde.right)) {
                        criticals++;
                    }
                    if ((useAttackCount) && (overallAttackCount - 1 == attackCount)) {
                        maxDamagePerHit = maxDamagePerHit / 100.0D * (ShdowPartnerAttackPercentage * (monsterstats.isBoss() ? stats.bossdam_r : 100.0) / 100.0D);
                    }
                    if (player.isShowInfo() && eachd > 0) {
                        player.dropMessage(-1, new StringBuilder().append("物理攻擊打怪傷害:").append(eachd).append(" 伺服端預計傷害:").append((int) maxDamagePerHit).append(" 是否超過:").append(eachd > maxDamagePerHit).toString());
                    }

                    if (fixeddmg != -1) {
                        if (monsterstats.getOnlyNoramlAttack()) {
                            eachd = attack.skill != 0 ? 0 : fixeddmg;
                        } else {
                            eachd = fixeddmg;
                        }
                    } else if (monsterstats.getOnlyNoramlAttack()) {
                        eachd = attack.skill != 0 ? 0 : Math.min(eachd, (int) maxDamagePerHit);
                    } else if (!player.isGM()) {
                        if (Tempest) {
                            if (eachd > monster.getMobMaxHp()) {
                                eachd = (int) Math.min(monster.getMobMaxHp(), 2147483647L);
                                player.getCheatTracker().registerOffense(CheatingOffense.HIGH_DAMAGE);
                            }
                        } else if (((player.getJob() >= 3200) && (player.getJob() <= 3212) && (!monster.isBuffed(MonsterStatus.DAMAGE_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.MAGIC_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.MAGIC_DAMAGE_REFLECT))) || (attack.skill == 23121003) || (((player.getJob() < 3200) || (player.getJob() > 3212)) && (!monster.isBuffed(MonsterStatus.DAMAGE_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.WEAPON_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.WEAPON_DAMAGE_REFLECT)))) {
                            if (eachd > maxDamagePerHit) {
                                player.getCheatTracker().registerOffense(CheatingOffense.HIGH_DAMAGE, new StringBuilder().append("[傷害: ").append(eachd).append(", 預計傷害: ").append((int) maxDamagePerHit).append(", 怪物: ").append(monster.getId()).append("] [职业: ").append(player.getJob()).append(", 等級: ").append(player.getLevel()).append(", 技能: ").append(attack.skill).append("]").toString());
                                if (attack.real) {
                                    player.getCheatTracker().checkSameDamage(eachd, maxDamagePerHit);
                                }
                                if (eachd > maxDamagePerHit * 2.0D) {
                                    player.getCheatTracker().registerOffense(CheatingOffense.HIGH_DAMAGE_2, new StringBuilder().append("[傷害: ").append(eachd).append(", 預計傷害: ").append((int) maxDamagePerHit).append(", 怪物: ").append(monster.getId()).append("] [职业: ").append(player.getJob()).append(", 等級: ").append(player.getLevel()).append(", 技能: ").append(attack.skill).append("]").toString());
                                    eachd = (int) (maxDamagePerHit * 2.0D);
                                    if (eachd >= 2499999) {
                                        player.getClient().getSession().close(true);
                                    }
                                }
                            }

                        } else if (eachd > maxDamagePerHit) {
                            eachd = (int) maxDamagePerHit;
                        }

                    }

                    if (player == null) {
                        return;
                    }
                    totDamageToOneMonster += eachd;

                    if (((eachd == 0) || (monster.getId() == 9700021)) && (player.getPyramidSubway() != null)) {
                        player.getPyramidSubway().onMiss(player);
                    }
                }
                totDamage += totDamageToOneMonster;
                player.checkMonsterAggro(monster);

                if ((GameConstants.getAttackDelay(attack.skill, theSkill) >= 100) && (!GameConstants.isNoDelaySkill(attack.skill)) && (attack.skill != 3101005) && (!monster.getStats().isBoss()) && (player.getTruePosition().distanceSq(monster.getTruePosition()) > GameConstants.getAttackRange(effect, player.getStat().defRange))) {
                    player.getCheatTracker().registerOffense(CheatingOffense.ATTACK_FARAWAY_MONSTER, new StringBuilder().append("[範圍: ").append(player.getTruePosition().distanceSq(monster.getTruePosition())).append(", 預計範圍: ").append(GameConstants.getAttackRange(effect, player.getStat().defRange)).append(" 职业: ").append(player.getJob()).append("]").toString());
                }

                if (player.getSkillLevel(36110005) > 0) {
                    Skill skill = SkillFactory.getSkill(36110005);
                    MapleStatEffect eff = skill.getEffect(player.getSkillLevel(skill));
                    if (player.getLastCombo() + 5000 < System.currentTimeMillis()) {
                        monster.setTriangulation(0);
                        //player.clearDamageMeters();
                    }
                    if (eff.makeChanceResult()) {
                        player.setLastCombo(System.currentTimeMillis());
                        if (monster.getTriangulation() < 3) {
                            monster.setTriangulation(monster.getTriangulation() + 1);
                        }
                        monster.applyStatus(player, new MonsterStatusEffect(MonsterStatus.DARKNESS, eff.getX(), eff.getSourceId(), null, false), false, eff.getY() * 1000, true, eff);
                        monster.applyStatus(player, new MonsterStatusEffect(MonsterStatus.TRIANGULATION, monster.getTriangulation(), eff.getSourceId(), null, false), false, eff.getY() * 1000, true, eff);
                    }
                }

                if (player.getBuffedValue(MapleBuffStat.PICKPOCKET) != null) {
                    switch (attack.skill) {
                        case 0:
                        case 4001334:
                        case 4201005:
                        case 4211002:
                        case 4211004:
                        case 4221003:
                        case 4221007:
                            handlePickPocket(player, monster, oned);
                    }

                }

                if ((totDamageToOneMonster > 0) || (attack.skill == 1221011) || (attack.skill == 21120006)) {
                    if (MapleJob.is惡魔殺手(player.getJob())) {
                        player.handleForceGain(monster.getObjectId(), attack.skill);
                    }
                    if ((MapleJob.is幻影俠盜(player.getJob())) && (attack.skill != 24120002) && (attack.skill != 24100003)) {
                        player.handleCardStack(monster.getObjectId());
                    }
                    if (MapleJob.is凱撒(player.getJob())) {
                        player.handleKaiserCombo();
                    }
                    if (MapleJob.is暗夜行者(player.getJob())) {
                        player.handleShadowBat(monster.getObjectId(), attack.skill);
                    }
                    if (MapleJob.is夜光(player.getJob())) {
                        player.handleLuminous(attack.skill);
                    }
                    if (MapleJob.is破風使者(player.getJob())) {
                        int rdz = server.Randomizer.nextInt(100);
                        int g_rate = 20, a_rate = 5;
                        if (player.getTotalSkillLevel(13100022) > 0) {
                            g_rate = 20;
                            a_rate = 5;
                        }
                        if (player.getTotalSkillLevel(13110022) > 0) {
                            g_rate = 30;
                            a_rate = 10;
                        }
                        if (player.getTotalSkillLevel(13120003) > 0) {
                            g_rate = 40;
                            a_rate = 15;
                        }
                        if (rdz <= g_rate) {
                            player.handleTriflingWhim(monster.getObjectId(), (rdz <= a_rate), false);
                        }
                        if (player.getBuffedValue(MapleBuffStat.STORM_BRINGER) != null && rdz <= 30) {
                            player.handleTriflingWhim(monster.getObjectId(), (rdz <= a_rate), true);
                        }
                    }
                    if (attack.skill != 1221011) {
                        monster.damage(player, totDamageToOneMonster, true, attack.skill);
                    } else {
                        monster.damage(player, (int) (monster.getStats().isBoss() ? 500000L : monster.getHp() - 1L), true, attack.skill);
                    }

                    if (monster.isBuffed(MonsterStatus.WEAPON_DAMAGE_REFLECT)) {
                        player.addHP(-(7000 + Randomizer.nextInt(8000)));
                    }
                    player.onAttack(monster.getMobMaxHp(), monster.getMobMaxMp(), attack.skill, monster.getObjectId(), totDamage, 0);
                    switch (attack.skill) {
                        case 4001002:
                        case 4001334:
                        case 4001344:
                        case 4111005:
                        case 4121007:
                        case 4201005:
                        case 4211002:
                        case 4221001:
                        case 4221007:
                        case 4301001:
                        case 4311002:
                        case 4311003:
                        case 4331000:
                        case 4331004:
                        case 4331005:
                        case 4331006:
                        case 4341002:
                        case 4341004:
                        case 4341005:
                        case 4341009:
                        case 14001004:
                        case 14111002:
                        case 14111005:
                            int[] skills = {4120005, 4220005, 4340001, 14110004};
                            for (int i : skills) {
                                Skill skill = SkillFactory.getSkill(i);
                                if (player.getTotalSkillLevel(skill) > 0) {
                                    MapleStatEffect venomEffect = skill.getEffect(player.getTotalSkillLevel(skill));
                                    if (!venomEffect.makeChanceResult()) {
                                        break;
                                    }
                                    monster.applyStatus(player, new MonsterStatusEffect(MonsterStatus.POISON, 1, i, null, false), true, venomEffect.getDuration(), true, venomEffect);
                                    break;
                                }

                            }

                            break;
                        case 4201004:
                            monster.handleSteal(player);
                            break;
                        case 21000002:
                        case 21100001:
                        case 21100002:
                        case 21100004:
                        case 21110002:
                        case 21110003:
                        case 21110004:
                        case 21110006:
                        case 21110007:
                        case 21110008:
                        case 21120002:
                        case 21120005:
                        case 21120006:
                        case 21120009:
                        case 21120010:
                            if ((player.getBuffedValue(MapleBuffStat.WK_CHARGE) != null) && (!monster.getStats().isBoss())) {
                                MapleStatEffect eff = player.getStatForBuff(MapleBuffStat.WK_CHARGE);
                                if (eff != null) {
                                    monster.applyStatus(player, new MonsterStatusEffect(MonsterStatus.SPEED, eff.getX(), eff.getSourceId(), null, false), false, eff.getY() * 1000, true, eff);
                                }
                            }
                            if ((player.getBuffedValue(MapleBuffStat.BODY_PRESSURE) != null) && (!monster.getStats().isBoss())) {
                                MapleStatEffect eff = player.getStatForBuff(MapleBuffStat.BODY_PRESSURE);

                                if ((eff != null) && (eff.makeChanceResult()) && (!monster.isBuffed(MonsterStatus.NEUTRALISE))) {
                                    monster.applyStatus(player, new MonsterStatusEffect(MonsterStatus.NEUTRALISE, 1, eff.getSourceId(), null, false), false, eff.getX() * 1000, true, eff);
                                }
                            }
                            break;
                    }
//          int randomDMG = Randomizer.nextInt(player.getDamage2() - player.getReborns() + 1) + player.getReborns();
//          monster.damage(player, randomDMG, true, attack.skill);
//          if (player.getshowdamage() == 1)
//            player.dropMessage(5, new StringBuilder().append("You have done ").append(randomDMG).append(" extra RB damage! (disable/enable this with @dmgnotice)").toString());
//        }
                    //else {
//          if (player.getDamage() > 2147483647L) {
//            long randomDMG = player.getDamage();
//            monster.damage(player, monster.getMobMaxHp(), true, attack.skill);
//            if (player.getshowdamage() == 1) {
//              player.dropMessage(5, new StringBuilder().append("You have done ").append(randomDMG).append(" extra RB damage! (disable/enable this with @dmgnotice)").toString());
//            }
//          }
                    if (totDamageToOneMonster > 0) {
                        Item weapon_ = player.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
                        if (weapon_ != null) {
                            MonsterStatus stat = GameConstants.getStatFromWeapon(weapon_.getItemId());
                            if ((stat != null) && (Randomizer.nextInt(100) < GameConstants.getStatChance())) {
                                MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(stat, Integer.valueOf(GameConstants.getXForStat(stat)), GameConstants.getSkillForStat(stat), null, false);
                                monster.applyStatus(player, monsterStatusEffect, false, 10000L, false, null);
                            }
                        }
                        if (player.getBuffedValue(MapleBuffStat.BLIND) != null) {
                            MapleStatEffect eff = player.getStatForBuff(MapleBuffStat.BLIND);

                            if ((eff != null) && (eff.makeChanceResult())) {
                                MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(MonsterStatus.ACC, Integer.valueOf(eff.getX()), eff.getSourceId(), null, false);
                                monster.applyStatus(player, monsterStatusEffect, false, eff.getY() * 1000, true, eff);
                            }
                        }

                        if (player.getBuffedValue(MapleBuffStat.HAMSTRING) != null) {
                            MapleStatEffect eff = player.getStatForBuff(MapleBuffStat.HAMSTRING);

                            if ((eff != null) && (eff.makeChanceResult())) {
                                MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(MonsterStatus.SPEED, Integer.valueOf(eff.getX()), 3121007, null, false);
                                monster.applyStatus(player, monsterStatusEffect, false, eff.getY() * 1000, true, eff);
                            }
                        }
                        if ((player.getJob() == 121) || (player.getJob() == 122)) {
                            Skill skill = SkillFactory.getSkill(1211006);
                            if (player.isBuffFrom(MapleBuffStat.WK_CHARGE, skill)) {
                                MapleStatEffect eff = skill.getEffect(player.getTotalSkillLevel(skill));
                                MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(MonsterStatus.FREEZE, Integer.valueOf(1), skill.getId(), null, false);
                                monster.applyStatus(player, monsterStatusEffect, false, eff.getY() * 2000, true, eff);
                            }
                        }
                    }
                    if ((effect != null) && (effect.getMonsterStati().size() > 0) && (effect.makeChanceResult())) {
                        for (Map.Entry z : effect.getMonsterStati().entrySet()) {
                            monster.applyStatus(player, new MonsterStatusEffect((MonsterStatus) z.getKey(), (Integer) z.getValue(), theSkill.getId(), null, false), effect.isPoison(), effect.getDuration(), true, effect);
                        }
                    }
                }
            }
        }
//        if ((attack.skill == 4331003) && ((hpMob <= 0L) || (totDamageToOneMonster < hpMob))) {
//            return;
//        }
        if ((hpMob > 0L) && (totDamageToOneMonster > 0)) {
            player.afterAttack(attack.targets, attack.hits, attack.skill);
        }
        boolean applySpecialEffect = true;
        switch (attack.skill) {
            case 4341002:
            case 4331003:
            case 131001001://皮卡啾攻擊
            case 131001002://皮卡啾攻擊
            case 131001003://皮卡啾攻擊
            case 131001101://皮卡啾攻擊
            case 131001102://皮卡啾攻擊
            case 131001103://皮卡啾攻擊
            case 131002000://皮卡啾攻擊
            case 131001000://皮卡啾攻擊
            case 131001113://電吉他
                // 必須攻擊命中才增加BUFF
                applySpecialEffect = attack.targets > 0;
                break;
            default:
                // 非無延遲技能
                applySpecialEffect = !GameConstants.isNoDelaySkill(attack.skill);
                break;
        }
        if (attack.skill != 0 && applySpecialEffect) {
            boolean applyTo = effect.applyTo(player, attack.position);
        }
        if ((totDamage > 1) && (GameConstants.getAttackDelay(attack.skill, theSkill) >= 100)) {
            CheatTracker tracker = player.getCheatTracker();

            tracker.setAttacksWithoutHit(true);
            if (tracker.getAttacksWithoutHit() > 1000) {
                tracker.registerOffense(CheatingOffense.ATTACK_WITHOUT_GETTING_HIT, Integer.toString(tracker.getAttacksWithoutHit()));
            }
        }
    }

    @SuppressWarnings("empty-statement")
    public static final void applyAttackMagic(AttackInfo attack, Skill theSkill, MapleCharacter player, MapleStatEffect effect, double maxDamagePerHit) {
        if (!player.isAlive()) {
            player.getCheatTracker().registerOffense(CheatingOffense.ATTACKING_WHILE_DEAD);
            if (player.isShowErr()) {
                player.showInfo("魔法攻擊", true, "角色已死亡");
            }
            return;
        }
        if ((attack.real) && (GameConstants.getAttackDelay(attack.skill, theSkill) >= 100)) {
            player.getCheatTracker().checkAttack(attack.skill, attack.lastAttackTickCount);
            if (player.isShowErr()) {
                player.showInfo("魔法攻擊", true, "TODO 技能延遲判斷異常");
            }
        }

        if (effect == null) {
            if (player.isShowErr()) {
                player.showInfo("魔法攻擊", true, "effect == null - " + (effect == null));
            }
            return;
        }

        if (effect.getBulletCount() > 1) {
            if ((attack.hits > effect.getBulletCount()) || (attack.targets > effect.getMobCount())) {
                player.getCheatTracker().registerOffense(CheatingOffense.MISMATCHING_BULLETCOUNT);
                if (player.isShowErr()) {
                    player.showInfo("魔法攻擊", true, "攻擊次數大於技能次數 - " + (attack.hits > effect.getBulletCount()) + "，攻擊怪物數量大於技能可攻擊數量 - " + (attack.targets > effect.getMobCount()));
                }
                return;
            }
        } else if (((attack.hits > effect.getAttackCount()) && (effect.getAttackCount() != 0)) || (attack.targets > effect.getMobCount())) {
            player.getCheatTracker().registerOffense(CheatingOffense.MISMATCHING_BULLETCOUNT);
            if (player.isShowErr()) {
                player.showInfo("魔法攻擊", true, "攻擊次數大於技能次數 - " + (attack.hits > effect.getAttackCount()) + "，技能次數不為0 - " + (attack.hits > effect.getAttackCount()) + "，攻擊怪物數量大於技能可攻擊數量 - " + (attack.targets > effect.getMobCount()));
            }
            return;
        }

        if ((attack.hits > 0) && (attack.targets > 0) && (!player.getStat().checkEquipDurabilitys(player, -1))) {
            player.dropMessage(5, "An item has run out of durability but has no inventory room to go to.");
            if (player.isShowErr()) {
                player.showInfo("魔法攻擊", true, "attack.hits > 0 - " + (attack.hits > 0) + ", attack.targets > 0 - " + (attack.targets > 0));
            }
            return;
        }

        if (GameConstants.isMulungSkill(attack.skill)) {
            if (player.getMapId() / 10000 != 92502) {
                if (player.isShowErr()) {
                    player.showInfo("魔法攻擊", true, "是道場技能但是不在道場 - " + (player.getMapId() / 10000 != 92502));
                }
                return;
            }
            if (player.getMulungEnergy() < 10000) {
                if (player.isShowErr()) {
                    player.showInfo("魔法攻擊", true, "道場能力不足 - " + (player.getMulungEnergy() < 10000));
                }
                return;
            }
            player.mulung_EnergyModify(false);
        } else if (GameConstants.isPyramidSkill(attack.skill)) {
            if (player.getMapId() / 1000000 != 926) {
                if (player.isShowErr()) {
                    player.showInfo("魔法攻擊", true, "是金字塔技能但不在金字塔 - " + (player.getMapId() / 1000000 != 926));
                }
                return;
            }

            if ((player.getPyramidSubway() != null) && (player.getPyramidSubway().onSkillUse(player)));
        } else if ((GameConstants.isInflationSkill(attack.skill)) && (player.getBuffedValue(MapleBuffStat.GIANT_POTION) == null)) {
            if (player.isShowErr()) {
                player.showInfo("魔法攻擊", true, "isInflationSkill - " + (GameConstants.isInflationSkill(attack.skill)) + "GIANT_POTION = null - " + (player.getBuffedValue(MapleBuffStat.GIANT_POTION) == null));
            }
            return;
        }

        if (player.isShowErr()) {
            player.dropMessage(-1, new StringBuilder().append("Animation: ").append(Integer.toHexString((attack.display & 0x8000) != 0 ? attack.display - 32768 : attack.display)).toString());
        }
        PlayerStats stats = player.getStat();
        Element element = player.getBuffedValue(MapleBuffStat.ELEMENT_RESET) != null ? Element.NEUTRAL : theSkill.getElement();

        double MaxDamagePerHit = 0.0D;
        int totDamage = 0;

        int CriticalDamage = stats.passive_sharpeye_percent();
        Skill eaterSkill = SkillFactory.getSkill(GameConstants.getMPEaterForJob(player.getJob()));
        int eaterLevel = player.getTotalSkillLevel(eaterSkill);

        MapleMap map = player.getMap();
        for (AttackPair oned : attack.allDamage) {
            MapleMonster monster = map.getMonsterByOid(oned.objectid);
            if ((monster == null) || (monster.getLinkCID() > 0)) {
                if (player.isShowErr()) {
                    player.showInfo("魔法攻擊", true, "怪物為空 - " + (monster == null) + ", monsterLinkCID>0 - " + (monster != null && monster.getLinkCID() > 0));
                }
            }

            if ((monster != null) && (monster.getLinkCID() <= 0)) {
                boolean Tempest = (monster.getStatusSourceID(MonsterStatus.FREEZE) == 21120006) && (!monster.getStats().isBoss());
                int totDamageToOneMonster = 0;
                MapleMonsterStats monsterstats = monster.getStats();
                int fixeddmg = monsterstats.getFixedDamage();
                if ((!Tempest) && (!player.isGM())) {
                    if ((!monster.isBuffed(MonsterStatus.MAGIC_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.MAGIC_DAMAGE_REFLECT))) {
                        MaxDamagePerHit = CalculateMaxMagicDamagePerHit(player, theSkill, monster, monsterstats, stats, element, CriticalDamage, maxDamagePerHit, effect);
                    } else {
                        MaxDamagePerHit = 1.0D;
                    }
                }
                byte overallAttackCount = 0;

                for (Pair eachde : oned.attack) {
                    Integer eachd = (Integer) eachde.left;
                    overallAttackCount = (byte) (overallAttackCount + 1);
                    if (fixeddmg != -1) {
                        eachd = monsterstats.getOnlyNoramlAttack() ? 0 : fixeddmg;
                    } else if (monsterstats.getOnlyNoramlAttack()) {
                        eachd = 0;
                    } else if (!player.isGM()) {
                        if (Tempest) {
                            if (eachd > monster.getMobMaxHp()) {
                                eachd = (int) Math.min(monster.getMobMaxHp(), 2147483647L);
                                player.getCheatTracker().registerOffense(CheatingOffense.HIGH_DAMAGE_MAGIC);
                            }
                        } else if ((!monster.isBuffed(MonsterStatus.MAGIC_IMMUNITY)) && (!monster.isBuffed(MonsterStatus.MAGIC_DAMAGE_REFLECT))) {
                            if (eachd > MaxDamagePerHit) {
                                player.getCheatTracker().registerOffense(CheatingOffense.HIGH_DAMAGE_MAGIC, new StringBuilder().append("[Damage: ").append(eachd).append(", Expected: ").append(MaxDamagePerHit).append(", Mob: ").append(monster.getId()).append("] [Job: ").append(player.getJob()).append(", Level: ").append(player.getLevel()).append(", Skill: ").append(attack.skill).append("]").toString());
                                if (attack.real) {
                                    player.getCheatTracker().checkSameDamage(eachd, MaxDamagePerHit);
                                }
                                if (eachd > MaxDamagePerHit * 2.0D) {
                                    player.getCheatTracker().registerOffense(CheatingOffense.HIGH_DAMAGE_MAGIC_2, new StringBuilder().append("[Damage: ").append(eachd).append(", Expected: ").append(MaxDamagePerHit).append(", Mob: ").append(monster.getId()).append("] [Job: ").append(player.getJob()).append(", Level: ").append(player.getLevel()).append(", Skill: ").append(attack.skill).append("]").toString());
                                    eachd = (int) (MaxDamagePerHit * 2.0D);

                                    if (eachd >= 2499999) {
                                        player.getClient().getSession().close(true);
                                    }
                                }
                            }

                        } else if (eachd > MaxDamagePerHit) {
                            eachd = (int) MaxDamagePerHit;
                        }

                    }

                    totDamageToOneMonster += eachd;
                }

                totDamage += totDamageToOneMonster;
                player.checkMonsterAggro(monster);

                if ((GameConstants.getAttackDelay(attack.skill, theSkill) >= 100) && (!GameConstants.isNoDelaySkill(attack.skill)) && (!monster.getStats().isBoss()) && (player.getTruePosition().distanceSq(monster.getTruePosition()) > GameConstants.getAttackRange(effect, player.getStat().defRange))) {
                    player.getCheatTracker().registerOffense(CheatingOffense.ATTACK_FARAWAY_MONSTER, new StringBuilder().append("[Distance: ").append(player.getTruePosition().distanceSq(monster.getTruePosition())).append(", Expected Distance: ").append(GameConstants.getAttackRange(effect, player.getStat().defRange)).append(" Job: ").append(player.getJob()).append("]").toString());
                    if (player.isShowErr()) {
                        player.showInfo("魔法攻擊", true, "攻擊延遲、範圍異常");
                    }
                    return;
                }
                if ((attack.skill == 2301002) && (!monsterstats.getUndead())) {
                    player.getCheatTracker().registerOffense(CheatingOffense.HEAL_ATTACKING_UNDEAD);
                    if (player.isShowErr()) {
                        player.showInfo("魔法攻擊", true, "群體治愈無法對非不死怪物造成傷害");
                    }
                    return;
                }
                if (MapleJob.is夜光(player.getJob())) {
                    player.handleLuminous(attack.skill);
                }

                monster.damage(player, totDamage, true, attack.skill);
            }

            if (attack.skill != 2301002) {
                effect.applyTo(player);
            }

            if ((totDamage > 1) && (GameConstants.getAttackDelay(attack.skill, theSkill) >= 100)) {
                CheatTracker tracker = player.getCheatTracker();
                tracker.setAttacksWithoutHit(true);

                if (tracker.getAttacksWithoutHit() > 1000) {
                    tracker.registerOffense(CheatingOffense.ATTACK_WITHOUT_GETTING_HIT, Integer.toString(tracker.getAttacksWithoutHit()));
                }
            }
        }
    }

    private static double CalculateMaxMagicDamagePerHit(MapleCharacter chr, Skill skill, MapleMonster monster, MapleMonsterStats mobstats, PlayerStats stats, Element elem, Integer sharpEye, double maxDamagePerMonster, MapleStatEffect attackEffect) {
        int dLevel = Math.max(mobstats.getLevel() - chr.getLevel(), 0) * 2;
        int HitRate = Math.min((int) Math.floor(Math.sqrt(stats.getAccuracy())) - (int) Math.floor(Math.sqrt(mobstats.getEva())) + 100, 100);
        if (dLevel > HitRate) {
            HitRate = dLevel;
        }
        HitRate -= dLevel;
        if ((HitRate <= 0) && ((!MapleJob.isBeginner(skill.getId() / 10000)) || (skill.getId() % 10000 != 1000))) {
            return 0.0D;
        }

        int CritPercent = sharpEye;
        ElementalEffectiveness ee = monster.getEffectiveness(elem);
        double elemMaxDamagePerMob;
        switch (ee) {
            case IMMUNE:
                elemMaxDamagePerMob = 1.0D;
                break;
            default:
                elemMaxDamagePerMob = ElementalStaffAttackBonus(elem, maxDamagePerMonster * ee.getValue(), stats);
        }

        int MDRate = monster.getStats().getMDRate();
        MonsterStatusEffect pdr = monster.getBuff(MonsterStatus.MDEF);
        if (pdr != null) {
            MDRate += pdr.getX();
        }
        elemMaxDamagePerMob -= elemMaxDamagePerMob * (Math.max(MDRate - stats.ignoreTargetDEF - attackEffect.getIgnoreMob(), 0) / 100.0D);

        elemMaxDamagePerMob += elemMaxDamagePerMob / 100.0D * CritPercent;

        elemMaxDamagePerMob *= (monster.getStats().isBoss() ? chr.getStat().bossdam_r : 100.0) / 100.0D;
        MonsterStatusEffect imprint = monster.getBuff(MonsterStatus.IMPRINT);
        if (imprint != null) {
            elemMaxDamagePerMob += elemMaxDamagePerMob * imprint.getX() / 100.0D;
        }
        elemMaxDamagePerMob += elemMaxDamagePerMob * chr.getDamageIncrease(monster.getObjectId()) / 100.0D;
        if (MapleJob.isBeginner(skill.getId() / 10000)) {
            switch (skill.getId() % 10000) {
                case 1000:
                    elemMaxDamagePerMob = 40.0D;
                    break;
                case 1020:
                    elemMaxDamagePerMob = 1.0D;
                    break;
                case 1009:
                    elemMaxDamagePerMob = monster.getStats().isBoss() ? monster.getMobMaxHp() / 30L * 100L : monster.getMobMaxHp();
            }
        }

        switch (skill.getId()) {
            case 32001000:
            case 32101000:
            case 32111002:
            case 32121002:
                elemMaxDamagePerMob *= 1.5D;
        }

        if (elemMaxDamagePerMob > 999999.0D) {
            elemMaxDamagePerMob = 999999.0D;
        } else if (elemMaxDamagePerMob <= 0.0D) {
            elemMaxDamagePerMob = 1.0D;
        }

        return elemMaxDamagePerMob;
    }

    private static double ElementalStaffAttackBonus(Element elem, double elemMaxDamagePerMob, PlayerStats stats) {
        switch (elem) {
            case FIRE:
                return elemMaxDamagePerMob / 100.0D * (stats.element_fire + stats.getElementBoost(elem));
            case ICE:
                return elemMaxDamagePerMob / 100.0D * (stats.element_ice + stats.getElementBoost(elem));
            case LIGHTING:
                return elemMaxDamagePerMob / 100.0D * (stats.element_light + stats.getElementBoost(elem));
            case POISON:
                return elemMaxDamagePerMob / 100.0D * (stats.element_psn + stats.getElementBoost(elem));
        }
        return elemMaxDamagePerMob / 100.0D * (stats.def + stats.getElementBoost(elem));
    }

    private static void handlePickPocket(MapleCharacter player, MapleMonster mob, AttackPair oned) {
        int maxmeso = player.getBuffedValue(MapleBuffStat.PICKPOCKET);

        for (Pair eachde : oned.attack) {
            Integer eachd = (Integer) eachde.left;
            if ((player.getStat().pickRate >= 100) || (Randomizer.nextInt(99) < player.getStat().pickRate)) {
                player.getMap().spawnMesoDrop(Math.min((int) Math.max(eachd / 20000.0D * maxmeso, 1.0D), maxmeso), new Point((int) (mob.getTruePosition().getX() + Randomizer.nextInt(100) - 50.0D), (int) mob.getTruePosition().getY()), mob, player, false, (byte) 0);
            }
        }
    }

    private static double CalculateMaxWeaponDamagePerHit(MapleCharacter player, MapleMonster monster, AttackInfo attack, Skill theSkill, MapleStatEffect attackEffect, double maximumDamageToMonster, Integer CriticalDamagePercent) {
        int dLevel = Math.max(monster.getStats().getLevel() - player.getLevel(), 0) * 2;
        int HitRate = Math.min((int) Math.floor(Math.sqrt(player.getStat().getAccuracy())) - (int) Math.floor(Math.sqrt(monster.getStats().getEva())) + 100, 100);
        if (dLevel > HitRate) {
            HitRate = dLevel;
        }
        HitRate -= dLevel;
        if ((HitRate <= 0) && ((!MapleJob.isBeginner(attack.skill / 10000)) || (attack.skill % 10000 != 1000)) && (!GameConstants.isPyramidSkill(attack.skill)) && (!GameConstants.isMulungSkill(attack.skill)) && (!GameConstants.isInflationSkill(attack.skill))) {
            return 0.0D;
        }
        if ((player.getMapId() / 1000000 == 914) || (player.getMapId() / 1000000 == 927)) {
            return 999999.0D;
        }

        List<Element> elements = new ArrayList();
        boolean defined = false;
        int CritPercent = CriticalDamagePercent;
        int PDRate = monster.getStats().getPDRate();
        MonsterStatusEffect pdr = monster.getBuff(MonsterStatus.WDEF);
        if (pdr != null) {
            PDRate += pdr.getX();
        }
        if (theSkill != null) {
            elements.add(theSkill.getElement());
            if (MapleJob.isBeginner(theSkill.getId() / 10000)) {
                switch (theSkill.getId() % 10000) {
                    case 1000:
                        maximumDamageToMonster = 40.0D;
                        defined = true;
                        break;
                    case 1020:
                        maximumDamageToMonster = 1.0D;
                        defined = true;
                        break;
                    case 1009:
                        maximumDamageToMonster = monster.getStats().isBoss() ? monster.getMobMaxHp() / 30L * 100L : monster.getMobMaxHp();
                        defined = true;
                }
            }

            switch (theSkill.getId()) {
                case 1311005:
                    PDRate = monster.getStats().isBoss() ? PDRate : 0;
                    break;
                case 3221001:
                case 33101001:
                    maximumDamageToMonster *= attackEffect.getMobCount();
                    defined = true;
                    break;
                case 3101005:
                    defined = true;
                    break;
                case 32001000:
                case 32101000:
                case 32111002:
                case 32121002:
                    maximumDamageToMonster *= 1.5D;
                    break;
                case 1221009:
                case 3221007:
                case 4331003:
                case 23121003:
                    if (!monster.getStats().isBoss()) {
                        maximumDamageToMonster = monster.getMobMaxHp();
                        defined = true;
                    }
                    break;
                case 1221011:
                case 21120006:
                    maximumDamageToMonster = monster.getStats().isBoss() ? 500000.0D : monster.getHp() - 1L;
                    defined = true;
                    break;
                case 3211006:
                    if (monster.getStatusSourceID(MonsterStatus.FREEZE) == 3211003) {
                        defined = true;
                        maximumDamageToMonster = 999999.0D;
                    }
                    break;
            }
        }
        double elementalMaxDamagePerMonster = maximumDamageToMonster;
        if ((player.getJob() == 311) || (player.getJob() == 312) || (player.getJob() == 321) || (player.getJob() == 322)) {
            Skill mortal = SkillFactory.getSkill((player.getJob() == 311) || (player.getJob() == 312) ? 3110001 : 3210001);
            if (player.getTotalSkillLevel(mortal) > 0) {
                MapleStatEffect mort = mortal.getEffect(player.getTotalSkillLevel(mortal));
                if ((mort != null) && (monster.getHPPercent() < mort.getX())) {
                    elementalMaxDamagePerMonster = 999999.0D;
                    defined = true;
                    if (mort.getZ() > 0) {
                        player.addHP(player.getStat().getMaxHp() * mort.getZ() / 100);
                    }
                }
            }
        } else if ((player.getJob() == 221) || (player.getJob() == 222)) {
            Skill mortal = SkillFactory.getSkill(2210000);
            if (player.getTotalSkillLevel(mortal) > 0) {
                MapleStatEffect mort = mortal.getEffect(player.getTotalSkillLevel(mortal));
                if ((mort != null) && (monster.getHPPercent() < mort.getX())) {
                    elementalMaxDamagePerMonster = 999999.0D;
                    defined = true;
                }
            }
        }
        if ((!defined) || ((theSkill != null) && ((theSkill.getId() == 33101001) || (theSkill.getId() == 3221001)))) {
            if (player.getBuffedValue(MapleBuffStat.WK_CHARGE) != null) {
                int chargeSkillId = player.getBuffSource(MapleBuffStat.WK_CHARGE);

                switch (chargeSkillId) {
                    case 1201011: //烈焰之劍Flame Charge
                        elements.add(Element.FIRE);
                        break;
                    case 1201012: //寒冰之劍Blizzard Charge
                    case 1211006: //寒冰之劍
                        elements.add(Element.ICE);
                        break;
                    case 1211008: //雷鳴之劍Lightning Charge
                    case 15101006: //雷鳴
                        elements.add(Element.LIGHTING);
                        break;
                    case 1221004: //聖靈之劍Holy Charge
                    case 11111007: //閃耀激發
                        elements.add(Element.HOLY);
                        break;
                    case 12101005:
                }

            }

            if (player.getBuffedValue(MapleBuffStat.LIGHTNING_CHARGE) != null) {
                elements.add(Element.LIGHTING);
            }
            if (player.getBuffedValue(MapleBuffStat.ELEMENT_RESET) != null) {
                elements.clear();
            }
            double elementalEffect;
            if (elements.size() > 0) {
                switch (attack.skill) {
                    case 3111003:
                    case 3211003:
                        elementalEffect = attackEffect.getX() / 100.0D;
                        break;
                    default:
                        elementalEffect = 0.5D / elements.size();
                }

                for (Element element : elements) {
                    switch (monster.getEffectiveness(element)) {
                        case IMMUNE:
                            elementalMaxDamagePerMonster = 1.0D;
                            break;
                        case WEAK:
                            elementalMaxDamagePerMonster *= (1.0D + elementalEffect + player.getStat().getElementBoost(element));
                            break;
                        case STRONG:
                            elementalMaxDamagePerMonster *= (1.0D - elementalEffect - player.getStat().getElementBoost(element));
                    }

                }

            }

            elementalMaxDamagePerMonster -= elementalMaxDamagePerMonster * (Math.max(PDRate - Math.max(player.getStat().ignoreTargetDEF, 0) - Math.max(attackEffect == null ? 0 : attackEffect.getIgnoreMob(), 0), 0) / 100.0D);

            elementalMaxDamagePerMonster += elementalMaxDamagePerMonster / 100.0D * CritPercent;

            MonsterStatusEffect imprint = monster.getBuff(MonsterStatus.IMPRINT);
            if (imprint != null) {
                elementalMaxDamagePerMonster += elementalMaxDamagePerMonster * imprint.getX() / 100.0D;
            }

            double skillDamage = 100.0D;
            if (attackEffect != null) {
                skillDamage = attackEffect.getDamage() + player.getStat().getDamageIncrease(theSkill.getId());
                switch (attackEffect.getSourceId()) {
                    case 131001000:
                        skillDamage += (double) player.getLevel() * attackEffect.getY();
                        break;
                    case 131001001:
                    case 131001101:
                        skillDamage = 100.0D + 3 * player.getLevel();
                        break;
                    case 131001002:
                    case 131001102:
                    case 131001003:
                    case 131001103:
                        skillDamage = 200.0D + 3 * player.getLevel();
                        break;
                    case 131001104:
                        skillDamage = 20.0D + player.getLevel() / 3;
                        break;
                }
            }
            if (player.isShowInfo()) {
                player.showMessage(6, "[傷害計算]屬性傷害：" + (int) Math.ceil(elementalMaxDamagePerMonster) + " 技能傷害：" + (int) Math.ceil(skillDamage) + "% BOSS傷害：" + (int) Math.ceil(((monster.getStats().isBoss()) && (attackEffect != null) ? player.getStat().bossdam_r + attackEffect.getBossDamage() : 100.0) - 100) + "%(" + ((monster.getStats().isBoss()) && (attackEffect != null))  + ")");
            }
            elementalMaxDamagePerMonster += elementalMaxDamagePerMonster * player.getDamageIncrease(monster.getObjectId()) / 100.0D;
            elementalMaxDamagePerMonster *= ((monster.getStats().isBoss()) && (attackEffect != null) ? player.getStat().bossdam_r + attackEffect.getBossDamage() : 100.0) / 100.0D;
            elementalMaxDamagePerMonster *= skillDamage / 100.0D;
        }
        if (elementalMaxDamagePerMonster > 50000000.0D) {
            if (!defined) {
                elementalMaxDamagePerMonster = 50000000.0D;
            }
        } else if (elementalMaxDamagePerMonster <= 0.0D) {
            elementalMaxDamagePerMonster = 1.0D;
        }
        return elementalMaxDamagePerMonster;
    }

    public static final AttackInfo DivideAttack(final AttackInfo attack, final int rate) {
        attack.real = false;
        if (rate <= 1) {
            return attack; //lol
        }
        for (AttackPair p : attack.allDamage) {
            if (p.attack != null) {
                for (Pair<Integer, Boolean> eachd : p.attack) {
                    eachd.left /= rate; //too ex.
                }
            }
        }
        return attack;
    }

    public static final AttackInfo Modify_AttackCrit(AttackInfo attack, MapleCharacter chr, int type, MapleStatEffect effect) {
        int CriticalRate;
        boolean shadow;
        List damages;
        List damage;
        if ((attack.skill != 4211006) && (attack.skill != 3211003) && (attack.skill != 4111004)) {
            CriticalRate = chr.getStat().passive_sharpeye_rate() + (effect == null ? 0 : effect.getCr());
            shadow = (chr.getBuffedValue(MapleBuffStat.SHADOWPARTNER) != null) && ((type == 1) || (type == 2));
            damages = new ArrayList();
            damage = new ArrayList();

            for (AttackPair p : attack.allDamage) {
                if (p.attack != null) {
                    int hit = 0;
                    int mid_att = shadow ? p.attack.size() / 2 : p.attack.size();

                    int toCrit = (attack.skill == 4221001) || (attack.skill == 3221007) || (attack.skill == 23121003) || (attack.skill == 4341005) || (attack.skill == 4331006) || (attack.skill == 21120005) ? mid_att : 0;
                    if (toCrit == 0) {
                        for (Pair eachd : p.attack) {
                            if ((!((Boolean) eachd.right)) && (hit < mid_att)) {
                                if ((((Integer) eachd.left) > 999999) || (Randomizer.nextInt(100) < CriticalRate)) {
                                    toCrit++;
                                }
                                damage.add(eachd.left);
                            }
                            hit++;
                        }
                        if (toCrit == 0) {
                            damage.clear();
                        } else {
                            Collections.sort(damage);
                            for (int i = damage.size(); i > damage.size() - toCrit; i--) {
                                damages.add(damage.get(i - 1));
                            }
                            damage.clear();
                        }
                    } else {
                        hit = 0;
                        for (Pair eachd : p.attack) {
                            if (!((Boolean) eachd.right)) {
                                if (attack.skill == 4221001) {
                                    eachd.right = hit == 3;
                                } else if ((attack.skill == 3221007) || (attack.skill == 23121003) || (attack.skill == 21120005) || (attack.skill == 4341005) || (attack.skill == 4331006) || (((Integer) eachd.left) > 999999)) {
                                    eachd.right = true;
                                } else if (hit >= mid_att) {
                                    eachd.right = ((Pair) p.attack.get(hit - mid_att)).right;
                                } else {
                                    eachd.right = damages.contains(eachd.left);
                                }
                            }
                            hit++;
                        }
                        damages.clear();
                    }
                }
            }
        }
        return attack;
    }

    public static final AttackInfo parseMagicDamage(final LittleEndianAccessor lea, final MapleCharacter chr) {
        final AttackInfo ret = new AttackInfo();
        ret.isMagicAttack = true;
        lea.skip(1);
        ret.tbyte = lea.readByte();
        ret.targets = (byte) ((ret.tbyte >>> 4) & 0xF);
        ret.hits = (byte) (ret.tbyte & 0xF);
        ret.skill = lea.readInt();
        lea.skip(5);
        if (GameConstants.isMagicChargeSkill(ret.skill)) {
            ret.charge = lea.readInt();
        } else {
            ret.charge = -1;
        }
        lea.skip(1);
        ret.unk = lea.readByte();
        ret.display = lea.readByte();
        ret.direction = lea.readByte();
        lea.skip(4);
        switch (ret.skill) {
            case 12120010:
            case 12110028:
            case 12100028:
            case 12000026:
                lea.skip(2);
                break;
            default:
                lea.skip(1);
        }
        ret.speed = lea.readByte(); // Confirmed
        ret.lastAttackTickCount = lea.readInt(); // Ticks
        lea.skip(4);
        switch (ret.skill) {
            case 12120010:
            case 12110028:
            case 12100028:
            case 12000026:
                lea.skip(4);
                break;
        }
        ret.allDamage = new ArrayList<>();
        for (int i = 0; i < ret.targets; i++) {
            int oid = lea.readInt();
            short unktype = lea.readShort();
            lea.skip(18);
            List<Pair<Integer, Boolean>> allDamageNumbers = new ArrayList();
            for (int j = 0; j < ret.hits; j++) {
                int damage = lea.readInt();
                if (chr.isShowInfo()) {
                    chr.dropMessage(-5, "魔法攻擊[" + ret.skill + "] - 攻擊數量: " + ret.targets + " 攻擊段數: " + ret.hits + " 怪物OID " + oid + " 傷害: " + damage);
                }
                allDamageNumbers.add(new Pair<>(damage, false));
            }
            lea.skip(4); // CRC of monster [Wz Editing]
            lea.skip(4);
            lea.skip(4);
            ret.allDamage.add(new AttackPair(oid, allDamageNumbers, unktype));
        }
        ret.position = lea.readPos();
        return ret;
    }

    public static AttackInfo parseCloseRangeAttack(LittleEndianAccessor lea, MapleCharacter chr) { // 近距離攻擊
        AttackInfo ret = new AttackInfo();
        ret.isCloseRangeAttack = true;
        lea.skip(1);
        ret.tbyte = lea.readByte();
        ret.targets = ((byte) (ret.tbyte >>> 4 & 0xF));
        ret.hits = ((byte) (ret.tbyte & 0xF));
        ret.skill = lea.readInt();
        if (MapleJob.is神之子(chr.getJob()) && ret.skill != 0) {
            lea.skip(1); //zero has byte
        }
        switch (ret.skill) {
            case 2221012:
            case 36101001:
            case 36101008:
            case 42120003:
                lea.skip(1);
                break;
        }
        lea.skip(GameConstants.isEnergyBuff(ret.skill) ? 1 : 2);
        lea.readInt(); // nSkillCRC
        lea.readByte();
        switch (ret.skill) {
            case 1311011:// La Mancha Spear
            case 2221012:
            case 2221052:
            case 4341002:
            case 4341003:
            case 4221052:
            case 5201002:
            case 5300007:
            case 5301001:
            case 5711021: // 飛龍在天
            case 5721061: // 龍襲亂舞
            case 11121052:// Styx Crossing
            case 11121055:// Styx Crossing charged
            case 14121004:
            case 14111006:
            case 24121000:
            case 24121005:
            case 25111005:
            case 25121030:
            case 27101202:
            case 27111100:
            case 27120211:
            case 27121201:
            case 31001000:
            case 31101000:
            case 31111005:
            case 32121003:
            case 36121000:
            case 36101001:
            case 42120003: // Monkey Spirits
            case 61111100:
            case 61111111:
            case 61111113:
            case 65121003:
            case 65121052:// Supreme Supernova
            case 101110101:
            case 101110102:
            case 101110104:
            case 101120200:
            case 101120203:
            case 101120205:
            case 131001004:
            case 131001008:
                ret.charge = lea.readInt();
                break;
            default:
                ret.charge = 0;
        }
        if ((MapleJob.is神之子(ret.skill / 10000))) {
            ret.zeroUnk = lea.readByte();
        }
        ret.unk = lea.readByte();
        ret.display = lea.readByte();
        ret.direction = lea.readByte();
        if (ret.skill == 2221012 || ret.skill == 36101001 || ret.skill == 36111009 || ret.skill == 42120003) {
            lea.skip(4);
        } else if (ret.skill != 131000016) {
            lea.skip(5);
        }
        if ((ret.skill == 5300007) || (ret.skill == 5101012) || (ret.skill == 5081001) || (ret.skill == 15101010)) {
            lea.readInt();
        }
        ret.speed = lea.readByte();
        ret.lastAttackTickCount = lea.readInt();
        lea.skip(4);
        if (!GameConstants.isEnergyBuff(ret.skill)) {
            int linkskill = lea.readInt();
            if (linkskill > 0) {
                lea.skip(1);
            }
        }
        switch (ret.skill) {
            case 14111022:
            case 14111023:
            case 14121004:
                lea.skip(2);
                break;
            case 5711021:
            case 5721061:
                lea.skip(4);
                break;
            case 14121052:
                lea.skip(6);
                break;
        }

        ret.allDamage = new ArrayList<>();
        if (ret.skill == 4211006) {
            return parseExplosionAttack(lea, ret, chr);
        }

        for (int i = 0; i < ret.targets; i++) {
            int oid = lea.readInt();
            short unktype = lea.readShort();
            lea.skip(18);
            List<Pair<Integer, Boolean>> allDamageNumbers = new ArrayList<>();
            for (int j = 0; j < ret.hits; j++) {
                int damage = lea.readInt();
                if (chr.isShowInfo()) {
                    chr.dropMessage(-5, "近距離攻擊[" + ret.skill + "] - 攻擊數量: " + ret.targets + " 攻擊段數: " + ret.hits + " 怪物OID " + oid + " 傷害: " + damage);
                }
                allDamageNumbers.add(new Pair<>(damage, false));
            }
            lea.skip(4);
            lea.skip(4);
            lea.skip(4);
            ret.allDamage.add(new AttackPair(oid, allDamageNumbers, unktype));
        }
        ret.position = lea.readPos();
        return ret;
    }

    public static AttackInfo parseRangedAttack(LittleEndianAccessor lea, MapleCharacter chr) { // 遠距離攻擊
        AttackInfo ret = new AttackInfo();
        ret.isRangedAttack = true;
        lea.skip(1);
        lea.skip(1);
        ret.tbyte = lea.readByte();
        ret.targets = ((byte) (ret.tbyte >>> 4 & 0xF));
        ret.hits = ((byte) (ret.tbyte & 0xF));
        ret.skill = lea.readInt();
        lea.skip(5);
        switch (ret.skill) {
            case 3121004: // 暴風神射
            case 3101008: // 躍退射擊
            case 3111009: // 暴風神射
            case 3121013: // 舞旋連矢
            case 3221001: // 光速神弩
            case 5081002: // 迅雷
            case 5321052: // 滾動彩虹加農炮
            case 5221004: // 瞬‧迅雷
            case 5311002: // 猴子的衝擊波
            case 5700010: // 疾風迅雷
            case 5711002: // 猛虎衝
            case 5721001: // 蒼龍連襲
            case 13111002:// 暴風神射
            case 13111020:// 寒冰亂舞
            case 13121001:// 天空之歌
            case 23121000:// 伊修塔爾之環
            case 24121000:// 連犽突進
            case 33121009:// 狂野帕爾坎
            case 35001001:// 火焰發射
            case 35101009:// 強化的火焰發射
            case 60011216:// 繼承人
//            case 5221022:
                lea.skip(4);
                break;
            case 3111013: // 箭座
                lea.skip(12);
                break;
        }

        if (MapleJob.is神之子(ret.skill / 10000)) {
            ret.zeroUnk = lea.readByte();
        }

        switch (ret.skill) { //暗夜
            case 14001020:
            case 14101020:
            case 14101021:
            case 14111020:
            case 14111021:
            case 14121001:
            case 14121002:
                lea.skip(4);
        }

        lea.skip(1);
        lea.skip(1);
        lea.skip(1);
        ret.charge = -1;
        ret.unk = lea.readByte();
        ret.display = lea.readByte();
        ret.direction = lea.readByte();
        switch (ret.skill) {
            case 5311010:
                lea.skip(4);
                break;
            case 3121013:
            case 3111013:
            case 5220023:
            case 5221022:
            case 5310011:
            case 95001000:
                lea.skip(8);
                break;
            case 23111001:
            case 36111010:
                lea.skip(12);
                break;
        }
        lea.skip(4);
        lea.skip(1);
        ret.speed = lea.readByte();
        ret.lastAttackTickCount = lea.readInt();
        lea.skip(4);
        ret.starSlot = ((byte) lea.readShort());
        ret.cashSlot = ((byte) lea.readShort());
        ret.AOE = lea.readByte();
        if (chr.getBuffStatValueHolder(MapleBuffStat.SPIRIT_CLAW) != null) {
            lea.skip(4);
        }
        switch (ret.skill) {
            case 5700010:
            case 5201008:
            case 5081002:
                lea.skip(4);//這邊讀取的是所使用的子彈/標
        }

        ret.allDamage = new ArrayList<>();
        for (int i = 0; i < ret.targets; i++) {
            int oid = lea.readInt();
            short unktype = lea.readShort();
            lea.skip(18);
            List<Pair<Integer, Boolean>> allDamageNumbers = new ArrayList<>();
            for (int j = 0; j < ret.hits; j++) {
                int damage = lea.readInt();
                if (chr.isShowInfo()) {
                    chr.dropMessage(-5, "遠距離攻擊[" + ret.skill + "] - 攻擊數量: " + ret.targets + " 攻擊段數: " + ret.hits + " 怪物OID " + oid + " 傷害: " + damage);
                }
                allDamageNumbers.add(new Pair<>(damage, false));
            }
            lea.skip(4);
            lea.skip(4);
            lea.skip(4);
            ret.allDamage.add(new AttackPair(oid, allDamageNumbers, unktype));
        }
        ret.position = lea.readPos();
        if (lea.available() == 4L) {
            ret.skillposition = lea.readPos();
        }
        return ret;
    }

    public static AttackInfo parseExplosionAttack(LittleEndianAccessor lea, AttackInfo ret, MapleCharacter chr) {
        if (ret.hits == 0) {
            lea.skip(4);
            byte bullets = lea.readByte();
            for (int j = 0; j < bullets; j++) {
                int mesoid = lea.readInt();
                lea.skip(2);
                if (chr.isShowInfo()) {
                    chr.dropMessage(-5, "楓幣炸彈攻擊怪物: 攻擊次數 " + ret.hits + " 楓幣ID " + mesoid);
                }
                ret.allDamage.add(new AttackPair(lea.readInt(), null, (short) 0));
            }
            lea.skip(2);
            return ret;
        }
        for (int i = 0; i < ret.targets; i++) {
            int oid = lea.readInt();
            lea.skip(19);
            byte bullets = lea.readByte();
            List allDamageNumbers = new ArrayList();
            for (int j = 0; j < bullets; j++) {
                int damage = lea.readInt();
                if (chr.isShowInfo()) {
                    chr.dropMessage(-5, "楓幣炸彈攻擊怪物: 怪物數量 " + ret.targets + " 攻擊次數 " + bullets + " 攻擊傷害 " + damage);
                }
                allDamageNumbers.add(new Pair(damage, false));
            }
            ret.allDamage.add(new AttackPair(oid, allDamageNumbers, (short) 0));
            lea.skip(4);
            lea.skip(4);
            lea.skip(4);
        }
        lea.skip(4);
        byte bullets = lea.readByte();
        for (int j = 0; j < bullets; j++) {
            int mesoid = lea.readInt();
            lea.skip(2);
            if (chr.isShowInfo()) {
                chr.dropMessage(-5, "楓幣炸彈攻擊怪物: 個數 " + bullets + " 楓幣ID: " + mesoid);
            }
            ret.allDamage.add(new AttackPair(mesoid, null, (short) 0));
        }
        return ret;
    }
}
