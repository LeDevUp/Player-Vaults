package me.devup.pv.utils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlayerConfig {
	
	private FileConfiguration config;
	
	private File file;
	
	private UUID uuid;
	
	protected Plugin pl;
	
	public PlayerConfig(Player p, Plugin pl) {
		if (!pl.getDataFolder().exists()) {
			pl.getDataFolder().mkdir();
		}
		
		this.uuid = p.getUniqueId();
		
		file = new File(pl.getDataFolder() + "/Vaults/" + p.getUniqueId().toString() + ".yml");
		
		if(!(file.exists())) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				Bukkit.getLogger().severe("[" + pl.getName() + "] Could not create " + p.getName() + "'s player config.");
			}
		}

		config = new YamlConfiguration();
		
		try {
			config.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().severe("[" + pl.getName() + "] Could not load " + p.getName() + "'s player config.");
		}
	}
	
	public FileConfiguration getConfig() {
		return this.config;
	}
	
	public void save() {
		try {
			this.config.save(file);
		} catch (Exception e) {
			Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save " + file.getName() + ".yml");
		}
	}
	
	public void reload() {
		config = new YamlConfiguration();
		
		try {
			config.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			Bukkit.getLogger().severe("[" + pl.getName() + "] Could not load config: " + uuid + ".yml");
		}
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
}
