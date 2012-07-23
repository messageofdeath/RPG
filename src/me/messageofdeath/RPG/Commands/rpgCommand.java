package me.messageofdeath.RPG.Commands;

import java.util.ArrayList;

import me.messageofdeath.RPG.API.Api;
import me.messageofdeath.RPG.API.Conf;
import me.messageofdeath.RPG.API.User;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class rpgCommand implements CommandExecutor {
	
	ArrayList<String> commands = new ArrayList<String>();
	
	// /rpg setbutton <message>
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = (Player) sender;
		User user = Api.getUser(player.getName());
		if(cmd.getName().equalsIgnoreCase("rpg")) {
			commands.add("save");
			commands.add("objective");
			commands.add("reload");
			commands.add("help");
			commands.add("setlocation");
			commands.add("deletebutton");
			commands.add("resetlocation");
			commands.add("setbutton");
			commands.add("setmessage");
			commands.add("setobjective");
			commands.add("deletequest");
			commands.add("newquest");
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("save")) {
					int x = player.getLocation().getBlockX();
					int y = player.getLocation().getBlockY();
					int z = player.getLocation().getBlockZ();
					user.setLocation(player.getLocation().getWorld().getName() + "," + x + "," + y + "," + z);
					Api.saveDatabase();
					user.sendMsg(ChatColor.GOLD + "Successfully saved Location!");
				}
				if(args[0].equalsIgnoreCase("objective")) {
					if(user.getActiveQuest() != 0) {
						String[] stuff = user.getQuest().split(",");
						Conf config = new Conf(stuff[0], stuff[1], stuff[2], null);
						if(config.getObjective() != null) {
							user.sendMsg(ChatColor.GOLD + "Objective: " + ChatColor.GRAY + config.getObjective());
						}else{user.sendMsg(ChatColor.DARK_RED + "This quest does not exist! Please notify a admin!");}
					}else{
						user.sendMsg(ChatColor.RED + "You do not have a active quest!");
					}
				}
			}
			if(args.length >= 1) {
				if(!commands.contains(args[0])) {
					user.sendMsg(ChatColor.DARK_RED + "That command does not exist! " + ChatColor.GOLD + Api.compileArgs(args, 0));
				}
			}
			if(args.length == 0) {
				user.sendMsg(ChatColor.BLUE + "/rpg help");
			}
			if(user.hasPermission("rpg.main")) {
				if(args.length == 1) {
					if(args[0].equalsIgnoreCase("reload")) {
						Api.loadConfig();
						Api.loadDatabase();
						user.sendMsg(ChatColor.GOLD + "Successfully reloaded the Database and Config file!");
					}
					if(args[0].equalsIgnoreCase("help")) {
						user.sendMsg(ChatColor.GOLD + " _______________________________");
						user.sendMsg(ChatColor.GOLD + "|__________-[RPG Help]-__________|");
						user.sendMsg(ChatColor.BLUE + "/rpg help");
						user.sendMsg(ChatColor.GOLD + "Reload config and database || /rpg reload");
						user.sendMsg(ChatColor.DARK_RED + "How to make a Quest");
						user.sendMsg(ChatColor.BLUE + "1.) /rpg newquest <Npc Name> <Region Name> <Quest Name | Spaced are created with _ > <Money MUST BE  IN 100.0 format!> <Item> <Amount of Item> <Objective How long you want it to be.");
						user.sendMsg(ChatColor.DARK_RED + "How to delete a Quest");
						user.sendMsg(ChatColor.BLUE + "1.) /rpg deletequest <Npc name> <Region Name> <Quest Name | Spaced are created with _>");
						user.sendMsg(ChatColor.DARK_RED + "How to make a Button");
						user.sendMsg(ChatColor.BLUE + "1.) /rpg setbutton <name of warp | Spaced are created with _> <message>");
						user.sendMsg(ChatColor.BLUE + "2.) Select the button by left clicking");
						user.sendMsg(ChatColor.BLUE + "3.) Stand where you want the teleportation to go to and type  /rpg setlocation");
						user.sendMsg(ChatColor.DARK_RED + "How to delete a Button");
						user.sendMsg(ChatColor.BLUE + "1.) /rpg deletebutton 2.) Left click the button");
						user.sendMsg(ChatColor.DARK_RED + "How to edit the message for a button");
						user.sendMsg(ChatColor.BLUE + "1.) /rpg setmessage <name of warp | Spaced are created with _> <message>");
						user.sendMsg(ChatColor.DARK_RED + "How to edit the location for a button");
						user.sendMsg(ChatColor.BLUE + "1.) /rpg resetlocation  2.) Left click button");
						user.sendMsg(ChatColor.BLUE + "3.) /rpg setlocation");
						user.sendMsg(ChatColor.DARK_RED + "How to edit the objective for a quest");
						user.sendMsg(ChatColor.BLUE + "1.) /rpg setobjective <npc name> <region> <quest name> 2.) In chat type your objective");
					}
					if(args[0].equalsIgnoreCase("setlocation")) {
						if(Api.getPlayers().contains(user.getName())) {
							String loc = player.getWorld().getName() + "," + player.getLocation().getX() + "," + player.getLocation().getY() + "," + player.getLocation().getZ() + "," + player.getLocation().getYaw() + "," + player.getLocation().getPitch();
							Conf config = new Conf(null, null, null, Api.getLocation());
							config.setButtonLocation(loc);
							config.save();
							Api.getPlayers().remove(user.getName());
							user.sendMsg(ChatColor.GOLD + "Successfully created a button!");
							return false;
						}
						if(Api.getPl().contains(user.getName())) {
							String loc = player.getWorld().getName() + "," + player.getLocation().getX() + "," + player.getLocation().getY() + "," + player.getLocation().getZ() + "," + player.getLocation().getYaw() + "," + player.getLocation().getPitch();
							Conf config = new Conf(null, null, null, Api.getLocation());
							config.setButtonLocation(loc);
							config.save();
							Api.getPl().remove(user.getName());
							user.sendMsg(ChatColor.GOLD + "Successfully reset the location of the button!");
							return false;
						}else{
							user.sendMsg(ChatColor.DARK_RED + "You must be resetting a button or making a button!");
						}
					}
					if(args[0].equalsIgnoreCase("resetlocation")) {
						if(!Api.getPl().contains(user.getName())) {
							Api.getPl().add(user.getName());
							user.sendMsg(ChatColor.GREEN + "Select a button you wish to reset the location of");
						}else{
							user.sendMsg(ChatColor.RED + "You already used this command!");
							user.sendMsg(ChatColor.GREEN + "Select a button you wish to reset the location of");
						}
					}
					if(args[0].equalsIgnoreCase("deletebutton")) {
						if(!Api.getPlayer().contains(user.getName())) {
							Api.getPlayer().add(user.getName());
							user.sendMsg(ChatColor.GREEN + "Select the button you wish to delete");
						}else{
							user.sendMsg(ChatColor.RED + "You already used this command!");
							user.sendMsg(ChatColor.GREEN + "Select the button you wish to delete");
						}
					}
					if(args[0].equalsIgnoreCase("setbutton") || args[0].equalsIgnoreCase("setmessage")) {
						user.sendMsg(ChatColor.RED + "Please use correct format!");
						user.sendMsg(ChatColor.GREEN + "/rpg " + args[0].toLowerCase() + " <name of tele> <message>");
					}
				}
				if(args.length > 1) {
					if(args[0].equalsIgnoreCase("setbutton")) {
						if(!Api.getPlayers().contains(user.getName())) {
							Api.getPlayers().add(user.getName());
							Api.poop = Api.compileArgs(args, 2);
							Api.poop1 = args[1];
							user.sendMsg(ChatColor.GREEN + "Select a button");
						}else{
							user.sendMsg(ChatColor.RED + "You already used this command!");
							user.sendMsg(ChatColor.GREEN + "Select a button");
						}
					}
					if(args[0].equalsIgnoreCase("setmessage")) {
						if(Api.getPlay().contains(user.getName())) {
							Api.getPlay().add(user.getName());
							Api.poop = Api.compileArgs(args, 2);
							Api.poop1 = args[1];
							user.sendMsg(ChatColor.GREEN + "Select a button you would like to put this message on");
						}else{
							user.sendMsg(ChatColor.RED + "You already used this command!");
							user.sendMsg(ChatColor.GREEN + "Select a button you would like to put this message on");
						}
					}
					if(args[0].equalsIgnoreCase("setobjective")) {
						Api.object1 = args[1] + "," + args[2] + "," + args[3];
						if(Api.getConfig().isSet("Quests." + args[1] + "." + args[2] + "." + args[3])) {
							if(Api.getObject().contains(user.getName())) {
								Api.getObject().add(user.getName());
								user.sendMsg(ChatColor.GREEN + "Type into the chat your objective for the quest");
							}else{
								user.sendMsg(ChatColor.RED + "You already used this command!");
								user.sendMsg(ChatColor.GREEN + "Type into the chat your objective for the quest");
							}
						}else{user.sendMsg(ChatColor.RED + "Quest does not exist!");}
					}
				}
				// /rpg deletequest <npcname> <region> <quest name>
				if(args.length == 4) {
					if(args[0].equalsIgnoreCase("deletequest")) {
						String name = args[1];
						String region = args[2];
						String quest = args[3];
						if(Api.getConfig().isSet("Quests." + name + "." + region + "." + quest)) {
							Api.getConfig().set("Quests." + name + "." + region, null);
							int i = Api.getConfig().getInt("Quest");
							Api.getConfig().set("Quest", i - 1);
							Api.saveConfig();
							user.sendMsg(ChatColor.GOLD + "Successfully deleted the quest " + quest.replace("_", " "));
							return false;
						}user.sendMsg(ChatColor.RED + "Quest does not exist!");
					}
				}
				// /rpg newquest <npcname> <region> <Quest Name> <Reward> <Requirement> <amount> <Objective>
				if(args.length > 6) {
					if(args[0].equalsIgnoreCase("newquest")) {
						String name = args[1];
						String region = args[2];
						String quest = args[3];
						double reward = Double.parseDouble(args[4]);
						String requirement = args[5];
						int amount = Integer.parseInt(args[6]);
						String objective = Api.compileArgs(args, 7);
						// Up are varibles || Down executed
						String start = "Quests." + name + "." + region + "." + quest;
						if(Api.getConfig().isSet(start)) {
							user.sendMsg(ChatColor.DARK_RED + "That quest already exists!");
							return false;
						}start = start.concat(".");
						Material mat = null;
						if(isInt(requirement) == true) {
							mat = Material.getMaterial(Integer.valueOf(requirement));
						}else{
							mat = Material.getMaterial(String.valueOf(requirement.toUpperCase()));
						}
						if(mat == null) {
							user.sendMsg(ChatColor.DARK_RED + "Item does not exist!");
							return false;
						}
						int questid = Api.getConfig().getInt("Quest") + 1;
						ItemStack item = new ItemStack(mat, amount);
						Api.getConfig().set(start + "Quest.Name", quest.replace("_", " "));
						Api.getConfig().set(start + "Quest.Objective", objective);
						Api.getConfig().set(start + "Quest.QuestId", questid);
						Api.getConfig().set(start + "Stuff.NPC_Name", name);
						Api.getConfig().set(start + "Stuff.WorldGuard_Region", region);
						Api.getConfig().set(start + "Reward.Money", reward);
						Api.getConfig().set(start + "Requirements.Item", item);
						Api.getConfig().set("Quest", questid);
						Api.saveConfig();
						user.sendMsg(ChatColor.GOLD + "Successfully saved quest!");
					}
				}
			}else{
				user.sendMsg(ChatColor.RED + "You do not have permission for that command");
			}
		}
		return false;
	}
	
	public boolean isInt(String require) {
		return require.matches(".*\\d.*");
	}
}