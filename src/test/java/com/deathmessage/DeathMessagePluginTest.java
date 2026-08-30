package com.deathmessage;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class DeathMessagePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(DeathMessagePlugin.class);
		RuneLite.main(args);
	}
}
