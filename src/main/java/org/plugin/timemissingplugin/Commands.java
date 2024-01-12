package org.plugin.timemissingplugin;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args[0].equalsIgnoreCase("list")){
            if(sender instanceof Player){
                Player player = (Player) sender;

                File file = new File("plugins/logout_times.yml");
                YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                Set<String> names = configuration.getKeys(false);
                player.sendMessage("Missing list: ");
                for(String name : names){

                    if(configuration.getLong(name) == 0) {
                        player.sendMessage(name + ": " + ChatColor.GREEN + ChatColor.BOLD + "ONLINE");
                    }
                    else{
                        long time = configuration.getLong(name);
                        TextComponent text = new TextComponent(name + ":");
                        text.addExtra(date(time));
                        player.spigot().sendMessage(text);
                    }
                }
            }
        }

        if(args[0].equalsIgnoreCase("remove")){
            if(sender instanceof Player){
                Player player = (Player) sender;
                if(player.isOp()) {
                    if(args.length == 2) {
                        String name = args[1];
                        File file = new File("plugins/logout_times.yml");
                        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                        if (!configuration.contains(name)) {
                            player.sendMessage(ChatColor.RED + name + " isn't on the missing list");
                        } else {
                            configuration.set(name, null);
                            try {
                                configuration.save(file);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            player.sendMessage(name + " was removed from the missing list");
                        }
                    }
                    else {
                        player.sendMessage(ChatColor.RED +  "You need to provide a name! \n Usage: /missing remove <Player>");
                    }
                }
                else{
                    player.sendMessage(ChatColor.RED + "You need to be op to run this command!");
                }

            }
        }

        return true;
    }


    public static TextComponent date(long time){

        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        String formatted = format.format(date);
        TextComponent text = new TextComponent(TimeMissingPlugin.format_time(System.currentTimeMillis() - time));
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(formatted).create()));
        return text;
    }
}
