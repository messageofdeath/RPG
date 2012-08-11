package me.messageofdeath.RPG.Listeners;

import me.messageofdeath.RPG.RPG;
import me.messageofdeath.RPG.API.Api;
import me.messageofdeath.RPG.API.Eco;
import me.messageofdeath.RPG.API.Quest;
import me.messageofdeath.RPG.API.User;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class questListener implements Listener {
	
	public RPG plugin;
	public questListener(RPG instance) {
		plugin = instance;
	}
		
	@EventHandler
	public void quest(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			if(NPCManager.isNPC(event.getEntity())) {
				HumanNPC npc = NPCManager.get(event.getEntity());
				User user = Api.getUser(player.getName());
				if(user.isImplemented() == false)user.addToImplemented();
				Quest quest = null;
				quest = new Quest(npc.getUID());
				if(quest.getQuests().size() > 0) {
					int i = 0;
					while(i != quest.getQuests().size()) {
						quest = new Quest(quest.getQuestID(), i);
						if(user.getCompletedQuests().contains(quest.getQuestID() + "")) {
							i = quest.getQuests().size();
						}else i++;
					}
				}
				if(quest.getQuestName() != null) {
					if(!user.getCompletedQuests().contains(String.valueOf(quest.getQuestID()))) {
						if(user.getInventory().contains(quest.getRequirementItem()) && user.getActiveQuest() == quest.getQuestID()) {
							user.sendMsg(ChatColor.DARK_RED + "You have finished the quest " + ChatColor.GOLD + quest.getQuestName());
							Eco eco = new Eco(user.getName(), quest.getRewardMoney());
							eco.give();
							user.sendMsg(ChatColor.DARK_RED + "You recieved " + eco.getFormat());
							user.getInventory().removeItem(quest.getRequirementItem());
							user.setCompletedQuests(user.getCompletedQuests() + "," + String.valueOf(user.getActiveQuest()));
							user.setActiveQuest(0);
							user.setQuest(0);
						}else{
							if(!(user.getActiveQuest() == quest.getQuestID())) {
								if(user.getActiveQuest() == 0) {
									user.sendQuest(quest);
									user.setWaiting(true);
									user.setPendingQuest(quest.getQuestID());
									user.setQuest(npc.getUID());
								}else{
									user.sendMsg(ChatColor.DARK_RED + "You already have a quest out!");
								}
							}else{
								user.sendMsg(ChatColor.DARK_RED + "You already have the quest!");
							}
						}
					}else{
						user.sendMsg(ChatColor.DARK_RED + "You already finished this quest!");
					}
				}
			}
		}
	}
}

/*
@EventHandler
public void quest(EntityDamageByEntityEvent event) {
	if(event.getDamager() instanceof Player) {
		Player player = (Player) event.getDamager();
		if(NPCManager.isNPC(event.getEntity())) {
			HumanNPC npc = NPCManager.get(event.getEntity());
			User user = Api.getUser(player.getName());
			if(Api.getConfig().get("Quests." + npc.getName()) == null)return;
			Object[] regions = Api.getConfig().getConfigurationSection("Quests." + npc.getName()).getKeys(false).toArray();
			WorldGuardPlugin worldguard = plugin.getWorldGuard();
			RegionManager region = worldguard.getRegionManager(Bukkit.getWorld("world"));
			Location loc = new Location(npc.getWorld(), npc.getLocation().getBlockX(), npc.getLocation().getBlockY(), npc.getLocation().getBlockZ());
			if(getRegion(region, regions, loc) == true) {
				Object[] quests = Api.getConfig().getConfigurationSection("Quests." + npc.getName() + "." + this.region).getKeys(false).toArray();
				getQuest(quests);
				Conf config = new Conf(npc.getName(), this.region, this.quest, null);
				if(user.isImplemented() == false)user.addToImplemented();
				if(!user.getCompletedQuests().contains(String.valueOf(config.getQuestID()))) {
					if(user.getInventory().contains(config.getItem()) && user.getActiveQuest() == config.getQuestID()) {
						user.sendMsg(ChatColor.DARK_RED + "You have finished the quest " + ChatColor.GOLD + config.getName());
						Eco eco = new Eco(user.getName(), config.getAmount());
						eco.give();
						user.sendMsg(ChatColor.DARK_RED + "You recieved " + eco.getFormat());
						user.getInventory().removeItem(config.getItem());
						user.setCompletedQuests(user.getCompletedQuests() + "," + String.valueOf(user.getActiveQuest()));
						user.setActiveQuest(0);
						user.setQuest(0);
						Api.saveDatabase();
					}else{
						if(!(user.getActiveQuest() == config.getQuestID())) {
							if(user.getActiveQuest() == 0) {
								user.sendQuest(config);
								user.setWaiting(true);
								user.setPendingQuest(config.getQuestID());
								user.setQuest(npc.getName() + "," + this.region + "," + this.quest);
								Api.saveDatabase();
							}else{
								user.sendMsg(ChatColor.DARK_RED + "You already have a quest out!");
							}
						}else{
							user.sendMsg(ChatColor.DARK_RED + "You already have the quest!");
						}
					}
				}else{
					user.sendMsg(ChatColor.DARK_RED + "You already finished this quest!");
				}
			}
		}
	}
}
String region, quest;
public boolean getRegion(RegionManager region, Object[] g, Location loc) {
	int i = g.length;
	i = i - 1;
	while(i != -1) {
		if(region.getRegion(g[i].toString()).contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
			this.region = g[i].toString();
			i = -1;
			return true;
		}
		i--;
	}
	return false;
}

public void getQuest(Object[] g) {
	int d = 0;
	quest = g[d].toString();
}*/