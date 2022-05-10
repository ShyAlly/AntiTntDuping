package net.simpvp.AntiTntDuping;

import java.util.Iterator;
import java.util.List;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class BlockExplodeEvent implements Listener{

	@EventHandler(priority = EventPriority.NORMAL,ignoreCancelled=true)
	public void onBlockExplode(EntityExplodeEvent event) {
		if (event.getEntityType() == EntityType.PRIMED_TNT) {
			List<Block> blockList = event.blockList();
			int tntId = event.getEntity().getEntityId();
			if (PistonEvent.tntIds.remove(tntId)) {
				Iterator<?> it = blockList.iterator();
				while(it.hasNext()) {
					Block block = (Block)it.next();
					if (!(block.getType().toString().equals("COBBLESTONE")) && !isValidStoneBlock(block)) {
						it.remove();
					}
				}
			}
		}
	}

	/* Return true if a block is stone and is above y=62, or it is in the end */
	public Boolean isValidStoneBlock(Block block) {

		if (block.getWorld().getEnvironment().equals(World.Environment.THE_END)) {
			return true;
		}

		return block.getType().toString().equals("STONE") && block.getY() >= 63;
	}

}
