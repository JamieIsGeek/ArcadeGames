package uk.jamieisgeek.arcadegames;

import org.bukkit.plugin.java.JavaPlugin;
import uk.jamieisgeek.arcadegames.Commands.AdminCommands.PigJoustingAdmin;
import uk.jamieisgeek.arcadegames.Games.PigJousting.PigJousting;
import uk.jamieisgeek.arcadegames.Storage.ConfigHandler;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ArcadeGames extends JavaPlugin {
    private static Logger logger;
    private ConfigHandler configHandler;
    private static ArcadeGames instance;
    private final Set<PigJousting> pigJoustingGames = new HashSet<>();

    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();
        this.configHandler = new ConfigHandler(this);

        if (!configHandler.initialize()) {
            Log(Level.SEVERE, "Failed to initialize config handler!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("pigjousting").setExecutor(new PigJoustingAdmin());
    }

    @Override
    public void onDisable() {
        Log(Level.INFO, "Disabling ArcadeGames...");
    }

    public void Reload() {
        this.configHandler = new ConfigHandler(this);
    }

    public static void Log(Level level, String message) {
        logger.log(level, message);
    }

    public static void Log(String message) {
        Log(Level.INFO, message);
    }

    public static ArcadeGames getInstance() {
        return instance;
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public static void addPigJoustingGame(PigJousting pigJousting) {
        instance.pigJoustingGames.add(pigJousting);
    }

    public static PigJousting getPigJousting() {
        return instance.pigJoustingGames.iterator().next();
    }
}
