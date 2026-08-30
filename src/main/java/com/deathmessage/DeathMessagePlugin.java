package com.deathmessage;

import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.Player;
import net.runelite.api.events.ActorDeath;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;

@Slf4j
@PluginDescriptor(
	name = "Death Message",
	description = "Shows a custom overhead message above your own player when you die",
	tags = {"death", "overhead", "message", "cosmetic"}
)
public class DeathMessagePlugin extends Plugin
{
	private static final String CHAT_NAME = "";

	private static final int CLIENT_TICKS_PER_GAME_TICK = Constants.GAME_TICK_LENGTH / Constants.CLIENT_TICK_LENGTH;

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private DeathMessageConfig config;

	private final Random random = new Random();

	private String lastOverheadText;

	@Override
	protected void startUp()
	{
		log.debug("Death Message started");
	}

	@Override
	protected void shutDown()
	{
		clientThread.invoke(this::clearOwnOverheadText);
		log.debug("Death Message stopped");
	}

	@Subscribe
	public void onActorDeath(ActorDeath actorDeath)
	{
		final Actor actor = actorDeath.getActor();
		clientThread.invoke(() -> onDeath(actor));
	}

	private void onDeath(Actor actor)
	{
		final Player local = client.getLocalPlayer();
		if (local == null || actor != local)
		{
			return;
		}

		final String picked = pickMessage();
		if (picked.isEmpty())
		{
			return;
		}

		// The game's font renderer reads '<', '>' and '@' as markup, so "HP < 1" would
		// render truncated. Escape the configured text so it shows up literally.
		final String message = Text.escapeJagex(picked);
		final DeathMessageMode mode = config.displayMode();

		if (mode != DeathMessageMode.CHATBOX)
		{
			local.setOverheadText(message);
			local.setOverheadCycle(config.overheadTicks() * CLIENT_TICKS_PER_GAME_TICK);
			lastOverheadText = message;
		}

		if (mode != DeathMessageMode.OVERHEAD)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, CHAT_NAME, message, null);
		}
	}

	private String pickMessage()
	{
		final List<String> variants = parseVariants(config.message());

		switch (variants.size())
		{
			case 0:
				return "";
			case 1:
				return variants.get(0);
			default:
				return variants.get(random.nextInt(variants.size()));
		}
	}

	static List<String> parseVariants(String raw)
	{
		final List<String> variants = new ArrayList<>();
		if (raw == null)
		{
			return variants;
		}

		for (String line : raw.split("\\R"))
		{
			final String trimmed = line.trim();
			if (!trimmed.isEmpty())
			{
				variants.add(trimmed);
			}
		}

		return variants;
	}

	private void clearOwnOverheadText()
	{
		final Player local = client.getLocalPlayer();
		if (local != null && lastOverheadText != null && lastOverheadText.equals(local.getOverheadText()))
		{
			local.setOverheadText("");
			local.setOverheadCycle(0);
		}

		lastOverheadText = null;
	}

	@Provides
	DeathMessageConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(DeathMessageConfig.class);
	}
}
