package me.islandscout.hawk.checks.movement;

import me.islandscout.hawk.checks.AsyncMovementCheck;
import me.islandscout.hawk.events.PositionEvent;
import me.islandscout.hawk.utils.AdjacentBlocks;
import me.islandscout.hawk.utils.PhysicsUtils;
import me.islandscout.hawk.utils.ServerUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerVelocityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Stops water walk NCP bypass.
 */
public class LiquidExit extends AsyncMovementCheck implements Listener {

    //TODO: false flag in lava

    private Map<UUID, DoubleTime> kbTime;

    public LiquidExit() {
        super("liquidexit", true, 0, 3, 0.99, 2000, "&7%player% failed liquid exit. VL: %vl%", null);
        kbTime = new HashMap<>();
    }

    @Override
    protected void check(PositionEvent e) {
        Player p = e.getPlayer();
        if(p.isFlying() || p.isInsideVehicle())
            return;

        Location from = e.getFrom();
        double deltaY = e.getTo().getY() - from.getY();

        Block atFrom = ServerUtils.getBlockAsync(from);
        Block belowFrom = ServerUtils.getBlockAsync(from.clone().add(0, deltaY, 0));
        if(atFrom == null || belowFrom == null)
            return;

        //emerged upwards from liquid
        if(deltaY > 0 && atFrom.isLiquid() && !belowFrom.isLiquid() && !AdjacentBlocks.blockNearbyIsSolid(from)) {
            DoubleTime kb = kbTime.getOrDefault(p.getUniqueId(), new DoubleTime(0, 0));
            double ticksSinceKb = System.currentTimeMillis() - kb.time;
            ticksSinceKb /= 50;
            //check if they're being knocked out of the water
            if(PhysicsUtils.waterYVelFunc(kb.value, ticksSinceKb) < 0) {
                punishAndTryRubberband(e.getHawkPlayer(), e, p.getLocation());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onVelocity(PlayerVelocityEvent e) {
        kbTime.put(e.getPlayer().getUniqueId(), new DoubleTime(e.getVelocity().getY(), System.currentTimeMillis() + ServerUtils.getPing(e.getPlayer())));
    }

    private class DoubleTime {

        private double value;
        private long time;

        private DoubleTime(double value, long time) {
            this.value = value;
            this.time = time;
        }
    }

    @Override
    public void removeData(Player p) {
        kbTime.remove(p.getUniqueId());
    }
}
