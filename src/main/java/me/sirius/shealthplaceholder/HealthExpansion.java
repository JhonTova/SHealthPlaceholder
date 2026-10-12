package me.sirius.shealthplaceholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class HealthExpansion extends PlaceholderExpansion {

    private record Effect(PotionEffectType type, String symbol, String color) {
    }

    private record Style(String heart, String heartColor, String absorptionColor,
                         String healthNumberColor, String absorptionNumberColor,
                         String witherHeart, String poisonHeart, String multipleEffects,
                         List<Effect> effects) {
    }

    // Los scoreboards y nametags piden estos placeholders varias veces por segundo
    // por jugador: la config se lee una vez al cargar, no en cada peticion.
    private volatile Style style;

    public HealthExpansion(SHealthPlaceholders plugin) {
        reload(plugin.getConfig());
    }

    public void reload(FileConfiguration config) {
        this.style = new Style(
                config.getString("symbols.heart", "❤"),
                config.getString("colors.heart", "&c"),
                config.getString("colors.absorption", "&e"),
                config.getString("colors.health-number", "&f"),
                config.getString("colors.absorption-number", "&f"),
                config.getString("colors.wither-heart", "&8"),
                config.getString("colors.poison-heart", "&a"),
                config.getString("colors.multiple-effects", "&5"),
                List.of(
                        effect(config, PotionEffectType.WITHER, "wither", "☠", "&8"),
                        effect(config, PotionEffectType.POISON, "poison", "⚗", "&a"),
                        effect(config, PotionEffectType.REGENERATION, "regeneration", "+", "&d"),
                        effect(config, PotionEffectType.STRENGTH, "strength", "⚔", "&c"),
                        effect(config, PotionEffectType.WEAKNESS, "weakness", "↓", "&7"),
                        effect(config, PotionEffectType.SPEED, "speed", "➤", "&b"),
                        effect(config, PotionEffectType.SLOWNESS, "slowness", "◆", "&9"),
                        effect(config, PotionEffectType.RESISTANCE, "resistance", "◈", "&6"),
                        effect(config, PotionEffectType.FIRE_RESISTANCE, "fire_resistance", "♨", "&6"),
                        effect(config, PotionEffectType.INSTANT_DAMAGE, "instant_damage", "⚡", "&4"),
                        effect(config, PotionEffectType.INVISIBILITY, "invisibility", "◯", "&7"),
                        effect(config, PotionEffectType.BLINDNESS, "blindness", "◉", "&8"),
                        effect(config, PotionEffectType.GLOWING, "glowing", "✦", "&e"),
                        effect(config, PotionEffectType.LEVITATION, "levitation", "↑", "&f")));
    }

    private static Effect effect(FileConfiguration config, PotionEffectType type, String key,
                                 String symbol, String color) {
        return new Effect(type,
                config.getString("effect-symbols." + key, symbol),
                config.getString("effect-colors." + key, color));
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
        Style style = this.style;
        double health = player.getHealth();
        double absorption = player.getAbsorptionAmount();

        return switch (identifier.toLowerCase(Locale.ROOT)) {
            case "health_basic" -> hearts(style, formatHealth(health), style.heartColor(), absorption).toString();
            case "health_colored" -> hearts(style, formatHealth(health), heartColor(style, player), absorption).toString();
            case "health_effects" -> appendEffects(style, player,
                    hearts(style, formatHealth(health), style.heartColor(), absorption)).toString();
            case "rompepitas" -> hearts(style, formatHealthRompepitas(health), style.heartColor(), absorption).toString();
            case "effects_only" -> appendEffects(style, player, new StringBuilder(32)).toString();
            case "health" -> formatHealth(health);
            case "health_int" -> String.valueOf((int) Math.ceil(health));
            case "max_health" -> formatHealth(maxHealth(player));
            case "max_health_int" -> String.valueOf((int) Math.ceil(maxHealth(player)));
            case "absorption" -> formatHealth(absorption);
            case "absorption_int" -> String.valueOf((int) Math.ceil(absorption));
            case "health_percentage" -> String.format("%.1f", (health / maxHealth(player)) * 100);
            case "hearts_count" -> formatHealth(health / 2);
            case "total_hearts_count" -> formatHealth((health + absorption) / 2);
            default -> null;
        };
    }

    private static double maxHealth(Player player) {
        AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
        return attribute != null ? attribute.getValue() : 20D;
    }

    private static StringBuilder hearts(Style style, String health, String heartColor, double absorption) {
        StringBuilder result = new StringBuilder(64);
        result.append(style.healthNumberColor()).append(health)
                .append(' ').append(heartColor).append(style.heart());

        if (absorption > 0) {
            result.append(' ').append(style.absorptionNumberColor()).append(formatHealth(absorption))
                    .append(' ').append(style.absorptionColor()).append(style.heart());
        }
        return result;
    }

    private static StringBuilder appendEffects(Style style, Player player, StringBuilder result) {
        for (Effect effect : style.effects()) {
            if (player.hasPotionEffect(effect.type())) {
                result.append(' ').append(effect.color()).append(effect.symbol());
            }
        }
        return result;
    }

    private static String heartColor(Style style, Player player) {
        boolean wither = player.hasPotionEffect(PotionEffectType.WITHER);
        boolean poison = player.hasPotionEffect(PotionEffectType.POISON);

        if (wither && poison) {
            return style.multipleEffects();
        }
        if (wither) {
            return style.witherHeart();
        }
        return poison ? style.poisonHeart() : style.heartColor();
    }

    private static String formatHealth(double value) {
        if (value % 1 == 0) {
            return String.valueOf((int) value);
        }
        return String.format("%.1f", value);
    }

    private static String formatHealthRompepitas(double value) {
        if (value < 2 && value % 1 != 0) {
            return String.format("%.1f", value);
        }
        return String.valueOf((int) Math.ceil(value));
    }
}
