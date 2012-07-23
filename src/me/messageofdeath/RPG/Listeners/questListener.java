package me.messageofdeath.RPG.Listeners;

import me.messageofdeath.RPG.RPG;
import me.messageofdeath.RPG.API.Api;
import me.messageofdeath.RPG.API.Eco;
import me.messageofdeath.RPG.API.Quest;
import me.messageofdeath.RPG.API.User;
import net.citizensnpcs.resources.npclib.HumanNPC;
import net.citizensnpcs.resources.npclib.NPCManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.QPDecoderStream;

public class questListener implements Listener {
	
	public RPG plugin;
	public questListener(RPG instance) {
		plugin = instance;
	}
	
	private int IQ = 0;
	
	@EventHandler
	public void quest(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			if(NPCManager.isNPC(event.getEntity())) {
				HumanNPC npc = NPCManager.get(event.getEntity());
				User user = Api.getUser(player.getName());
				Location loc = new Location(npc.getWorld(), npc.getLocation().getBlockX(), npc.getLocation().getBlockY(), npc.getLocation().getBlockZ());
				if(user.isImplemented() == false)user.addToImplemented();
				Object[] d = Api.getConfig().getStringList("Quest list").toArray();
				String type = getQuestType(d, npc.getUID());
				if(IQ == 1) {
					Quest quest = null;
					if(type.equalsIgnoreCase("Item")) {
						quest = new Quest(npc.getUID(), 1);
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
								Api.saveDatabase();
							}else{
								if(!(user.getActiveQuest() == quest.getQuestID())) {
									if(user.getActiveQuest() == 0) {
										user.sendQuest(quest);
										user.setWaiting(true);
										user.setPendingQuest(quest.getQuestID());
										user.setQuest(npc.getName());
										Api.saveDatabase();
									}else{
										user.sendMsg(ChatColor.DARK_RED + "You already have a quest out!");
									}
								}else{
									user.sendMsg(ChatColor.DARK_RED + "You already have the quest!");
								}
							}
						}
					}
					if(type.equalsIgnoreCase("Kill")) {
											
					}
					if(type.equalsIgnoreCase("Break")) {
						
					}
					if(type.equalsIgnoreCase("Place")) {
						
					}
				}
				if(IQ > 1) {
					
				}
			} 
		}
	}
	public String getQuestType(Object[] q, int UID) {
		int i = q.length;
		int iq = 0;
		i = i - 1;
		String type = null;
		while(i != -1) {
			if(q[i].toString().equalsIgnoreCase(UID + ",Item")) {
				if(type == "")type = "Item"; else type = type.concat(",Item");
				i = 0;
				iq++;
			}
			if(q[i].toString().equalsIgnoreCase(UID + ",Killing")) {
				if(type == "")type = "Kill"; else type = type.concat(",Kill");
				i = 0;
				iq++;
			}
			if(q[i].toString().equalsIgnoreCase(UID + ",Breaking")) {
				if(type == "")type = "Break"; else type = type.concat(",Break");
				i = 0;
				iq++;
			}
			if(q[i].toString().equalsIgnoreCase(UID + ",Placeing")) {
				if(type == "")type = "Place"; else type = type.concat(",Place");
				i = 0;
				iq++;
			}
			i--;
		}
		IQ = iq;
		return type;
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