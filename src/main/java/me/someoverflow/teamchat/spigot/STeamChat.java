package me.someoverflow.teamchat.spigot;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Level;

/**
 * @author SomeOverflow
 */
public final class STeamChat extends JavaPlugin {

    private static STeamChat instance;
    private ConfigAccessorPaper configAccessor;

    private String reloadPermission;
    private String serverName;
    private String messageSent;
    private String messageReceived;
    private List<String> chatTypes;

    @Override
    public void onEnable() {
        instance = this;
        instance.getLogger().log(Level.INFO, "Loading Config...");
        configAccessor = new ConfigAccessorPaper(this, "config.yml");
        configAccessor.saveDefaultConfig();
        instance.getLogger().log(Level.INFO, "Config Loaded");

        instance.getLogger().log(Level.INFO, "Loading messages from Config...");
        messageSent = configAccessor.getConfig().getString("message.sent", "%prefix% \u00A78> \u00A7fYou \u00A78>> \u00A7f%message%");
        messageReceived = configAccessor.getConfig().getString("message.received", "%prefix% \u00A78> \u00A7f%sender% \u00A78>> \u00A7f%message%");
        serverName = configAccessor.getConfig().getString("message.servername", "unknown");
        reloadPermission = configAccessor.getConfig().getString("permission.reload", "tc.reload");
        instance.getLogger().log(Level.INFO, "Messages from Config Loaded");

        instance.getLogger().log(Level.INFO, "Chat types from Config...");
        chatTypes = configAccessor.getConfig().getStringList("chatTypes");
        if (chatTypes.isEmpty()) {
            instance.getLogger().log(Level.INFO, "There are no chat types in the config...");
        } else {
            instance.getLogger().log(Level.INFO, "Loaded types:");
            for (String s : chatTypes) {
                String[] strings = s.split(";");
                instance.getLogger().log(Level.INFO, strings[0] + " | " + strings[1] + " | " + strings[2] + " | " + strings[3]);
            }
        }
        instance.getLogger().log(Level.INFO, "Chat types from Config Loaded");

        instance.getLogger().log(Level.INFO, "Loading Listener...");
        Bukkit.getPluginManager().registerEvents(new SListener(), this);
        instance.getLogger().log(Level.INFO, "Listener Loaded");
        instance.getLogger().log(Level.INFO, "Plugin started");
    }

    @Override
    public void onDisable() {
        instance.getLogger().log(Level.INFO, "Plugin disabled");
        instance.getLogger().log(Level.INFO, "Bye <3");
    }

    public void reloadConfig() {
        configAccessor.reloadConfig();
        messageSent = configAccessor.getConfig().getString("message.sent", "%prefix% \u00A78> \u00A7fYou \u00A78>> \u00A7f%message%");
        messageReceived = configAccessor.getConfig().getString("message.received", "%prefix% \u00A78> \u00A7f%sender% \u00A78>> \u00A7f%message%");
        serverName = configAccessor.getConfig().getString("message.servername", "unknown");
        chatTypes = configAccessor.getConfig().getStringList("chatTypes");
        reloadPermission = configAccessor.getConfig().getString("permission.reload", "tc.reload");
        if (chatTypes.isEmpty()) {
            instance.getLogger().log(Level.INFO, "There are no chat types in the config...");
        } else {
            this.getLogger().log(Level.INFO, "Reload config with types:");
            for (String s : chatTypes) {
                String[] strings = s.split(";");
                instance.getLogger().log(Level.INFO, strings[0] + " | " + strings[1] + " | " + strings[2] + " | " + strings[3]);
            }
        }
    }

    public static STeamChat getInstance() {
        return instance;
    }
    public String getMessageSent() {
        return messageSent;
    }
    public String getMessageReceived() {
        return messageReceived;
    }
    public List<String> getChatTypes() {
        return chatTypes;
    }
    public String getReloadPermission() {
        return reloadPermission;
    }
    public String getServerName() {
        return serverName;
    }
}
