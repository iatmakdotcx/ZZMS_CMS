package client;

import handling.Buffstat;
import java.io.Serializable;

public enum MapleBuffStat implements Serializable, Buffstat {

    //==========================Mask[0] - 1 - IDA[0xE]
 
    // 0    
    TEST_BUFF0(0, true),
    //提升最大爆擊                      [完成-182]
    INDIE_CR(1, true),
    // 2
    TEST_BUFF2(2, true),
    //物理攻擊力增加                    [完成-182]
    INDIE_PAD(3, true),//indiePad
    //魔法攻擊力增加                    [完成-182]
    INDIE_MAD(4, true),//indieMad
    //物理防御力增加                    [完成-182]
    INDIE_PDD(5, true),//indiePdd
    //魔法防御力增加                    [完成-182]
    INDIE_MDD(6, true),//indieMdd
    //最大體力                          [完成-182]
    INDIE_MAX_HP(7, true), //indieMaxHp, indieMhp
    //最大體力百分比                    [完成-182]
    INDIE_MHP_R(8, true), //indieMhpR
    //最大魔法                          [完成-182]
    INDIE_MAX_MP(9, true),//indieMaxMp
    //最大魔法百分比                    [完成-182]
    INDIE_MMP_R(10, true), //indieMmpR
    //命中值增加                        [完成-182]
    INDIE_ACC(11, true),//indieAcc
    //提升迴避值                        [完成-182]
    INDIE_EVA(12, true),
    //疾速之輪行蹤
    INDIE_JUMP(13, true),
    //疾速之輪行蹤                      [完成-182]
    INDIE_SPEED(14, true),
    //所有能力提升
    INDIE_ALL_STATE(15, true), //indieAllStat 
    // 16
    TEST_BUFF16(16, true),
    //疾速之輪行蹤 & 重生的輪行蹤        [完成-182]
    INDIE_EXP(17, true), //indieExp
    //攻擊速度提升                      [完成-182]
    INDIE_BOOSTER(18, true), //indieBooster
    // 19
    TEST_BUFF19(19, true),
    // 20
    TEST_BUFF20(20, true),
    // 21
    TEST_BUFF21(21, true),
    // 22
    TEST_BUFF22(22, true),
    // 23 
    STACKING_ATK(23, true),
    // 24
    TEST_BUFF24(24, true),
    //提高STR                          [完成-182] 
    INDIE_STR(25, true),
    //提高DEX                          [完成-182] 
    INDIE_DEX(26, true),
    //提高INT                          [完成-182] 
    INDIE_INT(27, true),
    //提高LUK                          [完成-182]  
    INDIE_LUK(28, true),    
    //提高技能攻擊力                    [完成-182]
    INDIE_DAM_R(29, true),//indieDamR
    // 30
    TEST_BUFF30(30, true),
    // 31
    TEST_BUFF31(31, true),
    
    //==========================Mask[1] - 2 - IDA[0xD]
    
    //傷害最大值
    INDIE_MAX_DAMAGE_OVER(32, true),
    //物理傷害減少百分比(重生的輪行蹤)   [完成-182]
    INDIE_ASR_R(33, true), //indieAsrR
    //魔法傷害減少百分比(重生的輪行蹤)   [完成-182]
    INDIE_TER_R(34, true), //indieTerR
    //爆擊率提升                        [完成-182]
    INDIE_CR_R(35, true),
    //超衝擊防禦_防禦力                 [完成-182]
    INDIE_PDD_R(36, true), 
    //提升最大爆擊                      [完成-182]
    INDIE_MAX_CR(37, true),    
    //提升BOSS攻擊力                    [完成-182]
    INDIE_BOSS(38, true),
    //提升所有屬性                      [完成-182]
    INDIE_ALL_STATE_R(39, true),
    //提升檔格                          [完成-182]
    INDIE_STANCE_R(40, true),
    //提升無視防禦                      [完成-182]
    INDIE_IGNORE_MOB_PDP_R(41, true), //indieIgnoreMobpdpR    
    // 42
    TEST_BUFF42(42, true),
    //提升攻擊％                        [完成-182]
    INDIE_PAD_R(43, true),
    //提升魔法攻擊％                     [完成-182]
    INDIE_MAD_R(44, true),
    //提升最大爆擊％                    [完成-182]
    INDIE_MAX_CR_R(45, true),
    //提升所有迴避值％                  [完成-182]
    INDIE_EVA_R(46, true),
    //提升魔法防禦力％                  [完成-182]
    INDIE_MDD_R(47, true),
    // 48
    TEST_BUFF48(48, true),
    // 49
    TEST_BUFF49(49, true),
    //物理攻擊力                        [IDA找的-186]
    WATK(51),
    //物理防御力                        [IDA找的-186]
    WDEF(52),
    //魔法攻擊力                        [IDA找的-186]
    MATK(53),
    //魔法防御力                        [IDA找的-186]
    MDEF(54),
    //命中率                            [IDA找的-186]
    ACC(55),
    //迴避率                            [IDA找的-186]
    AVOID(56),
    //手技                              [IDA找的-186]
    HANDS(57),
    //移動速度                          [IDA找的-186]
    SPEED(58),
    //跳躍力                            [IDA找的-186]
    JUMP(59),
    //魔心防禦                          [IDA找的-186]
    MAGIC_GUARD(60),
    //隱藏術                            [IDA找的-186]
    DARKSIGHT(61),
    //攻擊加速                          [IDA找的-186]
    BOOSTER(62),
    //傷害反擊                          [IDA找的-186]
    POWERGUARD(63),
    //神聖之火_最大MP                   [IDA找的-186]
    MAXMP(64),
    
