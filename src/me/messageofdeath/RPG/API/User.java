package me.messageofdeath.RPG.API;

import java.sql.ResultSet;
import java.sql.SQLException;
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
	// Name
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
		if(config.getRequirementItem() != null)sendMsg(ChatColor.GOLD + "Requirement: " + ChatColor.GRAY + config.getRequirementItem().getAmount() + " " + config.getRequirementItem().getType().name().toLowerCase().replace("_", " ") + "(s)");
		String reward = ChatColor.GOLD + "Reward: " + ChatColor.GRAY;
		if(config.getRewardMoney() != 0.0) {
			Eco eco = new Eco(getName(), config.getRewardMoney());
			reward = reward.concat(eco.getFormat());
			sendMsg(reward);
		}
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
	
	// Checking if null
	public boolean isImplemented() {
		ResultSet rs = Api.getMySQL().query("Select Name FROM Quests_Users WHERE Name = '"+name+"'");
        try {
            if (rs.first()) {
                try {
                    if(rs.getString("Name") != null) {
                    	return true;
                    }return false;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
       
            e.printStackTrace();
        }
        return false;
	}
	
	public void addToImplemented() {
		Api.getMySQL().query("INSERT INTO Quests_Users (Name, ActiveQuest, PendingQuest, CompletedQuests, Quest) VALUES ('"+name+"', 0, 0, '0', '0');");
	}
	
	// Database
	public int getPendingQuest() {
		ResultSet rs = Api.getMySQL().query("Select * FROM Quests_Users WHERE Name = '"+name+"'");
		try {
			if(rs.first()) {
				return rs.getInt("PendingQuest");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public void setPendingQuest(int quest) {
		Api.getMySQL().query("UPDATE Quests_Users SET PendingQuest = "+quest+" WHERE Name = '"+name+"'");
	}
	
	public int getActiveQuest() {
		ResultSet rs = Api.getMySQL().query("Select * FROM Quests_Users WHERE Name = '"+name+"'");
		try {
			if(rs.first()) {
				return rs.getInt("ActiveQuest");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public void setActiveQuest(int quest) {
		Api.getMySQL().query("UPDATE Quests_Users SET ActiveQuest = "+quest+" WHERE Name = '"+name+"'");
	}
	
	public String getCompletedQuests() {
		ResultSet rs = Api.getMySQL().query("Select * FROM Quests_Users WHERE Name = '"+name+"'");
		try {
			if(rs.first()) {
				return rs.getString("CompletedQuests");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setCompletedQuests(String quests) {
		Api.getMySQL().query("UPDATE Quests_Users SET CompletedQuests = '"+quests+"' WHERE Name = '"+name+"'");
	}
	
	public void setCompletedQuests(int quest) {
		Api.getMySQL().query("UPDATE Quests_Users SET CompletedQuests = "+quest+" WHERE Name = '"+name+"'");
	}
	
	public int getQuest() {
		ResultSet rs = Api.getMySQL().query("Select * FROM Quests_Users WHERE Name = '"+name+"'");
		try {
			if(rs.first()) {
				return rs.getInt("Quest");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public void setQuest(int quest) {
		Api.getMySQL().query("UPDATE Quests_Users SET Quest = "+quest+" WHERE Name = '"+name+"'");
	}
}
