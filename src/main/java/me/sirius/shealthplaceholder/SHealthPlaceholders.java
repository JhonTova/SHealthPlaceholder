package me.sirius.shealthplaceholder;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import me.sirius.core.SiriusPlugin;

public class SHealthPlaceholders extends SiriusPlugin implements Listener {
    
    private static SHealthPlaceholders instance;
    
    @Override
    protected void onStart() {
        instance = this;
        
        saveDefaultConfig();
        
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new HealthExpansion(this).register();
            getServer().getPluginManager().registerEvents(this, this);
            getLogger().info("PlaceholderAPI detectado! Placeholders registrados correctamente.");
        } else {
            getLogger().warning("PlaceholderAPI no encontrado! Este plugin requiere PlaceholderAPI.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        getLogger().info("§b╔═══════════════════════════════════════╗");
        getLogger().info("§b║ §d          ✦ SHealhtPlaceholders ✦             §b║");
        getLogger().info("§b║ §7      by §bSirius §8| §7GitHub: §fJhonTova      §b║");
        getLogger().info("§b║ §7       Discord: §fksirius                   §b║");
        getLogger().info("§b║ §a     Plugin habilitado con éxito!           §b║");
        getLogger().info("§b╚═══════════════════════════════════════╝");
    }
    
    @Override
    protected void onStop() {
        getLogger().info("§b╔═══════════════════════════════════════╗");
        getLogger().info("§b║ §d          ✦ SHealhtPlaceholders ✦             §b║");
        getLogger().info("§b║ §7      by §bSirius §8| §7GitHub: §fJhonTova      §b║");
        getLogger().info("§b║ §7       Discord: §fksirius                   §b║");
        getLogger().info("§b║ §a     Plugin deshabilitado con éxito!           §b║");
        getLogger().info("§b╚═══════════════════════════════════════╝");
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            scheduleUpdate((Player) event.getEntity());
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onHeal(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player) {
            scheduleUpdate((Player) event.getEntity());
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPotionEffect(EntityPotionEffectEvent event) {
        if (event.getEntity() instanceof Player) {
            scheduleUpdate((Player) event.getEntity());
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onConsume(PlayerItemConsumeEvent event) {
        scheduleUpdate(event.getPlayer());
    }
    
    private void scheduleUpdate(Player player) {
        getServer().getScheduler().runTaskLater(this, () -> {
            if (player.isOnline()) {
                player.getScoreboard();
            }
        }, 1L);
    }
    
    public static SHealthPlaceholders getInstance() {
        return instance;
    }
}