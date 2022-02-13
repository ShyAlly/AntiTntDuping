package net.simpvp.AntiTntDuping;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;

public class PistonExtendEvent implements Listener {

	private int minX;
	private int minZ;
	private int maxX;
	private int maxZ;
	
	
	public PistonExtendEvent(AntiTntDuping instance) {
		minX = instance.getConfig().getInt("minX");
		minZ = instance.getConfig().getInt("minZ");
		maxX = instance.getConfig().getInt("maxX");
		maxZ = instance.getConfig().getInt("maxZ");
	}
	
	
	@EventHandler(priority = EventPriority.NORMAL,ignoreCancelled=true)
	public void onPlayerDeath(BlockPistonExtendEvent event) {
		for (Block block : event.getBlocks().toArray(new Block[0])) {
            if (block.getType().equals(Material.TNT) && !isNearSpawn(block.getLocation())) {
                block.setType(Material.AIR);
                return;
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
