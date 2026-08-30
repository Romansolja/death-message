package com.deathmessage;

public enum DeathMessageMode
{
	OVERHEAD("Overhead only"),
	CHATBOX("Chatbox only"),
	BOTH("Overhead and chatbox");

	private final String label;

	DeathMessageMode(String label)
	{
		this.label = label;
	}

	@Override
	public String toString()
	{
		return label;
	}
}
