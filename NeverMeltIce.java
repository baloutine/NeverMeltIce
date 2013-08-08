package net.thechickenlegion.nmi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import net.thechickenlegion.nmi.commands.*;
import net.thechickenlegion.nmi.errors.InsufficientPermissionError;

public class NeverMeltIce/*Well, actually it should be called "SometimesMeltIce". It melts if you disable the plugin.*/ extends JavaPlugin implements Listener
{
	//The config file object.
	private File config;
	//Whether debug is enabled.
	private boolean debug;
	//Plugin version. Duh.
	private static String version = "1.0.0";
	//Whether or not block protection is enabled.
	private static boolean enabled = true;
	//Sets the prefix for plugin messages.
	private static String prefix = "\u00a70[\u00a7bNMI\u00a70]\u00a7f ";
	//Sets the color for message bodies.
	private static String chatcolor = "\u00a77";
	//Replaces color codes using '&' with '§' for working colors.
	String parseColorSpec(String spec) 
	{
		String res = spec.replaceAll("&(?<!&&)(?=[0-9a-fA-F])", "\u00A7");
		res = res.replace("&k", "\u00A7k");
		res = res.replace("&l", "\u00A7l");
		res = res.replace("&m", "\u00A7m");
		res = res.replace("&n", "\u00A7n");
		res = res.replace("&o", "\u00A7o");
		res = res.replace("&r", "\u00A7r");
		return res.replace("&", "&");
	}
	//Reverse of the previous: Replaces '§' with the more easily written and read '&'
	String parseColorSpecR(String spec) 
	{
		String res = spec.replaceAll("\u00A7", "&");
		return res;
	}
	public File getConfigFile() 
	{
		return config;
	}
	//The list of protected blocks.
	public List<?> protList() 
	{
		return protections;
	}
	public static void enable(boolean b)
	{
		enabled = b;
	}
	public static boolean enabled()
	{
		return enabled;
	}
	public ArrayList<?> protections = new ArrayList<Integer>();
	/*private HashMap<String,SubCommand> subcommands = new HashMap<String,SubCommand>();*/
	//Called on plugin enable, AKA on restart or reload. Used here primarily to get values from the YAML config.
	public void onEnable() 
	{
		FileConfiguration config = getConfig();
	    config.addDefault("options.protect", protections);
	    config.addDefault("options.debug-mode", false);
	    config.addDefault("chat.prefix", parseColorSpecR(prefix));
	    config.addDefault("chat.chat-color", parseColorSpecR(chatcolor));
	    config.options().copyDefaults(true);
	    saveConfig();
		/*this.saveDefaultConfig();
		folder = getDataFolder();
		config = new File(folder,"config.yml");*/
		
		protections = (ArrayList<?>) this.getConfig().getList("options.protect");
		debug = this.getConfig().getBoolean("options.debug-mode", false);
		prefix = parseColorSpec(this.getConfig().getString("chat.prefix", prefix));
		chatcolor = parseColorSpec(this.getConfig().getString("chat.chat-color", chatcolor));
		/*subcommands.put("toggle", new Enable());
		subcommands.put("version", new Version());*/
		
		Bukkit.getLogger().info("protect = " + protections);
		Bukkit.getLogger().info("debug = " + debug);
		getServer().getPluginManager().registerEvents(this,this);
		Bukkit.getLogger().info("Loaded NMI version 1.0.0");
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		//Base command
		if(cmd.getName().equalsIgnoreCase("nmi")&&args.length>0)
		{
			/*if(subcommands.containsKey(arg))
			{
				if(sender.hasPermission("NeverMeltIce."+arg) || sender.hasPermission("NeverMeltIce.*"))
				subcommands.get(arg).execute(sender);
			}*/
			String arg = args[0];
			// /nmi protections: lists protected blocks
			if(arg.equalsIgnoreCase("protections"))
			{
				if(sender.hasPermission("NeverMeltIce.list")) 
				{
					playerMsg(""+protections, sender);
					return true;
				}
				else
				{
					@SuppressWarnings("unused")
					boolean error = new InsufficientPermissionError(sender).call();
					return false;
				}
			}
			// /nmi toggle: toggles block decay on or off
			else if(arg.equalsIgnoreCase("toggle"))
			{
				if(sender.hasPermission("NeverMeltIce.toggle")) 
				{
					new Enable().execute(sender);
					return true;
				}
				else
				{
					@SuppressWarnings("unused")
					boolean error = new InsufficientPermissionError(sender).call();
					return false;
				}
			}
			// /nmi version: prints current version
			else if(arg.equalsIgnoreCase("version"))
			{
				if(sender.hasPermission("NeverMeltIce.version")) 
				{
					playerMsg(version, sender);
					return true;
				}
				else
				{
					@SuppressWarnings("unused")
					boolean error = new InsufficientPermissionError(sender).call();
					return false;
				}
			}
			// /nmi reload: reloads the plugin
			else if(arg.equalsIgnoreCase("reload"))
			{
				NeverMeltIce plugin = (NeverMeltIce) Bukkit.getServer().getPluginManager().getPlugin("NeverMeltIce");
				/*Player player = (sender instanceof Player? (Player)sender:null);*/
				if(sender.hasPermission("NeverMeltIce.reload")) 
				{
                   			getConfig();
                   			saveConfig();
                   			getServer().getPluginManager().disablePlugin(plugin);
                    		 	getServer().getPluginManager().enablePlugin(plugin);
                   			playerMsg("Reloaded!", sender);
                   			debugMsg(sender.getName().toUpperCase() + " reloaded NMI.");
                   			return true;
				}
				else
				{
					@SuppressWarnings("unused")
					boolean error = new InsufficientPermissionError(sender).call();
					return false;
				}
			}
			else
			{
				playerMsg("Valid subcommands:", sender);
				String[] scs = {"\u00A73protections","\u00A73toggle","\u00A73version"};
				sender.sendMessage(scs);
				return false;
			}
		}
		else return false;
	}
	//Output a debug message if debug mode is on.
	public void debugMsg(String s)
	{
		if(debug)
		Bukkit.getLogger().info(s);
	}
	//Sends a message with NMI formatting.
	public static void playerMsg(String str, CommandSender sender)
	{
		sender.sendMessage(prefix+chatcolor+str);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	void onBlockFade(BlockFadeEvent event)
	{
		int blockID = event.getBlock().getTypeId();
		debugMsg("Block " + event.getBlock() +" fade event called");
		if ((protections.contains((Integer)blockID))&&enabled)
		{
			event.setCancelled(true);
			debugMsg("Protected " +event.getBlock());
		}
	}
	public static String getVersion() 
	{
		return version;
	}
}
