package me.devup.pv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import me.devup.pv.utils.InventoryUtils;
import me.devup.pv.utils.PlayerConfig;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	
	private static Main plugin = null;
	
	protected HashMap<UUID, Inventory> vaults = new HashMap<UUID, Inventory>();
	
	protected ArrayList<PlayerConfig> playerConfigs = new ArrayList<PlayerConfig>();
	
	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		saveDefaultConfig();
		
		plugin = (Main) Bukkit.getPluginManager().getPlugin("PlayerVault");
		
		getServer().getPluginManager().registerEvents(this, this);
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			playerConfigs.add(new PlayerConfig(p, this));
			
			vaults.put(p.getUniqueId(), InventoryUtils.setupVault(p));
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onDisable() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			InventoryUtils.saveVault(p, vaults.get(p.getUniqueId()));
			
			vaults.remove(p.getUniqueId());
			
			playerConfigs.remove(getPlayerConfig(p));
		}
	}
	
	public static Main getPlugin() {
		return plugin;
	}
	
	public PlayerConfig getPlayerConfig(Player p) {
		for(PlayerConfig pc : playerConfigs) {
			if(p.getUniqueId().equals(pc.getUUID())) {
				return pc;
			}
		}
		return null;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		Player p = (Player) sender;
		
		if(command.getName().equalsIgnoreCase("vault")) {
			if(!(vaults.containsKey(p.getUniqueId()))) {
				p.sendMessage(ChatColor.RED + "Error: You currently do not have a vault. Try relogging and trying again.");
				
				return false;
			}
			
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("VaultOpening")));
			
			p.openInventory(vaults.get(p.getUniqueId()));
		}
		return false;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		playerConfigs.add(new PlayerConfig(p, this));
		
		vaults.put(p.getUniqueId(), InventoryUtils.setupVault(p));
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		
		InventoryUtils.saveVault(p, vaults.get(p.getUniqueId()));
		
		vaults.remove(p.getUniqueId());
		
		playerConfigs.remove(getPlayerConfig(p));
	}

}
