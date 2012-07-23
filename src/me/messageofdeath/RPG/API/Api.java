package me.messageofdeath.RPG.API;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;

import net.milkbowl.vault.economy.Economy;
import me.messageofdeath.RPG.RPG;

public class Api {
	
	private static RPG plugin;
	public Api(RPG instance) {plugin = instance;}
	public static User getUser(String name) {return new User(name);}
	
	@SuppressWarnings("static-access")
	public static String getLoggerPrefix() {return plugin.lprefix;}
	
	@SuppressWarnings("static-access")
	public static Economy getEconomy() {return plugin.economy;}
	
	@SuppressWarnings("static-access")
	public static FileConfiguration getDatabase() {return plugin.db;}
	
	@SuppressWarnings("static-access")
	public static File getFile() {return plugin.DBFile;}
	
	@SuppressWarnings("static-access")
	public static FileConfiguration getConfig() {return plugin.config;}
	
	public static RPG getPlugin() {return plugin;}
	
	public static String getActiveQuest(String name) {return getDatabase().getString("Users." + name + ".ActiveQuest");}
	
	public static ArrayList<String> button = new ArrayList<String>();
	public static ArrayList<String> getPlayers() {return button;}
	
	public static ArrayList<String> dbutton = new ArrayList<String>();
	public static ArrayList<String> getPlayer() {return dbutton;}
	
	public static ArrayList<String> ebutton = new ArrayList<String>();
	public static ArrayList<String> getPlay() {return ebutton;}
	
	public static ArrayList<String> lbutton = new ArrayList<String>();
	public static ArrayList<String> getPl() {return lbutton;}
	
	public static ArrayList<String> object = new ArrayList<String>();
	public static ArrayList<String> getObject() {return object;}
	
	@SuppressWarnings("static-access")
	public static void saveDatabase() {try {Api.getDatabase().save(Api.getPlugin().DBFile);} catch (Exception e) {e.printStackTrace();}}
	
	@SuppressWarnings("static-access")
	public static void saveConfig() {try {Api.getConfig().save(Api.getPlugin().CFile);} catch (Exception e) {e.printStackTrace();}}
	
	@SuppressWarnings("static-access")
	public static void loadDatabase() {try {Api.getConfig().load(Api.getPlugin().DBFile);} catch (Exception e) {e.printStackTrace();}}
	
	@SuppressWarnings("static-access")
	public static void loadConfig() {try {Api.getConfig().load(Api.getPlugin().CFile);} catch (Exception e) {e.printStackTrace();}}
	
	public static String object1;
	public static String getObject1() {return object1;}
	
	public static String poop;
	public static String getMessage() {return poop;}
	
	public static String poop1;
	public static String getName() {return poop1;}
	
	public static String loc;
	public static String getLocation() {return loc;}
	
	public static String compileArgs(String[] args, Integer startArg) {
		String argCompileStr = "";
		for (int i = startArg; i < args.length; i++) 
		{
			if (i != startArg) 
			{
				argCompileStr += " ";
			}
			argCompileStr += args[i];
		}
		return argCompileStr;
	}
}