    //==========================Mask[2] - 3 - IDA[0xC]
    
    //神聖之火_最大HP                   [IDA找的-186]
    MAXHP(65),    
    //神聖之光                          [IDA找的-186]
    INVICIBLE(66),
    //無形之箭                          [IDA找的-186]
    SOULARROW(67),
    //昏迷                              [IDA找的-186]
    STUN(68),
    //中毒                              [IDA找的-186]
    POISON(69),
    //封印                              [IDA找的-186]
    SEAL(70),
    //黑暗                              [IDA找的-186]
    DARKNESS(71),
    //鬥氣集中                          [IDA找的-186]
    COMBO(72),
    //召喚獸                            [IDA找的-186]
    SUMMON(72),
    //屬性攻擊                          [IDA找的-186]
    WK_CHARGE(73),
    //龍之力量                          [IDA找的-186]
    DRAGONBLOOD(73),
    //神聖祈禱                          [IDA找的-186]
    HOLY_SYMBOL(74),
    //(CMS_聚財術)                      [IDA找的-186]
    MESOUP(75),
    //影分身                            [IDA找的-186]
    SHADOWPARTNER(76),
    //勇者掠奪術                        [IDA找的-186]
    PICKPOCKET(77),
    //替身術                            [IDA找的-186]
    PUPPET(77),
    //楓幣護盾                          [IDA找的-186]
    MESOGUARD(78),
    //                                 [IDA找的-186]
    HP_LOSS_GUARD(79),
    //虛弱                              [IDA找的-186]
    WEAKEN(80),
    //詛咒                              [IDA找的-186]
    CURSE(81),
    //緩慢                              [IDA找的-186]
    SLOW(82),
    //變身                              [IDA找的-186]
    MORPH(83),
    //恢復                              [IDA找的-186]
    RECOVERY(84),
    //楓葉祝福                          [IDA找的-186]
    MAPLE_WARRIOR(85),
    //格擋(穩如泰山)                    [IDA找的-186]
    STANCE(86),
    //銳利之眼                          [IDA找的-186]
    SHARP_EYES(87),
    //魔法反擊                          [IDA找的-186]
    MANA_REFLECTION(88),
    //誘惑                              [IDA找的-186]
    SEDUCE(89),
    //暗器傷人                          [IDA找的-186]
    SPIRIT_CLAW(90),
    //時空門                            [IDA找的-186]
    MYSTIC_DOOR(90),
    //魔力無限                          [IDA找的-186]
    INFINITY(91),
    //進階祝福                          [IDA找的-186]
    HOLY_SHIELD(92),
    //敏捷提升                          [IDA找的-186]
    HAMSTRING(93),
    //命中率增加                        [IDA找的-186]
    BLIND(94),
    //集中精力                          [IDA找的-186]
    CONCENTRATE(95),
    //不死化                            [IDA找的-186]
    ZOMBIFY(96),
    //英雄的回響                        [IDA找的-186]
    ECHO_OF_HERO(97),
    //==========================Mask[3] - 4 - IDA[0xB]
    //楓幣獲得率                        [IDA找的-186]
    MESO_RATE(98),
    //鬼魂變身                          [IDA找的-186]
    GHOST_MORPH(99),
    //混亂                              [IDA找的-186]
    REVERSE_DIRECTION(100),
    //掉寶幾率                          [IDA找的-186]
    DROP_RATE(101),
    //經驗倍率                          [IDA找的-186]
    EXPRATE(102),
    //樂豆倍率                          [IDA找的-186]
    ACASH_RATE(103),
    //                                  [IDA找的-186]
    ILLUSION(104),
    //解多人Buff用的                    [IDA找的-186]
    PLAYERS_BUFF8(105),
    //解多人Buff用的                    [IDA找的-186]
    PLAYERS_BUFF9(106),
    //狂暴戰魂                          [IDA找的-186]
    BERSERK_FURY(107),
    //金剛霸體                          [IDA找的-186]
    DIVINE_BODY(108),
    //(CMS_闪光击)                      [IDA找的-186]
    SPARK(109),
    //(CMS_终极弓剑)                    [IDA找的-186]
    FINALATTACK(110),
    //隱藏刀                            [IDA找的-186]
    BLADE_CLONE(111),
    //自然力重置                        [IDA找的-186]
    ELEMENT_RESET(112),
    //(CMS_风影漫步)                    [IDA找的-186]
    WIND_WALK(113),
    //組合無限                          [IDA找的-186]
    UNLIMITED_COMBO(114),
    //矛之鬥氣                          [IDA找的-186]
    ARAN_COMBO(115),
    //嗜血連擊                          [IDA找的-186]
    COMBO_DRAIN(116),
    //宙斯之盾                          [IDA找的-186]
    COMBO_BARRIER(116),
    //強化連擊(CMS_戰神抗壓)            [IDA找的-186]
    BODY_PRESSURE(117),
    //釘錘(CMS_戰神威勢)                [IDA找的-186]
    SMART_KNOCKBACK(118),
    //(CMS_天使狀態)                    [IDA找的-186]
    PYRAMID_PQ(119),
    //解多人Buff用的                    [IDA找的-186]
    PLAYERS_BUFF10(120),
    //無法使用藥水                      [IDA找的-186]
    POTION_CURSE(121),
    //影子                              [IDA找的-186]
    SHADOW(123),
    //致盲                              [IDA找的-186]
    BLINDNESS(124),
    //緩慢術                            [IDA找的-186]
    SLOWNESS(125),
    //守護之力(CMS_魔法屏障)             [IDA找的-186]
    MAGIC_SHIELD(126),
    //魔法抵抗．改                      [IDA找的-186]
    MAGIC_RESISTANCE(127),
    //靈魂之石                          [IDA找的-186]
    SOUL_STONE(128),
    
