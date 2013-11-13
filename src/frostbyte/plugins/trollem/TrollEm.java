package frostbyte.plugins.trollem;

/******************
 * Plugin Name: Troll 'Em
 * Main Class: TrollEm.java
 * Author: _FrostByte_
 * Version: 0.1.0b
 ******************/

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

public class TrollEm extends JavaPlugin
{
    
    FileConfiguration config;
    Trolls trolls = new Trolls(this, config); //Create an instance of the trolls class
    
    @Override
    public void onEnable() //Called when the plugin is enabled
    {
        ConsoleCommandSender console = getServer().getConsoleSender();
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
                    case "fire": if(canRun("fire", cs, isOverriding)) trolls.fire(target);
                        break;
                    case "speed": if(canRun("speed", cs, isOverriding)) trolls.speed(target);
                        break;
                    case "drunk": if(canRun("drunk", cs, isOverriding)) trolls.drunk(target);
                        break;
                    case "up": if(canRun("up", cs, isOverriding)) trolls.up(target, world, loc);
                        break;
                    case "down": if(canRun("down", cs, isOverriding)) trolls.down(target, world, loc);
                        break;
                    case "hungry": if(canRun("hungry", cs, isOverriding)) trolls.hungry(target);
                        break;
                    case "drop": if(canRun("drop", cs, isOverriding)) trolls.drop(target, world, loc);
                        break;
                    case "gravel": if(canRun("gravel", cs, isOverriding)) trolls.gravel(target, world, loc);
                        break;
                    case "fatigue": if(canRun("fatigue", cs, isOverriding)) trolls.fatigue(target);
                        break;
                    case "chickens": if(canRun("chickens", cs, isOverriding)) trolls.chickens(target, world, loc);
                        break;
                    case "water": if(canRun("water", cs, isOverriding)) trolls.water(target, world, loc);
                        break;
                    case "entomb": if(canRun("entomb", cs, isOverriding)) trolls.entomb(target, world, loc);
                        break;
                    case "ghastscare": if(canRun("ghastscare", cs, isOverriding)) trolls.ghastscare(target, world, loc);
                        break;
                    case "fireball": if(canRun("fireball", cs, isOverriding)) trolls.fireball(target, world, loc);
                        break;
                    case "arrow": if(canRun("arrow", cs, isOverriding)) trolls.arrow(target, world, loc);
                        break;
                    case "hurt": if(canRun("hurt", cs, isOverriding)) trolls.hurt(target);
                        break;
                    case "replace": if(canRun("replace", cs, isOverriding)) trolls.replace(target, world, loc);
                        break;
                    case "herobrine": if(canRun("herobrine", cs, isOverriding)) trolls.herobrine(target);
                        break;
                    case "namechange": if(canRun("namechange", cs, isOverriding)) trolls.namechange(target);
                        break;
                    case "air": if(canRun("air", cs, isOverriding)) trolls.air(target, world, loc);
                        break;
                    case "poison": if(canRun("poison", cs, isOverriding)) trolls.poison(target);
                        break;
                    case "night": if(canRun("night", cs, isOverriding)) trolls.night(target);
                        break;
                    case "rain": if(canRun("rain", cs, isOverriding)) trolls.rain(target);
                        break;
                    case "chat": if(canRun("chat", cs, isOverriding)) trolls.chat(target);
                        break;
                    case "blind": if(canRun("blind", cs, isOverriding)) trolls.blind(target);
                        break;
                    case "weakness": if(canRun("weakness", cs, isOverriding)) trolls.weakness(target);
                        break;
                    case "wither": if(canRun("wither", cs, isOverriding)) trolls.wither(target);
                        break;
                    case "slow": if(canRun("slow", cs, isOverriding)) trolls.slow(target);
                        break;
                    case "bedsplode": if(canRun("bedsplode", cs, isOverriding)) trolls.bedsplode(target, world);
                        break;
                    case "compass": if(canRun("compass", cs, isOverriding)) trolls.compass(target, world);
                        break;
                    case "level": if(canRun("level", cs, isOverriding)) trolls.level(target);
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
