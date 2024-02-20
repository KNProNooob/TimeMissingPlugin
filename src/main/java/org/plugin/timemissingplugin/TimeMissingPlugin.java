package org.plugin.timemissingplugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import sun.font.Decoration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.random.RandomGenerator;

public final class TimeMissingPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this,this);
        Objects.requireNonNull(getCommand("missing")).setExecutor(new Commands());
        Objects.requireNonNull(getCommand("missing")).setTabCompleter(new TabComplete());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws IOException {
        boolean join_announcement = true;
        File file = new File("plugins/time_missing_settings.yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        if (configuration.contains("join_announcement")) {
            join_announcement = configuration.getBoolean("join_announcement");
        }


        file = new File("plugins/logout_times.yml");
        configuration = YamlConfiguration.loadConfiguration(file);
        if(join_announcement) {
            if (configuration.contains(event.getPlayer().getName())) {
                long time = System.currentTimeMillis() - configuration.getLong(event.getPlayer().getName());
                event.setJoinMessage(event.getJoinMessage() + "\n" + event.getPlayer().getName() + " was missing for:" + format_time(time));
            }
        }
        configuration.set(event.getPlayer().getName(), 0);
        configuration.save(file);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) throws IOException {

        long logout_time = System.currentTimeMillis();
        File file = new File("plugins/logout_times.yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        configuration.set(event.getPlayer().getName(), logout_time);
        configuration.save(file);
    }


    public static String format_time(long time){
        if(time < 1000){
            return ChatColor.BLUE + " " + ChatColor.BOLD + time + " Milliseconds";
        }else {
            time = time / 1000;
            if(time < 60){
                return ChatColor.BLUE + " " + ChatColor.BOLD + time + " Second(s)";
            }
            else {
                time = time / 60;
                if(time < 60){
                    return ChatColor.YELLOW + " " + ChatColor.BOLD + time + " Minute(s)";
                }
                else {
                    time = time / 60;
                    if(time < 24){
                        return ChatColor.GOLD + " " + ChatColor.BOLD + time + " Hour(s)";
                    }
                    else{
                        time = time / 24;
                        return ChatColor.RED + " " + ChatColor.BOLD + time + " Day(s)";
                    }
                }
            }
        }
    }
}