    //==========================Mask[4] - 5 - IDA[0xA]
    
    //飛天騎乘                          [IDA找的-186]
    SOARING(129),    
    //冰凍                              [IDA找的-186]
    FREEZE(130),
    //雷鳴之劍
    LIGHTNING_CHARGE(130),
    //鬥氣爆發                          [IDA找的-186]
    ENRAGE(132),
    //障礙                              [IDA找的-186]
    BACKSTEP(133),
    //無敵(隱‧鎖鏈地獄、自由精神等)     [IDA找的-186]
    INVINCIBILITY(134),
    //絕殺刃                            [IDA找的-186]
    FINAL_CUT(135),
    //咆哮                              [IDA找的-186]
    DAMAGE_BUFF(136),
    //狂獸附體                          [IDA找的-186]
    ATTACK_BUFF(137), 
    //地雷                              [IDA找的-186]
    RAINING_MINES(138),
    //增強_MaxHP                        [IDA找的-186]
    ENHANCED_MAXHP(139),
    //增強_MaxMP                        [IDA找的-186]
    ENHANCED_MAXMP(140),
    //增強_物理攻擊力                   [IDA找的-186]
    ENHANCED_WATK(141),
    //增強_魔法攻擊力                   [IDA找的-186]
    ENHANCED_MATK(142),
    //增強_物理防禦力                   [IDA找的-186]
    ENHANCED_WDEF(143),
    //增強_魔法防禦力                   [IDA找的-186]
    ENHANCED_MDEF(144),
    //全備型盔甲                        [IDA找的-186]
    PERFECT_ARMOR(145),
    //終極賽特拉特_PROC                 [IDA找的-186]
    SATELLITESAFE_PROC(146),
    //終極賽特拉特_吸收                 [IDA找的-186]
    SATELLITESAFE_ABSORB(147),                   
    //颶風                              [IDA找的-186]
    TORNADO(148),
    //咆哮_會心一擊機率增加              [IDA找的-186]
    CRITICAL_RATE_BUFF(149),
    //咆哮_MaxMP 增加                   [IDA找的-186]
    MP_BUFF(150),
    //咆哮_傷害減少                     [IDA找的-186]
    DAMAGE_TAKEN_BUFF(151),
    //咆哮_迴避機率                     [IDA找的-186]
    DODGE_CHANGE_BUFF(152),
    //                                 [IDA找的-186]
    CONVERSION(153),
    //甦醒                              [IDA找的-186]
    REAPER(154),
    //迷你啾出動                        [IDA找的-186]
    MINI_PINK_BEAN_SUMMON(155),
    //潛入                             [IDA找的-186]
    INFILTRATE(156),
    //合金盔甲                         [IDA找的-186]                  
    MECH_CHANGE(157),    
    
    //==========================Mask[5] - 6 - IDA[0x9]
    
