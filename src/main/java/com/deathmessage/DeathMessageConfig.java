package com.deathmessage;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;
import net.runelite.client.config.Units;

@ConfigGroup(DeathMessageConfig.GROUP)
public interface DeathMessageConfig extends Config
{
	String GROUP = "deathmessage";

	@ConfigItem(
		keyName = "message",
		name = "Death message",
		description = "Text shown when you die. Put each variant on its own line to have one picked at random. Shown literally, so < > and @ are safe to use.",
		position = 1
	)
	default String message()
	{
		return "Ouch.";
	}

	@ConfigItem(
		keyName = "displayMode",
		name = "Show in",
		description = "Where to show the message. Every option is client-side only.",
		position = 2
	)
	default DeathMessageMode displayMode()
	{
		return DeathMessageMode.BOTH;
	}

	@ConfigItem(
		keyName = "overheadTicks",
		name = "Overhead duration",
		description = "How long the overhead text stays up, in game ticks. One game tick is 0.6 seconds, so 5 ticks is 3 seconds.",
		position = 3
	)
	@Units(Units.TICKS)
	@Range(min = 1, max = 300)
	default int overheadTicks()
	{
		return 5;
	}
}
