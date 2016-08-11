package constants;

import java.io.File;
import server.ServerProperties;

public class ServerConfig {

    public static String SQL_IP = "127.0.0.1";
    public static String SQL_PORT = "3306";
    public static String SQL_DATABASE = "131";
    public static String SQL_USER = "root";
    public static String SQL_PASSWORD = "root";

    public static boolean ADMIN_ONLY = false;
    public static boolean LOG_PACKETS = true;
    public static boolean AUTO_REGISTER = false;
    public static String SERVER_NAME = "ZZMS";
    public static int USER_LIMIT = 1500;
    public static String IP = "221.231.130.70";
    public static boolean LOG_SHARK = false;
    public static int CHANNEL_MAX_CHAR_VIEW = 500;
    private static String EVENTS = null;
    public static int MAX_DAMAGE = 50000000;

    /*Anti-Sniff*/
    public static boolean USE_FIXED_IV = false;
    public static final byte[] Static_LocalIV = new byte[]{0x47, 0x71, 0x1A, 0x2C};
    public static final byte[] Static_RemoteIV = new byte[]{0x46, 0x70, 0x19, 0x2B};
    public static String scrollingMessage="";

    public static String[] getEvents(boolean reLoad) {
        return getEventList(reLoad).split(",");
    }

    public static String getEventList(boolean reLoad) {
        if (EVENTS == null || reLoad) {
            File root = new File("scripts/event");
            File[] files = root.listFiles();
            EVENTS = "";
            for (File file : files) {
                if (!file.isDirectory()) {
                    String[] fileName = file.getName().split("\\.");
                    if (fileName.length > 1 && "js".equals(fileName[fileName.length - 1])) {
                        for (int i = 0 ; i < fileName.length - 1 ; i++) {
                            EVENTS += fileName[i];
                        }
                        EVENTS += ",";
                    }
                }
            }
        }
        return EVENTS;
    }

    public static boolean isAutoRegister() {
        return AUTO_REGISTER;
    }

    public static void loadSetting() {
        SQL_IP = ServerProperties.getProperty("SQL_IP", SQL_IP);
        SQL_PORT = ServerProperties.getProperty("SQL_PORT", SQL_PORT);
        SQL_DATABASE = ServerProperties.getProperty("SQL_DATABASE", SQL_DATABASE);
        SQL_USER = ServerProperties.getProperty("SQL_USER", SQL_USER);
        SQL_PASSWORD = ServerProperties.getProperty("SQL_PASSWORD", SQL_PASSWORD);

        ADMIN_ONLY = ServerProperties.getProperty("ADMIN_ONLY", ADMIN_ONLY);
        LOG_PACKETS = ServerProperties.getProperty("LOG_PACKETS", LOG_PACKETS);
        AUTO_REGISTER = ServerProperties.getProperty("AUTO_REGISTER", AUTO_REGISTER);
        SERVER_NAME = ServerProperties.getProperty("SERVER_NAME", SERVER_NAME);
        USER_LIMIT = ServerProperties.getProperty("USER_LIMIT", USER_LIMIT);
        IP = ServerProperties.getProperty("IP", IP);
        LOG_SHARK = ServerProperties.getProperty("LOG_SHARK", LOG_SHARK);
        CHANNEL_MAX_CHAR_VIEW = ServerProperties.getProperty("CHANNEL_MAX_CHAR_VIEW", CHANNEL_MAX_CHAR_VIEW);

        USE_FIXED_IV = ServerProperties.getProperty("USE_FIXED_IV", USE_FIXED_IV);
    }

    static {
        loadSetting();
    }
}
