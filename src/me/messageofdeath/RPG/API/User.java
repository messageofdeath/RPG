package me.messageofdeath.RPG.API;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class User {
	
	private String name;
	private Player player;
	public User(String name) {
		this.name = name;
		this.player = Bukkit.getServer().getPlayer(name);
	}
	// Names
	public String getName() {return name;}
	
	// Permission
	public boolean hasPermission(String perm) {return player.hasPermission(perm);}
	
	// Played before
	public boolean hasPlayedBefore() {return player.hasPlayedBefore();}
	
	// Message
	public void sendMsg(String msg) {player.sendMessage(msg);}
	
	public void sendQuest(Quest config) {
		sendMsg(ChatColor.GOLD + "Quest: " + ChatColor.GREEN + config.getQuestName());
		sendMsg(ChatColor.GOLD + "Objectives: " + ChatColor.GRAY + config.getObjective());
		sendMsg(ChatColor.GOLD + "Requirement: " + ChatColor.GRAY + config.getRequirementItem().getAmount() + " " + config.getRequirementItem().getType().name().toLowerCase().replace("_", " ") + "(s)");
		String reward = ChatColor.GOLD + "Reward: " + ChatColor.GRAY;
		if(config.getRewardMoney() != 0.0) {
			Eco eco = new Eco(getName(), config.getRewardMoney());
			reward = reward.concat(eco.getFormat());
		}
		if(config.getRewardItem() != null) {
			if(config.getRewardMoney() != 0.0)reward = reward.concat(" " + config.getRewardItem().getType().name().toLowerCase().replace("_", " ")); 
			else reward = reward.concat(config.getRewardItem().getType().name().toLowerCase().replace("_", " "));
		}
		if(config.getRewardItem() == null && config.getRewardMoney() == 0.0) {
			sendMsg(ChatColor.GOLD + "Reward: " + ChatColor.GRAY + "No Reward");
		}else sendMsg(reward);
		sendMsg(ChatColor.DARK_RED + "Do you " + ChatColor.GOLD + "Accept" + ChatColor.DARK_RED + " or " + ChatColor.GOLD + "Decline" + ChatColor.DARK_RED + " this Quest?");
	}
	
	// Lists (Waitings)
	public static ArrayList<String> waiting = new ArrayList<String>();
	public ArrayList<String> getWaiting() {return waiting;}
	
	public ItemStack getItemInHand() {return player.getItemInHand();}
	
	public PlayerInventory getInventory() {return player.getInventory();}
	
	public void setWaiting(boolean wait) {
		if(wait == true) {
			if(!waiting.contains(name)) {
				waiting.add(name);
			}
		}
		if(wait == false) {
			waiting.remove(name);
		}
	}
	
	public boolean isWaiting() {
		if(waiting.contains(name)) {
			return true;
		}return false;
	}
	
	public void setLocation(String loc) {Api.getDatabase().set("Users." + name + ".Location", loc);}
	public String getLocation() {return Api.getDatabase().getString("Users." + name + ".Location");}
	
	// Checking if null
	public boolean isImplemented() {if(Api.getDatabase().isSet("Users." + name) == true)return true;return false;}
	
	@SuppressWarnings("static-access")
	public void addToImplemented() {setActiveQuest(0);setPendingQuest(0);setCompletedQuests(0);setQuest("0");try {Api.getDatabase().save(Api.getPlugin().DBFile);} catch (IOException e) {e.printStackTrace();}}
	
	// Database
	public int getPendingQuest() {return Api.getDatabase().getInt("Users." + name + ".PendingQuest");}
	
	public void setPendingQuest(int quest) {Api.getDatabase().set("Users." + name + ".PendingQuest", quest);}
	
	public int getActiveQuest() {return Api.getDatabase().getInt("Users." + name + ".ActiveQuest");}
	
	public void setActiveQuest(int quest) {Api.getDatabase().set("Users." + name + ".ActiveQuest", quest);}
	
	public String getCompletedQuests() {return Api.getDatabase().getString("Users." + name + ".CompletedQuests");}
	
	public void setCompletedQuests(String quests) {Api.getDatabase().set("Users." + name + ".CompletedQuests", quests);}
	
	public void setCompletedQuests(int quest) {Api.getDatabase().set("Users." + name + ".CompletedQuests", quest);}
	
	public String getQuest() {return Api.getDatabase().getString("Users." + name + ".Quest");}
	
	public void setQuest(String quest) {Api.getDatabase().set("Users." + name + ".Quest", quest);}
	public void setQuest(int quest) {Api.getDatabase().set("Users." + name + ".Quest", quest);}
}
