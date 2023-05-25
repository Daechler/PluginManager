package net.daechler.pluginmanager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.logging.Level;

public class PluginManager extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin activation message
        getLogger().info(ChatColor.GREEN + getName() + " has been enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin disable message
        getLogger().info(ChatColor.RED + getName() + " has been disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("pmload")) {
            if (args.length != 1) {
                sender.sendMessage(ChatColor.RED + "Usage: /pmload <plugin_name>");
                return false;
            }
            String pluginName = args[0];
            Plugin plugin = getServer().getPluginManager().getPlugin(pluginName);
            if (plugin != null) {
                if (plugin.isEnabled()) {
                    sender.sendMessage(ChatColor.RED + pluginName + " is already loaded!");
                    return false;
                }
                else {
                    getServer().getPluginManager().enablePlugin(plugin);
                    sender.sendMessage(ChatColor.GREEN + pluginName + " has been loaded!");
                    return true;
                }
            }
            else {
                // Check if the plugin is in a JAR file
                File pluginFile = new File(getDataFolder().getParentFile(), pluginName);
                if (!pluginFile.exists()) {
                    sender.sendMessage(ChatColor.RED + pluginName + " not found!");
                    return false;
                }
                try {
                    plugin = getServer().getPluginManager().loadPlugin(pluginFile);
                    if (plugin != null) {
                        getServer().getPluginManager().enablePlugin(plugin);
                        sender.sendMessage(ChatColor.GREEN + pluginName + " has been loaded!");
                        return true;
                    }
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "Failed to load plugin: " + pluginName);
                    getLogger().log(Level.WARNING, ChatColor.RED + "Failed to load plugin " + pluginName, e);
                }
                sender.sendMessage(ChatColor.RED + "Failed to load plugin: " + pluginName);
                return false;
            }
        }
        else if (cmd.getName().equalsIgnoreCase("pmunload")) {
            if (args.length != 1) {
                sender.sendMessage(ChatColor.RED + "Usage: /pmunload <plugin_name>");
                return false;
            }
            String pluginName = args[0];
            Plugin plugin = getServer().getPluginManager().getPlugin(pluginName);
            if (plugin == null) {
                sender.sendMessage(ChatColor.RED + pluginName + " not found!");
                return false;
            }
            if (!plugin.isEnabled()) {
                sender.sendMessage(ChatColor.RED + pluginName + " is already unloaded!");
                return false;
            }
            getServer().getPluginManager().disablePlugin(plugin);
            sender.sendMessage(ChatColor.GREEN + pluginName + " has been unloaded!");
            return true;
        }
        else if (cmd.getName().equalsIgnoreCase("pmreload")) {
            if (args.length != 1) {
                sender.sendMessage(ChatColor.RED + "Usage: /pmreload <plugin_name>");
                return false;
            }
            String pluginName = args[0];
            Plugin plugin = getServer().getPluginManager().getPlugin(pluginName);
            if (plugin == null) {
                sender.sendMessage(ChatColor.RED + pluginName + " not found!");
                return false;
            }
            getServer().getPluginManager().disablePlugin(plugin);
            getServer().getPluginManager().enablePlugin(plugin);
            sender.sendMessage(ChatColor.GREEN + pluginName + " has been reloaded!");
            return true;
        }
        return false;
    }
}
