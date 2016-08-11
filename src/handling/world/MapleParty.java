/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package handling.world;

import client.MapleCharacter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import server.MapleStatEffect;

public class MapleParty implements Serializable {

    private static final long serialVersionUID = 9179541993413738569L;
    private MaplePartyCharacter leader;
    private final List<MaplePartyCharacter> members = new LinkedList<>();
    private int id, expeditionLink = -1;
    private boolean disbanded = false;
    private boolean privateParty;
    private String name;
    private Map<Integer, Map<Integer, List<Integer>>> partyBuffs = new HashMap();

    public MapleParty(int id, MaplePartyCharacter chrfor, boolean privateParty, String name) {
        this.leader = chrfor;
        this.members.add(this.leader);
        this.id = id;
        this.privateParty = privateParty;
        this.name = name;
    }

    public MapleParty(int id, MaplePartyCharacter chrfor, int expeditionLink) {
        this.leader = chrfor;
        this.members.add(this.leader);
        this.id = id;
        this.expeditionLink = expeditionLink;
    }

    public boolean containsMembers(MaplePartyCharacter member) {
        return members.contains(member);
    }

    public void addMember(MaplePartyCharacter member) {
        members.add(member);
    }

    public void removeMember(MaplePartyCharacter member) {
        members.remove(member);
    }

    public void updateMember(MaplePartyCharacter member) {
        for (int i = 0; i < members.size(); i++) {
            MaplePartyCharacter chr = members.get(i);
            if (chr.equals(member)) {
                members.set(i, member);
            }
        }
    }

    public MaplePartyCharacter getMemberById(int id) {
        for (MaplePartyCharacter chr : members) {
            if (chr.getId() == id) {
                return chr;
            }
        }
        return null;
    }

    public MaplePartyCharacter getMemberByIndex(int index) {
        return members.get(index);
    }

    public Collection<MaplePartyCharacter> getMembers() {
        return new LinkedList<>(members);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MaplePartyCharacter getLeader() {
        return leader;
    }

    public void setLeader(MaplePartyCharacter nLeader) {
        leader = nLeader;
    }

    public int getExpeditionId() {
        return expeditionLink;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MapleParty other = (MapleParty) obj;
        return id == other.id;
    }

    public boolean isDisbanded() {
        return disbanded;
    }

    public void disband() {
        this.disbanded = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPrivate() {
        return privateParty;
    }

    public void setPrivate(boolean privateParty) {
        this.privateParty = privateParty;
    }

    public void givePartyBuff(int buffId, int applyfrom, int applyto) {
        if (partyBuffs.containsKey(buffId)) {
            if (partyBuffs.get(buffId).containsKey(applyfrom)) {
                if (!partyBuffs.get(buffId).keySet().isEmpty()) {
                    for (Integer from : partyBuffs.get(buffId).keySet()) {
                        if (partyBuffs.get(buffId).get(from).contains(applyto)) {
                            partyBuffs.get(buffId).get(from).remove(partyBuffs.get(buffId).get(from).indexOf(applyto));
                        }
                        if (partyBuffs.get(buffId).get(from).isEmpty()) {
                            partyBuffs.get(buffId).remove(from);
                        }
                    }
                }
                if (partyBuffs != null && !partyBuffs.get(buffId).isEmpty() && !partyBuffs.get(buffId).get(applyfrom).isEmpty() && !partyBuffs.get(buffId).get(applyfrom).contains(applyto)) {
                    partyBuffs.get(buffId).get(applyfrom).add(applyto);
                }
            } else {
                ArrayList applytos = new ArrayList();
                applytos.add(applyto);
                partyBuffs.get(buffId).put(applyfrom, applytos);
            }
        } else {
            Map<Integer, List<Integer>> hMap = new HashMap();
            ArrayList applytos = new ArrayList();
            applytos.add(applyto);
            hMap.put(applyfrom, applytos);
            partyBuffs.put(buffId, hMap);
        }
    }

    public int getPartyBuffs(int applyfrom) {
        ArrayList chrs = new ArrayList();
        for (Map<Integer, List<Integer>> buffs : partyBuffs.values()) {
            if (buffs.containsKey(applyfrom)) {
                for (List<Integer> applytos : buffs.values()) {
                    for (int i : applytos) {
                        if (!chrs.contains(i)) {
                            chrs.add(i);
                        }
                    }
                }
            }
        }
        return chrs.size();
    }

    public int cancelPartyBuff(int buffId, int cancelby) {
        if (partyBuffs.containsKey(buffId)) {
            if (partyBuffs.get(buffId).isEmpty()) {
                partyBuffs.remove(buffId);
            } else {
                for (Integer applyfrom : partyBuffs.get(buffId).keySet()) {
                    if (partyBuffs.get(buffId).get(applyfrom).isEmpty()) {
                        partyBuffs.get(buffId).remove(applyfrom);
                    } else if (partyBuffs.get(buffId).get(applyfrom).contains(cancelby)) {
                        partyBuffs.get(buffId).get(applyfrom).remove(partyBuffs.get(buffId).get(applyfrom).indexOf(cancelby));
                        return applyfrom;
                    }
                }
            }
        }
        return -1;
    }

    public void cancelAllPartyBuffsByChr(int cancelby) {
        if (partyBuffs.isEmpty()) {
            return;
        }
        try {
            for (Integer buffId : partyBuffs.keySet()) {
                if (partyBuffs.get(buffId).isEmpty()) {
                    partyBuffs.remove(buffId);
                } else {
                    for (Integer applyfrom : partyBuffs.get(buffId).keySet()) {
                        if (partyBuffs.get(buffId).get(applyfrom).isEmpty() || applyfrom == cancelby) {
                            partyBuffs.get(buffId).remove(applyfrom);
                            MapleCharacter chr = MapleCharacter.getOnlineCharacterById(applyfrom);
                            if (applyfrom == cancelby && chr != null) {
                                MapleStatEffect.applyPassiveBless(chr);
                            }
                        } else if (partyBuffs.get(buffId).get(applyfrom).contains(cancelby)) {
                            partyBuffs.get(buffId).get(applyfrom).remove(partyBuffs.get(buffId).get(applyfrom).indexOf(cancelby));
                            MapleCharacter chr = MapleCharacter.getOnlineCharacterById(applyfrom);
                            if (chr != null) {
                                MapleStatEffect.applyPassiveBless(chr);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }
}
