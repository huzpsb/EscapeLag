package com.mcml.space.command;

import com.mcml.space.config.ConfigMain;
import com.mcml.space.core.EscapeLag;

public class LocalizedHelper {
	public static String PleaseEnterelToHelp;
	public static String Sucesstoopenupdate;
	public static String HelpLine1;
	public static String HelpLine2;
	public static String HelpLine3;
	public static String HelpLine4;
	public static String HelpLine5;
	public static String HelpLine6;
	public static String HelpLine7;
	public static String HelpLine8;
	public static String PluginPrefixLine;

	public static void init() {
		if(ConfigMain.lang.equalsIgnoreCase("zh_cn")) {
			PleaseEnterelToHelp = "§c请输入/el help 来获取帮助";
			Sucesstoopenupdate = "§a§l[EscapeLag]§b已经成功开启自动更新！";
			HelpLine1 = "§e/el reload 重载插件";
			HelpLine2 = "§e/el chunkkeeper 查看关于区块保持者的帮助";
			HelpLine3 = "§e/el heap 查阅关于内存清理和分配的内容";
			HelpLine4 = "§e/el autosave 查阅关于自动储存的内容";
			HelpLine5 = "§e/el tps 查阅关于TPS和主线程";
			HelpLine6 = "§e/el autoset 查阅关于自动配端";
			HelpLine7 = "§e/el antiattack 查阅关于反压测模块";
			HelpLine8 = "§e/el monitor 查阅关于插件耗能侦测";
			PluginPrefixLine = "§b------§a§lEscapeLag - §e版本 " + EscapeLag.PluginMain.getDescription().getVersion() + "§b------";
		}
		if(ConfigMain.lang.equalsIgnoreCase("en_GB")) {
			PleaseEnterelToHelp = "§cPlease enter /el help to get help";
			Sucesstoopenupdate = "§a§l[EscapeLag]§bSucess to open AutoUpdate!";
			HelpLine1 = "§e/el reload ReloadPlugin";
			HelpLine2 = "§e/el chunkkeeper ask help for ChunkKeeper";
			HelpLine3 = "§e/el heap ask help for Heap";
			HelpLine4 = "§e/el autosave ask help for AutoSave";
			HelpLine5 = "§e/el tps ask help for tps";
			HelpLine6 = "§e/el autoset ask help for AutoSet model";
			HelpLine7 = "§e/el antiattack install and ask help for AntiAttack model";
			HelpLine8 = "§e/el monitor enter Plugin Used monitor";
			PluginPrefixLine = "§b------§a§lEscapeLag - §eVer " + EscapeLag.PluginMain.getDescription().getVersion() + "§b------";
		}
	}
}
