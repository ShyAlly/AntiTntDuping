package net.simpvp.AntiTntDuping;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;

public class PistonExtendEvent implements Listener {

	private int minX;
	private int minZ;
	private int maxX;
	private int maxZ;
	
	public static HashSet<Integer> tntIds = new HashSet<Integer>();
	
	public PistonExtendEvent(AntiTntDuping instance) {
		minX = instance.getConfig().getInt("minX");
		minZ = instance.getConfig().getInt("minZ");
		maxX = instance.getConfig().getInt("maxX");
		maxZ = instance.getConfig().getInt("maxZ");
	}
	
	
	@EventHandler(priority = EventPriority.NORMAL,ignoreCancelled=true)
	public void onPistonExtend(BlockPistonExtendEvent event) {
		for (Block block : event.getBlocks().toArray(new Block[0])) {
            if (block.getType().equals(Material.TNT)) {
            	
            	if (!isNearSpawn(block.getLocation())) {
            		block.setType(Material.AIR);
            		return;
            	}
            	
            	Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(AntiTntDuping.instance, new Runnable() {
            		public void run() {
            			for (Entity entity : block.getLocation().getWorld().getNearbyEntities(block.getLocation(), 3, 3, 3)) {
                    		if (entity.getType().equals(EntityType.PRIMED_TNT)) {
                    			tntIds.add(entity.getEntityId());
                    		}
                    	}
            		}
            	}, (long) .1);	
            }
        }
	}
	
	
	/* Return true if location is near spawn */
	public boolean isNearSpawn(Location loc) {
		if (!loc.getWorld().getName().equals("world")) {
			return false;
		}
		if (loc.getBlockX() < minX || loc.getBlockX() > maxX) {
			return false;
		}
		if (loc.getBlockZ() < minZ || loc.getBlockZ() > maxZ) {
			return false;
		}
		return true;
	}
	
}
