/*
 * This file is part of the OdinMS MapleStory Private Server
 * Copyright (C) 2012 Patrick Huy and Matthias Butz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package client.inventory;

/**
 *
 * @author AlphaEta
 */
public enum SocketFlag {

    //可以鑲嵌
    DEFAULT(0x01),
    //已打孔1
    SOCKET_BOX_1(0x02),
    //已打孔2
    SOCKET_BOX_2(0x04),
    //已打孔3
    SOCKET_BOX_3(0x08),
    //已鑲嵌1
    USED_SOCKET_1(0x10),
    //已鑲嵌2
    USED_SOCKET_2(0x20),
    //已鑲嵌3
    USED_SOCKET_3(0x40);
    private final int i;

    private SocketFlag(int i) {
        this.i = i;
    }

    public final int getValue() {
        return i;
    }

    public final boolean check(int flag) {
        return (flag & i) == i;
    }
}
