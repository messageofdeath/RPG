package me.messageofdeath.RPG.API;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Button {
	
	private String location;
	public Button(String loc) {
		location = loc;
	}
	// Locations
	public String getButtonLocation() {
		ResultSet rs = Api.getMySQL().query("Select * FROM Quests_Buttons WHERE ButtonLocation = '"+location+"'");
		try {
			if(rs.first()) {
				return rs.getString("Location");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public String getButtonName() {
		ResultSet rs = Api.getMySQL().query("Select * FROM Quests_Buttons WHERE ButtonLocation = '"+location+"'");
		try {
			if(rs.first()) {
				return rs.getString("Name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public String getButtonMessage() {
		ResultSet rs = Api.getMySQL().query("Select * FROM Quests_Buttons WHERE ButtonLocation = '"+location+"'");
		try {
			if(rs.first()) {
				return rs.getString("Message");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
		
	public void setButtonLocation(String location) {
		Api.getMySQL().query("UPDATE Quests_Buttons SET Location = '"+location+"' WHERE ButtonLocation = "+this.location);
	}
	public void setButtonName(String name) {
		Api.getMySQL().query("UPDATE Quests_Buttons SET Name = '"+name+"' WHERE ButtonLocation = "+this.location);
	}
	public void setButtonMessage(String message) {
		Api.getMySQL().query("UPDATE Quests_Buttons SET Message = '"+message+"' WHERE ButtonLocation = "+this.location);
	}
	
	public void deleteButton() {
		Api.getMySQL().query("DELETE FROM Quests_Buttons WHERE ButtonLocation= '"+location+"'");
	}
	
	public void addButton(String name, String message, String buttonlocation, String location) {
		name = name.replace("_", " ");
		Api.getMySQL().query("INSERT INTO Quests_Buttons (Name, Message, ButtonLocation, Location) VALUES ('"+name+"','"+message+"','"+buttonlocation+"','"+location+"')");
	}
}