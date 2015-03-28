package me.devup.pv.utils;

import java.util.List;

import me.devup.pv.Main;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {
	
	public static void saveVault(Player p, Inventory inv) {
		PlayerConfig pc = Main.getPlugin().getPlayerConfig(p);
		
		FileConfiguration c = pc.getConfig();
		
		c.set("Inventory.Content", inv.getContents());
		
		pc.save();
	}
	
	@SuppressWarnings("unchecked")
	public static Inventory setupVault(Player p) {
		PlayerConfig pc = Main.getPlugin().getPlayerConfig(p);
		
		Inventory inv = Bukkit.createInventory(null, Main.getPlugin().getConfig().getInt("VaultSize"), "Vault: " + p.getName());
		
		FileConfiguration c = pc.getConfig();
		
		if(c.contains("Inventory.Content")) {
			ItemStack[] content = ((List<ItemStack>) c.get("Inventory.Content")).toArray(new ItemStack[0]);
			
			inv.setContents(content);
		}
		
		return inv;
	}

}
