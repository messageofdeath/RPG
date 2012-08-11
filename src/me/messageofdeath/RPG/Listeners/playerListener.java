package me.messageofdeath.RPG.Listeners;

import me.messageofdeath.RPG.API.Api;
import me.messageofdeath.RPG.API.Button;
import me.messageofdeath.RPG.API.Quest;
import me.messageofdeath.RPG.API.User;
import net.citizensnpcs.resources.npclib.NPCManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class playerListener implements Listener {
	
	@EventHandler
	public void interact(PlayerInteractEvent event) {
		Action action = event.getAction();
		Player player = event.getPlayer();
		if(action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK) {
			if(event.getClickedBlock() != null) {
				Block block = event.getClickedBlock();
				if(block.getType() == Material.STONE_BUTTON) {
					String loc = block.getLocation().getBlockX() + "," + block.getLocation().getBlockY() + "," + block.getLocation().getBlockZ();
					Button config = new Button(loc);
					if(!Api.getPlayers().contains(player.getName()) && !Api.getPlayer().contains(player.getName()) && !Api.getPlay().contains(player.getName()) && !Api.getPl().contains(player.getName())) {
						if(config.getButtonLocation() != null) {
							String[] d = config.getButtonLocation().split(",");
							World world = Bukkit.getWorld(d[0]);
							double x = Double.parseDouble(d[1]);
							double y = Double.parseDouble(d[2]);
							double z = Double.parseDouble(d[3]);
							float yaw = Float.parseFloat(d[4]);
							float pitch = Float.parseFloat(d[5]);
							player.teleport(new Location(world, x, y, z, yaw, pitch));
							player.sendMessage(ChatColor.GREEN + "Welcome to " + ChatColor.DARK_RED + config.getButtonName());
							String[] dd = config.getButtonMessage().split(" ");
							if(dd[0].matches("[a-zA-z0-9]+")) {player.sendMessage(ChatColor.GOLD + config.getButtonMessage());}
							if(player.getGameMode() == GameMode.CREATIVE)event.setCancelled(true);
						}else{
							player.sendMessage("No Location Set!");
						}
					}
				}
			}
		}
		if(action == Action.LEFT_CLICK_BLOCK) {
			if(player.hasPermission("rpg.main")) {
				if(Api.getPlayers().contains(player.getName())) {
					Block block = event.getClickedBlock();
					if(block.getType() == Material.STONE_BUTTON) {
						String loc = block.getLocation().getBlockX() + "," + block.getLocation().getBlockY() + "," + block.getLocation().getBlockZ();
						Api.d = loc;
						player.sendMessage(ChatColor.GREEN + "Step 1: Done! Step 2: type /rpg setlocation for where this button will teleport to.");
						if(player.getGameMode() == GameMode.CREATIVE)event.setCancelled(true);
					}else{
						player.sendMessage(ChatColor.RED + "Please select a button.");
					}
				}
				if(Api.getPlayer().contains(player.getName())) {
					Block block = event.getClickedBlock();
					if(block.getType() == Material.STONE_BUTTON) {
						String loc = block.getLocation().getBlockX() + "," + block.getLocation().getBlockY() + "," + block.getLocation().getBlockZ();
						Button button = new Button(loc);
						button.deleteButton();
						Api.getPlayer().remove(player.getName());
						player.sendMessage(ChatColor.GOLD + "Successfully deleted button!");
					}else{
						player.sendMessage(ChatColor.RED + "Please select a button.");
					}
				}
				if(Api.getPlay().contains(player.getName())) {
					Block block = event.getClickedBlock();
					if(block.getType() == Material.STONE_BUTTON) {
						String loc = block.getLocation().getBlockX() + "," + block.getLocation().getBlockY() + "," + block.getLocation().getBlockZ();
						Button config = new Button(loc);
						Api.getPlay().remove(player.getName());
						config.setButtonName(Api.getName());
						config.setButtonMessage(Api.getMessage());
						player.sendMessage(ChatColor.GOLD  + "Successfully set the message to the button!");
						if(player.getGameMode() == GameMode.CREATIVE)event.setCancelled(true);
					}else{
						player.sendMessage(ChatColor.RED + "Please select a button.");
					}
				}
				if(Api.getPl().contains(player.getName())) {
					Block block = event.getClickedBlock();
					if(block.getType() == Material.STONE_BUTTON) {
						String loc = block.getLocation().getBlockX() + "," + block.getLocation().getBlockY() + "," + block.getLocation().getBlockZ();
						Api.loc = loc;
						player.sendMessage(ChatColor.GREEN + "Step 1 complete Step 2, use /rpg setlocation to set the location");
						if(player.getGameMode() == GameMode.CREATIVE)event.setCancelled(true);
					}else{
						player.sendMessage(ChatColor.RED + "Please select a button.");
					}
				}
			}
		}
	}
	
	@EventHandler
	public void death(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if(NPCManager.isNPC((Entity)player))if(player.getName().equalsIgnoreCase("bandit"))event.setDeathMessage("");
	}
	
	@EventHandler
	public void chat(AsyncPlayerChatEvent event) {
		User user = Api.getUser(event.getPlayer().getName());
		if(Api.getObject().contains(user.getName())) {
			Api.getObject().remove(user.getName());
			Quest quest = new Quest(Integer.parseInt(Api.object1));
			quest.setObjective(event.getMessage());
			user.sendMsg(ChatColor.GOLD + "Successfully set the objective!");
			event.setCancelled(true);
		}
		if(user.isWaiting()) {
			if(event.getMessage().equalsIgnoreCase("accept")) {
				user.sendMsg(ChatColor.GOLD + "You have accepted the quest!" + ChatColor.GRAY + " Quest #" + user.getPendingQuest());
				user.setWaiting(false);
				user.setActiveQuest(user.getPendingQuest());
				user.setPendingQuest(0);
				event.setCancelled(true);
				return;
			}
			if(event.getMessage().equalsIgnoreCase("decline")) {
				user.sendMsg(ChatColor.DARK_RED + "You have declined the quest, come again next time!");
				user.setPendingQuest(0);
				user.setQuest(0);
				user.setWaiting(false);
				event.setCancelled(true);
				return;
			}
			user.sendMsg(ChatColor.DARK_RED + "Do you " + ChatColor.GOLD + "Accept" + ChatColor.DARK_RED + " or " + ChatColor.GOLD + "Decline" + ChatColor.DARK_RED + " this Quest?");
			event.setCancelled(true);
		}
	}
}
