/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.inventory;

/**
 *
 * @author Itzik
 */
public enum EquipStat {

    // 可使用捲軸次數
    SLOTS(0x01, 1, 1),
    // 捲軸強化次數
    LEVEL(0x02, 1, 1),
    // 力量
    STR(0x04, 2, 1),
    // 敏捷
    DEX(0x08, 2, 1),
    // 智力
    INT(0x10, 2, 1),
    // 幸運
    LUK(0x20, 2, 1),
    // MaxHP
    MHP(0x40, 2, 1),
    // MaxMP
    MMP(0x80, 2, 1),
    // 攻擊力
    WATK(0x100, 2, 1),
    // 魔力
    MATK(0x200, 2, 1),
    // 防御力
    WDEF(0x400, 2, 1),
    // 魔法防御力
    MDEF(0x800, 2, 1),
    // 命中
    ACC(0x1000, 2, 1),
    // 閃避
    AVOID(0x2000, 2, 1),
    // 靈敏度
    HANDS(0x4000, 2, 1),
    // 移動速度
    SPEED(0x8000, 2, 1),
    // 跳躍力
    JUMP(0x10000, 2, 1),
    // Flag
    FLAG(0x20000, 2, 1),
    // 裝備技能
    INC_SKILL(0x40000, 1, 1),
    // 裝備等級
    ITEM_LEVEL(0x80000, 1, 1),
    // 裝備經驗
    ITEM_EXP(0x100000, 8, 1),
    // 耐久度
    DURABILITY(0x200000, 4, 1),
    // 鐵鎚提煉次數「黃金鐵鎚跟白金鐵鎚」
    VICIOUS_HAMMER(0x400000, 4, 1),
    // 大亂鬥傷害
    PVP_DAMAGE(0x800000, 2, 1),
    // 套用等級減少
    DOWNLEVEL(0x1000000, 1, 1),
    // 露塔必思箱子開出來的裝備的特殊功能(如 潛能等級100%提升、不會損毀、捲軸成功率100%)
    ENHANCT_BUFF(0x2000000, 2, 1),
    // 特殊耐久度
    DURABILITY_SPECIAL(0x4000000, 4, 1),
    // 需求等級
    REQUIRED_LEVEL(0x8000000, 1, 1),
    // 世界之樹的祝福 [20485XX]
    YGGDRASIL_WISDOM(0x10000000, 1, 1),
    // 最後武力 技能 [2048600]
    FINAL_STRIKE(0x20000000, 1, 1),
    // BOSS傷
    BOSS_DAMAGE(0x40000000, 1, 1),
    // 無視防禦
    IGNORE_PDR(0x80000000, 1, 1),
    // 總傷
    TOTAL_DAMAGE(0x1, 1, 2),
    // 全屬性
    ALL_STAT(0x2, 1, 2),
    // 白金剪刀次數
    KARMA_COUNT(0x4, 1, 2),
    UNK8(0x8, 8, 2), //long
    UNK10(0x10, 4, 2); //int
    private final int value, datatype, first;

    private EquipStat(int value, int datatype, int first) {
        this.value = value;
        this.datatype = datatype;
        this.first = first;
    }

    public final int getValue() {
        return value;
    }

    public final int getDatatype() {
        return datatype;
    }

    public final int getPosition() {
        return first;
    }

    public final boolean check(int flag) {
        return (flag & value) != 0;
    }

    public enum EnhanctBuff {

        // 除了裝備強化卷軸之外，其餘所有的卷軸失敗時也不會被破壞
        NO_DESTROY(0x1),
        // 使用方塊時，有 100%的幾率等級將會上升。
        UPGRADE_TIER(0x2),
        // 使用除了強化卷軸意外的所有卷軸時，將會成功。
        SCROLL_SUCCESS(0x4),
        // ？？裝備沒顯示
        UNK_8(0x8),
        // ？？裝備沒顯示
        UNK_10(0x10),
        // ？？裝備沒顯示(但是在永恆魔光盾的裝備痕跡裝備上有出現過)
        UNK_20(0x20),
        // ？？裝備沒顯示
        UNK_40(0x40),
        // 裝備痕跡
        EQUIP_MARK(0x80),
        ;
        private final int value;

        private EnhanctBuff(int value) {
            this.value = value;
        }

        public final short getValue() {
            return (short) value;
        }

        public final boolean check(int flag) {
            return (flag & value) != 0;
        }
    }
}