    //暴走形态                          [IDA找的-186]
    FELINE_BERSERK(158),
    //幸运骰子                          [IDA找的-186]
    DICE_ROLL(159),
    //祝福护甲                          [IDA找的-186]
    DIVINE_SHIELD(160),
    //攻擊增加百分比                    [IDA找的-186]
    DAMAGE_RATE(161),
    //瞬間移動精通                      [IDA找的-186]
    TELEPORT_MASTERY(162),
    //戰鬥命令                          [IDA找的-186]
    COMBAT_ORDERS(163),
    //追隨者                            [IDA找的-186]
    BEHOLDER(164),
    //功能不知道                        [IDA找的-186]
    IDA_UNK_BUFF11(164),
    //裝備潛能無效化                    [IDA找的-186]
    DISABLE_POTENTIAL(165),    
    //                                  [IDA找的-186]
    GIANT_POTION(166),
    //玛瑙的保佑                        [IDA找的-186]
    ONYX_SHROUD(167),
    //玛瑙的意志                        [IDA找的-186]
    ONYX_WILL(168),
    //龍捲風                            [IDA找的-186]
    TORNADO_CURSE(169),
    //天使祝福                          [IDA找的-186]
    BLESS(170),    
    //解多人Buff用的                    [IDA找的-186]
    PLAYERS_BUFF4(171),
    //解多人Buff用的                    [IDA找的-186]
    PLAYERS_BUFF6(172),
    //解多人Buff用的                    [IDA找的-186]
    PLAYERS_BUFF7(173),
    //压制术                            [IDA找的-186]
    THREATEN_PVP(173),
    //解多人Buff用的                    [IDA找的-186]
    PLAYERS_BUFF5(174),
    //冰骑士                            [IDA找的-186]
    ICE_KNIGHT(174),    
    //解多人Buff用的                    [IDA找的-186]
    PLAYERS_BUFF15(175),    
    //力量                              [IDA找的-186]
    STR(175),
    //智力                              [IDA找的-186]
    INT(176),
    //敏捷                              [IDA找的-186]
    DEX(177),
    //運氣                              [IDA找的-186]
    LUK(178),
    //未知(未確定)                      [IDA找的-186]
    ATTACK(179),    
    //未知(未確定)                      [IDA找的-186]
    STACK_ALLSTATS(180),
    //未知(未確定)                      [IDA找的-186]
    PVP_DAMAGE(181),
    //IDA移動Buff                       [IDA找的-186]
    IDA_MOVE_BUFF7(182),    
    // 更新BUFF用                       [IDA找的-186]
    IDA_BUFF_183(183),
    // 更新BUFF用                       [IDA找的-186]
    IDA_BUFF_184(184),
    // 更新BUFF用                       [IDA找的-186]
    IDA_BUFF_185(185),    
    //解多人Buff用的                    [IDA找的-186]
    PLAYERS_BUFF2(186),
    //解多人Buff用的                    [IDA找的-186]
    PLAYERS_BUFF1(187),
    //解多人Buff用的                    [IDA找的-186]
    PLAYERS_BUFF12(188),
    //未知(未確定)
    PVP_ATTACK(187),    
    // 更新BUFF用                       [IDA找的-186]
    IDA_BUFF_189(189),    
    //隱藏潛能(未確定)                  [IDA找的-186]
    HIDDEN_POTENTIAL(190),
    //未知(未確定)                      [IDA找的-186]
    ELEMENT_WEAKEN(191),
    //解多人Buff用的                    [IDA找的-186]
    PLAYERS_BUFF3(191),
    //未知(未確定)(90002000)            [IDA找的-186]
    SNATCH(192),     
    //凍結                              [IDA找的-186]
    FROZEN(192),    
    // 更新BUFF用                       [IDA找的-186]
    IDA_BUFF_193(193),
    // 更新BUFF用                       [IDA找的-186]
    IDA_BUFF_194(194),
    // 更新BUFF用                       [IDA找的-186]
    IDA_BUFF_195(195),

    //==========================Mask[6] - 7 - IDA[0x8]
    
    //未知(未確定)                      [IDA找的-186]
    PVP_FLAG(196),    
    //無限力量[猜測]                    [IDA找的-186]
    BOUNDLESS_RAGE(197),
    //聖十字魔法盾                      [IDA找的-186]
    HOLY_MAGIC_SHELL(197),
    //核爆術                            [IDA找的-186]
    BIG_BANG(198),
    //神秘狙擊                         [IDA找的-186]
    MANY_USES(199),
    //大魔法師(已改成被動,無BUFF)       [IDA找的-186]
    BUFF_MASTERY(199),
    //異常抗性                         [IDA找的-186]
    ABNORMAL_STATUS_R(200),
    //屬性抗性                         [IDA找的-186]
    ELEMENTAL_STATUS_R(201),
    //水之盾                           [IDA找的-186]
    WATER_SHIELD(202),
    //變形                             [IDA找的-186]
    DARK_METAMORPHOSIS(203),
    //随机橡木桶                       [IDA找的-186]
    BARREL_ROLL(204),
    //精神连接[跟靈魂灌注是同個東西]    [IDA找的-186]
    SPIRIT_LINK(205),
    //靈魂灌注_攻擊增加                [IDA找的-186]
    DAMAGE_R(205),
    //神圣拯救者的祝福                 [IDA找的-186]
    VIRTUE_EFFECT(206),    
    //光明綠化                        [IDA找的-186]
    COSMIC_GREEN(207),    
    //靈魂灌注_爆擊率增加              [IDA找的-186]
    CRITICAL_RATE(208),
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_209(209),
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_210(210),
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_211(211),
    
