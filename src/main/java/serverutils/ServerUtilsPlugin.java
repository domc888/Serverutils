package serverutils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class ServerUtilsPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    // FEATURE 1: Anti-Creeper Grief
    @EventHandler
    public void onCreeperExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof Creeper) {
            // Clears the blocks destroyed by the explosion, but keeps player damage
            event.blockList().clear();
        }
    }

    // FEATURES 2 & 3: Death Coords and PvP Head Drops
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();

        // Private Death Coordinates
        Location loc = victim.getLocation();
        String coords = String.format("%d, %d, %d", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        victim.sendMessage(Component.text("[☠] You died at: " + coords, NamedTextColor.RED));

        // PvP Player Head Drops
        if (victim.getKiller() != null) {
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            
            if (meta != null) {
                meta.setOwningPlayer(victim);
                head.setItemMeta(meta);
            }
            
            // Add the generated head to the loot table that drops on the floor
            event.getDrops().add(head);
        }
    }
}
