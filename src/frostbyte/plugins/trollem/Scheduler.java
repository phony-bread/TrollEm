package frostbyte.plugins.trollem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.World;

public class Scheduler extends BukkitRunnable
{
    private final TrollEm plugin;
    private Random gen = new Random();
    
    public Scheduler(TrollEm plugin)
    {
        this.plugin = plugin;
    }
    
    @Override
    public void run()
    {
        System.out.println("Random troll called!");
        Trolls trolls = plugin.trolls;
        Player[] list = plugin.getServer().getOnlinePlayers();
        if(list.length<1) return;
        Player target = list[gen.nextInt(list.length)];
        World world = target.getWorld();
        Location loc = target.getLocation();
        String[] trolllist = trolls.getTrollList();
        String troll = trolllist[gen.nextInt(trolllist.length)];
        try
        {
            Method trollMethod = trolls.getClass().getMethod(troll, Player.class, World.class, Location.class);
            try
            {
                trollMethod.invoke(trolls, target, world, loc);
            }
            catch(IllegalArgumentException|IllegalAccessException|InvocationTargetException e)
            {
                System.out.println("Troll failed");
                e.printStackTrace();
            }
        }
        catch(SecurityException|NoSuchMethodException e)
        {
            System.out.println("Method reflection denied");
        }
    }
}
