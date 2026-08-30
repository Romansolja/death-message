package com.deathmessage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class DeathMessageVariantsTest
{
	@Test
	public void singleLineYieldsOneVariant()
	{
		assertEquals(List.of("Ouch."), DeathMessagePlugin.parseVariants("Ouch."));
	}

	@Test
	public void unixNewlinesSplitIntoVariants()
	{
		assertEquals(List.of("one", "two", "three"), DeathMessagePlugin.parseVariants("one\ntwo\nthree"));
	}

	@Test
	public void windowsNewlinesSplitIntoVariants()
	{
		assertEquals(List.of("one", "two"), DeathMessagePlugin.parseVariants("one\r\ntwo"));
	}

	@Test
	public void blankLinesAndSurroundingWhitespaceAreDropped()
	{
		assertEquals(List.of("first", "second"), DeathMessagePlugin.parseVariants("\n  first  \n\n\t\nsecond\n\n"));
	}

	@Test
	public void emptyNullAndWhitespaceOnlyYieldNoVariants()
	{
		assertTrue(DeathMessagePlugin.parseVariants(null).isEmpty());
		assertTrue(DeathMessagePlugin.parseVariants("").isEmpty());
		assertTrue(DeathMessagePlugin.parseVariants("   \n\t\n  ").isEmpty());
	}

	@Test
	public void everyVariantIsReachableByIndex()
	{
		final List<String> variants = DeathMessagePlugin.parseVariants("a\nb\nc\nd");
		assertEquals(4, variants.size());

		final Set<String> seen = new HashSet<>();
		for (int i = 0; i < variants.size(); i++)
		{
			seen.add(variants.get(i));
		}

		assertEquals(new HashSet<>(List.of("a", "b", "c", "d")), seen);
	}
}
