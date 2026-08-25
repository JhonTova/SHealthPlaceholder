package me.sirius.shealthplaceholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class HealthExpansion extends PlaceholderExpansion {
    
    private final SHealthPlaceholders plugin;
    
    public HealthExpansion(SHealthPlaceholders plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public @NotNull String getIdentifier() {
        return "shealth";
    }
    
    @Override
    public @NotNull String getAuthor() {
        return "Sirius";
    }
    
    @Override
    public @NotNull String getVersion() {
        return "1.2";
    }
    
    @Override
    public boolean persist() {
        return true;
    }
    
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) {
            return "";
        }
        
        double health = player.getHealth();
        double maxHealth = player.getAttribute(Attribute.MAX_HEALTH).getValue();
        double absorption = player.getAbsorptionAmount();
        
        switch (identifier.toLowerCase()) {
            case "health_basic":
                return getHealthBasic(health, absorption);
                
            case "health_colored":
                return getHealthColored(player, health, absorption);
                
            case "health_effects":
                return getHealthEffects(player, health, absorption);
                
            case "rompepitas":
                return getRompepitas(player, health, absorption);
                
            case "effects_only":
                return getEffectsOnly(player);
                
            case "health":
                return formatHealth(health);
                
            case "health_int":
                return String.valueOf((int) Math.ceil(health));
                
            case "max_health":
                return formatHealth(maxHealth);
                
            case "max_health_int":
                return String.valueOf((int) Math.ceil(maxHealth));
                
            case "absorption":
                return formatHealth(absorption);
                
            case "absorption_int":
                return String.valueOf((int) Math.ceil(absorption));
                
            case "health_percentage":
                return String.format("%.1f", (health / maxHealth) * 100);
                
            case "hearts_count":
                return formatHealth(health / 2);
                
            case "total_hearts_count":
                return formatHealth((health + absorption) / 2);
                
            default:
                return null;
        }
    }
    
    private String formatHealth(double value) {
        if (value % 1 == 0) {
            return String.valueOf((int) value);
        }
        return String.format("%.1f", value);
    }
    
    private String formatHealthRompepitas(double value) {
        if (value < 2 && value % 1 != 0) {
            return String.format("%.1f", value);
        }
        return String.valueOf((int) Math.ceil(value));
    }
    
    private String getHealthBasic(double health, double absorption) {
        StringBuilder result = new StringBuilder(32);
        
        String heartSymbol = plugin.getConfig().getString("symbols.heart", "❤");
        String heartColor = plugin.getConfig().getString("colors.heart", "&c");
        String absorptionColor = plugin.getConfig().getString("colors.absorption", "&e");
        String healthNumberColor = plugin.getConfig().getString("colors.health-number", "&f");
        String absorptionNumberColor = plugin.getConfig().getString("colors.absorption-number", "&f");
        
        result.append(healthNumberColor).append(formatHealth(health));
        result.append(" ").append(heartColor).append(heartSymbol);
        
        if (absorption > 0) {
            result.append(" ").append(absorptionNumberColor).append(formatHealth(absorption));
            result.append(" ").append(absorptionColor).append(heartSymbol);
        }
        
        return result.toString();
    }
    
    private String getHealthColored(Player player, double health, double absorption) {
        StringBuilder result = new StringBuilder(32);
        
        String heartSymbol = plugin.getConfig().getString("symbols.heart", "❤");
        String healthColor = getHealthColorByEffect(player);
        String absorptionColor = plugin.getConfig().getString("colors.absorption", "&e");
        String healthNumberColor = plugin.getConfig().getString("colors.health-number", "&f");
        String absorptionNumberColor = plugin.getConfig().getString("colors.absorption-number", "&f");
        
        result.append(healthNumberColor).append(formatHealth(health));
        result.append(" ").append(healthColor).append(heartSymbol);
        
        if (absorption > 0) {
            result.append(" ").append(absorptionNumberColor).append(formatHealth(absorption));
            result.append(" ").append(absorptionColor).append(heartSymbol);
        }
        
        return result.toString();
    }
    
    private String getHealthEffects(Player player, double health, double absorption) {
        StringBuilder result = new StringBuilder(64);
        
        String heartSymbol = plugin.getConfig().getString("symbols.heart", "❤");
        String heartColor = plugin.getConfig().getString("colors.heart", "&c");
        String absorptionColor = plugin.getConfig().getString("colors.absorption", "&e");
        String healthNumberColor = plugin.getConfig().getString("colors.health-number", "&f");
        String absorptionNumberColor = plugin.getConfig().getString("colors.absorption-number", "&f");
        
        result.append(healthNumberColor).append(formatHealth(health));
        result.append(" ").append(heartColor).append(heartSymbol);
        
        if (absorption > 0) {
            result.append(" ").append(absorptionNumberColor).append(formatHealth(absorption));
            result.append(" ").append(absorptionColor).append(heartSymbol);
        }
        
        appendEffects(player, result);
        
        return result.toString();
    }
    
    private String getEffectsOnly(Player player) {
        StringBuilder result = new StringBuilder(32);
        appendEffects(player, result);
        return result.toString();
    }
    
    private String getRompepitas(Player player, double health, double absorption) {
        StringBuilder result = new StringBuilder(32);
        
        String heartSymbol = plugin.getConfig().getString("symbols.heart", "❤");
        String heartColor = plugin.getConfig().getString("colors.heart", "&c");
        String absorptionColor = plugin.getConfig().getString("colors.absorption", "&e");
        String healthNumberColor = plugin.getConfig().getString("colors.health-number", "&f");
        String absorptionNumberColor = plugin.getConfig().getString("colors.absorption-number", "&f");
        
        result.append(healthNumberColor).append(formatHealthRompepitas(health));
        result.append(" ").append(heartColor).append(heartSymbol);
        
        if (absorption > 0) {
            result.append(" ").append(absorptionNumberColor).append(formatHealth(absorption));
            result.append(" ").append(absorptionColor).append(heartSymbol);
        }
        
        return result.toString();
    }
    
    private void appendEffects(Player player, StringBuilder result) {
        if (player.hasPotionEffect(PotionEffectType.WITHER)) {
            String symbol = plugin.getConfig().getString("effect-symbols.wither", "☠");
            String color = plugin.getConfig().getString("effect-colors.wither", "&8");
            result.append(" ").append(color).append(symbol);
        }
        
        if (player.hasPotionEffect(PotionEffectType.POISON)) {
            String symbol = plugin.getConfig().getString("effect-symbols.poison", "⚗");
            String color = plugin.getConfig().getString("effect-colors.poison", "&a");
            result.append(" ").append(color).append(symbol);
        }
        
        if (player.hasPotionEffect(PotionEffectType.REGENERATION)) {
            String symbol = plugin.getConfig().getString("effect-symbols.regeneration", "+");
            String color = plugin.getConfig().getString("effect-colors.regeneration", "&d");
            result.append(" ").append(color).append(symbol);
        }
        
        if (player.hasPotionEffect(PotionEffectType.STRENGTH)) {
            String symbol = plugin.getConfig().getString("effect-symbols.strength", "⚔");
            String color = plugin.getConfig().getString("effect-colors.strength", "&c");
            result.append(" ").append(color).append(symbol);
        }
        
        if (player.hasPotionEffect(PotionEffectType.WEAKNESS)) {
            String symbol = plugin.getConfig().getString("effect-symbols.weakness", "↓");
            String color = plugin.getConfig().getString("effect-colors.weakness", "&7");
            result.append(" ").append(color).append(symbol);
        }
        
        if (player.hasPotionEffect(PotionEffectType.SPEED)) {
            String symbol = plugin.getConfig().getString("effect-symbols.speed", "➤");
            String color = plugin.getConfig().getString("effect-colors.speed", "&b");
            result.append(" ").append(color).append(symbol);
        }
        
        if (player.hasPotionEffect(PotionEffectType.SLOWNESS)) {
            String symbol = plugin.getConfig().getString("effect-symbols.slowness", "◆");
            String color = plugin.getConfig().getString("effect-colors.slowness", "&9");
            result.append(" ").append(color).append(symbol);
        }
        
        if (player.hasPotionEffect(PotionEffectType.RESISTANCE)) {
            String symbol = plugin.getConfig().getString("effect-symbols.resistance", "◈");
            String color = plugin.getConfig().getString("effect-colors.resistance", "&6");
            result.append(" ").append(color).append(symbol);
        }
        
        if (player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
            String symbol = plugin.getConfig().getString("effect-symbols.fire_resistance", "♨");
            String color = plugin.getConfig().getString("effect-colors.fire_resistance", "&6");
            result.append(" ").append(color).append(symbol);
        }
        
        if (player.hasPotionEffect(PotionEffectType.INSTANT_DAMAGE)) {
            String symbol = plugin.getConfig().getString("effect-symbols.instant_damage", "⚡");
            String color = plugin.getConfig().getString("effect-colors.instant_damage", "&4");
            result.append(" ").append(color).append(symbol);
        }
        
        if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            String symbol = plugin.getConfig().getString("effect-symbols.invisibility", "◯");
            String color = plugin.getConfig().getString("effect-colors.invisibility", "&7");
            result.append(" ").append(color).append(symbol);
        }
        
        if (player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
            String symbol = plugin.getConfig().getString("effect-symbols.blindness", "◉");
            String color = plugin.getConfig().getString("effect-colors.blindness", "&8");
            result.append(" ").append(color).append(symbol);
        }
        
        if (player.hasPotionEffect(PotionEffectType.GLOWING)) {
            String symbol = plugin.getConfig().getString("effect-symbols.glowing", "✦");
            String color = plugin.getConfig().getString("effect-colors.glowing", "&e");
            result.append(" ").append(color).append(symbol);
        }
        
        if (player.hasPotionEffect(PotionEffectType.LEVITATION)) {
            String symbol = plugin.getConfig().getString("effect-symbols.levitation", "↑");
            String color = plugin.getConfig().getString("effect-colors.levitation", "&f");
            result.append(" ").append(color).append(symbol);
        }
    }
    
    private String getHealthColorByEffect(Player player) {
        int effectCount = 0;
        boolean hasWither = player.hasPotionEffect(PotionEffectType.WITHER);
        boolean hasPoison = player.hasPotionEffect(PotionEffectType.POISON);
        
        if (hasWither) effectCount++;
        if (hasPoison) effectCount++;
        
        if (effectCount >= 2) {
            return plugin.getConfig().getString("colors.multiple-effects", "&5");
        }
        
        if (hasWither) {
            return plugin.getConfig().getString("colors.wither-heart", "&8");
        }
        
        if (hasPoison) {
            return plugin.getConfig().getString("colors.poison-heart", "&a");
        }
        
        return plugin.getConfig().getString("colors.heart", "&c");
    }
}