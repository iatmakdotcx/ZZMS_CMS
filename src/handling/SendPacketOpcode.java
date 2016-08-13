package handling;

import constants.ServerConfig;
import tools.EncodingDetect;
import tools.FileoutputUtil;
import tools.StringUtil;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public enum SendPacketOpcode implements WritableIntValueHolder {

    //================================
    // CLogin::OnPacket 開始(185-完成)
    //================================ 
    // 密碼驗證
    LOGIN_STATUS(0x00),
    //服务器列表前
    STATSERVERLIST(0x3c),
    // 伺服器選單
    SERVERLIST(0x01),
    //服务器 检测更新
    GET_UNK(0x0022),
    // CLogin::OnLatestConnectedWorld
    ENABLE_RECOMMENDED(0x02),
    // CLogin::OnRecommendWorldMessage
    SEND_RECOMMENDED(0x03),
    
    // 0x04 [Long] * 8

    // 選擇伺服器 [Int(伺服器編號)]
    CHANNEL_SELECTED(0x05),
    // 角色選單
    CHARLIST(0x06),
   
    // 伺服器IP
    SERVER_IP(0x07),
    // 帳號信息
    ACCOUNT_INFO(0x114),
    
    //创建角色验证
    CREATE_VERIFY(0x39),

    // 檢查角色名稱
    CHAR_NAME_RESPONSE(0x0A),
    // 建立角色
    ADD_NEW_CHAR_ENTRY(0x0B),
    // 刪除角色
    DELETE_CHAR_RESPONSE(0x0C),
    
    // 0x0D 【刪除新星世界角色】[Int][Boolean] false => [Long][Long]
    // 0x0E 【登入新星世界伺服器(未確認)】[Int][Byte]
    // 0x0F 【變更角色名稱】[Int][String][Short]
    // 0x10 [Int]

    // 變更頻道
    CHANGE_CHANNEL(0x11),
    // Ping
    PING(0x12),
    
    // 0x13

    // 購物商城
    CS_USE(0x14),
    
    // 建立角色驗證回覆
    CREATE_CHAR_AUTH_RESPONSE(0x1A),

    // 打工系統
    PART_TIME(0x1E),
    
    // 0x1F [-]
    // 0x20 [-]
    // 0x21 [Int][Int]
    // 0x22 【購買道具】[Int][Int][Int][Byte][Byte][Byte]

    // 選擇性別 + 設置第二組密碼
    CHOOSE_GENDER(0x23),
    // 選擇性別 + 設置第二組密碼回覆
    GENDER_SET(0x24),
    // 外掛偵測
    HACKSHIELD_REQUEST(0x25),
    // 強制變更角色名稱 [-]
    FORCED_CHANGE_CHAR_NAME(0x26),
    // 強制變更角色名稱訊息 [Byte]
    FORCED_CHANGE_CHAR_NAME_NOTICE(0x27),
    // 伺服器狀態,选择频道
    SERVERSTATUS(0x35),
    // 背景驗證
    LOGIN_AUTH(0x36),
    
    // 0x2A
    // 0x2B
    // 0x2C [Byte][Byte][Int]
    // 0x2D 【新星世界按鈕 + 刪除角色按鈕】[Int]

    // 顯示視窗
    MAPLESTORY_AUTH(0x2E),
    // 變更名稱申請成功
    NAME_CHANGE_SUCCESS(0x2F),
    // 第二組密碼錯誤
    SECONDPW_ERROR(0x30),
    //================================
    // CMapLoadable::OnPacket 開始(186-完成)
    //================================ 

    // 移除背景層
    REMOVE_BG_LAYER(0x31),
    // 變更背景
    CHANGE_BACKGROUND(0x32),
    // 設置物件狀態
    SET_MAP_OBJECT_VISIBLE(0x33),
    
    // 0x34

    // 重置畫面
    RESET_SCREEN(0x35),
    
    // 0x36
    // 0x37
    // 0x38
    // 0x39
    // 0x3A
    // 0x3B
    // 0x3C
    // 0x3D
    // 0x3E
    // 0x3F

    //================================
    // CWvsContext::OnPacket 開始(186-完成)
    //================================    
    // 道具操作[完成]
    INVENTORY_OPERATION(0x46),
    // 擴充道具欄位[完成]
    INVENTORY_GROW(0x47),
    // 更新能力值[完成]
    UPDATE_STATS(0x48),
    // 獲得輔助效果[完成]
    GIVE_BUFF(0x49),
    // 取消輔助效果[完成]
    CANCEL_BUFF(0x50),
    // 臨時能力值開始[完成]
    TEMP_STATS(0x51),
    // 重置臨時能力值[完成]
    TEMP_STATS_RESET(0x52),
    // 更新技能[完成]
    UPDATE_SKILLS(0x53),
    // 幻影俠盜複製技能成功
    UPDATE_STOLEN_SKILLS(0x54),
    // 幻影俠盜竊取技能時顯示 
    TARGET_SKILL(0x55),
    // 0x4A
    // 0x4B
    // 0x4C
    // 0x4D

    // 偷竊技能檢查(Done)
    STEEL_SKILL_CHECK(0x4E),
    // 0x4F
    // 惡魔追蹤發送
    CONVEY_TO(0x50),
    // 名聲回覆[完成]
    FAME_RESPONSE(0x51),
    // 顯示角色狀態信息[完成]
    SHOW_STATUS_INFO(0x52),
    // 訊息[完成]
    SHOW_NOTES(0x53),
    // 瞬移之石[完成]
    TROCK_LOCATIONS(0x54),
    // 測謊機[完成]
    LIE_DETECTOR(0x55),
    // 炸彈測謊機[完成]
    BOMB_LIE_DETECTOR(0x56),
    // 0x57 (null)

    // 設置舉報[完成]
    REPORT_RESPONSE(0x58),
    // 舉報時間[完成]
    REPORT_TIME(0x59),
    // 舉報狀態[完成]
    REPORT_STATUS(0x5A),
    // 0x5B [Int] [do { [Int][Int] }] [176+]

    // 更新騎寵[完成]
    UPDATE_MOUNT(0x68),
    // 任務完成[完成]
    SHOW_QUEST_COMPLETION(0x5D),
    // 精靈商人[完成]
    SEND_TITLE_BOX(0x5E),
    // 使用技能書[完成]
    USE_SKILL_BOOK(0x5F),
    // 重置SP[完成]
    SP_RESET(0x60),
    // 重置AP[完成]
    AP_RESET(0x61),
    // 經驗瓶[完成]
    EXP_POTION(0x62),
    // 散佈道具[完成] [Byte][Int][Byte]
    DISTRIBUTE_ITEM(0x63),
    // 擴充角色欄位[完成]
    EXPAND_CHARACTER_SLOTS(0x64),
    // 申請變更角色名稱[完成]
    APPLY_NAME_CHANGE(0x65),
    // 向上整理[完成]
    FINISH_SORT(0x66),
    // 種類排序[完成]
    FINISH_GATHER(0x67),
    // 0x68 (null)
    // 0x69 (null)

    // 角色信息[完成]
    CHAR_INFO(0x6A),
    // 隊伍操作[完成]
    PARTY_OPERATION(0x6B),
    // 尋找隊員
    MEMBER_SEARCH(0x6C),
    // 隊伍搜尋
    PARTY_SEARCH(0x6D),
    // 0x6E
    // 0x6F
    // 0x70 [186+]
    // 0x71 [186+]
    // 0x72 (null)

    // 遠征隊操作
    EXPEDITION_OPERATION(0x73),
    // 好友列表[完成]
    BUDDYLIST(0x74),
    // 0x75 [176+]
    // 0x76 [176+]

    // 請求回覆[完成]
    GUILD_REQUEST(0x83),
    // 公會操作[完成]
    GUILD_OPERATION(0x84),
    // 公會聯盟操作[完成]
    ALLIANCE_OPERATION(0x85),
    // 時空門[完成]
    SPAWN_PORTAL(0x86),
    // 開放通道[完成] // 要重抓
    MECH_PORTAL(0x7FFF),
    // 伺服器訊息[完成]
    SERVERMESSAGE(0x87),
    // 阿斯旺海防戰訊息[完成]
    AZWAN_MSG(0x88),
    // 花生機獎勵[完成]
    PIGMI_REWARD(0x89),
    // 獲得道具[完成]
    ITEM_OBTAIN(0x8A),
    // 智慧貓頭鷹[完成]
    OWL_OF_MINERVA(0x8B),
    // 智慧貓頭鷹回覆
    OWL_RESULT(0x8C),
    // 0x81 (null)
    // 0x82 (null)

    // 戒指操作請求[完成]
    ENGAGE_REQUEST(0x83),
    // 戒指操作返回[完成]
    ENGAGE_RESULT(0x84),
    // 結婚禮物[完成]
    WEDDING_GIFT(0x85),
    // 結婚地圖變更[完成] [Int][Int]
    WEDDING_MAP_TRANSFER(0x86),
    // 使用寵物飼料[完成]
    USE_CASH_PET_FOOD(0x87),
    // 使用寵物技能[完成]
    USE_CASH_PET_SKILL(0x88),
    // 0x89 [-]

    // 神秘的鐵砧[完成]
    FUSION_ANVIL(0x8A),
    // 0x8B [Byte][Long]

    // 黃色公告[完成]
    YELLOW_CHAT(0x8C),
    // 商店優惠[完成]
    SHOP_DISCOUNT(0x8D),
    // 捕捉怪物[完成]
    CATCH_MOB(0x8E),
    // 建立玩家Npc[完成] [Byte]
    MAKE_PLAYER_NPC(0x8F),
    // 玩家Npc[完成]
    PLAYER_NPC(0x90),
    // 0x91(null)

    // 隱藏Npc[完成]
    DISABLE_NPC(0x92),
    // 獲得卡片[完成]
    GET_CARD(0x93),
    // 卡片設置[完成] [Int]
    CARD_SET(0x94),
    // 變更小時[完成]
    CHANGE_HOUR(0x95),
    // 重置小地圖[完成]
    RESET_MINIMAP(0x96),
    // 教師更新[完成]
    CONSULT_UPDATE(0x97),
    // 班級更新[完成]
    CLASS_UPDATE(0x98),
    // 網頁瀏覽更新[完成]
    WEB_BOARD_UPDATE(0x99),
    // 擊殺數量[完成]
    SESSION_VALUE(0x9A),
    // 組隊數值[完成]
    PARTY_VALUE(0x9B),
    // 地圖數值[完成]
    MAP_VALUE(0x9C),
    // 0x9D [String][String]

    // 精靈墜飾[完成]
    EXP_BONUS(0x9E),
    // 家族系統[已關閉][完成]
    SEND_PEDIGREE(0x9F),
    OPEN_FAMILY(0xA0),
    FAMILY_MESSAGE(0xA1),
    FAMILY_INVITE(0xA2),
    FAMILY_JUNIOR(0xA3),
    SENIOR_MESSAGE(0xA4),
    FAMILY(0xA5),
    REP_INCREASE(0xA6),
    FAMILY_LOGGEDIN(0xA7),
    FAMILY_BUFF(0xA8),
    FAMILY_USE_REQUEST(0xA9),
    // (公會成員)升級訊息[完成]
    LEVEL_UPDATE(0xAA),
    // 结婚訊息[完成]
    MARRIAGE_UPDATE(0xAB),
    // 轉職訊息[完成]
    JOB_UPDATE(0xAC),
    // 項鍊擴充[完成]
    SLOT_UPDATE(0xAD),
    // 請求跟隨提示[完成]
    FOLLOW_REQUEST(0xAE),
    // 新頂部訊息[完成]
    TOP_MSG2(0xAF),
    // 頂部訊息[完成]
    TOP_MSG(0xB0),
    // 新頂部訊息[完成]
    NEW_TOP_MSG(0xB1),
    // 中間訊息[完成]
    MID_MSG(0xB2),
    // 清理中間的訊息[完成]
    CLEAR_MID_MSG(0xB3),
    // 特殊訊息[完成]
    SPECIAL_MSG(0xB4),
    // 182新增的訊息[完成]
    MAPLE182_ADD_MSG(0xB5),
    // 楓之谷提示訊息[完成]
    MAPLE_TIP_MSG(0xB6),
    // 楓之谷管理員訊息[完成]
    MAPLE_ADMIN_MSG(0xB7),
    // 檢查道具欄位[完成]
    INVENTORY_FULL(0xB8),
    // 更新美洲豹[完成]
    UPDATE_JAGUAR(0xB9),
    // 神之子能力值
    ZERO_STATS(0xBA),
    // 神之子更新
    ZERO_UPDATE(0xBB),
    // 0xBC

    // 冒险家[完成]
    ULTIMATE_EXPLORER(0xBD),
    // 0xBE

    // 能力值信息[完成][hyper]
    SPECIAL_STAT(0xBF),
    // 更新培養皿時間[完成]
    UPDATE_IMP_TIME(0xC0),
    // 使用培養皿[完成]
    ITEM_POT(0xC1),
    // 0xC2
    // 0xC3

    // 武陵道場訊息
    MULUNG_MESSAGE(0xC4),
    // 傳授角色技能
    GIVE_CHARACTER_SKILL(0xC5),
    // 0xC6【00 00】
    // 0xC7
    // 0xC8
    // 0xC9

    // 武陵排行[完成] // 要重抓
    MULUNG_DOJO_RANKING(0xCA),
    // 0xCB

    // 更新潛在能力值
    UPDATE_INNER_ABILITY(0xCC),
    // 0xCD

    // 使用/刪除技能[完成]
    REPLACE_SKILLS(0xCE),
    // 內在能力值訊息[完成]
    INNER_ABILITY_MSG(0xCF),
    // 地圖指引[完成] [Int]
    MINIMAP_ARROW(0xD0),
    // 0xD1 [Boolean][Int] true => [Int]

    // 角色潛在能力設定[完成]
    ENABLE_INNER_ABILITY(0xD2),
    // 角色潛在能力重置
    DISABLE_INNER_ABILITY(0xD3),
    // 獲得名聲值[完成]
    UPDATE_HONOUR(0xD4),
    // 阿斯旺未知[未知]
    AZWAN_UNKNOWN(0xD5),
    // 阿斯旺結果[完成] [Int][Int][Int][Int][Int][Int][Int][Int][Boolean]
    AZWAN_RESULT(0xD6),
    // 阿斯旺擊殺[完成]
    AZWAN_KILLED(0xD7),
    // 0xD8 【point】
    // 0xD9 阿斯旺復活[String(玩家名稱)][Int](復活時間)

    // 能力傳播者
    CIRCULATOR_ON_LEVEL(0xDA),
    // 十字獵人訊息
    SILENT_CRUSADE_MSG(0xDB),
    // 十字獵人商店
    SILENT_CRUSADE_SHOP(0xDC),
    
    // 0xDD 幸運怪物【UI/UIWindow2.img/mapleMuseum】【UI/UIWindow2.img/mapleMuseum2)(UI/UIWindow2.img/luckyMonstery】
    // 0xDE
    // 0xDF 幸運怪物【UI/UIWindow2.img/mapleMuseum】【UI/UIWindow2.img/mapleMuseum2)(UI/UIWindow2.img/luckyMonstery】
    // 0xE0 【state】
    // 0xE1 【state】
    // 0xE2
    
    // 自動飛行 [Int]
    AUTO_FLYING(0xE3),
    // 禁止完成任務
    DISALLOW_DELIVERY_QUEST(0xE4),
    // 0xE5 【彈跳視窗】
    // 0xE6 【賓果活動?】 [Byte] 【deck】【bingo】
    // 0xE7 【賓果活動?】 [Byte] 【deck】【bingo】
    // 0xE8
    // 0xE9 【獲取物品?】【---】

    // 楓葉點數(完成)
    MAPLE_POINT(0xEA),
    // 0xEB [Long]
    // 0xEC 【方塊洗洗樂?】
    // 0xED 【重置神聖SP】 [Byte][Int][Byte]
    // 0xEE 【Debug信息?】 [Short](年)[Short](月)[Short](日)[Short](點)[Short](分)[Int]((DWORD)%u, (LONG)%d)【(DWORD)%u, (LONG)%d】
    // 0xEF 【Debug信息?】 [Int][Long]
    // 0xF0
    // 0xF1 【沒有可以套用回復效果的對象。】【Recv => 0x176([Int][Byte])】
    // 0xF2
    // 0xF3 【Recv => 0xC9([Int][Short][Long])】
    // 0xF4 【Recv => 0xC9([Int][Short][Long])】
    // 0xF5

    // 變成破壞天使
    CHANGE_ANGELIC(0xF6),
    // 解鎖充電技能
    UNLOCK_CHARGE_SKILL(0xF7),
    // 上鎖充電技能
    LOCK_CHARGE_SKILL(0xF8),
    // 自動登入
    AUTO_LOGIN(0xF9),
    // 0xFA 【寵物名牌戒指?】【[BP:%02d] %d】【///////////////////////////////】

    // 進化系統
    EVOLVING_ACTION(0xFB),
    // BossPvP技能【Recv => 0x16D】
    BOSSPVP_SKILL_UI(0xFC),
    
    // 0xFD

    // 公會搜索
    GUILD_SEARCH(0xFE),
    // 0xFF
    // 0x100 【幸運怪物】【UI/UIWindow2.img/mapleMuseum】【UI/UIWindow2.img/mapleMuseum2】【UI/UIWindow2.img/luckyMonstery】【UI/UIWindow2.img/luckyMonResult】
    // 0x101 【獲取物品?】
    // 0x102 【10 00 00 00 00 00 00 00 00 00 00 00 00 00 00】
    // 0x103

    // 請求進程列表
    SYSTEM_PROCESS_LIST(0x104),
    
    // 0x105
    // 0x106
    // 0x107
    // 0x108

    // 情景喇叭訊息
    AVATAR_MEGA_RESULT(0x109),
    // 情景喇叭
    AVATAR_MEGA(0x10A),
    // 移除情景喇叭
    AVATAR_MEGA_REMOVE(0x10B),
    // 活動清單
    EVENT_LIST(0x119),
    // 楓之谷聊天室
    MESSENGER_OPEN(0x10D),
    // 0x10E 【簽名】
    // 0x10F 【問候玩家】
    
    // 王冠活動
    EVENT_CROWN(0x11D),
    
    // 0x111 [Byte][Byte]

    // 自由轉職
    FREE_CHANGE_JOB(0x112),
    
    // 0x113
    // 0x114
    // 0x115
    // 0x116
    // 0x117
    // 0x118
    // 0x119
    // 0x11A
    // 0x11B
    // 0x11C 【頂部訊息】
    // 0x11D 【IP驗證】
    // 0x11E 【Etc/CashPackage.img/%d/SN】
    // 0x11F 【獸魔激鬥擂台賽】
    // 0x120 【UI/UIWindowBT.img/MonsterBattleSelection/num】
    // 0x121 【任務抵達】
    // 0x122
    // 0x123
    // 0x124
    // 0x125
    // 0x126

    // 開啟墜飾欄(175+)
    UPDATE_PENDANT_SLOT(0x127),
    // 魔王競技場配對成功
    BOSSPVP_FOUND(0x128),
    // 魔王競技場配對失敗
    BOSSPVP_FAIL(0x129),
    // 參加魔王競技場配對
    BOSSPVP_SEARCH(0x12A),
    // 0x12B
    // 0x12C
    // 0x12D
    // 0x12E
    // 0x12F 【00】
    // 0x130 【伺服器移民】
    // 0x131 【擴充倉庫欄位】
    // 0x132

    // 菁英王訊息
    ELITE_BOSS_NOTICE(0x133),
    
    // 0x134 【00 00 00 00 00 00 00 00 00】

    // 咒文的痕跡
    EQUIPMENT_ENCHANT(0x135),
    // The Seed 排行
    TOWER_OF_OZ_RANKING(0x136),
    // The Seed 好友排行
    TOWER_OF_OZ_FRIEND_RANKING(0x137),
    // The Seed 獎勵 [Int](樓層)[Int](時間)[Int](The Seed點數)[Int](獲得經驗值)[Int][Int]
    TOWER_OF_OZ_REWARD(0x138),
    // 0x139 [Int][Int][Byte]
    // 0x13A
    // 0x13B
    // 0x13C
    // 0x13D
    // 0x13E

    // 離開遊戲
    EXIT_GAME(0x13F),
    // 火步行[未知]
    FIRE_STEP(0x140),
    
    // 0x141
    // 0x142
    // 0x143
    // 0x144 【臉部情緒?】【Unlock request Failed】
    // 0x145 【Recv => [Byte][Int][String][Byte]】
    // 0x146 【Item/Cash/0501.img/%08d/effect】
    // 0x147
    // 0x148
    // 0x149
    // 0x14A
    // 0x14B
    // 0x14C 【彈跳視窗】
    
    // 申請變更角色名稱
    NAME_CHANGE(0x14D),
    // 雪橇活動【UI/Sboating.img/Basic/backgrnd】
    SELECT_SLEIGH(0x14E),
    // 潘姆音樂
    PAM_SONG(0x14F),
    // 餽贈認證
    MAPLE_FEED_AUTHEN(0x150),
    // 速配指數
    QUICK_PAIR_RESULT(0x151),
    
    // 0x152
    // 0x153
    // 0x154
    
    // 贈送小鋼珠
    GIFTS_BALL(0x155),
    // 九龍珠
    DRAGON_BALL(0x156),
    // 開啟寶箱
    TREASURE_BOX(0x157), //[Int] [0:金、1:銀、5:神秘開罐器、6:幻想開罐器]
    // 0x158
    // 0x159 【釣魚系統】
    // 0x15A
    // 0x15B 【周圍沒有攻擊的怪物。】、【無法連續使用.】

    // 管理員警告[完成] [GMKiKi][HACK]
    GM_POLICE(0x15C),    
    // 新年賀卡[完成]
    NEW_YEAR_CARD(0x15D),
    // 隨機變身藥水[完成]
    RANDOM_MORPH(0x15E),
    // 個性文字[完成](5480000)
    DISPOSITION_TEXT(0x15F),
    // 經驗值椅子[完成]
    CHAIR_EXP_MSG(0x160),
    // 0x161    
    // 0x162

    // 變更頻道 + 訊息[完成]
    AUTO_CC_MSG(0x163),
    // 0x164
    // 0x165
    // 0x166
    // 0x167 [Int][Int]
    // 0x168
    // 0x169
    // 0x16A

    // 獲得獎勵[完成]
    REWARD(0x16B),
    // 0x16C
    // 0x16D

    // 閃炫方塊回覆
    SHIMMER_CUBE_RESPONSE(0x16E),
    
    // 0x16F  178新增
    // 0x170 【任務抵達】
    // 0x171
    // 0x172 【購物商城 => [B0 1A 25 00 00 00 00 00 00 80 59 DA 6B 2E CE 01 80 29 A5 02 EC 33 CE 01 18 00 00 00 98 A3 98 00 99 A3 98 00 9A A3 98 00 9B A3 98 00 24 52 A6 00 25 52 A6 00 76 6F 40 01 77 6F 40 01 78 6F 40 01 79 6F 40 01 20 4A CB 01 22 4A CB 01 2E 4A CB 01 2F 4A CB 01 30 4A CB 01 4E 4A CB 01 4F 4A CB 01 50 4A CB 01 55 4A CB 01 6C 4A CB 01 6D 4A CB 01 6E 4A CB 01 6F 4A CB 01 70 4A CB 01]
    // 0x173 【[BlackListLoadDone] [BlackSize:%d] [sTargetIGNs:%s]】
    // 0x174 【神奇剪刀】
    // 0x175
    // 0x176 【組隊任務?】
    // 0x177 
    // 0x178 【輸入觀戰板內容】【Recv => 0x95】
    // 0x179 【UI/UIMiniGame.img/starPlanetResult/backgrnd】
    // 0x17A 【新星世界試穿衣服】
    // 0x17B 【等待列表】
    // 0x17C
    // 0x17D
    // 0x17E
    // 0x17F 178新增
    // 0x180 178新增
    // 0x181 178新增
    // 0x182 178新增
    // 0x183 178新增
    // 0x184 178新增
    // 0x185 178新增    
    // 0x186
    // 0x187 179新增 
    // 0x188 179新增
    // 0x189 179新增

    // 咒文的痕跡(FOREVER_TIME)
    FEVER_TIME(0x18A),
    
    // 0x18B
    // 0x18C
    // 0x18D
    // 0x18E
    // 0x18F
    // 0x190
    // 0x191
    // 0x192
    // 0x193
    // 0x194
    // 0x195
    // 0x196
    // 0x197
    // 0x198
    // 0x199
    // 0x19A
    // 0x19B
    
    // 技能組合[完成]
    SKILL_MACRO(0x19C),
    //================================
    // CStage::OnPacket 開始(186-完成)
    //================================ 

    // 地圖傳送[完成]
    WARP_TO_MAP(0x1B4),
    // 農場[關閉]
    FARM_OPEN(0x19E),
    // 拍賣系統[完成]
    MTS_OPEN(0x19F),
    // 購物商城[完成]
    CS_OPEN(0x1A0),
    // 購物商城信息[完成]
    CS_INFO(0x1A1),
    CASH_SHOP(0x7FFE),
    //================================
    // CField::OnPacket 開始(186-完成)
    //================================ 

    // 地圖阻擋[完成]
    MAP_BLOCKED(0x1A2),
    // 伺服器阻擋[完成]
    SERVER_BLOCKED(0x1A3),
    // 隊伍阻擋[完成]
    PARTY_BLOCKED(0x1A4),
    // 裝備效果[完成]
    SHOW_EQUIP_EFFECT(0x1A5),
    // 組隊公會聊天
    MULTICHAT(0x1A6),
    // 世界聊天模式[完成]
    WORLD_MULTICHAT(0x1A7),
    // 悄悄話
    WHISPER(0x1A8),
    // 夫妻聊天
    SPOUSE_CHAT(0x1A9),
    // Boss血條[完成]
    BOSS_ENV(0x1AA),
    // 地圖效果[完成]
    MAP_EFFECT(0x1AB),
    // 祝賀音樂(5100000)[完成]
    CASH_SONG(0x1AC),
    // GM效果[完成]
    GM_EFFECT(0x1AD),
    // GM日誌[完成]
    GM_LOG(0x1AE),
    // 選邊站[完成]
    OX_QUIZ(0x1AF),
    // GM活動說明[完成]
    GMEVENT_INSTRUCTIONS(0x1B0),
    // 計時器[完成]
    CLOCK(0x1B1),
    //================================
    // CField_ContiMove::OnPacket 開始(186-完成)
    //================================ 

    // 船隻移動[完成]
    BOAT_MOVE(0x1B2),
    // 船隻狀態[完成]
    BOAT_STATE(0x1B3),
    //================================
    // CField_ContiMove::OnPacket 結束
    //================================ 

    // 0x1B4
    // 0x1B5
    // 0x1B6

    // 停止計時[完成]
    STOP_CLOCK(0x1B7),
    //================================
    //CField_AriantArena::OnPacket（CField_AriantArena::OnShowResult） 開始(186-完成)
    //================================ 
    // 納希競技大會分數[完成]
    ARIANT_SCOREBOARD(0x1B8),
    //================================
    // CField_AriantArena::OnPacket（CField_AriantArena::OnShowResult） 結束
    //================================ 
    // 金字塔更新[完成]
    PYRAMID_UPDATE(0x1B9),
    //================================
    // CField_KillCount::OnPacket 開始(186-完成)
    //================================ 
    // 金字塔擊殺數量[完成]
    PYRAMID_KILL_COUNT(0x1BA),
    //================================
    // CField_KillCount::OnPacket 結束
    //================================ 
    // 金字塔分數[完成]
    PYRAMID_RESULT(0x1BB),
    // 快速按鍵[完成]
    QUICK_SLOT(0x1BC),
    
    // 0x1BD
    
    // 移動平臺[完成]
    MOVE_PLATFORM(0x1BE),
    
    // 0x1BF 
    // 0x1C0
    // 0x1C1
    // 0x1C2
    // 0x1C3
    // 0x1C4
    // 0x1C5
    // 0x1C6

    // PvP信息[完成]
    PVP_INFO(0x1C7),
    // 角色站立方向狀態[完成]
    DIRECTION_STATUS(0x1C8),
    // 惡魔之力[完成]
    GAIN_FORCE(0x1C9),
    // 組隊任務達成率[完成]
    ACHIEVEMENT_RATIO(0x1CA),
    // 快速移動[完成]
    QUICK_MOVE(0x1CB),
    
    // 0x1CC
    // 招喚障礙物[完成]
    SPAWN_OBTACLE_ATOM(0x1CD),
    // 0x1CE
    // 0x1CF
    // 0x1D0 // 副本BOSS技能特效
    // 0x1D1
    // 0x1D2 // 副本BOSS技能特效
    // 0x1D3
    // 0x1D4
    // 0x1D5
    // 0x1D6
    // 0x1D7
    // 0x1D8 // 帳號保護
    // 0x1D9
    // 0x1DA
    // 0x1DB
    // 0x1DC
    // 0x1DD // 完成造型王預賽參加申請
    // 0x1DE // 菁英怪物
    // 0x1DF
    // 0x1E0
    // 0x1E1
    // 0x1E2
    // 0x1E3
    // 0x1E4
    // 0x1E5
    // 0x1E6
    // 0x1E7
    // 0x1E8
    // 0x1E9
    // 0x1EA
    // 0x1EB
    // 0x1EC
    // 0x1ED

    //================================
    // CUserPool::OnPacket 開始(186-完成)
    //================================ 

    // 召喚玩家[完成]
    SPAWN_PLAYER(0x1EE),
    // 移除玩家[完成]
    REMOVE_PLAYER_FROM_MAP(0x1EF),
    //================================
    // CUserPool::OnUserCommonPacket 開始(186-完成)
    //================================ 

    // 普通聊天[完成]
    CHATTEXT(0x1F0),
    // 黑板[完成]
    CHALKBOARD(0x1F1),
    // 更新玩家[完成]
    UPDATE_CHAR_BOX(0x1F2),
    // 消費效果[未知]
    SHOW_CONSUME_EFFECT(0x1F3),
    // 使用卷軸效果[完成]
    SHOW_SCROLL_EFFECT(0x1F4),
    
    // 0x1F5 (null)

    // 咒文的痕跡[完成]
    SHOW_ENCHANTER_EFFECT(0x1F6),
    // 使用魂之珠[完成]
    SHOW_SOULSCROLL_EFFECT(0x1F7),
    // 放大鏡效果[完成]
    SHOW_MAGNIFYING_EFFECT(0x1F8),
    // 擴充潛能欄位
    SHOW_POTENTIAL_EXPANSION(0x1F9),
    // 潛能重置效果
    SHOW_POTENTIAL_RESET(0x1FA),
    // 重新設置潛能效果
    SHOW_BONUS_POTENTIAL_RESET(0x1FB),
    
    // 0x1FC 使用幸運卷?

    // 顯示煙花效果
    SHOW_FIREWORKS_EFFECT(0x1FD),
    
    // 0x1FE
    // 0x1FF
    // 0x200

    // 顯示星岩效果
    SHOW_NEBULITE_EFFECT(0x201),
    // 顯示合成效果
    SHOW_FUSION_EFFECT(0x202),
    // PvP攻擊
    PVP_ATTACK(0x203),
    // PvP煙霧[完成] find:[ invenom ]
    PVP_MIST(0x204),
    
    // 0x205

    // PvP冷卻時間
    PVP_COOL(0x206),
    // 磁場技能
    TESLA_TRIANGLE(0x207),
    
    // 0x208

    // 跟隨效果
    FOLLOW_EFFECT(0x209),
    // 顯示組隊任務獎勵[完成]
    SHOW_PQ_REWARD(0x20A),
    // 工藝效果
    CRAFT_EFFECT(0x20B),
    // 工藝完成
    CRAFT_COMPLETE(0x20C),
    // 採集結束特效
    HARVESTED_EFFECT(0x20D),
    // 採集結束
    HARVESTED(0x20E),
    
    // 0x20F

    // 玩家傷害
    PLAYER_DAMAGED(0x210),
    // 奈特的金字塔
    NETT_PYRAMID(0x211),
    // 設定特效
    SET_PHASE(0x212),
    
    // 0x213
    // 0x214
    // 0x215
    // 0x216
    // 0x217

    // 潘姆音樂
    PAMS_SONG(0x218),
    // 取消椅子
    CANCEL_CHAIR(0x219),
    
    // 0x21A
    // 0x21B
    // 0x21C

    // 攻擊Skin[完成]
    SHOW_DAMAGE_SKIN(0x21D),
    
    // 0x21E
    // 0x21F
    // 0x220
    // 0x221
    
    // 裝備特效開關
    EFFECT_SWITCH(0x222),
    
    // 0x223
    // 0x224
    // 0x225
    // 0x226
    // 0x227
    // 0x228
    // 0x229
    // 0x22A
    // 0x22B
    // 0x22C
    // 0x22D
    // 0x22E
    // 0x22F
    // 0x230
    // 0x231
    // 0x232
    // 0x233
    // 0x234
    // 0x235

    //================================
    // CUserPool::OnUserPetPacket 開始(186-完成)
    //================================ 

    // 召喚寵物[完成]
    SPAWN_PET(0x236),
    // 寵物移動[完成]
    MOVE_PET(0x237),
    // 寵物說話[完成]
    PET_CHAT(0x238),
    // 變更寵物名稱
    PET_NAMECHANGE(0x239),
    
    // 0x23A

    // 寵物例外清單[完成]
    PET_EXCEPTION_LIST(0x23B),
    // 寵物顏色[完成]
    PET_COLOR(0x23C),
    // 寵物大小[完成]
    PET_SIZE(0x23D),
    
    // 0x23E

    // 顯示寵物[完成]
    SHOW_PET(0x23F),
    // 寵物指令[完成]
    PET_COMMAND(0x240),
    //================================
    // CUserPool::OnUserDragonPacket 開始(186-完成)
    //================================ 

    // 召喚龍神[完成] 
    DRAGON_SPAWN(0x241),
    // 龍神移動[完成]
    DRAGON_MOVE(0x242),
    // 移除龍神[完成]
    DRAGON_REMOVE(0x243),
    //================================
    // CUserPool::OnUserAndroidPacket 開始(186-完成)
    //================================ 

    // 召喚機器人[完成]
    ANDROID_SPAWN(0x244),
    // 機器人移動[完成]
    ANDROID_MOVE(0x245),
    // 機器人情緒[完成]
    ANDROID_EMOTION(0x246),
    // 更新機器人外觀[完成]
    ANDROID_UPDATE(0x247),
    // 移除機器人[完成]
    ANDROID_DEACTIVATED(0x248),
    //================================
    // CUserPool::OnUserHakuPacket1 開始(186-完成)
    //================================ 

    // 變更花弧
    HAKU_CHANGE_1(0x249),
    
    // 0x24A

    // 花狐使用技能後發的(Done)
    HAKU_USE_BUFF(0x24B),
    // 變更花弧
    HAKU_CHANGE_0(0x24C),
    
    // 0x24D

    // 花弧未知
    HAKU_UNK(0x24E),
    //================================
    // CUserPool::OnUserHakuPacket2 開始(186-完成)
    //================================ 

    // 0x24F

    //花狐移動
    HAKU_MOVE(0x250),
    //花狐更新
    HAKU_UPDATE(0x251),
    //變更花狐
    HAKU_CHANGE(0x252),
    
    // 0x253[null]
    // 0x254[null]

    //召喚花狐[完成]
    SPAWN_HAKU(0x255),
    
    // 0x256
    // 0x257

    //================================
    // CUserPool::OnUserRemotePacket 開始(186-完成)
    //================================ 

    // 玩家移動[完成]
    MOVE_PLAYER(0x258),
    // 近距離攻擊[完成]
    CLOSE_RANGE_ATTACK(0x259),
    // 遠距離攻擊[完成]
    RANGED_ATTACK(0x25A),
    // 魔法攻擊[完成]
    MAGIC_ATTACK(0x25B),
    // 被動攻擊[完成]
    PASSIVE_ATTACK(0x25C),
    //技能效果[完成][用主教的创世之破抓到包]
    SKILL_EFFECT(0x25D),
    //移動攻擊[完成][33121214 - 狂野機關砲]
    MOVE_ATTACK(0x25E),
    //取消技能效果[完成]
    CANCEL_SKILL_EFFECT(0x25F),
    //玩家受到傷害[完成]
    DAMAGE_PLAYER(0x260),
    //玩家面部表情[完成]
    FACIAL_EXPRESSION(0x261),
    // 0x262

    //顯示物品效果
    SHOW_EFFECT(0x263),
    //顯示頭上稱號
    SHOW_TITLE(0x264),
    //天使破壞者變更
    ANGELIC_CHANGE(0x265),
    // 0x266
    // 0x267
    // 0x268

    //顯示椅子效果[完成]
    SHOW_CHAIR(0x269),
    //更新玩家外觀[完成]
    UPDATE_CHAR_LOOK(0x26A),
    //玩家外觀效果[完成]
    SHOW_FOREIGN_EFFECT(0x26B),
    //獲得異常狀態[完成]
    GIVE_FOREIGN_BUFF(0x26C),
    //取消異常狀態
    CANCEL_FOREIGN_BUFF(0x26D),
    //更新隊員血量
    UPDATE_PARTYMEMBER_HP(0x26E),
    //讀取公會名稱[完成]
    LOAD_GUILD_NAME(0x26F),
    //讀取公會標誌[完成]
    LOAD_GUILD_ICON(0x270),
    //讀取隊伍(Done)
    LOAD_TEAM(0x271),
    //採集
    SHOW_HARVEST(0x272),
    //PvP血量
    PVP_HP(0x273),
    // 0x274
    // 0x275
    // 0x276
    // 0x277
    // 0x278
    // 0x279
    // 0x27A
    // 0x27B
    // 0x27C
    // 0x27D

    //神之子狀態
    ZERO_MUITTAG(0x27E),
    // 0x27F
    // 0x280
    // 0x281
    // 0x282 [NULL]
    // 0x283
    // 0x284
    // 0x285
    // 0x286
    // 0x287
    // 0x288

    //================================
    // CUserPool::OnUserLocalPacket 開始(186-完成)
    //================================ 

    // 動畫表情[完成]
    DIRECTION_FACIAL_EXPRESSION(0x289),
    // 畫面移動
    MOVE_SCREEN(0x28A),
    // 顯示物品效果[完成]
    SHOW_SPECIAL_EFFECT(0x28B),
    // 角色地圖瞬移 武陵道場會出現
    CURRENT_MAP_WARP(0x28C),
    
    // 0x28D

    // 使用福包成功(5200000)[完成]
    MESOBAG_SUCCESS(0x28E),
    // 使用福包失敗(5200000)[完成]
    MESOBAG_FAILURE(0x28F),
    // 更新任務信息
    UPDATE_QUEST_INFO(0x290),
    // 血量減少
    HP_DECREASE(0x291),
    // 變更寵物技能[完成]
    PET_FLAG_CHANGE(0x292),
    // 玩家提示[完成]
    PLAYER_HINT(0x293),
    // 播放事件音效
    PLAY_EVENT_SOUND(0x294),
    // 播放迷你遊戲音效
    PLAY_MINIGAME_SOUND(0x295),
    // 生產用技能
    MAKER_SKILL(0x296),
    
    // 0x297 (null)
    // 0x298

    // 開啟介面[完成]
    OPEN_UI(0x299),
    
    // 0x29A

    // 開啟選項介面[完成]
    OPEN_UI_OPTION(0x29B),
    
    // 0x29C

    // 鎖定玩家按鍵動作[完成]
    LOCK_KEY(0x29D),
    // 劇情鎖定介面[完成]
    LOCK_UI(0x29E),
    // 不顯示其他玩家[完成]
    DISABLE_OTHERS(0x29F),
    // 召喚初心者幫手[完成]
    SUMMON_HINT(0x2A0),
    // 初心者幫手訊息[完成]
    SUMMON_HINT_MSG(0x2A1),
    
    // 0x2A2
    // 0x2A3
    // 0x2A4

    // 狂狼勇士連擊[完成]
    ARAN_COMBO(0x2A5),
    // 狂狼勇士鬥氣重生[完成]
    ARAN_COMBO_RECHARGE(0x2A6),
    
    // 0x2A7
    // 0x2A8

    // 公告提示[完成]
    GAME_MSG(0x2A9),
    // 遊戲訊息[完成]
    GAME_MESSAGE(0x2AA),
    
    // 0x2AB [String][Int]

    //
    BUFF_ZONE_EFFECT(0x2AC),
    //
    DAMAGE_METER(0x2AD),
    // 炸彈攻擊
    TIME_BOMB_ATTACK(0x2AE),
    // 跟隨移動
    FOLLOW_MOVE(0x2AF),
    // 跟隨訊息
    FOLLOW_MSG(0x2B0),
    
    //　0x2B1

    // 建立終極冒險家
    CREATE_ULTIMATE(0x2B2),
    // 採集訊息
    HARVEST_MESSAGE(0x2B3),
    // 符文介面
    RUNE_ACTION(0x2B4),
    // 礦物背包
    OPEN_BAG(0x2B5),
    // 龍之氣息
    DRAGON_BLINK(0x2B6),
    // PvP冰騎士
    PVP_ICEGAGE(0x2B7),
    // 位置信息[完成]
    DIRECTION_INFO(0x2B8),
    // 重新獲得勳章
    REISSUE_MEDAL(0x2B9),
    
    // 0x2BA
    // 0x2BB [Int]

    // 動畫播放[完成]
    PLAY_MOVIE(0x2BC),
    // 蛋糕 vs 派餅 活動
    CAKE_VS_PIE_MSG(0x2BD),
    // 幻影俠盜卡片[完成]
    PHANTOM_CARD(0x2BE),
    
    // 0x2BF [Int]
    // 0x2C0 [Int][Byte]

    // 夜光連擊
    LUMINOUS_COMBO(0x2C1),
    
    // 0x2C2
    // Aggressive排名[完成]
    AGGRESSIVE_RANKING(0x2C3),
    // 0x2C4
    // 0x2C5
    // 0x2C6
    // 0x2C7
    // 0x2C8
    // 0x2C9
    // 0x2CA
    // 0x2CB
    // 0x2CC
    // 0x2CD
    // 0x2CE
    // 0x2CF
    
    // 死亡視窗
    DEATH_TIP(0x2D0),
    
    // 0x2D1
    // 0x2D2
    // 0x2D3

    // 時間膠囊[完成] (3010587)
    TIME_CAPSULE(0x2D4),
    
    // 0x2D5
    // 0x2D6
    // 0x2D7
    // 0x2D8

    // 神之子衝擊波[完成] (101000102)
    ZERO_SHOCKWAVE(0x2D9),
    // 設定槍的名稱[完成]
    SET_GUN_NAME(0x2DA),
    // 設定槍彈[完成]
    SET_GUN_AMMO(0x2DB),
    // 建立槍[完成]
    CREATE_GUN(0x2DC),
    // 清除槍[完成]
    CLEAR_GUN(0x2DD),
    
    // 0x2DE
    // 0x2DF // 進入副本錯誤訊息
    // 0x2E0
    // 0x2E1
    // 0x2E2
    // 0x2E3

    // 戰鬥回復[完成] (101110205)
    ZERO_BATTLE_HEAL(0x2E4),
    
    // 0x2E5
    // 0x2E6
    // 0x2E7
    // 0x2E8
    // 0x2E9
    // 0x2EA
    // 0x2EB
    // 0x2EC
    // 0x2ED
    // 0x2EE

    // 神之子參數(Done)
    ZERO_OPTION(0x2EF),
    // 翻轉硬幣(Done)
    FLIP_THE_COIN(0x2F0),
    
    // 0x2ED
    // 0x2EF
    // 0x2F0
    // 0x2F1

    //幽靈水彩特效(Done) (skill == 80001408)
    GHOST_WATERCOLOR_EFFECT(0x2F5),
    
    // 0x2F6 [186+]
    
    //符文特效(Done) (80001429)
    RUNE_EFFECT(0x2F7),
    
    // 0x2F8
    // 0x2F9
    // 0x2FA
    // 0x2FB
    // 0x2FC
    // 0x2FD
    // 0x2FE
    // 0x2FF
    // 0x300
    // 0x301
    // 0x302
    // 0x303
    // 0x304

    // Setp GiftID[完成]
    SETP_GIFT_ID(0x304),
    
    // 0x305

    // Step Coin[完成]
    SETP_COIN(0x306),
    //凱撒快速鍵[完成]
    KAISER_QUICK_KEY(0x307),
    
    // 0x308
    // 0x309
    // 0x30A
    // 0x30B
    // 0x30C
    // 0x30D
    // 0x30E (42100007)(61111008)
    // 0x30F
    // 0x310 (透過隨機楓幣袋子，得到 %d 楓幣。)
    // 0x311 (楓幣一個也沒有出現。)
    // 0x312
    // 0x313
    // 0x314 (小鋼珠儲值完畢！)
    // 0x315 (小鋼珠儲值失敗！)
    // 0x316
    // 0x317
    // 0x318
    // 0x319
    // 0x31A
    // 0x31B
    // 0x31C
    // 0x31D
    // 0x31E
    // 0x31F (邀請失敗或無法騎乘的區域。)
    // 0x320 (邀請失敗或無法騎乘的區域。)
    // 0x321
    // 0x322
    // 0x323
    // 0x324
    // 0x325
    // 0x326 (請將消耗欄位空出一格以上後再試一次)

    //閃耀方塊反饋 (CField頂端內容有/12的用xRef回找)
    FLASH_CUBE_RESPONSE(0x327),
    
    // 0x328 (flag)(deck)(bingo)
    // 0x329 (今天沒有可使用的免費智慧女神的貓頭鷹)
    // 0x32A
    // 0x32B
    // 0x32C
    // 0x32D
    // 0x32E
    // 0x32F (已將天燈升上天空了。請明天再試)
    // 0x330
    // 0x331
    // 0x332
    // 0x333
    // 0x334
    // 0x335
    // 0x336
    // 0x337
    // 0x338
    // 0x339
    // 0x33A
    // 0x33B
    // 0x33C
    // 0x33D
    // 0x33E
    // 0x33F
    // 0x340
    // 0x341
    // 0x342
    // 0x343
    // 0x344
    // 0x345
    // 0x346
    // 0x347
    // 0x348
    // 0x349
    // 0x34A
    // 0x34B
    // 0x34C
    // 0x34D
    // 0x34E
    
    // 寒冰迅移[完成]
    SPAWN_SPECIAL(0x34F),
    
    // 0x350

    // 技能冷卻[完成]
    COOLDOWN(0x351),
    
    // 0x352

    //================================
    // CUser::OnSummonedPacket 開始(186-完成)
    //================================ 

    // 召喚召喚獸[完成]
    SPAWN_SUMMON(0x353),
    // 移除召喚獸[完成]
    REMOVE_SUMMON(0x354),
    // 召喚獸移動[完成]
    MOVE_SUMMON(0x355),
    // 召喚獸攻擊[完成]
    SUMMON_ATTACK(0x356),
    // PvP召喚獸
    PVP_SUMMON(0x357),
    // 召喚獸技能
    SUMMON_SKILL_2(0x358),
    // 召喚獸技能
    SUMMON_SKILL(0x359),
    // 召喚獸延遲
    SUMMON_DELAY(0x35A),
    // 召喚獸受傷
    DAMAGE_SUMMON(0x35B),
    
    // 0x35C
    // 0x35D
    // 0x35E
    // 0x35F
    // 0x360
    // 0x361

    //================================
    // CMobPool::OnMobPacket 開始(186-完成)
    //================================ 

    // 怪物召喚[完成]
    SPAWN_MONSTER(0x362),
    // 殺除怪物[完成]
    KILL_MONSTER(0x363),
    // 控制召喚怪物[完成]
    SPAWN_MONSTER_CONTROL(0x364),
    
    // 0x365 [Int(MOB_ID)][Short][Int][Byte]

    // 怪物移動[完成]
    MOVE_MONSTER(0x368),
    // 怪物移動回覆[完成]
    MOVE_MONSTER_RESPONSE(0x369),
    
    // 0x368
    // 0x369
    // 0x36A (NULL)

    // 添加怪物状态[完成]
    APPLY_MONSTER_STATUS(0x36B),
    // 取消怪物状态[完成]
    CANCEL_MONSTER_STATUS(0x36C),
    // 怪物暫停重置[完成]
    MONSTER_SUSPEND_RESET(0x36D),
    // 影響怪物[完成]
    MONSTER_AFFECTED(0x36E),
    // 怪物受到傷害
    DAMAGE_MONSTER(0x36F),
    // 怪物技能特效[完成]
    SKILL_EFFECT_MOB(0x370),
    
    // 0x371 (null)

    // 怪物CRC[完成] 接收=> 0x156
    MONSTER_CRC_CHANGE(0x372),
    // 顯示怪物HP[完成]
    SHOW_MONSTER_HP(0x373),
    // 捕捉怪物[完成]
    CATCH_MONSTER(0x374),
    // 怪物物品特效[完成]
    ITEM_EFFECT_MOB(0x375),
    
    // 0x376

    // 怪物說話[完成]
    TALK_MONSTER(0x377),
    // 移除怪物說話
    REMOVE_TALK_MONSTER(0x378),
    // 怪物技能延遲[完成]
    MONSTER_SKILL_DELAY(0x379),
    // 怪物護送全部路徑[完成]
    MONSTER_ESCORT_FULL_PATH(0x37A),
    // 怪物護送暫停/停止允許[完成]
    MONSTER_ESCORT_STOP_END_PERMISSION(0x37B),
    // 怪物護送暫停/停止允許[完成]
    MONSTER_ESCORT_STOP_END_PERMISSION2(0x37C),
    // 怪物護送暫停說話[完成]
    MONSTER_ESCORT_STOP_SAY(0x37D),
    // 怪物護送返回前[完成]
    MONSTER_ESCORT_RETURN_BEFORE(0x37E),
    // 怪物下個攻擊[完成]
    MONSTER_NEXT_ATTACK(0x37F),
    
    // 0x380
    // 0x381
    // 0x382
    // 0x383
    // 0x384
    // 0x385
    // 0x386
    // 0x387
    // 0x388
    // 0x389
    // 0x38A
    // 0x38B
    // 0x38C
    // 0x38D
    // 0x38E
    // 0x38F
    // 0x390
    // 0x391
    // 0x392
    // 0x393
    // 0x394
    // 0x395
    // 0x396
    // 0x397
    // 0x398
    
    // 怪物攻擊怪物[完成]
    MOB_TO_MOB_DAMAGE(0x399),
    
    // 0x39A (null)    

    //================================
    // CMobPool::OnAzwanMobPacket 開始(186-完成)
    //================================ 
    
    // 阿斯旺怪物攻擊怪物[完成]
    AZWAN_MOB_TO_MOB_DAMAGE(0x39F),
    // 阿斯旺怪物召喚[完成]
    AZWAN_SPAWN_MONSTER(0x3A0),
    // 阿斯旺怪物死亡[完成]
    AZWAN_KILL_MONSTER(0x3A1),
    // 阿斯旺控制召喚怪物[完成]
    AZWAN_SPAWN_MONSTER_CONTROL(0x3A2),
    //================================
    // CNpcPool::OnPacket 開始(186-完成)
    //================================ 

    // 召喚Npc[完成]
    SPAWN_NPC(0x3A3),
    // 移除Npc[完成]
    REMOVE_NPC(0x3A4),
    
    // 0x3A5

    // 控制召喚Npc[完成]
    SPAWN_NPC_REQUEST_CONTROLLER(0x3A6),
    // Npc動作[完成]
    NPC_ACTION(0x3A7),
    
    // 0x3A8
    // 0x3A9
    // 0x3AA

    // 更新Npc狀態信息
    NPC_UPDATE_LIMITED_INFO(0x3AB),
    
    // 0x3AC
    // 0x3AD
    // 0x3AE
    // 0x3AF
    // 0x3B0
    // 0x3B1
    
    // 重置Npc動作
    RESET_NPC(0x3B2),
    
    // 0x3B3
    // 0x3B4
    // 0x3B5
    // 0x3B6

    // Npc特殊事件[完成]
    NPC_SET_SPECIAL_ACTION(0x3B7),
    // 設置Npc腳本[完成]
    NPC_SCRIPTABLE(0x3B8),
    
    // 0x3B9

    //================================
    // CEmployeePool::OnPacket 開始(186-完成)
    //================================ 

    // 召喚精靈商人
    SPAWN_HIRED_MERCHANT(0x3BA),
    // 移除精靈商人
    DESTROY_HIRED_MERCHANT(0x3BB),
    
    // 0x3BC

    // 精靈商人更新
    UPDATE_HIRED_MERCHANT(0x3BD),
    //================================
    // CDropPool::OnPacket 開始(186-完成)
    //================================ 

    // 物品掉落[完成]
    DROP_ITEM_FROM_MAPOBJECT(0x3BE),
    
    // 0x3BF (null)

    // 物品消失[完成]
    REMOVE_ITEM_FROM_MAP(0x3C0),
    //================================
    // CMessageBoxPool::OnPacket 開始(186-完成)
    //================================ 

    // 召喚風箏錯誤[完成]
    SPAWN_KITE_ERROR(0x3C1),
    // 召喚風箏[完成]
    SPAWN_KITE(0x3C2),
    // 移除風箏[完成]
    DESTROY_KITE(0x3C3),
    //================================
    // CAffectedAreaPool::OnPacket 開始(186-完成)
    //================================ 

    // 召喚煙霧[完成]
    SPAWN_MIST(0x3C4),
    // 煙霧未知[完成]
    MIST_UNK(0x3C5),
    // 移除煙霧[完成]
    REMOVE_MIST(0x3C6),
    //================================
    // CTownPortalPool::OnPacket 開始(186-完成)
    //================================ 

    // 時空門[完成]
    SPAWN_DOOR(0x3C7),
    // 移除時空門[完成]
    REMOVE_DOOR(0x3C8),
    //================================
    // C182AddUnkPool::OnPacket 開始(186-完成)
    //================================ 

    // 0x3C9
    // 0x3CA
    // 0x3CB

    //================================
    // COpenGatePool::OnPacket 開始(186-完成)
    //================================ 

    // 召喚開放通道[完成]
    MECH_DOOR_SPAWN(0x3CC),
    // 移除開放通道[完成]
    MECH_DOOR_REMOVE(0x3CD),
    
    // 0x3CE [186+]
    
    //================================
    // CReactorPool::OnPacket 開始(186-完成)
    //================================ 

    // 攻擊箱子[完成]
    REACTOR_HIT(0x3CF),
    // 箱子移動[完成]
    REACTOR_MOVE(0x3D0),
    // 召喚箱子[完成]
    REACTOR_SPAWN(0x3D1),
    // 箱子未知[完成]
    REACTOR_UNK(0x3D2),
    // 箱子未知2[完成]
    REACTOR_UNK2(0x3D3),
    // 箱子消失(隱藏?)[Int - oid][完成]
    REACTOR_UNK3(0x3D4),
    // 重置箱子[完成]
    REACTOR_DESTROY(0x3D5),
    //================================
    // CReactorPool::OnExtractorPacket 開始(185-完成)
    //================================ 

    // 召喚分解機[完成]
    SPAWN_EXTRACTOR(0x3A9),
    // 移除分解機[完成]
    REMOVE_EXTRACTOR(0x3AA),
    //================================
    // CEventsPool::OnPacket 開始(185-完成)「CField_SnowBall::OnPacket」
    //================================ 

    //滾動雪球
    ROLL_SNOWBALL(0x3AB),
    //攻擊雪球
    HIT_SNOWBALL(0x3AC),
    //雪球訊息
    SNOWBALL_MESSAGE(0x3AD),
    //向左擊飛
    LEFT_KNOCK_BACK(0x3AE),
    //================================
    // CField_Coconut::OnPacket 開始(185-完成)
    //================================ 

    //攻擊椰子
    HIT_COCONUT(0x3AF),
    //椰子活動分數
    COCONUT_SCORE(0x3B0),
    //================================
    // CField_GuildBoss::OnPacket 開始(185-完成)
    //================================ 

    // CField_GuildBoss::OnHealerMove[完成]
    MOVE_HEALER(0x3B1),
    // CField_GuildBoss::OnPulleyStateChange[完成]
    PULLEY_STATE(0x3B2),
    //================================
    // CField_MonsterCarnival::OnPacket 開始(183-完成)
    //================================ 
    // 怪物擂台賽開始[完成]
    MONSTER_CARNIVAL_START(0x3B1),
    // 怪物擂台賽獲得CP[完成]
    MONSTER_CARNIVAL_OBTAINED_CP(0x3B2),
    // 怪物擂台賽狀態[完成]
    MONSTER_CARNIVAL_STATS(0x3B3),
    // 0x3B4 [Int] * 4

    // 怪物擂台賽召喚[完成]
    MONSTER_CARNIVAL_SUMMON(0x3B5),
    // 怪物擂台賽訊息[完成]
    MONSTER_CARNIVAL_MESSAGE(0x3B6),
    // 怪物擂台賽死亡[完成]
    MONSTER_CARNIVAL_DIED(0x3B7),
    // 怪物擂台賽離開[完成]
    MONSTER_CARNIVAL_LEAVE(0x3B8),
    // 怪物擂台賽分數[完成]
    MONSTER_CARNIVAL_RESULT(0x3B9),
    // 怪物擂台賽排名[完成]
    MONSTER_CARNIVAL_RANKING(0x3BA),
    //================================
    // CField_AriantArena::OnPacket 開始(183-完成)
    //================================ 

    // 更新納希競技大會分數[完成]
    ARIANT_SCORE_UPDATE(0x3BB),
    //================================
    // CField_AriantArena::OnPacket 結束
    //================================ 

    // 0x3BC
    // 開心牧場資訊
    SHEEP_RANCH_INFO(0x3BD),
    // 開心牧場衣服
    SHEEP_RANCH_CLOTHES(0x3BE),
    // 魔女之塔
    WITCH_TOWER(0x3BF),
    // 遠征隊挑戰[完成]
    EXPEDITION_CHALLENGE(0x3C0),
    // 炎魔祭壇[完成]
    ZAKUM_SHRINE(0x3C1),
    // PvP類型[完成]
    PVP_TYPE(0x3C2),
    // PvP轉移[完成]
    PVP_TRANSFORM(0x3C3),
    // PvP[完成]
    PVP_DETAILS(0x3C4),
    // PvP開始[完成]
    PVP_ENABLED(0x3C5),
    // PvP分數[完成]
    PVP_SCORE(0x3C6),
    // PvP結果[完成]
    PVP_RESULT(0x3C7),
    // PvP隊伍[完成]
    PVP_TEAM(0x3C8),
    // PvP計分板[完成]
    PVP_SCOREBOARD(0x3C9),
    // 0x3CA [Int][Byte]

    // PvP點數[完成]
    PVP_POINTS(0x3CB),
    // PvP擊殺數[完成]
    PVP_KILLED(0x3CC),
    // PvP模式[完成]
    PVP_MODE(0x3CD),
    // PvP冰騎士[完成]
    PVP_ICEKNIGHT(0x3CE),
    // sub_688DEC {
    // 0x3CF
    // 0x3D0
    // 0x3D1
    // 0x3D2(NULL)
    // 0x3D3
    // }

    // sub_6BAE8E {
    // 0x3D4
    // 0x3D7
    // }

    // sub_6C093D {
    // 0x3D5
    // 0x3D6
    // 0x3D8
    // }

    // sub_6B8599 {
    // 0x3D9
    // 0x3DA
    // 0x3DB
    // 0x3DC
    // 0x3DD
    // 0x3DE
    // 0x3DF
    // 0x3E0
    // 0x3E1
    // }

    // sub_6A7E5B {
    // 0x3E2
    // 0x3E3
    // }

    // 0x3E4

    // sub_6C8B36 { 【遊戲(未確認)】
    // 0x3E5
    // 0x3E6
    // 0x3E7
    // 0x3E8
    // 0x3E9
    // 0x3EA
    // 0x3EB
    // 0x3EC
    // 0x3ED 【楓葉戰士】
    // 0x3EE
    // 0x3EF
    // 0x3F0
    // 0x3F4
    // }

    // sub_6BDC3E {
    // 0x3F1
    // 0x3F2
    // 0x3F3
    // }

    // sub_63E919 {
    // 0x3F5【排行介面】
    // }

    // 0x3F6
    // 0x3F7

    // sub_6AF283 {
    // 0x3F8
    // 0x3F9
    //}

    //0x3FA
    //0x3FB

    // sub_68DCA9 {
    // 0x3FC
    // 0x3FD
    // 0x3FE
    // 0x3FF
    // }

    // sub_679B8A { 【楓葉戰士(未確認)】
    // 0x400 【楓葉戰士】【Effect/BasicEff.img/rhythmGame/fever%d】 [Int][Int]
    // 0x401
    // 0x402
    // 0x403
    // 0x404
    // }

    // sub_697DDF {
    // 0x40A
    // 0x40B
    // 0x40C
    // 0x40D
    // 0x410
    // }

    // sub_67E742 { 【BossPvP】
    // 0x405 [Int]
    // 0x406 [Int][Int][Int]
    // 0x407 [Int][Int][Int]
    // 0x408 【UI/UIWindow4.img/bossArena/selectUi/boss/%d】
    // 0x409 【UI/UIWindow4.img/bossArena/selectUi/ready】
    // 0x40E
    // 0x40F [Int]
    // 0x411
    // 0x412 【座椅效果?】
    // 0x413
    // 0x414
    // }

    // sub_6D6C10 {
    // 0x415
    // 0x416
    // 0x417
    // 0x418
    // 0x419
    // 0x41A
    // 0x3DC
    // }

    // 召喚符文[完成-183]
    SPAWN_RUNE(0x422),
    // 移除符文[完成-183]
    REMOVE_RUNE(0x423),
    // 重新召喚符文[完成-183]
    RESPAWN_RUNE(0x424),
    // sub_63AB26 {
    // 0x3E3 【UI/StarCityUI.img/Screen/WorldEvent/%d】
    // }

    // sub_6E6480 {
    // 0x3EE
    // 0x3EF
    // 0x3F0
    // 0x3F1
    // 0x3F2
    // 0x3F3
    // 0x3F4 【UI/UIWindow4.img/typingDefense/Result/gameover】【UI/UIWindow4.img/typingDefense/Result/clear】
    // 0x3F5
    // 0x3F6
    // 0x3F7
    // 0x3F8
    // 0x3F9
    // }

    // sub_69A320 { 【迷你遊戲】
    // 0x3E4
    // 0x3E5
    // 0x3E6 【Sound/MiniGame.img/】
    // 0x3E7
    // 0x3E8
    // 0x3E9
    // 0x3F0
    // 0x3F1
    // }

    // sub_69D380 {
    // 0x3FA
    // 0x3FB
    // 0x3FC
    // 0x3FD
    // 0x3FE
    // 0x3FF
    // 0x400
    // }

    // sub_6CBFA7 {
    // 0x401
    // }

    // 混沌炎魔祭壇[完成]
    CHAOS_ZAKUM_SHRINE(0x402),
    // 0x403

    // 闇黑龍王祭壇[完成]【未確認】
    HORNTAIL_SHRINE(0x404),
    // 購物商城更新楓幣
    CS_MESO_UPDATE(0x40E),
    // 商城搭配
    CS_COLLOCATTON(0x411),
    // sub_67C061 { 【怪物擂台賽】
    // 0x408
    // 0x409
    // 0x40A [-]
    // 0x40B [-]
    // 0x40C
    // 0x40D
    // 0x40E
    // 0x40F
    // 0x410
    // 0x411
    // 0x412
    // }

    // sub_680FAC { 【蛋糕 Vs 派餅 活動】
    // 0x415
    // 0x416 【%s 陣營的Boss已經被召喚。】
    // 0x417 【更新Boss血條】
    // 0x418 【攻擊效果】[Byte(00:Miss、01:Cool)]
    // 0x41C
    // 0x41F
    // }

    // sub_67FEDA {
    // 0x419 [Byte][Int][Byte]
    // }

    // sub_67F7EB {
    // 0x41A
    // 0x41B
    // }

    // sub_67F8C5 {
    // 0x41D [Byte]
    // }

    // sub_6DAE51 {
    // 0x422
    // 0x423
    // 0x424
    // 0x425
    // 0x426
    // 0x427
    // 0x428
    // }

    // sub_6CF7E2 {+0x18 178ok
    // 0x429
    // 0x42A
    // 0x42B
    // 0x42C
    // 0x42D
    // 0x42E
    // 0x42F
    // }

    // PvP(奪旗模式)
    CAPTURE_FLAGS(0x4DA),
    CAPTURE_POSITION(0x4DB),
    CAPTURE_RESET(0x4DC),
    // 粉紅色炎魔祭壇[完成]
    PINK_ZAKUM_SHRINE(0x4DD),
    //================================
    // CScriptMan::OnPacket 開始(186-完成)
    //================================ 

    // Npc交談[完成]
    NPC_TALK(0x4DE),
    //================================
    // CShopDlg::OnPacket 開始(186-完成)
    //================================ 

    // Npc商店[完成]
    OPEN_NPC_SHOP(0x4DF),
    // 購買Npc商店道具[完成]
    CONFIRM_SHOP_TRANSACTION(0x4E0),
    //================================
    // CAdminShopDlg::OnPacket 開始(186-完成)
    //================================ 

    // 管理員商店[完成]
    ADMIN_SHOP_RESULT(0x4E1),
    // 管理員商店-商品[完成]
    ADMIN_SHOP_COMMODITY(0x4E2),
    //================================
    // CAdminShopDlg::OnPacket 結束
    //================================ 

    // 0x4E3
    // 0x4E4
    // 0x4E5
    // 0x4E6
    // 0x4E7
    // 0x4E8
    // 0x4E9

    //================================
    // sub_6F1C61 開始(186-完成)
    //================================ 

    // 0x4EA
    // 0x4EB
    // 0x4EC 【靈魂陷阱】
    // 0x4ED
    // 0x4EE
    // 0x4EF
    // 0x4F0
    // 0x4F1 【姆勒姆勒地城地圖】、【姆勒姆勒的炸彈】
    // 0x4F2

    //================================
    // sub_6F1C61 結束
    //================================ 

    // 0x4F3
    // 0x4F4

    //================================
    // sub_6A7380 開始(183-完成)
    //================================ 

    // 0x4F5

    //================================
    // sub_6A7380 結束
    //================================ 

    // 0x4F6

    // 倉庫[完成]
    OPEN_STORAGE(0x4F7),
    //================================
    // CStoreBankDlg::OnPacket 開始(186-完成)
    //================================

    // 富蘭德里訊息
    MERCH_ITEM_MSG(0x4F8),
    // 富蘭德里倉庫
    MERCH_ITEM_STORE(0x4F9),
    //================================
    // CStoreBankDlg::OnPacket 結束(186-完成)
    //================================

    // 猜拳遊戲[完成]
    RPS_GAME(0x4FA),
    
    // 0x4FB
    // 0x4FC

    // 聊天室[完成]
    MESSENGER(0x4FD),
    // 玩家互動[完成]
    PLAYER_INTERACTION(0x4FE),
    
    // 0x4FF
    // 0x500
    
    //================================
    // CField_Tournament::OnPacket 開始(186-完成)
    //================================

    // CField_Tournament::OnTournament[完成]
    TOURNAMENT(0x501),
    // CField_Tournament::OnTournamentMatchTable[完成]
    TOURNAMENT_MATCH_TABLE(0x502),
    // CField_Tournament::OnTournamentSetPrize[完成]
    TOURNAMENT_SET_PRIZE(0x503),
    // CField_Tournament::OnTournamentUEW[完成]
    TOURNAMENT_UEW(0x504),
    // CField_Tournament::OnTournamentAvatarInfo[完成]
    TOURNAMENT_CHARACTERS(0x505),
    // CField_Wedding::OnWeddingProgress[完成]
    WEDDING_PROGRESS(0x506),
    // CField_Wedding::OnWeddingCeremonyEnd[完成]
    WEDDING_CEREMONY_END(0x507),

    // 小鋼珠 {
    // 0x4DE
    // 0x4DF
    // 0x4E0
    // }
    
    // 宅配操作(完成)
    PACKAGE_OPERATION(0x4E5),
    
    // 0x4E6

    // 購物商城更新[完成]
    CS_UPDATE(0x51D),
    // 購物商城操作[完成]
    CS_OPERATION(0x51E),
    // CCashShop::OnPurchaseExpChanged[完成]
    CS_EXP_PURCHASE(0x51F),
    // 0x520
    // 0x521 【00 00 04 00 00 00 00 00 84 11 06 00 00 00 00 00 FF 00 00】
    // 0x522
    // 0x523

    // 購物商城未知[完成]
    CASH_USE4(0x524),
    
    // 0x525
    // 0x526
    // 0x527
    // 0x528
    // 0x529
    // 0x52A
    // 0x52B
    // 0x52C
    // 0x52D

    // 購物商城帳號[完成]
    CS_ACCOUNT(0x52E),
    // 購物商城未知[完成]
    CASH_USE3(0x52F),
    // 購物商城未知[完成]
    CASH_USE(0x530),
    
    // 0x531
    // 0x532
    // 0x533
    // 0x534
    // 0x535

    // 購物商城未知[完成]
    CASH_USE2(0x536),
    
    // 0x4FE 【CCashShop::OnMemberShopResult】

    // 0x4FF
    // 0x500
    // 0x501

    // 0x502
    // 0x503
    // 0x504
    // 0x505
    // 0x506
    // 0x507 神獸學院
    // 0x508
    // 0x509
    // 0x50A 羊群牧場[Boolean] 【true => [String][Long]「[String][Long]」* 3】【false => [Int](分數)[Int](排行)[Int](咒文的痕跡)】

    //==================================
    // CFuncKeyMappedMan::OnPacket 開始(186-完成)
    //==================================

    // 鍵盤設置[完成]
    KEYMAP(0x561),
    // 寵物技能(HP)[完成]
    PET_AUTO_HP(0x562),
    // 寵物技能(MP)[完成]
    PET_AUTO_MP(0x563),
    // 寵物技能(Buff)[完成]
    PET_AUTO_CURE(0x564),
    //================================
    // CLogin::OnPacket 開始(186-完成)
    //================================ 

    // 創建角色第二組密碼驗證[完成]【獲得驗證碼 + 第二組密碼錯誤回應 + 獲得可建立職業】
    CREATE_CHAR_RESPONSE(0x565),
    
    // 0x566

    //==================================
    // CField::OnGoldHammerRes 開始(186-完成)
    //==================================

    // 0x56B

    // 黃金鐵鎚使用完成[完成]
    VICIOUS_HAMMER(0x56C),
    // 0x56D   

    //==================================
    // CField::OnPlatinumHammerRes 開始(186-完成)
    //==================================

    // 0x56E

    // 白金槌子
    PLATINUM_HAMMER(0x56F),
    // 0x570 

    //==================================
    // CField::OnZeroScroll 開始(186-完成)
    //==================================

    // 0x571

    // 神之子使用卷軸[完成]
    ZERO_SCROLL_START(0x572),
    // 神之子武器介面[完成]
    ZERO_RESULT(0x573),
    // 神之子卷軸[完成]
    ZERO_SCROLL(0x574),
    // 0x575
    // 0x576

    //==================================
    // CField::OnZeroWeapon 開始(186-完成)
    //==================================

    // 武器資訊
    ZERO_WEAPONINFO(0x577),
    // 武器成長[完成]
    ZERO_UPGRADE(0x578),
    // 0x579
    // 0x57A

    //==================================
    // CField::OnZeroWeapon 結束
    //==================================

    // 0x53E    
    // 0x53F
    // 0x540

    //==================================
    // CField::OnArrowBlaster 開始(186-完成)
    //==================================

    // 召喚箭座[完成]
    SPAWN_ARROW_BLASTER(0x57E),
    // 0x57F 【dispear】

    // 取消箭座[完成]
    CANCEL_ARROW_BLASTER(0x580),
    // 0x581
    // 0x582

    // 箭座控制[完成]
    ARROW_BLASTER_CONTROL(0x583),
    // 0x584
    // 0x585 【破除封印】
    // 0x586
    // 0x587
    // 0x588

    //==================================
    // CField::OnArrowBlaster 結束
    //==================================

    // sub_50630B {
    // 0x4CC
    // 0x4CD
    // 0x4CE
    // 0x4CF
    // 0x4D0
    // }

    // 潛能方塊[完成-186]
    STRENGTHEN_UI(0x5DB),
    // 階段介面[完成-186]
    LEVEL_UI(0x5DE),
    // sub_6921D9 {
    // 0x54B
    // 0x54C
    // }

    // sub_63AD6C {
    // 0x54D [Byte]
    // }

    // 每日免費強化任意門[完成-186]
    DAY_OF_CHRONOSPHERE(0x5F6),
    // 強化任意門錯誤[完成-186]
    ERROR_CHRONOSPHERE(0x5F7),
    // sub_6DF4C6 {
    // 0x56D
    // 0x56E
    // 0x56F
    // 0x570
    // 0x571
    // }

    // General

    UNKNOWN,
    AUTH_RESPONSE,
    GUEST_ID_LOGIN,
    // Login

    SEND_LINK,
    PIN_OPERATION,
    PIN_ASSIGNED,
    ALL_CHARLIST,
    RELOG_RESPONSE,
    REGISTER_PIC_RESPONSE,
    EXTRA_CHAR_INFO,
    SPECIAL_CREATION,
    // Channel

    FULL_CLIENT_DOWNLOAD,
    BOOK_INFO,
    REPORT_RESULT,
    TRADE_LIMIT,
    UPDATE_GENDER,
    BBS_OPERATION,
    CODEX_INFO_RESPONSE,
    ECHO_MESSAGE,
    BOOK_STATS,
    UPDATE_CODEX,
    CARD_DROPS,
    FAMILIAR_INFO,
    POTION_BONUS,
    MAPLE_TV_MSG,
    LUCKY_LUCKY_MONSTORY,
    POPUP2,
    CANCEL_NAME_CHANGE,
    CANCEL_WORLD_TRANSFER,
    CLOSE_HIRED_MERCHANT,
    CANCEL_NAME_CHANGE_2,
    GM_STORY_BOARD,
    FIND_FRIEND,
    VISITOR,
    PINKBEAN_CHOCO,
    EQUIP_STOLEN_SKILL,
    INNER_ABILITY_RESET_MSG,
    CASSANDRAS_COLLECTION,
    SET_OBJECT_STATE,
    POPUP,
    YOUR_INFORMATION,
    CANDY_RANKING,
    ATTENDANCE,
    RANDOM_RESPONSE,
    MAGIC_WHEEL,
    MOVE_ENV,
    UPDATE_ENV,
    CHATTEXT_1,
    SPAWN_PET_2,
    SPAWN_FAMILIAR,
    MOVE_FAMILIAR,
    TOUCH_FAMILIAR,
    ATTACK_FAMILIAR,
    RENAME_FAMILIAR,
    SPAWN_FAMILIAR_2,
    UPDATE_FAMILIAR,
    R_MESOBAG_SUCCESS,
    R_MESOBAG_FAILURE,
    MAP_FADE,
    MAP_FADE_FORCE,
    RANDOM_EMOTION,
    RADIO_SCHEDULE,
    OPEN_SKILL_GUIDE,
    AP_SP_EVENT,
    QUEST_GUIDE_NPC,
    REGISTER_FAMILIAR,
    FAMILIAR_MESSAGE,
    SHOW_MAP_NAME,
    CAKE_VS_PIE,
    MOVE_SCREEN_X,
    MOVE_SCREEN_DOWN,
    CAKE_PIE_INSTRUMENTS,
    SEALED_BOX,
    //怪物屬性
    MONSTER_PROPERTIES,
    //
    CYGNUS_ATTACK,
    //怪物抗擊
    MONSTER_RESIST,
    TELE_MONSTER,
    SHOW_MAGNET,
    NPC_TOGGLE_VISIBLE,
    INITIAL_QUIZ,
    RED_LEAF_HIGH,
    LOGOUT_GIFT,
    CS_CHARGE_CASH,
    GIFT_RESULT,
    CHANGE_NAME_CHECK,
    CHANGE_NAME_RESPONSE,
    //0x314 int itemid int sn

    CASH_SHOP_UPDATE,
    GACHAPON_STAMPS,
    FREE_CASH_ITEM,
    CS_SURPRISE,
    XMAS_SURPRISE,
    ONE_A_DAY,
    NX_SPEND_GIFT,
    RECEIVE_GIFT,
    RANDOM_CHECK,
    START_TV,
    REMOVE_TV,
    ENABLE_TV,
    GM_ERROR,
    ALIEN_SOCKET_CREATOR,
    BATTLE_RECORD_DAMAGE_INFO,
    CALCULATE_REQUEST_RESULT,
    BOOSTER_PACK,
    BOOSTER_FAMILIAR,
    BLOCK_PORTAL,
    NPC_CONFIRM,
    RSA_KEY,
    BUFF_BAR,
    GAME_POLL_REPLY,
    GAME_POLL_QUESTION,
    ENGLISH_QUIZ,
    FISHING_BOARD_UPDATE,
    BOAT_EFFECT,
    FISHING_CAUGHT,
    SIDEKICK_OPERATION,
    FARM_PACKET1,
    FARM_ITEM_PURCHASED,
    FARM_ITEM_GAIN,
    HARVEST_WARU,
    FARM_MONSTER_GAIN,
    FARM_INFO,
    FARM_MONSTER_INFO,
    FARM_QUEST_DATA,
    FARM_QUEST_INFO,
    FARM_MESSAGE,
    UPDATE_MONSTER,
    AESTHETIC_POINT,
    UPDATE_WARU,
    FARM_EXP,
    FARM_PACKET4,
    QUEST_ALERT,
    FARM_PACKET8,
    FARM_FRIENDS_BUDDY_REQUEST,
    FARM_FRIENDS,
    FARM_USER_INFO,
    FARM_AVATAR,
    FRIEND_INFO,
    FARM_RANKING,
    SPAWN_FARM_MONSTER1,
    SPAWN_FARM_MONSTER2,
    RENAME_MONSTER,
    //Unplaced:
    DEATH_COUNT,
    REDIRECTOR_COMMAND;

    private short code = -2;
    public static boolean record = false;

    private SendPacketOpcode() {
        this.code = 0x7FFE;
    }

    private SendPacketOpcode(int code) {
        this.code = (short) code;
    }

    public void setValue(int code) {
        this.code = (short) code;
    }
    
    @Override
    public void setValue(short newval) {
        this.code = newval;
    }

    @Override
    public short getValue() {
        return getValue(true);
    }

    public short getValue(boolean show) {
        if (show && ServerConfig.LOG_PACKETS) {
            if (isRecordHeader(this)) {
                record = true;
                String tab = "";
                for (int i = 4; i > name().length() / 8; i--) {
                    tab += "\t";
                }
                FileoutputUtil.log(FileoutputUtil.Packet_Record, "[發送]\t" + name() + tab + "\t包頭:0x" + StringUtil.getLeftPaddedStr(String.valueOf(code), '0', 4) + "\r\n");
            } else {
                record = false;
            }
        }
        return code;
    }

    public String getType(short code) {
        String type = null;
        if (code >= 0 && code < 0xE || code >= 0x17 && code < 0x21) {
            type = "CLogin";
        } else if (code >= 0xE && code < 0x17) {
            type = "LoginSecure";
        } else if (code >= 0x21 && code < 0xCB) {
            type = "CWvsContext";
        } else if (code >= 0xD2) {
            type = "CField";
        }
        return type;
    }

    public static String nameOf(int value) {
        for (SendPacketOpcode opcode : SendPacketOpcode.values()) {
            if (opcode.getValue(false) == value) {
                return opcode.name();
            }
        }
        return "UNKNOWN";
    }

    public static boolean isRecordHeader(SendPacketOpcode opcode) {
        switch (opcode.name()) {
//            case "WARP_TO_MAP":
//            case "GUILD_OPERATION":
//            case "PARTY_OPERATION":
//            case "GIVE_BUFF":
//            case "SPAWN_PLAYER":
//            case "DROP_ITEM_FROM_MAPOBJECT":
//            case "INVENTORY_OPERATION":
//            case "SPAWN_MONSTER":
            case "UNKNOWN":
                return true;
            default:
                return false;
        }
    }

    public static boolean isSpamHeader(SendPacketOpcode opcode) {
        switch (opcode.name()) {
            case "WARP_TO_MAP":
            case "PING":
            case "NPC_ACTION":
//            case "AUTH_RESPONSE":
//            case "SERVERLIST":
            case "UPDATE_STATS":
            case "MOVE_PLAYER":
            case "SPAWN_NPC":
            case "SPAWN_NPC_REQUEST_CONTROLLER":
            case "REMOVE_NPC":
            case "MOVE_MONSTER":
            case "MOVE_MONSTER_RESPONSE":
            case "SPAWN_MONSTER":
            case "SPAWN_MONSTER_CONTROL":
//            case "HAKU_MOVE":
//            case "MOVE_SUMMON":
//            case "MOVE_FAMILIAR":
            case "ANDROID_MOVE":
//            case "INVENTORY_OPERATION":
//            case "MOVE_PET":
//            case "SHOW_SPECIAL_EFFECT":
//            case "DROP_ITEM_FROM_MAPOBJECT":
//            case "REMOVE_ITEM_FROM_MAP":
//            case "UPDATE_PARTYMEMBER_HP":
//            case "DAMAGE_PLAYER":
//            case "SHOW_MONSTER_HP":
//            case "CLOSE_RANGE_ATTACK":
//            case "RANGED_ATTACK":
//            case "ARAN_COMBO":
//            case "REMOVE_BG_LAYER":
//            case "SPECIAL_STAT":
//            case "TOP_MSG":
//            case "ANGELIC_CHANGE":
//            case "UPDATE_CHAR_LOOK":
//            case "KILL_MONSTER":
            case "SYSTEM_PROCESS_LIST":
                return true;
            default:
                return false;
        }
    }

    public static void loadValues() {
        Properties props = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("sendops.properties")) {
            props.load(new BufferedReader(new InputStreamReader(fileInputStream, EncodingDetect.getJavaEncode("sendops.properties"))));
        } catch (IOException ex) {
            InputStream in = SendPacketOpcode.class.getClassLoader().getResourceAsStream("sendops.properties");
            if (in == null) {
                System.out.println("未讀取 sendops.properties 檔案, 使用內建 SendPacketOpcode 列舉");
                return;
            }
            try {
                props.load(in);
            } catch (IOException e) {
                throw new RuntimeException("讀取 sendops.properties 檔案異常", e);
            }
        }
        ExternalCodeTableGetter.populateValues(props, values());
    }

    static {
        loadValues();
    }
}
