package me.messageofdeath.RPG.API;

public class Button {
	
	private String location;
	public Button(String loc) {
		location = loc;
	}
	// Locations
	public String getButtonLocation() {return Api.getConfig().getString("ButtonLocations." + location + ".Location");}
	public String getButtonName() {return Api.getConfig().getString("ButtonLocations." + location + ".Name");}
	public String getButtonMessage() {return Api.getConfig().getString("ButtonLocations." + location + ".Message");}
		
	public void setButtonLocation(String location) {Api.getConfig().set("ButtonLocations." + this.location + ".Location", location);}
	public void setButtonName(String name) {Api.getConfig().set("ButtonLocations." + location + ".Name", name);}
	public void setButtonMessage(String message) {Api.getConfig().set("ButtonLocations." + location + ".Message", message);}
	
	// Save
	public void save() {Api.saveConfig();}
}
