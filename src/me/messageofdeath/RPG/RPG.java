package me.messageofdeath.RPG;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class RPG extends JavaPlugin {
	
	public static String lprefix;
	public static File DBFile;
	public static File CFile;
	public static FileConfiguration db;
	public static FileConfiguration config;
	public static Economy economy = null;
	
	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = getDescription();
		PluginManager pm = getServer().getPluginManager();
		Plugin citizens = pm.getPlugin("Citizens");
		lprefix = "[" + pdfFile.getName() + "] v" + pdfFile.getVersion() + ": ";
		if(citizens.isEnabled()) {
			log("is now enabling...");
			getWorldGuard();
			getCommand("rpg").setExecutor(new rpgCommand());
			pm.registerEvents(new questListener(this), this);
			pm.registerEvents(new playerListener(), this);
			DBFile = new File(this.getDataFolder(), "database.yml");
			CFile = new File(this.getDataFolder(), "config.yml");
			try {firstRun();} catch (Exception e) {e.printStackTrace();}
			config = new YamlConfiguration();
			db = new YamlConfiguration();
			loadYamls();
			setupEconomy();
			log("is now enabled!");
		}else{
			log("Citizens is now enabled! Shutting down...");
			pm.disablePlugin(this);
		}
	} 
	
	@Override
	public void onDisable() {
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
		if (!this.DBFile.exists()) {
			this.DBFile.getParentFile().mkdirs();
			copy(getResource("database.yml"), this.DBFile);
		}
		if(!this.CFile.exists()) {
			this.CFile.getParentFile().mkdirs();
			copy(getResource("config.yml"), CFile);
		}
	}
	
	public WorldGuardPlugin getWorldGuard() {
	    Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldGuard");
	    if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
	    	this.getServer().getPluginManager().disablePlugin(Api.getPlugin());
	    }
	    return (WorldGuardPlugin) plugin;
	}
	
	public void loadYamls() {
		try {
			db.load(DBFile);
			config.load(CFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void saveYamls() {
		try {
			db.save(DBFile);
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