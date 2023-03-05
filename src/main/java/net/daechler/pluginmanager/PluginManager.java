package net.daechler.pluginmanager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.logging.Level;

public class PluginManager extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("PluginManager has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("PluginManager has been disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("pmload")) {
            if (args.length != 1) {
                sender.sendMessage("Usage: /pmload <plugin_name>");
                return false;
            }
            String pluginName = args[0];
            Plugin plugin = getServer().getPluginManager().getPlugin(pluginName);
            if (plugin != null) {
                if (plugin.isEnabled()) {
                    sender.sendMessage(pluginName + " is already loaded!");
                    return false;
                }
                else {
                    getServer().getPluginManager().enablePlugin(plugin);
                    sender.sendMessage(pluginName + " has been loaded!");
                    return true;
                }
            }
            else {
                // Check if the plugin is in a JAR file
                File pluginFile = new File(getDataFolder().getParentFile(), pluginName);
                if (!pluginFile.exists()) {
                    sender.sendMessage(pluginName + " not found!");
                    return false;
                }
                try {
                    plugin = getServer().getPluginManager().loadPlugin(pluginFile);
                    if (plugin != null) {
                        getServer().getPluginManager().enablePlugin(plugin);
                        sender.sendMessage(pluginName + " has been loaded!");
                        return true;
                    }
                } catch (Exception e) {
                    sender.sendMessage("Failed to load plugin: " + pluginName);
                    getLogger().log(Level.WARNING, "Failed to load plugin " + pluginName, e);
                }
                sender.sendMessage("Failed to load plugin: " + pluginName);
                return false;
            }
        }
        else if (cmd.getName().equalsIgnoreCase("pmunload")) {
            if (args.length != 1) {
                sender.sendMessage("Usage: /pmunload <plugin_name>");
                return false;
            }
            String pluginName = args[0];
            Plugin plugin = getServer().getPluginManager().getPlugin(pluginName);
            if (plugin == null) {
                sender.sendMessage(pluginName + " not found!");
                return false;
            }
            if (!plugin.isEnabled()) {
                sender.sendMessage(pluginName + " is already unloaded!");
                return false;
            }
            getServer().getPluginManager().disablePlugin(plugin);
            sender.sendMessage(pluginName + " has been unloaded!");
            return true;
        }
        else if (cmd.getName().equalsIgnoreCase("pmreload")) {
            if (args.length != 1) {
                sender.sendMessage("Usage: /pmreload <plugin_name>");
                return false;
            }
            String pluginName = args[0];
            Plugin plugin = getServer().getPluginManager().getPlugin(pluginName);
            if (plugin == null) {
                sender.sendMessage(pluginName + " not found!");
                return false;
            }
            getServer().getPluginManager().disablePlugin(plugin);
            getServer().getPluginManager().enablePlugin(plugin);
            sender.sendMessage(pluginName + " has been reloaded!");
            return true;
        }
        return false;
    }
}