    // 212
    
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_213(213),
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_214(214),    
    //未知(未確定)                    [IDA找的-186]
    NO_SLIP(215),
    //未知(未確定)                    [IDA找的-186]
    FAMILIAR_SHADOW(216),
    //吸血鬼之觸                      [IDA找的-186]
    ABSORB_DAMAGE_HP(216),
    //提高防禦力[猜測]                [IDA找的-186]
    DEFENCE_BOOST_R(217),
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_218(218),    
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_219(219),    
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_220(220),    
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_221(221), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_222(222),    
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_223(223),  
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_224(224),   
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_225(225), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_226(226), 

    //==========================Mask[7] - 8 - IDA[0x7]
    
    //IDA特殊Buff                     [IDA找的-186]
    IDA_SPECIAL_BUFF_1(227),
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_228(228), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_229(229),     
    //角設預設Buff                    [IDA找的-186]
    CHAR_BUFF(230),
    //角設預設Buff                    [IDA找的-186]
    MOUNT_MORPH(231),
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_232(232),    
    //使用技能中移動                   [IDA找的-186]
    USING_SKILL_MOVE(233),
    //無視防禦力                       [IDA找的-186]
    IGNORE_DEF(234),
    //幸運幻影                         [IDA找的-186]
    FINAL_FEINT(235),
    //幻影斗蓬                         [IDA找的-186]
    PHANTOM_MOVE(236),
    //最小爆擊傷害                     [IDA找的-186]
    MIN_CRITICAL_DAMAGE(237),
    //爆擊機率增加                     [IDA找的-186]
    CRITICAL_RATE2(238),
    //審判                             [IDA找的-186]
    JUDGMENT_DRAW(239),
    //增加_物理攻擊                     [IDA找的-186]
    DAMAGE_UP(240),    
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_241(241),
    //IDA移動Buff                  [IDA找的-186]
    IDA_MOVE_BUFF2(242),
    //IDA移動Buff                  [IDA找的-186]
    IDA_MOVE_BUFF3(243),
    //IDA移動Buff                  [IDA找的-186]
    IDA_MOVE_BUFF4(244),
    //解多人Buff用的                [IDA找的-186]
    PLAYERS_BUFF14(245),    
    //黑暗之眼                     [IDA找的-186]
    PRESSURE_VOID(246),    
    //光蝕 & 暗蝕                  [IDA找的-186]
    LUMINOUS_GAUGE(247),
    //黑暗強化 & 全面防禦          [IDA找的-186]
    DARK_CRESCENDO(248),
    //黑暗祝福                     [IDA找的-186]
    BLACK_BLESSING(249),
    //抵禦致命異常狀態              [IDA找的-186]
    //(如 元素適應(火、毒), 元素適應(雷、冰), 聖靈守護)
    ABNORMAL_BUFF_RESISTANCES(250),
    //血之限界                     [IDA找的-186]
    LUNAR_TIDE(251),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_252(252),    
    //凱撒變型值                   [IDA找的-186]
    KAISER_COMBO(253),    
    
    //==========================Mask[8] - 9 - IDA[0x6]
    
