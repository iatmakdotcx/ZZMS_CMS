/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import client.inventory.MapleInventoryType;
import database.DatabaseConnection;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import server.CashItemFactory;
import server.CashItemInfo.CashModInfo;
import server.MapleItemInformationProvider;

/**
 *
 * @author Flower
 */
public class CashShopDumper {
    
    private static final MapleDataProvider data = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/Etc.wz"));
    
    public static final CashModInfo getModInfo(int sn) {
        CashModInfo ret = null;
        
        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM cashshop_modified_items WHERE serial = ?")) {
            ps.setInt(1, sn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ret = new CashModInfo(sn, rs.getInt("discount_price"), rs.getInt("mark"), rs.getInt("showup") > 0, rs.getInt("itemid"), rs.getInt("priority"), rs.getInt("package") > 0, rs.getInt("period"), rs.getInt("gender"), rs.getInt("count"), rs.getInt("meso"), rs.getInt("unk_1"), rs.getInt("unk_2"), rs.getInt("unk_3"), rs.getInt("extra_flags"));
                    
                }
            }
            
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        return ret;
    }
    
    public static void main(String[] args) {
        CashModInfo m = getModInfo(20000393);
        CashItemFactory.getInstance().initialize(false);
        Connection con = DatabaseConnection.getConnection();
        Map<Integer, List<String>> dics = new HashMap<>();
        
        for (MapleData field : data.getData("Commodity.img").getChildren()) {
            try {
                final int itemId = MapleDataTool.getIntConvert("ItemId", field, 0);
                final int sn = MapleDataTool.getIntConvert("SN", field, 0);
                final int count = MapleDataTool.getIntConvert("Count", field, 0);
                final int price = MapleDataTool.getIntConvert("Price", field, 0);
                final int priority = MapleDataTool.getIntConvert("Priority", field, 0);
                final int period = MapleDataTool.getIntConvert("Period", field, 0);
                final int gender = MapleDataTool.getIntConvert("Gender", field, -1);
                final int meso = MapleDataTool.getIntConvert("Meso", field, 0);
                if (itemId == 0) {
                    continue;
                }
                
                int cat = itemId / 10000;
                if (dics.get(cat) == null) {
                    dics.put(cat, new ArrayList());
                }
                boolean check = false;
                if (meso > 0) {
                    check = true;
                }
                if (MapleItemInformationProvider.getInventoryType(itemId) == MapleInventoryType.EQUIP) {
                    if (!(MapleItemInformationProvider.getInstance().isCash(itemId))) {
                        check = true;
                    }
                }
                if (MapleItemInformationProvider.getInventoryType(itemId) == MapleInventoryType.EQUIP) {
                    if (period > 0) {
                        check = true;
                    }
                }
                
                if (check) {
                    System.out.println(MapleItemInformationProvider.getInstance().getName(itemId));
                    continue;
                }
                try (PreparedStatement ps = con.prepareStatement("INSERT INTO cashshop_modified_items (serial, showup,itemid,priority,period,gender,count,meso,discount_price,mark, unk_1, unk_2, unk_3  ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                    ps.setInt(1, sn);
                    ps.setInt(2, 1);
                    ps.setInt(3, 0);
                    ps.setInt(4, 0);
                    ps.setInt(5, period);
                    ps.setInt(6, gender);
                    ps.setInt(7, count > 1 ? count : 0);
                    ps.setInt(8, meso);
                    if ((1000000 <= itemId || itemId <= 1003091) && sn >= 20000000) {
                        ps.setInt(9, price);
                    } else {
                        ps.setInt(9, 0);
                    }
                    ps.setInt(10, 0);
                    ps.setInt(11, 0);
                    ps.setInt(12, 0);
                    ps.setInt(13, 0);
                    ps.execute();
                    ps.toString();
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
            
        }
    }
}
