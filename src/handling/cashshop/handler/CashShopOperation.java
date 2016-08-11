package handling.cashshop.handler;

import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import client.MapleQuestStatus;
import client.inventory.Item;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryIdentifier;
import client.inventory.MapleInventoryType;
import client.inventory.MapleRing;
import constants.GameConstants;
import constants.ItemConstants;
import database.DatabaseConnection;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.login.LoginServer;
import handling.world.CharacterTransfer;
import handling.world.World;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import server.CashItem;
import server.CashItemFactory;
import server.CashItemInfo;
import server.CashShop;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.quest.MapleQuest;
import tools.FileoutputUtil;
import tools.Triple;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CSPacket;
import tools.packet.CWvsContext;

public class CashShopOperation {

    public static void LeaveCS(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        CashShopServer.getPlayerStorage().deregisterPlayer(chr);
        c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION, c.getSessionIPAddress());

        try {

            World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), c.getChannel());
            c.getSession().write(CField.getChannelChange(c, Integer.parseInt(ChannelServer.getInstance(c.getChannel()).getIP().split(":")[1])));
        } finally {
            final String s = c.getSessionIPAddress();
            LoginServer.addIPAuth(s.substring(s.indexOf('/') + 1, s.length()));
            chr.saveToDB(false, true);
            c.setPlayer(null);
            c.setReceiving(false);
            c.getSession().close(true);
        }
    }

    public static void EnterCS(final CharacterTransfer transfer, final MapleClient c) {
        if (transfer == null) {
            c.getSession().close(true);
            return;
        }
        MapleCharacter chr = MapleCharacter.ReconstructChr(transfer, c, false);

        c.setPlayer(chr);
        c.setAccID(chr.getAccountID());

        if (!c.CheckIPAddress()) { // Remote hack
            c.getSession().close(true);
            return;
        }

        final int state = c.getLoginState();
        if (state == MapleClient.LOGIN_SERVER_TRANSITION || state == MapleClient.CHANGE_CHANNEL) {
            World.isCharacterListConnected(c.loadCharacterNames(c.getWorld()));
        }
        c.updateLoginState(MapleClient.LOGIN_LOGGEDIN, c.getSessionIPAddress());
        CashShopServer.getPlayerStorage().registerPlayer(chr);
        c.getSession().write(CSPacket.warpCS(c));
        c.getSession().write(CSPacket.warpCSInfo(c));
        c.getSession().write(CSPacket.disableCS());
        // 0x159
        c.getSession().write(CSPacket.CashUse(10500002, 0x32, 0xD2, 20130320, 20130326));
        c.getSession().write(CSPacket.CashUse2(0x3));
        c.getSession().write(CSPacket.getCSInventory(c));
        c.getSession().write(CSPacket.CashUse3());
        //c.getSession().write(CSPacket.CS_Picture_Item()); // Updated - Need to check if nothing changed
        //c.getSession().write(CSPacket.CS_Top_Items()); // Updated to v146.1
        //c.getSession().write(CSPacket.CS_Special_Item()); // Updated to v146.1
        //c.getSession().write(CSPacket.CS_Featured_Item()); // Updated to v146.
        c.getSession().write(CSPacket.doCSMagic());
        c.getSession().write(CSPacket.getCSGifts(c));
        c.getSession().write(CSPacket.showCSAccount(c));
        c.getSession().write(CSPacket.sendWishList(c.getPlayer(), false));
        c.getSession().write(CSPacket.CashUse4());
        c.getSession().write(CSPacket.showNXMapleTokens(c.getPlayer()));
        c.getSession().write(CSPacket.showNXMapleTokens(c.getPlayer()));
    }

    public static void CSUpdate(final MapleClient c) {
        doCSPackets(c);
    }

    private static boolean CouponCodeAttempt(final MapleClient c) {
        c.couponAttempt++;
        return c.couponAttempt > 5;
    }

    public static void CouponCode(final String code, final MapleClient c) {
        if (code.length() <= 0) {
            return;
        }
        Triple<Boolean, Integer, Integer> info = null;
        try {
            info = MapleCharacterUtil.getNXCodeInfo(code);
        } catch (SQLException e) {
        }
        if (info != null && info.left) {
            if (!CouponCodeAttempt(c)) {
                int type = info.mid, item = info.right;
                try {
                    MapleCharacterUtil.setNXCodeUsed(c.getPlayer().getName(), code);
                } catch (SQLException e) {
                }
                /*
                 * Explanation of type!
                 * Basically, this makes coupon codes do
                 * different things!
                 *
                 * Type 1: 樂豆點數
                 * Type 2: 楓葉點數
                 * Type 3: 普通物品(SN)
                 * Type 4: 楓幣
                 */
                Map<Integer, Item> itemz = new HashMap<>();
                int maplePoints = 0, mesos = 0;
                switch (type) {
                    case 1:
                    case 2:
                        c.getPlayer().modifyCSPoints(type, item, false);
                        maplePoints = item;
                        break;
                    case 3:
                        CashItemInfo itez = CashItemFactory.getInstance().getItem(item);
                        if (itez == null) {
                            c.getSession().write(CSPacket.sendCSFail(0));
                            return;
                        }
                        byte slot = MapleInventoryManipulator.addId(c, itez.getId(), (short) 1, "", "Cash shop: coupon code" + " on " + FileoutputUtil.CurrentReadable_Date());
                        if (slot < 0) {
                            c.getSession().write(CSPacket.sendCSFail(0));
                            return;
                        } else {
                            itemz.put(item, c.getPlayer().getInventory(GameConstants.getInventoryType(item)).getItem(slot));
                        }
                        break;
                    case 4:
                        c.getPlayer().gainMeso(item, false);
                        mesos = item;
                        break;
                }
                c.getSession().write(CSPacket.showCouponRedeemedItem(itemz, mesos, maplePoints, c));
                doCSPackets(c);
            }
        } else {
            if (CouponCodeAttempt(c) == true) {
                c.getSession().write(CSPacket.sendCSFail(48)); //A1, 9F
            } else {
                c.getSession().write(CSPacket.sendCSFail(info == null ? 14 : 17)); //A1, 9F
            }
        }
    }

    public static void BuyCashItem(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        final int action = slea.readByte();
//        System.out.println("action " + action);
        if (action == 0) { // 兌換券
            slea.skip(2);
            CouponCode(slea.readMapleAsciiString(), c);
        } else if (action == 2) {
            slea.skip(1);
            int type = slea.readInt();
            int sn = slea.readInt();
            final CashItem item = CashItemFactory.getInstance().getAllItem(sn);
            final int toCharge = slea.readInt();
            if (item == null) {
                c.getSession().write(CSPacket.sendCSFail(0));
            }
            chr.modifyCSPoints(type, -toCharge, true);
            Item itemz = chr.getCashInventory().toItem(item);
            if (itemz != null) {
                chr.getCashInventory().addToInventory(itemz);
                c.getSession().write(CSPacket.showBoughtCSItem(itemz, item.getSN(), c.getAccID()));
            } else {
                c.getSession().write(CSPacket.sendCSFail(0));
            }
        } else if (action == 3) { // 購買道具
            final int toCharge = slea.readByte() + 1;
            slea.skip(1);
            slea.skip(1);
            final CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());
            if (item != null && chr.getCSPoints(toCharge) >= item.getPrice()) {
                if (!item.genderEquals(c.getPlayer().getGender())/* && c.getPlayer().getAndroid() == null*/) {
                    c.getSession().write(CSPacket.sendCSFail(0xA7));
                    doCSPackets(c);
                    return;
                } else if (item.getId() == 5211046 || item.getId() == 5211047 || item.getId() == 5211048 || item.getId() == 5050100 || item.getId() == 5051001) {
                    c.getSession().write(CWvsContext.broadcastMsg(1, "目前無法購買本道具。"));
                    c.getSession().write(CWvsContext.enableActions());
                    doCSPackets(c);
                    return;
                } else if (c.getPlayer().getCashInventory().getItemsSize() >= 100) {
                    c.getSession().write(CSPacket.sendCSFail(0xB2));
                    doCSPackets(c);
                    return;
                }
                for (int id : GameConstants.cashBlock) {
                    if (item.getId() == id) {
                        c.getSession().write(CWvsContext.broadcastMsg(1, "目前無法購買本道具。"));
                        c.getSession().write(CWvsContext.enableActions());
                        doCSPackets(c);
                        return;
                    }
                }
                chr.modifyCSPoints(toCharge, -item.getPrice(), false);
                Item itemz = chr.getCashInventory().toItem(item);
                if (itemz != null && itemz.getUniqueId() > 0 && itemz.getItemId() == item.getId() && itemz.getQuantity() == item.getCount()) {
                    chr.getCashInventory().addToInventory(itemz);
                    //c.getSession().write(CSPacket.confirmToCSInventory(itemz, c.getAccID(), item.getSN()));
                    c.getSession().write(CSPacket.showBoughtCSItem(itemz, item.getSN(), c.getAccID()));
                } else {
                    c.getSession().write(CSPacket.sendCSFail(0));
                }
            } else {
                c.getSession().write(CSPacket.sendCSFail(0));
            }
        } else if (action == 4 /*|| action == 34*/) {
        } else if (action == 5) { // 購物車
            chr.clearWishlist();
            if (slea.available() < 48) {
                c.getSession().write(CSPacket.sendCSFail(0));
                doCSPackets(c);
                return;
            }
            int[] wishlist = new int[12];
            for (int i = 0; i < 12; i++) {
                wishlist[i] = slea.readInt();
            }
            chr.setWishlist(wishlist);
            c.getSession().write(CSPacket.sendWishList(chr, true));
        } else if (action == 6) { // 擴充道具欄位
            final int toCharge = slea.readByte() + 1;
            final boolean coupon = slea.readByte() > 0;
            if (coupon) {
                final MapleInventoryType type = getInventoryType(slea.readInt());
                if ((type == MapleInventoryType.SETUP ? chr.getCSPoints(toCharge) >= 150 : chr.getCSPoints(toCharge) >= 180) && chr.getInventory(type).getSlotLimit() < 89) {
                    chr.modifyCSPoints(toCharge, type == MapleInventoryType.SETUP ? -150 : -180, false);
                    chr.getInventory(type).addSlot((byte) 8);
                    chr.dropMessage(1, "道具欄位擴充至 " + chr.getInventory(type).getSlotLimit() + " 格。");
                } else {
                    c.getSession().write(CSPacket.sendCSFail(0xA4));
                }
            } else {
                final MapleInventoryType type = MapleInventoryType.getByType(slea.readByte());
                if (chr.getCSPoints(toCharge) >= 100 && chr.getInventory(type).getSlotLimit() < 93) {
                    chr.modifyCSPoints(toCharge, -100, false);
                    chr.getInventory(type).addSlot((byte) 4);
                    chr.dropMessage(1, "道具欄位擴充至 " + chr.getInventory(type).getSlotLimit() + " 格。");
                } else {
                    c.getSession().write(CSPacket.sendCSFail(0xA4));
                }
            }

        } else if (action == 7) { // 擴充倉庫欄位
            final int toCharge = slea.readByte() + 1;
            final int coupon = slea.readByte() > 0 ? 2 : 1;
            if ((coupon == 1 ? chr.getCSPoints(toCharge) >= 100 : chr.getCSPoints(toCharge) >= 180) && chr.getStorage().getSlots() < (49 - (4 * coupon))) {
                chr.modifyCSPoints(toCharge, coupon == 1 ? -100 : -180, false);
                chr.getStorage().increaseSlots((byte) (4 * coupon));
                chr.getStorage().saveToDB();
                chr.dropMessage(1, "道具欄位擴充至 " + chr.getStorage().getSlots() + " 格。");
            } else {
                c.getSession().write(CSPacket.sendCSFail(0xA4));
            }
        } else if (action == 8) {  // 擴充角色欄位
            final int toCharge = slea.readByte() + 1;
            CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());
            int slots = c.getCharacterSlots();
            if (item == null || c.getPlayer().getCSPoints(toCharge) < item.getPrice() || slots >= 30 || item.getId() != 5430000) {
                c.getSession().write(CSPacket.sendCSFail(0));
                doCSPackets(c);
                return;
            }
            if (c.gainCharacterSlot()) {
                c.getPlayer().modifyCSPoints(toCharge, -item.getPrice(), false);
                chr.dropMessage(1, "角色欄位擴充至 " + (slots + 1) + " 格。");
            } else {
                c.getSession().write(CSPacket.sendCSFail(0));
            }
        } else if (action == 10) { // 擴充墜飾欄位
            final int toCharge = slea.readByte() + 1;
            final int sn = slea.readInt();
            CashItemInfo item = CashItemFactory.getInstance().getItem(sn);
            if (item == null || c.getPlayer().getCSPoints(toCharge) < item.getPrice() || item.getId() / 10000 != 555) {
                c.getSession().write(CSPacket.sendCSFail(0));
                doCSPackets(c);
                return;
            }
            MapleQuestStatus marr = c.getPlayer().getQuestNoAdd(MapleQuest.getInstance(GameConstants.墜飾欄));
            if (marr != null && marr.getCustomData() != null && Long.parseLong(marr.getCustomData()) >= System.currentTimeMillis()) {
                c.getSession().write(CSPacket.sendCSFail(0));
            } else {
                c.getPlayer().getQuestNAdd(MapleQuest.getInstance(GameConstants.墜飾欄)).setCustomData(String.valueOf(System.currentTimeMillis() + ((long) item.getPeriod() * 24 * 60 * 60000)));
                c.getPlayer().modifyCSPoints(toCharge, -item.getPrice(), false);
                chr.dropMessage(1, "墜飾欄位擴充成功。");
            }
        } else if (action == 14) { // 購物商城→道具欄位
            Item item = c.getPlayer().getCashInventory().findByCashId((int) slea.readLong());
            if (item != null && item.getQuantity() > 0 && MapleInventoryManipulator.checkSpace(c, item.getItemId(), item.getQuantity(), item.getOwner())) {
                Item item_ = item.copy();
                short pos = MapleInventoryManipulator.addbyItem(c, item_, true);
                if (pos >= 0) {
                    if (item_.getPet() != null) {
                        item_.getPet().setInventoryPosition(pos);
                        c.getPlayer().addPet(item_.getPet());
                    }
                    c.getPlayer().getCashInventory().removeFromInventory(item);
                    c.getSession().write(CSPacket.confirmFromCSInventory(item_, pos));
                } else {
                    c.getSession().write(CSPacket.sendCSFail(0xB1));
                }
            } else {
                c.getSession().write(CSPacket.sendCSFail(0xB1));
            }
        } else if (action == 15) { // 道具欄位→購物商城
            Item item1;
            int sn;
            CashShop cs = chr.getCashInventory();
            int cashId = (int) slea.readLong();
            byte type = slea.readByte();
            MapleInventory mi = chr.getInventory(MapleInventoryType.getByType(type));
            item1 = mi.findByUniqueId(cashId);
            if (item1 == null) {
                c.getSession().write(CSPacket.showNXMapleTokens(chr));
                return;
            }
            if (cs.getItemsSize() < 100) {
                sn = CashItemFactory.getInstance().getItemSN(item1.getItemId());
                cs.addToInventory(item1);
                mi.removeSlot(item1.getPosition());
                c.getSession().write(CSPacket.confirmToCSInventory(item1, c.getAccID(), sn));
            } else {
                chr.dropMessage(1, "移動失敗。");
            }
        } else if (action == 34 || action == 40) { // 好友戒指
            slea.readMapleAsciiString();
            final int toCharge = slea.readByte() + 1;
            final CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());
            slea.readInt();
            final String partnerName = slea.readMapleAsciiString();
            final String msg = slea.readMapleAsciiString();
            if (item == null || !ItemConstants.类型.特效戒指(item.getId()) || c.getPlayer().getCSPoints(toCharge) < item.getPrice() || msg.getBytes().length > 73 || msg.getBytes().length < 1) {
                c.getSession().write(CSPacket.sendCSFail(0));
                doCSPackets(c);
                return;
            } else if (!item.genderEquals(c.getPlayer().getGender())) {
                c.getSession().write(CSPacket.sendCSFail(0xA6));
                doCSPackets(c);
                return;
            } else if (c.getPlayer().getCashInventory().getItemsSize() >= 100) {
                c.getSession().write(CSPacket.sendCSFail(0xB1));
                doCSPackets(c);
                return;
            }
            Triple<Integer, Integer, Integer> info = MapleCharacterUtil.getInfoByName(partnerName, c.getPlayer().getWorld());
            if (info == null || info.getLeft() <= 0 || info.getLeft() == c.getPlayer().getId()) {
                c.getSession().write(CSPacket.sendCSFail(0xB4));
                doCSPackets(c);
                return;
            } else if (info.getMid() == c.getAccID()) {
                c.getSession().write(CSPacket.sendCSFail(0xA3));
                doCSPackets(c);
                return;
            } else {
                if (info.getRight() == c.getPlayer().getGender() && action == 30) {
                    c.getSession().write(CSPacket.sendCSFail(0xA1));
                    doCSPackets(c);
                    return;
                }
                int err = MapleRing.createRing(item.getId(), c.getPlayer(), partnerName, msg, info.getLeft().intValue(), item.getSN());
                if (err != 1) {
                    c.getSession().write(CSPacket.sendCSFail(0));
                    doCSPackets(c);
                    return;
                }
                c.getPlayer().modifyCSPoints(toCharge, -item.getPrice(), false);
            }
        } else if (action == 35) { // 購買套組
            final int toCharge = slea.readByte() + 1;
            final CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());
            List<Integer> ccc = null;
            if (item != null) {
                ccc = CashItemFactory.getInstance().getPackageItems(item.getId());
            }
            if (item == null || ccc == null || c.getPlayer().getCSPoints(toCharge) < item.getPrice()) {
                c.getSession().write(CSPacket.sendCSFail(0));
                doCSPackets(c);
                return;
            } else if (!item.genderEquals(c.getPlayer().getGender())) {
                c.getSession().write(CSPacket.sendCSFail(0xA6));
                doCSPackets(c);
                return;
            } else if (c.getPlayer().getCashInventory().getItemsSize() >= (100 - ccc.size())) {
                c.getSession().write(CSPacket.sendCSFail(0xB1));
                doCSPackets(c);
                return;
            }

            Map<Integer, Item> ccz = new HashMap<>();
            for (int i : ccc) {
                final CashItemInfo cii = CashItemFactory.getInstance().getSimpleItem(i);
                if (cii == null) {
                    continue;
                }
                Item itemz = c.getPlayer().getCashInventory().toItem(cii);
                if (itemz == null || itemz.getUniqueId() <= 0) {
                    continue;
                }
                for (int iz : GameConstants.cashBlock) {
                    if (itemz.getItemId() == iz) {
                    }
                }
                ccz.put(i, itemz);
                c.getPlayer().getCashInventory().addToInventory(itemz);
            }
            chr.modifyCSPoints(toCharge, -item.getPrice(), false);
            c.getSession().write(CSPacket.showBoughtCSPackage(ccz, c.getAccID()));

        } else if (action == 37 || action == 99) { // 購買任務道具
            final CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());
            if (item == null || !MapleItemInformationProvider.getInstance().isQuestItem(item.getId())) {
                c.getSession().write(CSPacket.sendCSFail(0));
                doCSPackets(c);
                return;
            } else if (c.getPlayer().getMeso() < item.getPrice()) {
                c.getSession().write(CSPacket.sendCSFail(0xB8));
                doCSPackets(c);
                return;
            } else if (c.getPlayer().getInventory(GameConstants.getInventoryType(item.getId())).getNextFreeSlot() < 0) {
                c.getSession().write(CSPacket.sendCSFail(0xB1));
                doCSPackets(c);
                return;
            }
            byte pos = MapleInventoryManipulator.addId(c, item.getId(), (short) item.getCount(), null, "Cash shop: quest item" + " on " + FileoutputUtil.CurrentReadable_Date());
            if (pos < 0) {
                c.getSession().write(CSPacket.sendCSFail(0xB1));
                doCSPackets(c);
                return;
            }
            chr.gainMeso(-item.getPrice(), false);
            c.getSession().write(CSPacket.showBoughtCSQuestItem(item.getPrice(), (short) item.getCount(), pos, item.getId()));
        } else if (action == 49) {
//            c.getSession().write(CSPacket.updatePurchaseRecord());
        } else if (action == 58) { //get item from csinventory
            //uniqueid, 00 01 01 00, type->position(short)
//            Item item = c.getPlayer().getCashInventory().findByCashId((int) slea.readLong());
////            Item item = MapleItemInformationProvider.getInstance().getEquipById(item);
//            if (item != null && item.getQuantity() > 0 && MapleInventoryManipulator.checkSpace(c, item.getItemId(), item.getQuantity(), item.getOwner())) {
//                Item item_ = item.copy();
//                short pos = MapleInventoryManipulator.addbyItem(c, item_, true);
//                if (pos >= 0) {
//                    if (item_.getPet() != null) {
//                        item_.getPet().setInventoryPosition(pos);
//                        c.getPlayer().addPet(item_.getPet());
//                    }
//                    c.getPlayer().getCashInventory().removeFromInventory(item);
//                    c.getSession().write(CSPacket.confirmFromCSInventory(item_, pos));
//                } else {
//                    c.getSession().write(CSPacket.sendCSFail(0xB1));
//                }
//            } else {
//                c.getSession().write(CSPacket.sendCSFail(0xB1));
//            }
        } else if (action == 63) { // 商城隨機箱開啟
            
        } else if (action == 91) { // Open random box.
            final int uniqueid = (int) slea.readLong();

            //c.getSession().write(CSPacket.sendRandomBox(uniqueid, new Item(1302000, (short) 1, (short) 1, (short) 0, 10), (short) 0));
            //} else if (action == 99) { //buy with mesos
            //    int sn = slea.readInt();
            //    int price = slea.readInt();
        } else if (action == 101) {
//            System.out.println("action 101");//might be farm mesos? RITE NOW IS FREEH
            slea.skip(1);
            int type = slea.readInt();
            int sn = slea.readInt();
            final CashItem item = CashItemFactory.getInstance().getAllItem(sn);
            if (item == null) {
                c.getSession().write(CSPacket.sendCSFail(0));
            }
//            chr.modifyCSPoints(type, -toCharge, true);
            Item itemz = chr.getCashInventory().toItem(item);
            if (itemz != null) {
                chr.getCashInventory().addToInventory(itemz);
                c.getSession().write(CSPacket.showBoughtCSItem(itemz, item.getSN(), c.getAccID()));
            } else {
                c.getSession().write(CSPacket.sendCSFail(0));
            }
        } else {
            System.out.println("未知操作碼: " + action + " Remaining: " + slea.toString());
            c.getSession().write(CSPacket.sendCSFail(0));
        }
        doCSPackets(c);
    }

    public static void sendCSgift(final LittleEndianAccessor slea, final MapleClient c) {
        String secondPassword = slea.readMapleAsciiString();
        final CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());
        String partnerName = slea.readMapleAsciiString();
        String msg = slea.readMapleAsciiString();