    //堅韌護甲                     [IDA找的-186]
    GRAND_ARMOR(254),
    //凱撒模式切換                 [IDA找的-186]
    KAISER_MODE_CHANGE(255),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_256(256),    
    //IDA移動Buff                  [IDA找的-186]
    IDA_MOVE_BUFF5(257),
    //意志之劍                     [IDA找的-186]
    TEMPEST_BLADES(258),
    //會心一擊傷害                 [IDA找的-186]
    CRIT_DAMAGE(259),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_260(260),  
    //靈魂傳動                      [IDA找的-186]
    DAMAGE_ABSORBED(261),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_262(262),  
    //功能不知道                    [IDA找的-186]
    IDA_UNK_BUFF3(263),    
    //IDA特殊Buff                   [IDA找的-186]
    IDA_SPECIAL_BUFF_3(264),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_265(265),  
    //功能不知道                    [IDA找的-186]
    IDA_UNK_BUFF5(266),      
    //IDA移動Buff                   [IDA找的-186]
    IDA_MOVE_BUFF6(267),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_268(268),  
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_269(269),  
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_270(270),  
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_271(271), 
    //繼承人                        [IDA找的-186]
    SOUL_BUSTER(272),
    //未知(未確定)                  [IDA找的-186]
    PRETTY_EXALTATION(272),
    //未知(未確定)                  [IDA找的-186]
    KAISER_MAJESTY3(273),
    //未知(未確定)                  [IDA找的-186]
    KAISER_MAJESTY4(274),     
    //靈魂深造                      [IDA找的-186]
    RECHARGE(275),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_276(276), 
    //隱‧鎖鏈地獄                  [IDA找的-186] 
    STATUS_RESIST_TWO(277),
    //終極審判[猜測]                [IDA找的-186]
    FINAL_JUDGMENT_DRAW(277),    
    //冰雪結界                      [IDA找的-186]
    ABSOLUTE_ZERO_AURA(277),
    //未知(未確定)                  [IDA找的-186]
    PARTY_STANCE(277),    
    //火靈結界[猜測]                [IDA找的-186]
    INFERNO_AURA(277),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_278(278), 
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_279(279), 
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_280(280), 
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_281(281), 
    //復仇天使                      [IDA找的-186]
    AVENGING_ANGEL(282),
    //天堂之門                      [IDA找的-186]
    HEAVEN_IS_DOOR(283),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_284(284), 
    //戰鬥準備                      [IDA找的-186]
    BOWMASTERHYPER(285),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_286(286),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_287(287),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_288(288),     
    //修羅                          [IDA找的-186]
    ASURA_IS_ANGER(289),
    //暴能續發                      [IDA找的-186]
    STIMULATING_CONVERSATION(290),
    //IDA特殊Buff                   [IDA找的-186]
    IDA_SPECIAL_BUFF_2(291),
    
    //==========================Mask[9] - 10 - IDA[0x5]
    
    //功能不知道                     [IDA找的-186]
    IDA_UNK_BUFF10(292),
    //BOSS傷害                      [IDA找的-186]
    BOSS_DAMAGE(293),
    //功能不知道                     [IDA找的-186]
    IDA_UNK_BUFF8(293),
    //全域代碼                       [IDA找的-186]
    OOPARTS_CODE(294),    
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_295(295),    
    //超越_攻擊                      [IDA找的-186]
    EXCEED_ATTACK(296),
    //急速療癒                       [IDA找的-186]
    DIABOLIC_RECOVERY(297),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_298(298),    
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_299(299),      
    //超越                          [IDA找的-186]
    EXCEED(300),
    //沉月-攻擊數量                  [IDA找的-186]
    ATTACK_COUNT(301),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_302(302),  
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_303(303),  
    //傑諾能量                      [IDA找的-186]
    SUPPLY_SURPLUS(304),
    //IDA特殊Buff                   [IDA找的-186]
    IDA_SPECIAL_BUFF_5(305),    
    //IDA BUFF列表更新用            [IDA找的-186]
    IDA_BUFF_306(306),    
    //傑諾飛行                      [IDA找的-186]
    XENON_FLY(307),
    //阿瑪蘭斯發電機                [IDA找的-186]
    AMARANTH_GENERATOR(308),
    //解多人Buff用的                [IDA找的-186]
    PLAYERS_BUFF13(309),
    //元素： 風暴                   [IDA找的-186]
    STORM_ELEMENTAL(310),
    //開天闢地[猜測]                [IDA找的-186]
    PRIMAL_BOLT(311),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_312(312),  
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_313(313),
    //風暴使者                      [IDA找的-186]
    STORM_BRINGER(314),
    //光之劍-命中提升                [IDA找的-186]
    ACCURACY_PERCENT(315),
    //迴避提升                      [IDA找的-186]
    AVOID_PERCENT(316),
    //阿爾法                        [IDA找的-186]
    ALBATROSS(317),
    //                              [IDA找的-186]
    SPEED_LEVEL(318),
    //雙重力量 : 沉月/旭日           [IDA找的-186]
    SOLUNA_EFFECT(319),    
    //光之劍                        [IDA找的-186]
    ADD_AVOIDABILITY(320),
    //元素： 靈魂                   [IDA找的-186]
    SOUL_ELEMENT(321),
    //雙重力量 : 沉月/旭日           [IDA找的-186]
    EQUINOX_STANCE(322),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_323(323),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_324(324),
    
    //==========================Mask[10] - 11 - IDA[0x4]
    
    //靈魂球BUFF                    [IDA找的-186]
    SOUL_WEAPON(325),
    //靈魂BUFF                      [IDA找的-186]
    SOUL_WEAPON_HEAD(329),
    //復原                          [IDA找的-186]
    HP_RECOVER(327),
    //功能不知道                    [IDA找的-186]
    IDA_UNK_BUFF4(328),
    
