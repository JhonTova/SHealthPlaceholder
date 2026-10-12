package me.sirius.shealthplaceholder;

import me.sirius.core.SiriusPlugin;

public class SHealthPlaceholders extends SiriusPlugin {

    private static SHealthPlaceholders instance;

    private HealthExpansion expansion;

    @Override
    protected void onStart() {
        instance = this;

        saveDefaultConfig();

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().warning("PlaceholderAPI no encontrado! Este plugin requiere PlaceholderAPI.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        expansion = new HealthExpansion(this);
        expansion.register();
        getLogger().info("PlaceholderAPI detectado! Placeholders registrados correctamente.");
    }

    @Override
    protected void onStop() {
        // Con persist() la expansion sobrevive a un /papi reload, pero no debe
        // sobrevivir al plugin: seguiria registrada apuntando a una instancia muerta.
        if (expansion != null) {
            expansion.unregister();
            expansion = null;
        }
        instance = null;
    }

    public static SHealthPlaceholders getInstance() {
        return instance;
    }
}
