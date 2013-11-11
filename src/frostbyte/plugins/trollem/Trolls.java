package frostbyte.plugins.trollem;

import java.util.ListIterator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import java.util.Random;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class Trolls //Class
{
    final TrollEm plugin; //Create the instance data for the plugin
    Random generator = new Random(); //Create a new random generator
    FileConfiguration config;
    String[] trolllist = {"tnt", "creepscare", "lightning", 
        "fire", "speed", "drunk", "up", "down", "hungry", 
        "drop", "fatigue", "gravel", "chickens", "water", 
        "entomb", "ghastscare", "fireball", "arrow", "hurt", 
        "replace", "herobrine", "namechange", "air", "poison", 
        "night", "rain", "chat", "blind", "weakness", "wither", 
        "slow", "bedsplode", "compass", "level", "torch"};
    
    public Trolls(TrollEm t) //Constructor
    {
        plugin = t; //Get plugin instance
        config = plugin.getConfig();
    }
    
    public boolean isAllowed(String troll, CommandSender cs)
    {
        if(config.getInt("trolls." + troll + ".allow", 0)==1)
        {
            return true;
        }
        else
        {
            cs.sendMessage(troll + " has been disabled");
            return false;
        }
    }
    
    public void getConfig()
    {
        config = plugin.getConfig();
    }
    
    public boolean canOverride(String troll, CommandSender cs)
    {
        if(config.getInt("trolls." + troll + ".can-override", 0)==1)
        {
            return true;
        }
        else
        {
            cs.sendMessage(troll + " cannot be overridden");
            return false;
        }
    }
    
    public boolean isAllowedByIndex(int index, CommandSender cs)
    {
        if(config.getInt("trolls." + trolllist[index] + ".allow", 0)==1)
        {
            return true;
        }
        else
        {
            cs.sendMessage(trolllist[index] + " has been disabled");
            return false;
        }
    }
    
    public boolean canOverrideByIndex(int index, CommandSender cs)
    {
        if(config.getInt("trolls." + trolllist[index] + ".can-override", 0)==1)
        {
            return true;
        }
        else
        {
            cs.sendMessage(trolllist[index] + " cannot be overridden");
            return false;
        }
    }
    
    public int getIndex(String troll)
    {
        for(int i=0; i<=trolllist.length; i++)
        {
            if(trolllist[i].equalsIgnoreCase(troll)) return i;
        }
        return -1;
    }
    
    public String[] getTrollList()
    {
        return trolllist;
    }
    
    public void tnt(Player target, World world, Location loc) //Places tnt at the player's eye height
    {
        loc.setY(loc.getY() + 1); //Add get players eye height
        if(world.getBlockAt(loc).getType()==Material.BEDROCK) {} //If block is bedrock, do nothing
        else 
        {
            world.getBlockAt(loc).setType(Material.AIR); //If not, replace it with air
            target.getWorld().spawn(loc, TNTPrimed.class).setFuseTicks(25); //Place a primed tnt entity with a 25 tick fuse
        }
    }
    
    public void creepscare(Player target, World world, Location loc) //Play the creeper sound at the player
    {
        world.playSound(loc, Sound.CREEPER_HISS, 20, 1); //Play the trademark creeper hiss
    }
    
    public void lightning(Player target, World world, Location loc) //Strike the player with lightning
    {
        world.strikeLightning(loc); //Strike lightning at the players feet
    }
    
    public void fire(Player target) //Set the player on fire
    {
        target.setFireTicks(200); //Set the players fire ticks to 200
    }
    
    public void speed(final Player target) //Make the player have to jump to move
    {
        target.setWalkSpeed(-1.0f); //Set the players walk to -1 (Jumping only)
        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() //Create a new task schedule
        {
            @Override
            public void run() 
            {
                target.setWalkSpeed(0.2f); //Set the walkspeed back
            }
        }, 200l);
        
    }
    
    public void drunk(Player target) //Intoxicate the player
    {
        target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 320, 5, false)); //Add the nausea effect to the player
    }
    
    public void up(Player target, World world, Location loc) //Deletes 16 blocks above the player and teleports them up
    {
       for(int i=1; i<16; i++) //Loop 15 times
       {
           loc.setY(loc.getY()+1); //Increment the y location
           world.getBlockAt(loc).setType(Material.AIR); //Replace the current block with air
       }
       loc.setY(loc.getY()-1); //Decrement the y location
       target.teleport(loc); //Teleport the player
    }
    
    public void down(Player target, World world, Location loc) //Deletes 16 blocks below the player
    {
        loc.setY(loc.getY()+1); //Increment the y location
        for(int i=16; i>1; i--) //Loop 15 times
        {
            loc.setY(loc.getY()-1); //Decrement the y location
            if(world.getBlockAt(loc).getType()==Material.BEDROCK) return; //If the material is bedrock do nothing
            else world.getBlockAt(loc).setType(Material.AIR); //If not, replace it with air
        }
    }
    
    public void hungry(Player target) //Makes the player hungry
    {
        target.setFoodLevel(0); //Zero the players food level
        target.setSaturation(0f); //Zero the players saturation level
    }
    
    public void drop(Player target, World world, Location loc) //Drops the currently held item
    {
        if(target.getItemInHand()==null||target.getItemInHand().getType().equals(Material.AIR)){} //If the player is not holding anything, do nothing
        else
        {
        ItemStack stack = target.getItemInHand().clone(); //Clone the item the player is holding
        target.setItemInHand(null); //Delete the item the player is holding
        world.dropItem(loc, stack); //Drop the cloned stack into the world
        }
    }
    
    public void fatigue(Player target) //Slows the players digging speed to a crawl
    {
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 350, 10, false)); //Add the mining fatigue effect
    }
    
    public void gravel(Player target, World world, Location loc) //Spawns a 3x3 cube of gravel above the player's head
    {
        loc.setY(loc.getY()+4); //Add 4 to the y location
        loc.setX(loc.getX()-1); //Decrement the x location
        loc.setZ(loc.getZ()-1); //Decrement the z location
        
        for(int i=0; i<3; i++)//Y //Loop 3 times
        {
            for(int i2=0; i2<3; i2++)//X //Sub-loop 3 times
            {
                for(int i3=0; i3<3; i3++)//Z //Sub-loop 3 times
                {
                    world.getBlockAt(loc).setType(Material.GRAVEL); //Set the block at the current location to gravel
                    loc.setZ(loc.getZ()+1); //Increment the z location
                }
                loc.setZ(loc.getZ()-3); //Subtract 3 from the z location(flyback)
                loc.setX(loc.getX()+1); //Increment the x location
            }
            loc.setX(loc.getX()-3); //Subtract 3 from the x location(flyback)
            loc.setY(loc.getY()-1); //Decrement the y location
        }
    }
    
    public void chickens(Player target, World world, Location loc) //Spawns 50 chickens at the player
    {
        for(int i=0; i<50; i++) //Loop 50 times
        {
        world.spawnEntity(loc, EntityType.CHICKEN); //Spawn a chicken at the player
        }
    }
    
    public void water(Player target, World world, Location loc) //Excavates 7 blocks around the player and spawns a water source block above them
    {
        loc.setY(loc.getY()-1); //Decrement the y location
        loc.setX(loc.getX()-3); //Subtract 3 from the x location
        loc.setZ(loc.getZ()-3); //Subtract 3 from the z location
        for(int i2=0; i2<7; i2++)//X //Loop 7 times
        {
            for(int i3=0; i3<7; i3++)//Z //Sub-loop 7 times
            {
               world.getBlockAt(loc).setType(Material.AIR); //Set the block at the current location to air
               loc.setZ(loc.getZ()+1); //Increment the z location
            }
            loc.setZ(loc.getZ()-7); //Subtract 7 from the z location(flyback)
            loc.setX(loc.getX()+1); //Increment the x location
        }
        
        Location loc2 = target.getLocation(); //Set a new location at the players feet
        loc2.setY(loc2.getY()+3); //Increment the y of the new location
        world.getBlockAt(loc2).setType(Material.WATER); //Place a water source block at the new location
    }
    
    public void entomb(Player target, World world, Location loc) //Entombs the player in dirt
    {
        loc.setX(loc.getX()-1); //Decrement the x location
        loc.setZ(loc.getZ()-1); //Decrement the z location
        for(int c=0; c<3; c++) //Loop 3 times
        {
            for(int i=0; i<3; i++)//y //Sub-loop 3 times
            {
                for(int i2=0; i2<3; i2++)//x //Sub-loop 3 times
                {
                    for(int i3=0; i3<3; i3++)//z //Sub-loop 3 times
                    {
                        world.getBlockAt(loc).setType(Material.DIRT); //Set the block at the current location to dirt
                        loc.setZ(loc.getZ()+1);// Increment the z location
                    }
                    loc.setX(loc.getX()+1); //Increment the x location
                    loc.setZ(loc.getZ()-3); //Subtract 3 from the z location(flyback)
                }
                loc.setY(loc.getY()+1); //Increment the x location
                loc.setX(loc.getX()-3); //Subtract 3 from the x location(flyback)
            }
            loc.setY(loc.getY()-3); //Subtract 3 from the y location(flyback)
        }
        Location loc2 = target.getLocation(); //Set a new location at the players feet
        world.getBlockAt(loc2).setType(Material.AIR); //Set the block at the current location to be air
        loc2.setY(loc2.getY()+1); //Increment the y location
        world.getBlockAt(loc2).setType(Material.AIR); //Set the block at the current location to be air
        loc2.setY(loc2.getY()+2); //Add 2 to the y location
        world.getBlockAt(loc2).setType(Material.AIR); //Set the block at the current location to be air
    }
    
    public void ghastscare(Player target, final World world, final Location loc) //Plays the ghast fireball sound at the player
    {
        world.playSound(loc, Sound.GHAST_CHARGE, 20, 1); //Play the ghast charging sound
        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() //Schedule a task for 15 ticks later
        {
            @Override
            public void run() 
            {
                world.playSound(loc, Sound.GHAST_FIREBALL, 20, 1); //Play the ghast firing sound
            }
        }, 15);
    }
    
    public void fireball(Player target, World world, Location loc)//INCOMPLETE
    {
        Fireball fireball = world.spawn(loc, Fireball.class); //Spawn a fireball at the player
        fireball.setYield(0.5f); //Half the explosive yield
    }
    
    public void arrow(Player target, World world, Location loc)//INCOMPLETE
    {
        Location loc2 = loc; //Clone the location
        loc2.setY(loc2.getY()+1); //Increment the y location
        loc2.setX(loc2.getX()+5); //Add 5 to the x location
        Vector vector = loc2.toVector().subtract(loc.toVector()); //Calculate the vector between the arrow and the player
        world.spawnArrow(loc2, vector, 0.6f, 12f); //Spawn the arrow and set the vector
    }
    
    public void hurt(Player target) //Hurts the player for 1 heart of damage
    {
        target.damage(2d); //Damage the player for 2 point (1 heart)
    }

    public void replace(Player target, World world, Location loc) //Drops the players whole inventory and replace it with stacks of dirt
    {
        ListIterator<ItemStack> iterator = target.getInventory().iterator(); //Create an iterator for the players inventory
        for(int i=0; i<36; i++) //Loop 36 times(the size of the inventory)
        {
            if(iterator.hasNext()) //If the next spot in the inventory exists(just in case)
            {
                ItemStack stack = iterator.next(); //Go the the next inventory slot
                if(stack!=null&&stack!=new ItemStack(Material.AIR)) //If there is anything in the slot
                {
                    world.dropItem(loc, stack.clone()); //Drop a clone of the item into the world
                    iterator.set(new ItemStack(Material.DIRT, 64)); //Replace the item with a stack of dirt
                }
                else 
                {
                    iterator.set(new ItemStack(Material.DIRT, 64)); //Place a stack of dirt in the slot
                }
            }
            else break; //If there is not a next inventory slot, abandon the loop
        }
    }
    
    public void herobrine(Player target)//INCOMPLETE
    {
        target.sendRawMessage(null);
    }
    
    public boolean namechange(Player target)//UNTESTED
    {
        if(plugin.getServer().getPlayerExact("gumgum")==null) //Make sure there is not already a player called gumgum(you never know!)
        {
            target.setPlayerListName("GumGum"); //Set the scoreboard name to GumGum
            target.setDisplayName("GumGum"); //Set the in-game name to GumGum
            return true; //Let the caller know that we succeded
        }
        else return false; //Let the caller know that we failed
    }
    
    public boolean air(Player target, World world, Location loc)//UNTESTED
    {
        if(world.getBlockAt(loc).isLiquid()) //If the playing is swimming
        {
            target.setRemainingAir(0); //Remove the remaining air
            return true; //Let the caller know that we succeded
        }
        return false; //Let the caller know that we failed
    }
    
    public void poison(Player target)//UNTESTED
    {
        target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 300, 1, false), true); //Add the poison effect for 300 ticks(15 seconds)
    }
    
    public void night(final Player target)//UNTESTED
    {
        target.setPlayerTime(12000l, true); //Set the player's local time to night
        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() //Schedule a task for 19000(~15 mins) later
        {
            @Override
            public void run() 
            {
                target.resetPlayerTime(); //Catch the player's time back up with the server
            }
        }, 19000);
    }
    
    public void rain(final Player target)//UNTESTED
    {
        target.setPlayerWeather(WeatherType.DOWNFALL); //Set the players local weather to rain
        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() //Schedule a task for 19000(~15 mins later)
        {
            @Override
            public void run() 
            {
                target.resetPlayerWeather(); //Catch the player's weather back up with the server
            }
        }, 19000);
    }
    
    public void chat(Player target)//UNTESTED
    {
        target.chat("I just got trolled! :("); //Tell the world that this player just got trolled
    }
    
    public void blind(Player target)//UNTESTED
    {
        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 420, 1, false), true); //Add the poison effect for 420 ticks(21 seconds)
    }
    
    public void weakness(Player target)//UNTESTED
    {
        target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 820, 1, false), true); //Add the poison effect for 820 ticks(41 seconds)
    }
    
    public void wither(Player target)//UNTESTED
    {
        target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 300, 1, false), true); //Add the poison effect for 300 ticks(15 seconds)
    }
    
    public void slow(Player target)//UNTESTED
    {
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 420, 1, false), true); //Add the poison effect for 420 ticks(21 seconds)
    }
    
    public void bedsplode(Player target, World world)//UNTESTED
    {
        Location loc = target.getBedSpawnLocation(); //Find out the player's bed location
        world.createExplosion(loc, 4, false); //Explode it!
    }
    
    public boolean compass(Player target, World world)//UNTESTED
    {
        ListIterator<ItemStack> iterator = target.getInventory().iterator(); //Create an object to iterate through the player's inventory
        boolean hasCompass = false; //Create a variable to remember if the player has a compass
        
        for(int i=0; i<36; i++) //Loop 36 times(the length of an inventory)
        {
            if(iterator.hasNext()) //If the inventory has a next slot(just in case)
            {
                ItemStack stack = iterator.next(); //Go to the next inventory slot
                if(stack==new ItemStack(Material.COMPASS)) //If the slot containes a compass
                {
                    hasCompass = true; //Remember that the compass exists
                }
            }
            else break; //Otherwise, screw the whole thing
        }
        
        if(hasCompass) //If the player has a compass
        {
            Location loc = new Location(world, generator.nextGaussian()*1000, generator.nextGaussian()*1000, 0); //Set a new random location within 2000 blocks
            target.setCompassTarget(loc); //Set the compass target to be the new location
        }
        return hasCompass; //Let the caller know whether or not we succeded
    }
    
    public void level(Player target)//UNTESTED
    {
        target.setLevel(1); //Reset the player's level to one
    }
    
    public boolean torch(Player target, World world, Location loc) //Scans a 31x31x31 area around the player for torches and deletes them
    {
        loc.setX(loc.getX()-15); //Subtract 15 from the x location
        loc.setZ(loc.getZ()-15); //Subtract 15 from the z location
        boolean hasTorch = false; //Create a variable to remember if there are any torches
        for(int c=0; c<3; c++) //Loop 3 times
        {
            for(int i=0; i<31; i++)//y //Sub-loop 31 times
            {
                for(int i2=0; i2<31; i2++)//x //Sub-loop 31 times
                {
                    for(int i3=0; i3<31; i3++)//z //Sub-loop 31 times
                    {
                        if(world.getBlockAt(loc).getType()==Material.TORCH) //If a torch exists at the current location
                        {
                            world.getBlockAt(loc).setType(Material.AIR); //Replace it with air
                            world.createExplosion(loc, 0.5f, false); //Blow that shit up
                            hasTorch=true; //Remember that we did so
                        }
                        loc.setZ(loc.getZ()+1); //Increment the z location
                    }
                    loc.setX(loc.getX()+1); //Increment the x location
                    loc.setZ(loc.getZ()-31); //Subtract 31 from the z location(flyback)
                }
                loc.setY(loc.getY()+1); //Increment the y location
                loc.setX(loc.getX()-31); //Subtract 31 from the x location(flyback)
            }
            loc.setY(loc.getY()-31); //Subtract 31 from the y location(flyback)
        }
        return hasTorch; //Let the caller know if we succeded
    }
}
