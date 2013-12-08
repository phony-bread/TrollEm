package frostbyte.plugins.trollem;

/******************
 * Plugin Name: Troll 'Em
 * Main Class: TrollEm.java
 * Author: _FrostByte_
 * Version: 1.0b
 ******************/

import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class TrollEm extends JavaPlugin
{
    
    FileConfiguration config;
    int taskNum;
    static BukkitScheduler scheduler;
    Trolls trolls = new Trolls(this); //Create an instance of the trolls class
    
    @Override
    public void onEnable() //Called when the plugin is enabled
    {
        scheduler = this.getServer().getScheduler();
        ConsoleCommandSender console = getServer().getConsoleSender();
        saveDefaultConfig();
        try
        {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        }
        catch(IOException e)
        {
            this.getServer().getLogger().log(Level.WARNING, "Could not connect to MCStats.org, Stats tracking disabled");
        }
        if(!loadConfiguration()) //Load the yml config file
        {
            console.sendMessage("[Troll Em] " + ChatColor.RED + "Failed to load");
            getServer().getPluginManager().disablePlugin(this);
        }
        else console.sendMessage("[Troll Em] Enabled."); //Print to console that we've been enabled
        taskNum = scheduler.scheduleSyncRepeatingTask(this, new Scheduler(this), config.getInt("scheduler.frequency"), config.getInt("scheduler.frequency"));
    }
    
    @Override
    public void onDisable() //Called when the plugin is enabled
    {
        super.onDisable(); //Default shutdown behaviour(for now)
        scheduler.cancelTask(taskNum);
    }

    public boolean loadConfiguration() //Called by onEnable
    {
        ConsoleCommandSender console = this.getServer().getConsoleSender();
        config = getConfig();
        console.sendMessage("[Troll Em] " + ChatColor.GREEN + "Config file successfully loaded.");
        if(!getConfig().contains("scheduler"))
        {
            console.sendMessage("[Troll Em] " + ChatColor.RED + "ERROR: Failed to load config file!");
            return false;
        }
        else 
        {
            return true;
        }
        
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) //Called when a command specific to this plugin is called
    { 
        
        if(cmd.getName().equalsIgnoreCase("troll")) //If the command is 'troll'
        {
           
            if(args.length < 2 && args.length >0) //Check for a troll type argument
            {
                cs.sendMessage("You need arguments, gumgum!\n Type '/trollhelp' for arg lists");
                return false;
            } 
            else if(args.length < 1) //Check for a player argument
            {
                cs.sendMessage("You need a target, gumgum!\n Type '/trollhelp' for arg lists");
                return false;
            }
            
            boolean isOverriding = false;
            if(args.length>2)
            {
                 if(args[2].equalsIgnoreCase("override")) isOverriding = true;
            }
            
            if(Bukkit.getPlayer(args[0]) == null) //Check that the player exists
            {
                cs.sendMessage("Player not found");
            }
            else
            {
                Player target = Bukkit.getPlayer(args[0]); //Get the player
                World world = target.getWorld(); //Get the players current world
                Location loc = target.getLocation(); //Get the players current location
                Object[] blacklist = getConfig().getStringList("players.blacklist").toArray();

                for(int i=0;i<blacklist.length;i++)
                {
                    if(blacklist[i].equals(target.getName()))
                    {
                        if(isOverriding&&getConfig().getString("players.can-override").equalsIgnoreCase("true")){}
                        else
                        {
                            cs.sendMessage(target.getName() + " is blacklisted and cannot be trolled");
                            return true;
                        }
                    }
                }
                
                switch(args[1])
                {
                    default: cs.sendMessage("Invalid troll, gumgum!\n Type '/trollhelp' for a list of trolls");
                        break;
                    case "tnt": if(canRun("tnt", cs, isOverriding)) trolls.tnt(target, world, loc);
                        break;
                    case "creepscare": if(canRun("creepscare", cs, isOverriding)) trolls.creepscare(target, world, loc);
                        break;
                    case "lightning": if(canRun("lightning", cs, isOverriding)) trolls.lightning(target, world, loc);
                        break;
                    case "fire": if(canRun("fire", cs, isOverriding)) trolls.fire(target, world, loc);
                        break;
                    case "speed": if(canRun("speed", cs, isOverriding)) trolls.speed(target, world, loc);
                        break;
                    case "drunk": if(canRun("drunk", cs, isOverriding)) trolls.drunk(target, world, loc);
                        break;
                    case "up": if(canRun("up", cs, isOverriding)) trolls.up(target, world, loc);
                        break;
                    case "down": if(canRun("down", cs, isOverriding)) trolls.down(target, world, loc);
                        break;
                    case "hungry": if(canRun("hungry", cs, isOverriding)) trolls.hungry(target, world, loc);
                        break;
                    case "drop": if(canRun("drop", cs, isOverriding)) trolls.drop(target, world, loc);
                        break;
                    case "gravel": if(canRun("gravel", cs, isOverriding)) trolls.gravel(target, world, loc);
                        break;
                    case "fatigue": if(canRun("fatigue", cs, isOverriding)) trolls.fatigue(target, world, loc);
                        break;
                    case "chickens": if(canRun("chickens", cs, isOverriding)) trolls.chickens(target, world, loc);
                        break;
                    case "water": if(canRun("water", cs, isOverriding)) trolls.water(target, world, loc);
                        break;
                    case "entomb": if(canRun("entomb", cs, isOverriding)) trolls.entomb(target, world, loc);
                        break;
                    case "ghastscare": if(canRun("ghastscare", cs, isOverriding)) trolls.ghastscare(target, world, loc);
                        break;
                    case "hurt": if(canRun("hurt", cs, isOverriding)) trolls.hurt(target, world, loc);
                        break;
                    case "replace": if(canRun("replace", cs, isOverriding)) trolls.replace(target, world, loc);
                        break;
                    case "air": if(canRun("air", cs, isOverriding)) trolls.air(target, world, loc);
                        break;
                    case "poison": if(canRun("poison", cs, isOverriding)) trolls.poison(target, world, loc);
                        break;
                    case "night": if(canRun("night", cs, isOverriding)) trolls.night(target, world, loc);
                        break;
                    case "rain": if(canRun("rain", cs, isOverriding)) trolls.rain(target, world, loc);
                        break;
                    case "chat": if(canRun("chat", cs, isOverriding)) trolls.chat(target, world, loc);
                        break;
                    case "blind": if(canRun("blind", cs, isOverriding)) trolls.blind(target, world, loc);
                        break;
                    case "weakness": if(canRun("weakness", cs, isOverriding)) trolls.weakness(target, world, loc);
                        break;
                    case "wither": if(canRun("wither", cs, isOverriding)) trolls.wither(target, world, loc);
                        break;
                    case "slow": if(canRun("slow", cs, isOverriding)) trolls.slow(target, world, loc);
                        break;
                    case "bedsplode": if(canRun("bedsplode", cs, isOverriding)) trolls.bedsplode(target, world, loc);
                        break;
                    case "compass": if(canRun("compass", cs, isOverriding)) trolls.compass(target, world, loc);
                        break;
                    case "level": if(canRun("level", cs, isOverriding)) trolls.level(target, world, loc);
                        break;
                    case "torch": if(canRun("torch", cs, isOverriding)) trolls.torch(target, world, loc);
                        break;
                }
            }
            return true;
         } 
        return false;
    }
    
    public boolean canRun(String troll, CommandSender cs, boolean isOverriding)
    {
        if(getConfig().getString("trolls." + troll + ".allow").equalsIgnoreCase("true"))
        {
            
            return true;
        }
        else
        {
            if(getConfig().getString("trolls." + troll + ".can-override").equalsIgnoreCase("true"))
            {
                if(isOverriding)
                {
                    return true;
                }
                else 
                {
                    cs.sendMessage(troll + " has been disabled");
                    return false;
                }
                
            }
            else 
            {
                if(isOverriding) cs.sendMessage(troll + " has been disabled and cannot be overridden");
                else cs.sendMessage(troll + " has been disabled");
                return false;
            }
        }
    }
}