//        if (!secondPassword.equals(c.getSecondPassword())) {
//            c.getPlayer().dropMessage(1, "第二組密碼錯誤，請重新輸入。");
//            doCSPackets(c);
//            return;
//        }
        if (item == null || c.getPlayer().getCSPoints(1) < item.getPrice() || msg.getBytes().length > 73 || msg.getBytes().length < 1) { //dont want packet editors gifting random stuff =P
            c.getSession().write(CSPacket.sendCSFail(0));
            doCSPackets(c);
            return;
        }
        Triple<Integer, Integer, Integer> info = MapleCharacterUtil.getInfoByName(partnerName, c.getPlayer().getWorld());
        if (info == null || info.getLeft() <= 0 || info.getLeft() == c.getPlayer().getId() || info.getMid() == c.getAccID()) {
            c.getSession().write(CSPacket.sendCSFail(0xA2));
            doCSPackets(c);
            return;
        } else if (!item.genderEquals(info.getRight())) {
            c.getSession().write(CSPacket.sendCSFail(0xA3));
            doCSPackets(c);
            return;
        } else {
            c.getPlayer().getCashInventory().gift(info.getLeft(), c.getPlayer().getName(), msg, item.getSN(), MapleInventoryIdentifier.getInstance());
            c.getPlayer().modifyCSPoints(1, -item.getPrice(), false);
            c.getSession().write(CSPacket.sendGift(item.getPrice(), item.getId(), item.getCount(), partnerName, true));
        }
        doCSPackets(c);
    }

    public static void SwitchCategory(final LittleEndianAccessor slea, final MapleClient c) {
        int Scategory = slea.readByte();
//        System.out.println("Scategory " + Scategory);
        if (Scategory == 103) {
            slea.skip(1);
            int itemSn = slea.readInt();
            try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO `wishlist` VALUES (?, ?)")) {
                ps.setInt(1, c.getPlayer().getId());
                ps.setInt(2, itemSn);
                ps.executeUpdate();
                ps.close();
            } catch (SQLException ex) {
                System.out.println("error");
            }
            c.getSession().write(CSPacket.addFavorite(itemSn));
        } else if (Scategory == 105) {
            int item = slea.readInt();
            try {
                Connection con = DatabaseConnection.getConnection();
                try (PreparedStatement ps = con.prepareStatement("UPDATE cashshop_items SET likes = likes+" + 1 + " WHERE sn = ?")) {
                    ps.setInt(1, item);
                    ps.executeUpdate();
                }
            } catch (SQLException ex) {
            }
            c.getSession().write(CSPacket.Like(item));
        } else if (Scategory == 109) {
            c.getSession().write(CSPacket.Favorite(c.getPlayer()));
        } else if (Scategory == 112) {//click on special item
            //int C8 - C9 - CA
        } else if (Scategory == 113) {//buy from cart inventory
            //byte buy = 1 or gift = 0
            //byte amount
            //for each SN
        } else {
            int category = slea.readInt();
            if (category == 1060100) {
                c.getSession().write(CSPacket.showNXChar(category));
                c.getSession().write(CSPacket.changeCategory(category));
            } else {
//                System.err.println(category);
                c.getSession().write(CSPacket.changeCategory(category));
            }
        }
    }

    private static MapleInventoryType getInventoryType(final int id) {
        switch (id) {
            case 140500002:
                return MapleInventoryType.EQUIP;
            case 140500003:
                return MapleInventoryType.USE;
            case 140500005:
                return MapleInventoryType.SETUP;
            case 140500004:
                return MapleInventoryType.ETC;
            default:
                return MapleInventoryType.UNDEFINED;
        }
    }

    public static void doCSPackets(MapleClient c) {
        //c.getSession().write(CSPacket.getCSInventory(c));
        //c.getSession().write(CSPacket.doCSMagic());
        //c.getSession().write(CSPacket.getCSGifts(c));
        //c.getSession().write(CWvsContext.BuddylistPacket.updateBuddylist(c.getPlayer().getBuddylist().getBuddies()));
        c.getSession().write(CSPacket.showNXMapleTokens(c.getPlayer()));
//        c.getSession().write(CSPacket.sendWishList(c.getPlayer(), false));
//        c.getSession().write(CSPacket.showNXMapleTokens(c.getPlayer()));
        c.getSession().write(CSPacket.getCSInventory(c));
        c.getSession().write(CSPacket.disableCS());
        //c.getSession().write(CSPacket.enableCSUse());
        //c.getPlayer().getCashInventory().checkExpire(c);
    }
}
