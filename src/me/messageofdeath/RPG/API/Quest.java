package me.messageofdeath.RPG.API;

import org.bukkit.inventory.ItemStack;

public class Quest {
	
	private String begin;
	public Quest(int id, int quest) {
		this.begin = "Quests." + id + ".Quests." + quest + ".";
	}	
	//Quest
	public String getQuestName() {return Api.getConfig().getString(begin + "Quest.Name");}
	public String getObjective() {return Api.getConfig().getString(begin + "Quest.Objective");}
	public int getQuestID() {return Api.getConfig().getInt(begin + "Quest.Quest Id");}
	public String getType() {return Api.getConfig().getString(begin + "Quest.Quest Type");}
	
	// Reward
	public double getRewardMoney() {return Api.getConfig().getDouble(begin + "Reward.Money");}
	public ItemStack getRewardItem() {return Api.getConfig().getItemStack(begin + "Reward.Item");}
	
	// Requirements
	public ItemStack getRequirementItem() {return Api.getConfig().getItemStack(begin + "Requirement.Item");}
	
	// Save
	public void save() {Api.saveConfig();}
}
