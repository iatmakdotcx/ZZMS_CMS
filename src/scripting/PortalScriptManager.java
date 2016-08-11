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
package scripting;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import client.MapleClient;
import constants.GameConstants;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;
import javax.script.ScriptException;
import server.MaplePortal;
import tools.EncodingDetect;
import tools.FileoutputUtil;

public class PortalScriptManager {

    private static final PortalScriptManager instance = new PortalScriptManager();
    private final Map<String, PortalScript> scripts = new HashMap<>();
    private final static ScriptEngineFactory sef = new ScriptEngineManager().getEngineByName("javascript").getFactory();

    public static PortalScriptManager getInstance() {
        return instance;
    }

    private PortalScript getPortalScript(final String scriptName) {
        if (scripts.containsKey(scriptName)) {
            return scripts.get(scriptName);
        }

        final File scriptFile = new File("scripts/portal/" + scriptName + ".js");
        if (!scriptFile.exists()) {
            return null;
        }

        InputStream in = null;
        final ScriptEngine portal = sef.getScriptEngine();
        try {
            in = new FileInputStream(scriptFile);
            BufferedReader bf = new BufferedReader(new InputStreamReader(in, EncodingDetect.getJavaEncode(scriptFile)));
            String lines = "load('nashorn:mozilla_compat.js');" + bf.lines().collect(Collectors.joining(System.lineSeparator()));
            CompiledScript compiled = ((Compilable) portal).compile(lines);
            compiled.eval();
        } catch (final FileNotFoundException | UnsupportedEncodingException | ScriptException e) {
            System.err.println("读取传送点脚本出错: " + scriptName + ":" + e);
            FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "读取传送点脚本出错 (" + scriptName + ") " + e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (final IOException e) {
                    System.err.println("ERROR CLOSING" + e);
                }
            }
        }
        final PortalScript script = ((Invocable) portal).getInterface(PortalScript.class);
        scripts.put(scriptName, script);
        return script;
    }

    public final void executePortalScript(final MaplePortal portal, final MapleClient c) {
        final PortalScript script = getPortalScript(portal.getScriptName());

        boolean err = false;
        if (script != null) {
            try {
                if (c.getPlayer().isShowInfo()) {
                    c.getPlayer().showInfo("传送点脚本", false, "执行传送点脚本:" + portal.getScriptName() + ".js" + c.getPlayer().getMap());
                }
                script.enter(new PortalPlayerInteraction(c, portal));
            } catch (Exception e) {
                err = true;
                System.err.println("执行传送点脚本出错: " + portal.getScriptName() + " : " + e);
            }
        } else {
            err = true;
            if (c.getPlayer().isShowErr()) {
                c.getPlayer().showInfo("传送点脚本", true, "未找到处理传送点(" + portal.getScriptName() + ")的脚本" + c.getPlayer().getMap());
            }
            System.out.println("\r\n未找到处理传送点(" + portal.getScriptName() + ")的脚本" + c.getPlayer().getMap() + "\r\n");
            FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "\r\n\r\n未找到处理传送点(" + portal.getScriptName() + ")的脚本" + c.getPlayer().getMap() + "\r\n\r\n");
        }
        if (err && !GameConstants.isTutorialMap(c.getPlayer().getMapId())) {
            c.getPlayer().卡图 = c.getPlayer().getMapId();
            c.getPlayer().dropMessage(5, "你好像被卡在了奇怪的地方，这里有个东西未处理，请联系管理员反馈讯息：" + portal.getScriptName() + "\r\n你现在可以点击 拍卖 或者输入 @卡图 来移动到弓箭手村。");
        }
    }

    public final void clearScripts() {
        scripts.clear();
    }
}
