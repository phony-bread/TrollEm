package frostbyte.plugins.trollem;

/******************
 * Plugin Name: Troll 'Em
 * Main Class: TrollEm.java
 * Author: _FrostByte_
 * Version: 0.1.0b
 */

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TrollEm extends JavaPlugin
{
    
    Trolls trolls = new Trolls(this); //Create an instance of the trolls class
    
    @Override
    public void onEnable() //Called when the plugin is enabled
    {
        ConsoleCommandSender console = getServer().getConsoleSender();
        getServer().getPluginManager().registerEvents(new EventListener(), this); //Register the event listener
        if(!loadConfiguration()) //Load the yml config file
        {
            console.sendMessage("[Troll Em] " + ChatColor.RED + "Failed to load");
            getServer().getPluginManager().disablePlugin(this);
        }
        else console.sendMessage("[Troll Em] Enabled."); //Print to console that we've been enabled
    }
    
    @Override
    public void onDisable() //Called when the plugin is enabled
    {
        super.onDisable(); //Default shutdown behaviour(for now)
    }

    public boolean loadConfiguration() //Called by onEnable
    {
        ConsoleCommandSender console = this.getServer().getConsoleSender();
        if(!getConfig().contains("scheduler"))
        {
            console.sendMessage("[Troll Em] " + ChatColor.RED + "ERROR: Failed to load config file!");
            console.sendMessage("[Troll Em] " + ChatColor.YELLOW + "Did you remember to copy config.yml to /plugins/Troll Em?");
            return false;
        }
        else return true;
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) //Called when a command specific to this plugin is called
    { 
        
        if(cmd.getName().equalsIgnoreCase("troll")) //If the command is 'troll'
        {
           
            if(args.length < 2 && args.length >0) //Check for a troll type argument
            {
                cs.sendMessage("You need arguments, gumgum!\n Type '/troll help' for arg lists");
                return false;
            } 
            else if(args.length < 1) //Check for a player argument
            {
                cs.sendMessage("You need a target, gumgum!\n Type '/troll help' for arg lists");
                return false;
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
                
                switch(args[1])
                {
                    default: cs.sendMessage("Invalid troll, gumgum!\n Type '/troll help' for a list of trolls");
                        break;
                    case "tnt": trolls.tnt(target, world, loc);
                        break;
                    case "creepscare": trolls.creepscare(target, world, loc);
                        break;
                    case "lightning": trolls.lightning(target, world, loc);
                        break;
                    case "fire": trolls.fire(target);
                        break;
                    case "speed": trolls.speed(target);
                        break;
                    case "drunk": trolls.drunk(target);
                        break;
                    case "up": trolls.up(target, world, loc);
                        break;
                    case "down": trolls.down(target, world, loc);
                        break;
                    case "hungry": trolls.hungry(target);
                        break;
                    case "drop": trolls.drop(target, world, loc);
                        break;
                    case "gravel": trolls.gravel(target, world, loc);
                        break;
                    case "fatigue": trolls.fatigue(target);
                        break;
                    case "chickens": trolls.chickens(target, world, loc);
                        break;
                    case "water": trolls.water(target, world, loc);
                        break;
                    case "entomb": trolls.entomb(target, world, loc);
                        break;
                    case "ghastscare": trolls.ghastscare(target, world, loc);
                        break;
                    case "fireball": trolls.fireball(target, world, loc);
                        break;
                    case "arrow": trolls.arrow(target, world, loc);
                        break;
                    case "hurt": trolls.hurt(target);
                        break;
                    case "replace": trolls.replace(target, world, loc);
                        break;
                    case "herobrine": trolls.herobrine(target);
                        break;
                    case "namechange": trolls.namechange(target);
                        break;
                    case "air": trolls.air(target, world, loc);
                        break;
                    case "poison": trolls.poison(target);
                        break;
                    case "night": trolls.night(target);
                        break;
                    case "rain": trolls.rain(target);
                        break;
                    case "chat": trolls.chat(target);
                        break;
                    case "blind": trolls.blind(target);
                        break;
                    case "weakness": trolls.weakness(target);
                        break;
                    case "wither": trolls.wither(target);
                        break;
                    case "slow": trolls.slow(target);
                        break;
                    case "bedsplode": trolls.bedsplode(target, world);
                        break;
                    case "compass": trolls.compass(target, world);
                        break;
                    case "level": trolls.level(target);
                        break;
                    case "torch": trolls.torch(target, world, loc);
                        break;
                }
            }
            return true;
         } 
        else if (cmd.getName().equalsIgnoreCase("reloadtrollconfig"))
        {
            trolls.getConfig();
            return true;
        }
        return false;
    }
    
    public boolean canRun(String troll, CommandSender cs, String isOverriding)
    {
        
    }
}
