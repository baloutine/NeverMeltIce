package net.thechickenlegion.nmi.commands;

import org.bukkit.command.*;

public abstract class SubCommand 
{
	public SubCommand() 
	{
	}
	public abstract boolean execute(CommandSender sender);
}
