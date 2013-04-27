package net.thechickenlegion.nmi.errors;

import org.bukkit.permissions.Permissible;

public abstract class Error 
{
	public Error(Permissible sender)
	{
		errorSender = sender;
	}
	
	protected Permissible errorSender;
	public String errorMessage = "";
	
	public abstract boolean call();
}
