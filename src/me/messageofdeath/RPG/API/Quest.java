package me.messageofdeath.RPG.API;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Quest {
	
	private int id, quest = 0;
	public Quest(int id) {
		this.id = id;
	}
	public Quest(int id, int quest) {
		this.id = id;
		this.quest = quest;
	}
	public int getNpcId() {
		return id;
	}
	
	public ArrayList<String> quests = new ArrayList<String>();
	
	public void addQuest(int id, String questname, String Objective, double money, ItemStack item, int d) {
		int i = Api.getConfig().getInt("Quest Amount");
		i = i + 1;
		Api.getConfig().set("Quest Amount", i);
		Api.saveConfig();
		Api.getMySQL().query("INSERT INTO Quests (NpcId, QuestName, QuestObjective, QuestId, RewardMoney, RequirementItem) VALUES ("+id+",'"+questname+"','"+Objective+"',"+i+","+money+", '"+item.getType().name()+","+item.getAmount()+","+item.getData().getData()+"')");
	}
	
	public void deleteQuest() {
		int i = Api.getConfig().getInt("Quest Amount");
		Api.getMySQL().query("DELETE * FROM Quests WHERE = '+id+'");
		Api.getConfig().set("QuestAmount", i - 1);
	}
	
	public ArrayList<String> getQuests() {
		ResultSet rs = Api.getMySQL().query("Select * FROM Quests WHERE NpcId = "+id+" AND Select * FROM Quests WHERE QuestId = "+quest);
		try {
			if(rs.next()) {
				quests.add(rs.getInt("RequiredQuest") + "," + rs.getInt("QuestId") + "");
				return quests;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Quest
	public String getQuestName() {
		ResultSet rs = Api.getMySQL().query("Select * FROM Quests WHERE NpcId = "+id);
		try {
			if(rs.first()) {
				return rs.getString("QuestName");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public String getObjective() {
		ResultSet rs = Api.getMySQL().query("Select * FROM Quests WHERE NpcId = "+id);
		try {
			if(rs.first()) {
				return rs.getString("QuestObjective");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public void setObjective(String objective) {
		Api.getMySQL().query("UPDATE Quests SET QuestObjective = '"+objective+"' WHERE NpcId = "+id);
	}
	public int getQuestID() {
		ResultSet rs = Api.getMySQL().query("Select * FROM Quests WHERE NpcId = "+id);
		try {
			if(rs.first()) {
				return rs.getInt("QuestId");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	public String getType() {
		ResultSet rs = Api.getMySQL().query("Select * FROM Quests WHERE NpcId = "+id);
		try {
			if(rs.first()) {
				return rs.getString("QuestType");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// Reward
	public double getRewardMoney() {
		ResultSet rs = Api.getMySQL().query("Select * FROM Quests WHERE NpcId = "+id);
		try {
			if(rs.first()) {
				return rs.getDouble("RewardMoney");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public ItemStack getRewardItem() {
		ResultSet rs = Api.getMySQL().query("Select * FROM quests WHERE NpcId = "+id);
		try {
			if(rs.first()) {
				if(Api.containsChar(rs.getString("RewardItem"))) {
					String[] item = rs.getString("RewardItem").split(",");
					Material mat = Material.getMaterial(item[0]);
					int amount = Integer.parseInt(item[1]);
					short data = Short.parseShort(item[2]);
					return new ItemStack(mat, amount, data);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	// Requirements
	
	public ItemStack getRequirementItem() {
		ResultSet rs = Api.getMySQL().query("Select * FROM quests WHERE NpcId = "+id);
		try {
			if(rs.first()) {
				if(Api.containsChar(rs.getString("RequirementItem"))) {
					String[] item = rs.getString("RequirementItem").split(",");
					Material mat = Material.getMaterial(item[0]);
					int amount = Integer.parseInt(item[1]);
					short data = Short.parseShort(item[2]);
					return new ItemStack(mat, amount, data);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public int getRequiredQuest() {
		ResultSet rs = Api.getMySQL().query("Select * FROM Quests WHERE NpcId = "+id);
		try {
			if(rs.first()) {
				return rs.getInt("RequiredQuest");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
