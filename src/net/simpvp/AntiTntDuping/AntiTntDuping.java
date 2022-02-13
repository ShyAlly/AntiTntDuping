package net.simpvp.AntiTntDuping;

import org.bukkit.plugin.java.JavaPlugin;


public class AntiTntDuping extends JavaPlugin {

	public static AntiTntDuping instance;
	
	public AntiTntDuping() {
		instance = this;
	}
	
	public void onEnable() {
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		
		getServer().getPluginManager().registerEvents(new PistonExtendEvent(instance), this);
		getServer().getPluginManager().registerEvents(new BlockExplodeEvent(), this);
	}
}
