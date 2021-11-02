package me.someoverflow.teamchat;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.logging.Level;

/**
 * @author KeksGauner
 */
public class ConfigAccessor {
 private final String fileName;
 private final Plugin plugin;

 private final File configFile;
 private Configuration fileConfiguration;

 public ConfigAccessor(Plugin plugin, String fileName) {
  if (plugin == null)
   throw new IllegalArgumentException("plugin cannot be null");
  this.plugin = plugin;
  this.fileName = fileName;
  if (!plugin.getDataFolder().exists())
   plugin.getDataFolder().mkdir();
  File dataFolder = plugin.getDataFolder();
  if (dataFolder == null)
   throw new IllegalStateException();
  this.configFile = new File(plugin.getDataFolder(), fileName);
 }

 public void reloadConfig() {
  try {
   fileConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
  } catch (IOException e) {
   e.printStackTrace();
  }
  // Look for defaults in the jar
  InputStream defConfigStream = plugin.getResourceAsStream(fileName);
  if (defConfigStream != null) {
   Configuration defConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new InputStreamReader(defConfigStream));
   for(String key : defConfig.getKeys()) {
    if(!fileConfiguration.contains(key)) {
     plugin.getLogger().log(Level.SEVERE, "Could not read config \"" + configFile + "\" check key \"" + key + "\" Did you try delete the config?", defConfig);
    }
   }
  }
 }

 public Configuration getConfig() {
  if (fileConfiguration == null) {
   this.reloadConfig();
  }
  return fileConfiguration;
 }

 public void saveConfig() {
  if (fileConfiguration != null && configFile != null) {
   try {
    ConfigurationProvider.getProvider(YamlConfiguration.class).save(getConfig(), configFile);
   } catch (IOException ex) {
    plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
   }
  } else
   plugin.getLogger().log(Level.INFO, "Could not save config to " + configFile, getConfig());
 }

 public void saveDefaultConfig() {
  if (!configFile.exists()) {
   try (InputStream in = plugin.getResourceAsStream(fileName)) {
    Files.copy(in, configFile.toPath());
   } catch (IOException e) {
    e.printStackTrace();
   }
  }
 }
}