package xyz.acyber.chunker_Tweaked;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Chunker_Tweaked extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        PluginSettings settings = new PluginSettings(this);

        PreGenerator preGenerator;
        try {
            preGenerator = new PreGenerator(this);
        } catch (RuntimeException e) {
            e.printStackTrace();
            getLogger().severe("Failed to initialize PreGenerator: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getServer().getPluginManager().registerEvents(preGenerator, this);

        PreGeneratorCommands preGeneratorCommands = new PreGeneratorCommands(preGenerator, settings, this);
        Objects.requireNonNull(getCommand("pregen")).setExecutor(preGeneratorCommands);
        Objects.requireNonNull(getCommand("pregen")).setTabCompleter(preGeneratorCommands);
        Objects.requireNonNull(getCommand("pregenoff")).setExecutor(preGeneratorCommands);
        Objects.requireNonNull(getCommand("pregenoff")).setTabCompleter(preGeneratorCommands);

        ServerStateDefaults defaults = new ServerStateDefaults();
        defaults.DEFAULT_SPAWN_CHUNK_RADIUS = 0;
        defaults.DEFAULT_RANDOM_TICK_SPEED = 3;
        defaults.DEFAULT_MOB_SPAWNING = true;
        defaults.DEFAULT_FIRE_TICK = false;

        new ServerStateManager(this, preGeneratorCommands, defaults);
    }
}