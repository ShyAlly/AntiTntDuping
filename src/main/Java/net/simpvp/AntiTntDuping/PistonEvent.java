package net.simpvp.AntiTntDuping;

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
import org.bukkit.event.block.BlockPistonRetractEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class PistonEvent implements Listener {

    public static List<?> listOfWorlds;
    private final int minX;
    private final int minZ;
    private final int maxX;
    private final int maxZ;

    public static HashSet<Integer> tntIds = new HashSet<>();

    public PistonEvent(AntiTntDuping instance) {
        listOfWorlds = instance.getConfig().getList("worlds");
        minX = instance.getConfig().getInt("minX");
        minZ = instance.getConfig().getInt("minZ");
        maxX = instance.getConfig().getInt("maxX");
        maxZ = instance.getConfig().getInt("maxZ");
    }

    @EventHandler(priority = EventPriority.NORMAL,ignoreCancelled=true)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        List<Block> blocks = event.getBlocks();
        manageTntDupers(blocks);
    }

    @EventHandler(priority = EventPriority.NORMAL,ignoreCancelled=true)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        List<Block> blocks = event.getBlocks();
        manageTntDupers(blocks);
    }

    public boolean isNearSpawn(Location loc) {
        if (!listOfWorlds.contains(Objects.requireNonNull(loc.getWorld()).getName())) {
            return false;
        }
        if (loc.getBlockX() < minX || loc.getBlockX() > maxX) {
            return false;
        }
        return loc.getBlockZ() >= minZ && loc.getBlockZ() <= maxZ;
    }


    public void manageTntDupers(List<Block> blocks) {
        for (Block block : blocks.toArray(new Block[0])) {
            if (block.getType().equals(Material.TNT)) {

                if (!isNearSpawn(block.getLocation())) {
                    return;
                }

                Location location = block.getLocation();

                Bukkit.getServer().getRegionScheduler().run(AntiTntDuping.instance, location, (task) -> {
                    for (Entity entity : Objects.requireNonNull(location.getWorld()).getNearbyEntities(block.getLocation(), 3, 3, 3)) {
                        if (entity.getType().equals(EntityType.TNT)) {
                            tntIds.add(entity.getEntityId());
                            entity.setPersistent(false);
                        }
                    }
                });
            }
        }
    }
}
