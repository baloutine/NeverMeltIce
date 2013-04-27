package net.thechickenlegion.nmi.errors;

import net.thechickenlegion.nmi.NeverMeltIce;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permissible;

public class InsufficientPermissionError extends Error
{
	public String errorMessage = "Insufficient permission.";
	public InsufficientPermissionError(Permissible sender) 
	{
		super(sender);
	}

	@Override
	public boolean call() 
	{
		if(errorSender instanceof CommandSender)
		{
			NeverMeltIce.playerMsg(errorMessage, (CommandSender) errorSender);
			return true;
		}
		else return false;
	}
	
}
