package me.messageofdeath.RPG.API;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;

import net.milkbowl.vault.economy.Economy;
import lib.PatPeter.SQLibrary.MySQL;
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
	public static FileConfiguration getConfig() {return plugin.config;}
	
	@SuppressWarnings("static-access")
	public static MySQL getMySQL() {return plugin.mysql;}
	
	public static RPG getPlugin() {return plugin;}
		
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
	public static void saveConfig() {try {Api.getConfig().save(Api.getPlugin().CFile);} catch (Exception e) {e.printStackTrace();}}
		
	@SuppressWarnings("static-access")
	public static void loadConfig() {try {Api.getConfig().load(Api.getPlugin().CFile);} catch (Exception e) {e.printStackTrace();}}
	
	public static int otherApp(int id) {
		ResultSet rs = Api.getMySQL().query("Select * FROM Quests WHERE NpcId = "+id);
		try {
			int i = 0;
			while(rs.first()) {
				i++;
			}
			return i;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 1;
	}
	
	public static boolean containsChar(String gf) {
		String[] string = gf.split(",");
		return string[0].matches(".*\\d.*");
	}
	
	public static String object1;
	public static String getObject1() {return object1;}
	
	public static String poop;
	public static String getMessage() {return poop;}
	
	public static String poop1;
	public static String getName() {return poop1;}
	
	public static String d;
	public static String getButtonLocation() {return d;}
	
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