    // 326
    
    //十字深鎖鏈                    [IDA找的-186]
    CROSS_SURGE(330),
    //超衝擊防禦_防禦概率           [IDA找的-186]
    PARASHOCK_GUARD(331),
    //功能不知道                   [IDA找的-186]
    IDA_UNK_BUFF12(332),
    // 更新BUFF用                  [IDA找的-186]          
    IDA_BUFF_333(333),    
    //寒冰迅移                     [IDA找的-186]
    CHILLING_STEP(334),
    
    //
    
    //祝福福音                      [IDA找的-186]
    PASSIVE_BLESS(336),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_337(337),    
    //功能不知道                    [IDA找的-186]
    IDA_UNK_BUFF13(338),
    //進階顫抖                      [IDA找的-186]
    QUIVER_KARTRIGE(339),        
    //IDA特殊Buff                   [IDA找的-186]
    IDA_SPECIAL_BUFF_6(340),
    //IDA移動Buff                   [IDA找的-186]
    IDA_MOVE_BUFF8(342),
    //IDA特殊Buff                   [IDA找的-186]
    IDA_SPECIAL_BUFF_7(343),
    //功能不知道                    [IDA找的-186]
    IDA_UNK_BUFF14(344),
    //時之威能                      [IDA找的-186]
    DIVINE_FORCE_AURA(345),
    //聖靈神速                      [IDA找的-186]
    DIVINE_SPEED_AURA(346),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_347(347),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_348(348),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_349(349),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_350(350),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_351(351),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_352(352),
    // 大砲(95001002)               [IDA找的-186]
    CANNON(353),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_354(354),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_355(355),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_356(356),

    //==========================Mask[11] - 12 - IDA[0x3]
    
    //無視怪物傷害(重生的輪行蹤)       [IDA找的-186]
    IGNORE_MOB_DAM_R(357), //ignoreMobDamR
    //靈魂結界                        [IDA找的-186]
    SPIRIT_WARD(358),
    //死裡逃生                        [IDA找的-186]
    CLOSE_CALL(359),    
    //IDA特殊Buff                     [IDA找的-186]
    IDA_SPECIAL_BUFF_9(360),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_361(361),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_362(362),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_363(363),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_364(364),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_365(365),    
    //IDA移動Buff                   [IDA找的-186]
    IDA_MOVE_BUFF9(366),
    //元素 : 闇黑                   [IDA找的-186]
    DARK_ELEMENTAL(367),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_368(368),
    //燃燒                          [IDA找的-186]
    CONTROLLED_BURN(369),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_370(370),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_371(371),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_372(372),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_373(373),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_374(374),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_375(375),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_376(376),    
    //功能不知道                     [IDA找的-186]
    IDA_UNK_BUFF16(377),
    //危機繩索                      [IDA找的-186]
    AURA_BOOST(378),
    //拔刀術                        [IDA找的-186]
    HAYATO(379),
    //拔刀術-新技體                  [IDA找的-186]
    BATTOUJUTSU_SOUL(380),
    //制敵之先                         [IDA找的-186]
    COUNTERATTACK(381),
    //柳身                           [IDA找的-186]
    WILLOW_DODGE(382),        
    //紫扇仰波                       [IDA找的-186]
    SHIKIGAMI(383),
    //武神招來                       [IDA找的-186]
    MILITARY_MIGHT(384),
    //武神招來                       [IDA找的-186]
    MILITARY_MIGHT1(385),
    //武神招來                    
    MILITARY_MIGHT2(383),
    //拔刀術                           [IDA找的-186]
    BATTOUJUTSU_STANCE(386),
    
    // 385
    
    //==========================Mask[12] - 13 - IDA[0x2]

    // 386
    
    //迅速                             [IDA找的-186]
    JINSOKU(389),
    //一閃                             [IDA找的-186]
    HITOKIRI_STRIKE(390), 
    //花炎結界                         [IDA找的-186]
    FOX_FIRE(391),
    //影朋‧花狐                        [IDA找的-186]
    HAKU_REBORN(392),
    //花狐的祝福                        [IDA找的-186]
    HAKU_BLESS(393),
    // 更新BUFF用                       [IDA找的-186]
    IDA_BUFF_394(394),
    //解多人Buff用的                    [IDA找的-186]
    PLAYERS_BUFF11(395),    
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_396(396),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_397(397),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_398(398),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_399(399),
    
