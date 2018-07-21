package com.mcml.space.config;

import org.bukkit.Bukkit;

import com.mcml.space.util.Configurable;
import com.mcml.space.util.VersionLevel;
import com.mcml.space.util.VersionLevel.Version;

public abstract class Patches extends Configurable {
	@Locale
	@Node("AntiWEcalc.WarnMessage")
    public static String AntiWEcalcWarnMessage = "§a§l[EscapeLag]§c禁止利用WE bug炸服! =.=";

    @Node("AntiLongStringCrash.enable")
    public static boolean AntiLongStringCrashenable = true;

    @Node("AntiLongStringCrash.WarnMessage")
    public static String AntiLongStringCrashWarnMessage = "§c警告！严禁利用长字符串来导致服务器崩溃！";

    @Locale
    @Node("AntiFakeDeath.KickMessage")
    public static String messageFakedeath = "§c严禁卡假死BUG！";

    @Node("AntiFakeDeath.enable")
    public static boolean noFakedeath = true;

    @SuppressWarnings("deprecation")
    @Node("NoDoubleOnline.enable")
    public static boolean fixDupeOnline = !Bukkit.getOnlineMode() && !(VersionLevel.isHigherEquals(Version.MINECRAFT_1_7_R4) &&
            VersionLevel.isSpigot() ? Bukkit.getServer().spigot().getConfig().getBoolean("settings.bungeecord") : false); // since 1.7.10

    @Locale
    @Node("NoDoubleOnline.KickMessage")
    public static String messageKickDupeOnline = "抱歉，服务器中您已经在线了。ԅ(¯ㅂ¯ԅ)";

    @Node("AntiBedExplode.enable")
    public static boolean noBedExplore = true;

    @Node("AntiBreakUseingChest.enable")
    public static boolean protectUsingChest = true;

    @Node("AntiCheatBook.enable")
    public static boolean noCheatBook = true;

    @Node("AntiCrashSign.enable")
    public static boolean fixCrashSign = true;

    @Node("AntiDupeDropItem.enable")
    public static boolean fixDupeDropItem = true;
    
    @Node("patches.vaildate-actions.enable")
    public static boolean enableVaildateActions = false;

    @Node("AntiSkullCrash.enable")
    public static boolean noSkullCrash = true;

    @Node("AntiCrashChat.enable")
    public static boolean noCrashChat = true;

    @Locale
    @Node("AntiCrashChat.SpecialStringWarnMessage")
    public static String AntiCrashChatSpecialStringWarnMessage = "§c严禁使用崩服代码炸服！";

    @Locale
    @Node("AntiCrashChat.ColorChatWarnMessage")
    public static String AntiCrashChatColorChatWarnMessage = "§c抱歉！为了防止服务器被破坏，服务器禁止使用颜色代码.";

    @Locale
    @Node("AntiBreakUseingChest.WarnMessage")
    public static String AntiBreakUsingChestWarnMessage = "§c抱歉！您不可以破坏一个正在被使用的容器";

    @Locale
    @Node("AntiBedExplode.TipMessage")
    public static String AntiBedExplodeTipMessage = "§r你不能在这里睡觉";

    @Locale
    @Node("AntiCrashSign.WarnMessage")
    public static String AntiCrashSignWarnMessage = "§c您输入的内容太长了！";

}