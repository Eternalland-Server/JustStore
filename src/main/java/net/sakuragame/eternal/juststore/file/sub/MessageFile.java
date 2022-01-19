package net.sakuragame.eternal.juststore.file.sub;

import com.taylorswiftcn.justwei.util.MegumiUtil;
import net.sakuragame.eternal.juststore.JustStore;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class MessageFile {
    private static YamlConfiguration message;

    public static List<String> help;
    public static List<String> adminHelp;

    public static String noPermission;

    public static void init() {
        message = JustStore.getFileManager().getMessage();

        help = getStringList("help");
        adminHelp = getStringList("admin-help");

        noPermission = getString("message.no-permission");
    }

    private static String getString(String path) {
        return MegumiUtil.onReplace(message.getString(path));
    }

    private static List<String> getStringList(String path) {
        return MegumiUtil.onReplace(message.getStringList(path));
    }


}
