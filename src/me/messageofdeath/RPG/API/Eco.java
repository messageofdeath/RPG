package me.messageofdeath.RPG.API;

public class Eco {
	
	private String name; private double amount;
	public Eco(String name, double amount) {
		this.name = name;
		this.amount = amount;
	}
	
	public boolean hasEnough() {if(Api.getEconomy().has(name, amount))return true; return false;}
	
	public boolean hasAccount() {if(Api.getEconomy().hasAccount(name))return true; return false;}
	
	public void charge() {if(hasEnough() == true)Api.getEconomy().withdrawPlayer(name, amount);}
	
	public void give() {Api.getEconomy().depositPlayer(name, amount);}
	
	public String getFormat() {return Api.getEconomy().format(amount);}
	
	public void newAccount() {Api.getEconomy().createPlayerAccount(name);}
}
