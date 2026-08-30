# Death Message

Shows a custom message when **your own player** dies — as overhead text above your
head, as a chatbox line, or both.

![plugin is cosmetic and client-side only](https://img.shields.io/badge/scope-client--side%20only-blue)

## Features

- Fires on your own death only. Other actors dying are ignored.
- Overhead text above your player, with a configurable duration.
- A game message in your chatbox.
- Enter several lines in the message box to have one picked at random each death.

## Configuration

| Setting | Description | Default |
| --- | --- | --- |
| **Death message** | Text shown when you die. Each non-blank line is a separate variant; one is chosen at random per death. | `Ouch.` |
| **Show in** | Overhead only, Chatbox only, or both. | Overhead and chatbox |
| **Overhead duration** | How long the overhead text stays up, in game ticks (1 tick = 0.6s). | 5 ticks (3s) |

## Scope

This plugin is display-only and entirely client-side:

- It reacts to the `ActorDeath` event and ignores every actor except your local player.
- It writes overhead text and a chat line into your own client's state via
  `Actor#setOverheadText`, `Actor#setOverheadCycle` and `Client#addChatMessage`
  (`ChatMessageType.GAMEMESSAGE`).
- **Nothing is sent to the game server.** No input or keypresses are injected, no
  packets are sent, no menu entries are added or invoked, and nothing is written to
  the chatbox *input*. No other player can see any of it.
- The configured text is escaped with `Text#escapeJagex` before display, so it renders
  literally rather than being interpreted as markup.

It adds no dependencies beyond the transitive dependencies of `runelite-client`.

## Building

```
./gradlew build
```

To run a development client with the plugin loaded:

```
./gradlew run
```

## License

BSD 2-Clause. See [LICENSE](LICENSE).
