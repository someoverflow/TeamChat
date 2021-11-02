package me.someoverflow.teamchat;

import net.md_5.bungee.api.plugin.Plugin;

import java.util.List;
import java.util.logging.Level;

public final class TeamChat extends Plugin {

    private static TeamChat instance;
    private ConfigAccessor configAccessor;

    private String messageSent;
    private String messageReceived;
    private List<String> chatTypes;

    @Override
    public void onEnable() {
        instance = this;
        instance.getLogger().log(Level.INFO, "Loading Config...");
        configAccessor = new ConfigAccessor(this, "config.yml");
        configAccessor.saveDefaultConfig();
        configAccessor.saveConfig();
        instance.getLogger().log(Level.INFO, "Config Loaded");

        instance.getLogger().log(Level.INFO, "Loading messages from Config...");
        messageSent = configAccessor.getConfig().getString("message.sent", "%prefix% \u00A78> \u00A7fYou \u00A78>> \u00A7f%message%");
        messageReceived = configAccessor.getConfig().getString("message.received", "%prefix% \u00A78> \u00A7f%sender% \u00A78>> \u00A7f%message%");
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
        this.getProxy().getPluginManager().registerListener(this, new Listener());
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
        chatTypes = configAccessor.getConfig().getStringList("chatTypes");
        if (chatTypes.isEmpty()) {
            instance.getLogger().log(Level.INFO, "There are no chat types in the config...");
        } else {
            TeamChat.getInstance().getLogger().log(Level.INFO, "Reload config with types:");
            for (String s : chatTypes) {
                String[] strings = s.split(";");
                instance.getLogger().log(Level.INFO, strings[0] + " | " + strings[1] + " | " + strings[2] + " | " + strings[3]);
            }
        }
    }

    public static TeamChat getInstance() {
        return instance;
    }

    public String getMessageReceived() {
        return messageReceived;
    }

    public String getMessageSent() {
        return messageSent;
    }

    public List<String> getChatTypes() {
        return chatTypes;
    }

    public ConfigAccessor getConfigAccessor() {
        return configAccessor;
    }
}
