package frostbyte.plugins.trollem;

import java.util.List;
import java.util.ListIterator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.Random;
import org.bukkit.command.CommandSender;

public class Trolls //Class
{
    final TrollEm plugin; //Create the instance data for the plugin
    Random generator = new Random(); //Create a new random generator
    String[] trolllist = {"tnt", "creepscare", "lightning", 
        "fire", "speed", "drunk", "up", "down", "hungry", 
        "drop", "fatigue", "gravel", "chickens", "water", 
        "entomb", "ghastscare", "hurt", 
        "replace", "air", "poison", 
        "night", "rain", "chat", "blind", "weakness", "wither", 
        "slow", "bedsplode", "compass", "level", "torch"};
    
    public Trolls(TrollEm t) //Constructor
    {
        plugin = t; //Get plugin instance
    }
    
    public boolean isAllowed(String troll, CommandSender cs)
    {
        if(plugin.getConfig().getInt("trolls." + troll + ".allow", 0)==1)
        {
            return true;
        }
        else
        {
            cs.sendMessage(troll + " has been disabled");
            return false;
        }
    }
    
    public boolean canOverride(String troll, CommandSender cs)
    {
        if(plugin.getConfig().getInt("trolls." + troll + ".can-override", 0)==1)
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
        if(plugin.getConfig().getInt("trolls." + trolllist[index] + ".allow", 0)==1)
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
        if(plugin.getConfig().getInt("trolls." + trolllist[index] + ".can-override", 0)==1)
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
        loc.setY(loc.getY() + 1); //Add to get the players eye height
        if(world.getBlockAt(loc).getType()==Material.BEDROCK) {} //If block is bedrock, do nothing
        else 
        {
            world.getBlockAt(loc).setType(Material.AIR); //If not, replace it with air
            target.getWorld().spawn(loc, TNTPrimed.class).setFuseTicks(plugin.getConfig().getInt("trolls.tnt.parameters.fuse-length")); //Place a primed tnt entity with a 25 tick fuse
        }
    }
    
    public void creepscare(Player target, World world, Location loc) //Play the creeper sound at the player
    {
        world.playSound(loc, Sound.CREEPER_HISS, plugin.getConfig().getInt("trolls.creepscare.parameters.volume"), plugin.getConfig().getInt("trolls.creepscare.parameters.pitch")); //Play the trademark creeper hiss
    }
    
    public void lightning(Player target, World world, Location loc) //Strike the player with lightning
    {
        world.strikeLightning(loc); //Strike lightning at the players feet
    }
    
    public void fire(Player target, World world, Location loc) //Set the player on fire
    {
        target.setFireTicks(plugin.getConfig().getInt("trolls.fire.parameters.ticks")); //Set the players fire ticks to 200
    }
    
