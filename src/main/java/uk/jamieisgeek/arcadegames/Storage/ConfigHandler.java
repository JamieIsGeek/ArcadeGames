package uk.jamieisgeek.arcadegames.Storage;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import uk.jamieisgeek.arcadegames.ArcadeGames;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigHandler {
    private final ArcadeGames plugin;
    private Configuration config;
    private Configuration messages;

    public ConfigHandler(ArcadeGames plugin) {
        this.plugin = plugin;
    }

    public boolean initialize() {
        try {
            plugin.saveDefaultConfig();
            plugin.saveResource("messages.yml", false);

            File messages = Arrays.stream(plugin.getDataFolder().listFiles())
                    .filter(file -> file.getName().equals("messages.yml"))
                    .findFirst()
                    .orElse(null);

            if (messages == null) {
                return false;
            }

            this.config = plugin.getConfig();
            this.messages = YamlConfiguration.loadConfiguration(messages);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Object getFromConfig(String path) {
        return config.get(path);
    }

    public String getFromMessages(String path) {
        return messages.getString(path);
    }
}