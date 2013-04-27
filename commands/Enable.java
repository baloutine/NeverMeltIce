package net.thechickenlegion.nmi.commands;

import net.thechickenlegion.nmi.*;
import org.bukkit.command.CommandSender;

public class Enable extends SubCommand
{
	public Enable() 
	{
	}
	private boolean booltoggle(boolean b)
	{
		return !b;
	}

	public boolean execute(CommandSender sender) 
	{
		NeverMeltIce.playerMsg("Block decay protection toggled to " + booltoggle(NeverMeltIce.enabled()), sender);
		NeverMeltIce.enable(booltoggle(NeverMeltIce.enabled()));
		return true;
	}
	
}
