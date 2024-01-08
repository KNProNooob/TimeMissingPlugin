package org.plugin.timemissingplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.swing.text.TabExpander;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TabComplete implements TabCompleter {


    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length == 1){
            List<String> options = new ArrayList<String>();
            options.add("list");
            options.add("remove");
            return options;
        }
        if(args[0].equalsIgnoreCase("remove") && args.length == 2){
            File file = new File("plugins/logout_times.yml");
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            Set<String> names = configuration.getKeys(false);
            List<String> options = new ArrayList<String>(names);
            return options;
        }
    return new ArrayList<>();
    }
}