    //精靈召喚模式                   [IDA找的-186]
    ANIMAL_SELECT(400),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_401(401),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_402(402),
    // 401
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_404(404),    
    //IDA特殊Buff                   [IDA找的-186]
    IDA_SPECIAL_BUFF_4(405),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_406(406),    
    //依古尼斯咆哮-迴避提升          [IDA找的-186]
    PROP(407),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_408(408), 
    //召喚美洲豹                    [IDA找的-186]
    SUMMON_JAGUAR(409),
    //自由精神                      [IDA找的-186]
    SPIRIT_OF_FREEDOM(410),    
    //功能不知道                    [IDA找的-186]
    IDA_UNK_BUFF1(411),
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_412(412), 
    //功能不知道                    [IDA找的-186]
    IDA_UNK_BUFF2(413),    
    //光環效果                      [IDA找的-186]
    NEW_AURA(414),
    //黑暗閃電                      [IDA找的-186]
    DARK_SHOCK(415),
    //戰鬥精通                      [IDA找的-186]
    BATTLE_MASTER(416),
    //死神契約                      [IDA找的-186]
    GRIM_CONTRACT(417), 
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_418(418), 
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_419(419), 
    
    //==========================Mask[13] - 14 - IDA[0x0]
    
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_420(420), 
    // 更新BUFF用                   [IDA找的-186]
    IDA_BUFF_421(421),    
    //神盾系統                        [IDA找的-186]
    AEGIS_SYSTEM(422),    
    //索魂精通                        [IDA找的-186]
    SOUL_SEEKER(423),
    //小狐仙                          [IDA找的-186]
    FOX_SPIRITS(424),
    //暗影蝙蝠                        [IDA找的-186]
    SHADOW_BAT(425),
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_426(426), 
    //燎原之火                        [IDA找的-186]
    IGNITE(427),    
    //能量獲得                        [IDA找的-186]
    ENERGY_CHARGE(428, true),
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_429(429), 
    //預設Buff-3                       [IDA找的-186]
    DEFAULTBUFF3(430, true),
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_431(431), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_432(432), 
    //皮卡啾攻擊                       [IDA找的-186]
    PINK_BEAN_ATTACK(433),
    //皮卡啾未知                       [IDA找的-186]
    PINK_BEAN_UNK(434),
    //預設Buff-4                        [IDA找的-186]
    DEFAULTBUFF4(435),
    //烈焰溜溜球                       [IDA找的-186]
    PINK_BEAN_YOYO(436),    
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_437(437), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_438(438), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_439(439), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_440(440), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_441(441), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_442(442), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_443(443), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_444(444), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_445(445), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_446(446), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_447(447), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_448(448), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_449(449), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_450(450), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_451(451), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_452(452), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_453(453), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_454(454), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_455(455), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_456(456), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_457(457), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_458(458), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_459(459), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_460(460), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_461(461), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_462(462), 
    // 更新BUFF用                     [IDA找的-186]
    IDA_BUFF_463(463), 
    
    // 464
    
    //預設Buff-5                        [IDA找的-186]
    DEFAULTBUFF5(465, true),
    //衝鋒_速度                         [IDA找的-186]
    DASH_SPEED(466, true),    
    //衝鋒_跳躍                         [IDA找的-186]
    DASH_JUMP(467, true),
    //怪物騎乘                          [IDA找的-186]
    MONSTER_RIDING(468, true),
    //最終極速                          [IDA找的-186]
    SPEED_INFUSION(469, true),
    //指定攻擊(無盡追擊)                 [IDA找的-186]
    HOMING_BEACON(470, true),
    //預設Buff-1                        [IDA找的-186]
    DEFAULTBUFF1(471, true),
    //預設Buff-2                        [IDA找的-186]
    DEFAULTBUFF2(472, true),
    
    
    //-----------------[已停用的Buff]
    //黑色繩索                      
    DARK_AURA_OLD(888),
    //藍色繩索                      
    BLUE_AURA_OLD(888),
    //黃色繩索                      
    YELLOW_AURA_OLD(888),
    //貓頭鷹召喚
    OWL_SPIRIT(888),
    //超級體
    BODY_BOOST(888),
    
    ;
    private static final long serialVersionUID = 0L;
    private final int buffstat;
    private final int first;
    private boolean stacked = false;

    private MapleBuffStat(int buffstat) {
        this.buffstat = 1 << (31 - (buffstat % 32));
        this.first = (int) Math.floor(buffstat / 32);
    }

    private MapleBuffStat(int buffstat, boolean stacked) {
        this.buffstat = 1 << (31 - (buffstat % 32));
        this.first = (int) Math.floor(buffstat / 32);
        this.stacked = stacked;
    }

    @Override
    public int getPosition() {
        return first;
    }

    @Override
    public int getValue() {
        return buffstat;
    }
    
    public static MapleBuffStat getMapleBuffStat(int buff) 
    {
        for (MapleBuffStat bb : values()){
            if (bb.getValue() == buff) {
                return bb;
            }
        }
        return MapleBuffStat.TEST_BUFF0;
    }

    public final boolean canStack() {
        return stacked;
    }
}