    public void speed(final Player target, World world, Location loc) //Make the player have to jump to move
    {
        target.setWalkSpeed(plugin.getConfig().getLong("trolls.speed.parameters.newspeed")); //Set the players walk to -1 (Jumping only)
        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() //Create a new task schedule
        {
            @Override
            public void run() 
            {
                target.setWalkSpeed(0.2f); //Set the walkspeed back
            }
        }, plugin.getConfig().getInt("trolls.speed.parameters.ticks"));
        
    }
    
    public void drunk(Player target, World world, Location loc) //Intoxicate the player
    {
        target.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, plugin.getConfig().getInt("trolls.drunk.parameters.ticks"), plugin.getConfig().getInt("trolls.drunk.parameters.apmlitude"), plugin.getConfig().getBoolean("trolls.drunk.parameters.ambient"))); //Add the nausea effect to the player
    }
    
    public void up(Player target, World world, Location loc) //Deletes 16 blocks above the player and teleports them up
    {
       int blocks = plugin.getConfig().getInt("trolls.up.parameters.blocks");
       for(int i=0; i<blocks; i++) //Loop 15 times
       {
           loc.setY(loc.getY()+1); //Increment the y location
           world.getBlockAt(loc).setType(Material.AIR); //Replace the current block with air
       }
       loc.setY(loc.getY()-1); //Decrement the y location
       target.teleport(loc); //Teleport the player
    }
    
    public void down(Player target, World world, Location loc) //Deletes 16 blocks below the player
    {
        int blocks = plugin.getConfig().getInt("trolls.down.parameters.blocks");
        loc.setY(loc.getY()+1); //Increment the y location
        for(int i=blocks; i>1; i--) //Loop 15 times
        {
            loc.setY(loc.getY()-1); //Decrement the y location
            if(world.getBlockAt(loc).getType()==Material.BEDROCK) return; //If the material is bedrock do nothing
            else world.getBlockAt(loc).setType(Material.AIR); //If not, replace it with air
        }
    }
    
    public void hungry(Player target, World world, Location loc) //Makes the player hungry
    {
        target.setFoodLevel(plugin.getConfig().getInt("trolls.hungry.parameters.newhunger")); //Zero the players food level
        target.setSaturation(plugin.getConfig().getInt("trolls.hungry.parameters.newsaturation")); //Zero the players saturation level
    }
    
    public void drop(Player target, World world, Location loc) //Drops the currently held item
    {
        if(target.getItemInHand()==null||target.getItemInHand().getType().equals(Material.AIR)){} //If the player is not holding anything, do nothing
        else
        {
        ItemStack stack = target.getItemInHand().clone(); //Clone the item the player is holding
        target.setItemInHand(null); //Delete the item the player is holding
        if(plugin.getConfig().getBoolean("trolls.drop.parameters.intoworld"))
            world.dropItem(loc, stack); //Drop the cloned stack into the world
        }
    }
    
    public void fatigue(Player target, World world, Location loc) //Slows the players digging speed to a crawl
    {
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, plugin.getConfig().getInt("trolls.fatigue.parameters.ticks"), plugin.getConfig().getInt("trolls.fatigue.parameters.amplitude"), plugin.getConfig().getBoolean("trolls.fatigue.parameters.ambient"))); //Add the mining fatigue effect
    }
    
    public void gravel(Player target, World world, Location loc) //Spawns a 3x3 cube of gravel above the player's head
    {
        loc.setY(loc.getY()+plugin.getConfig().getInt("trolls.gravel.parameters.height")+2); //Add 4 to the y location
        loc.setX(loc.getX()-1); //Decrement the x location
        loc.setZ(loc.getZ()-1); //Decrement the z location
        int size = plugin.getConfig().getInt("trolls.gravel.parameters.size");
        for(int i=0; i<3; i++)//Y //Loop 3 times
        {
            for(int i2=0; i2<size; i2++)//X //Sub-loop 3 times
            {
                for(int i3=0; i3<size; i3++)//Z //Sub-loop 3 times
                {
                    world.getBlockAt(loc).setType(Material.GRAVEL); //Set the block at the current location to gravel
                    loc.setZ(loc.getZ()+1); //Increment the z location
                }
                loc.setZ(loc.getZ()-size); //Subtract 3 from the z location(flyback)
                loc.setX(loc.getX()+1); //Increment the x location
            }
            loc.setX(loc.getX()-size); //Subtract 3 from the x location(flyback)
            loc.setY(loc.getY()-1); //Decrement the y location
        }
    }
    
    public void chickens(Player target, World world, Location loc) //Spawns 50 chickens at the player
    {
        for(int i=0; i<plugin.getConfig().getInt("trolls.chickens.parameters.amount"); i++) //Loop 50 times
        {
        world.spawnEntity(loc, EntityType.CHICKEN); //Spawn a chicken at the player
        }
    }
    
    public void water(Player target, World world, Location loc) //Excavates 7 blocks around the player and spawns a water source block above them
    {
        int size = plugin.getConfig().getInt("trolls.water.parameters.size");
        loc.setY(loc.getY()-1); //Decrement the y location
        loc.setX(loc.getX()-3); //Subtract 3 from the x location
        loc.setZ(loc.getZ()-3); //Subtract 3 from the z location
        for(int i2=0; i2<size; i2++)//X //Loop 7 times
        {
            for(int i3=0; i3<size; i3++)//Z //Sub-loop 7 times
            {
               world.getBlockAt(loc).setType(Material.AIR); //Set the block at the current location to air
               loc.setZ(loc.getZ()+1); //Increment the z location
            }
            loc.setZ(loc.getZ()-size); //Subtract 7 from the z location(flyback)
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
        world.playSound(loc, Sound.GHAST_CHARGE, plugin.getConfig().getInt("trolls.ghastscare.parameters.volume"), plugin.getConfig().getInt("trolls.ghastscare.parameters.pitch")); //Play the ghast charging sound
        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() //Schedule a task for 15 ticks later
        {
            @Override
            public void run() 
            {
                world.playSound(loc, Sound.GHAST_FIREBALL, plugin.getConfig().getInt("trolls.ghastscare.parameters.volume"), plugin.getConfig().getInt("trolls.ghastscare.parameters.pitch")); //Play the ghast firing sound
            }
        }, 15);
    }
    
    public void hurt(Player target, World world, Location loc) //Hurts the player for 1 heart of damage
    {
        target.damage(plugin.getConfig().getDouble("trolls.hurt.parameters.damage")); //Damage the player for 2 point (1 heart)
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
    
    public boolean air(Player target, World world, Location loc)//UNTESTED
    {
        if(world.getBlockAt(loc).isLiquid()) //If the playing is swimming
        {
            target.setRemainingAir(plugin.getConfig().getInt("trolls.air.parameters.newair")); //Remove the remaining air
            return true; //Let the caller know that we succeded
        }
        return false; //Let the caller know that we failed
    }
    
    public void poison(Player target, World world, Location loc)//UNTESTED
    {
        target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, plugin.getConfig().getInt("trolls.poison.parameters.ticks"), plugin.getConfig().getInt("trolls.poison.parameters.amplitude"), plugin.getConfig().getBoolean("trolls.poison.parameters.ambient")), true); //Add the poison effect for 300 ticks(15 seconds)
    }
    
    public void night(final Player target, World world, Location loc)//UNTESTED
    {
        target.setPlayerTime(12000l, true); //Set the player's local time to night
        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() //Schedule a task for 19000(~15 mins) later
        {
            @Override
            public void run() 
            {
                target.resetPlayerTime(); //Catch the player's time back up with the server
            }
        }, plugin.getConfig().getInt("trolls.night.parameters.ticks"));
    }
    
    public void rain(final Player target, World world, Location loc)//UNTESTED
    {
        target.setPlayerWeather(WeatherType.DOWNFALL); //Set the players local weather to rain
        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() //Schedule a task for 19000(~15 mins later)
        {
            @Override
            public void run() 
            {
                target.resetPlayerWeather(); //Catch the player's weather back up with the server
            }
        }, plugin.getConfig().getInt("trolls.rain.parameters.ticks"));
    }
    
    public void chat(Player target, World world, Location loc)//UNTESTED
    {
        List<String> list = plugin.getConfig().getStringList("trolls.chat.parameters.messages");
        target.chat(list.get(generator.nextInt(list.size()))); //Tell the world that this player just got trolled
    }
    
    public void blind(Player target, World world, Location loc)//UNTESTED
    {
        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, plugin.getConfig().getInt("trolls.blind.parameters.ticks"), plugin.getConfig().getInt("trolls.blind.parameters.amplitude"), plugin.getConfig().getBoolean("trolls.blind.parameters.ambient")), true); //Add the poison effect for 420 ticks(21 seconds)
    }
    
    public void weakness(Player target, World world, Location loc)//UNTESTED
    {
        target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, plugin.getConfig().getInt("trolls.weakness.parameters.ticks"), plugin.getConfig().getInt("trolls.weakness.parameters.amplitude"), plugin.getConfig().getBoolean("trolls.weakness.parameters.ambient")), true); //Add the poison effect for 820 ticks(41 seconds)
    }
    
    public void wither(Player target, World world, Location loc)//UNTESTED
    {
        target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, plugin.getConfig().getInt("trolls.wither.parameters.ticks"), plugin.getConfig().getInt("trolls.wither.parameters.amplitude"), plugin.getConfig().getBoolean("trolls.wither.parameters.ambient")), true); //Add the poison effect for 300 ticks(15 seconds)
    }
    
    public void slow(Player target, World world, Location loc)//UNTESTED
    {
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, plugin.getConfig().getInt("trolls.slow.parameters.ticks"), plugin.getConfig().getInt("trolls.slow.parameters.amplitude"), plugin.getConfig().getBoolean("trolls.slow.parameters.ambient")), true); //Add the poison effect for 420 ticks(21 seconds)
    }
    
    public void bedsplode(Player target, World world, Location loc)//UNTESTED
    {
        loc = target.getBedSpawnLocation(); //Find out the player's bed location
        world.createExplosion(loc, plugin.getConfig().getInt("trolls.bedsplode.parameters.yield"), plugin.getConfig().getBoolean("trolls.bedsplode.parameters.fire")); //Explode it!
    }
    
    public boolean compass(Player target, World world, Location loc)//UNTESTED
    {
        ListIterator<ItemStack> iterator = target.getInventory().iterator(); //Create an object to iterate through the player's inventory
        boolean hasCompass = false; //Create a variable to remember if the player has a compass
        int distance = plugin.getConfig().getInt("trolls.compass.parameters.distance");
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
            loc = new Location(world, generator.nextGaussian()*distance, generator.nextGaussian()*distance, 0); //Set a new random location within 2000 blocks
            target.setCompassTarget(loc); //Set the compass target to be the new location
        }
        return hasCompass; //Let the caller know whether or not we succeded
    }
    
    public void level(Player target, World world, Location loc)//UNTESTED
    {
        target.setLevel(plugin.getConfig().getInt("trolls.level.parameters.newlevel")); //Reset the player's level to one
    }
    
    public boolean torch(Player target, World world, Location loc) //Scans a 31x31x31 area around the player for torches and deletes them
    {
        int range = plugin.getConfig().getInt("trolls.torch.parameters.range");
        loc.setX(loc.getX()-range/2); //Subtract 15 from the x location
        loc.setZ(loc.getZ()-range/2); //Subtract 15 from the z location
        boolean hasTorch = false; //Create a variable to remember if there are any torches
        for(int c=0; c<3; c++) //Loop 3 times
        {
            for(int i=0; i<range; i++)//y //Sub-loop 31 times
            {
                for(int i2=0; i2<range; i2++)//x //Sub-loop 31 times
                {
                    for(int i3=0; i3<range; i3++)//z //Sub-loop 31 times
                    {
                        if(world.getBlockAt(loc).getType()==Material.TORCH) //If a torch exists at the current location
                        {
                            world.getBlockAt(loc).setType(Material.AIR); //Replace it with air
                            if(plugin.getConfig().getBoolean("trolls.torch.parameters.explode"))
                                world.createExplosion(loc, plugin.getConfig().getLong("trolls.torch.parameters.yield"), false); //Blow that shit up
                            hasTorch=true; //Remember that we did so
                        }
                        loc.setZ(loc.getZ()+1); //Increment the z location
                    }
                    loc.setX(loc.getX()+1); //Increment the x location
                    loc.setZ(loc.getZ()-range); //Subtract 31 from the z location(flyback)
                }
                loc.setY(loc.getY()+1); //Increment the y location
                loc.setX(loc.getX()-range); //Subtract 31 from the x location(flyback)
            }
            loc.setY(loc.getY()-range); //Subtract 31 from the y location(flyback)
        }
        return hasTorch; //Let the caller know if we succeded
    }
}
