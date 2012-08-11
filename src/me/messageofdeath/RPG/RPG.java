package me.messageofdeath.RPG;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lib.PatPeter.SQLibrary.MySQL;
import me.messageofdeath.RPG.API.Api;
import me.messageofdeath.RPG.Commands.rpgCommand;
import me.messageofdeath.RPG.Listeners.playerListener;
import me.messageofdeath.RPG.Listeners.questListener;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class RPG extends JavaPlugin {
	
	public static String lprefix;
	public static File CFile;
	public static FileConfiguration config;
	public static Economy economy;
	public static MySQL mysql;
	
	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = getDescription();
		PluginManager pm = getServer().getPluginManager();
		lprefix = "[" + pdfFile.getName() + "] v" + pdfFile.getVersion() + ": ";
		log("is now enabling...");
		log("is Citizens running?");
		Plugin citizens = pm.getPlugin("Citizens");
		if(citizens.isEnabled()) {
			log("Citizens is enabled. Continuing");
			log("Registering Commands...");
			getCommand("rpg").setExecutor(new rpgCommand());
			log("Registered Commands!");
			log("Registering Listeners...");
			pm.registerEvents(new questListener(this), this);
			pm.registerEvents(new playerListener(), this);
			log("Registered Listeners!");
			log("Checking Config.yml");
			CFile = new File(this.getDataFolder(), "config.yml");
			try {firstRun();} catch (Exception e) {e.printStackTrace();}
			config = new YamlConfiguration();
			loadYamls();
			log("Config.yml loaded");
			log("Hooking into MySQL!");
			sqlConnection();
	        sqlDoesDatabaseExist();
	        log("is now connected to MySQL!");
	        log("Hooking into Vault economy!");
			setupEconomy();
			log("Hooked into Vault economy!");
			log("is now enabled!");
		}else{
			log("Citizens is not enabled! Shutting down...");
			pm.disablePlugin(this);
		}
	} 
	
	@Override
	public void onDisable() {
		mysql.close();
		log("is now disabled!");
	}
	
	public void log(String log) {
		Bukkit.getLogger().info(Api.getLoggerPrefix() + log);
	}
	
	@SuppressWarnings("rawtypes")
	private Boolean setupEconomy() {
		RegisteredServiceProvider economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
		if (economyProvider != null) {
			economy = (Economy)economyProvider.getProvider();
		}
		if (economy != null) return Boolean.valueOf(true); return Boolean.valueOf(false);
	}
	
	@SuppressWarnings("static-access")
	private void firstRun() throws Exception {
		if(!this.CFile.exists()) {
			this.CFile.getParentFile().mkdirs();
			copy(getResource("config.yml"), CFile);
		}
	}
	public void sqlConnection() {
		mysql = new MySQL(Bukkit.getLogger(), "[RPG]", config.getString("MySQL.Host"), config.getString("MySQL.Port")
				, config.getString("MySQL.Database"), config.getString("MySQL.User"), config.getString("MySQL.Password"));
		mysql.open();
	}
	
	public void sqlDoesDatabaseExist() {
		if(!mysql.checkTable("Quests")) {
			log("Error: The Table Quests does not exist creating...");
			mysql.query("CREATE TABLE Quests (NpcId INT PRIMARY KEY, QuestName VARCHAR(100), QuestObjective VARCHAR(300), QuestId INT, QuestType VARCHAR(50), RewardItem VARCHAR(100), RewardMoney DOUBLE, RequirementItem VARCHAR(100), RequiredQuest INT);");
			log("The Table Quests has been created!");
		}
		if(!mysql.checkTable("Quests_Users")) {
			log("Error: The Table Quests_Users does not exist creating...");
			mysql.query("CREATE TABLE Quests_Users (Name VARCHAR(50), ActiveQuest INT, PendingQuest INT, CompletedQuests VARCHAR(600), Quest VARCHAR(100));");
			log("The Table Quests_Users has been created!");
		}
		if(!mysql.checkTable("Quests_Buttons")) {
			log("Error: The Table Quests_Buttons does not exist creating...");
			mysql.query("CREATE TABLE Quests_Buttons (Name VARCHAR(100), Message VARCHAR(150), ButtonLocation VARCHAR(50), Location VARCHAR(100));");
			log("The Table Quests_Buttons has been created!");
		}
    }
	
	public void loadYamls() {
		try {
			config.load(CFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void saveYamls() {
		try {
			config.save(CFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void copy(InputStream in, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
			saveYamls();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}