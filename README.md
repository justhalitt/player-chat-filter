# 🎯 Player Chat Filter

A lightweight Fabric client-side mod that lets you filter the chat to only show messages from a specific player. Perfect for Tier Lists, 1v1s, or any situation where you want to focus on one player's chat.

---

## ✨ Features

- Filter chat to show only one player's messages
- Tab-complete support for online player names
- Show/hide your own messages while filtering
- Show/hide server system messages while filtering
- Works on cracked and premium servers
- Configurable via Mod Menu (optional)

---

## 📋 Commands

| Command | Description |
|---|---|
| `/chatfilter <player>` | Show only that player's messages |
| `/chatfilteroff` | Disable the filter, show everyone |

---

## 🔧 Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft **1.21.11**
2. Download and place into your `.minecraft/mods/` folder:
   - [Fabric API](https://modrinth.com/mod/fabric-api/versions?g=1.21.11) *(required)*
   - `player-chat-filter.jar` *(this mod)*
3. **Optional** — for the settings GUI:
   - [ModMenu](https://modrinth.com/mod/modmenu/version/13.0.0)
   - [Cloth Config](https://modrinth.com/mod/cloth-config/versions?g=1.21.11)

---

## ⚙️ Settings

If you have ModMenu and Cloth Config installed, you can access the settings screen from the Mods menu:

| Setting | Default | Description |
|---|---|---|
| Show my own messages | ✅ On | Show your own chat messages while filtering |
| Show server messages | ✅ On | Show server system messages while filtering |

---

## 🧩 Compatibility

| | Status |
|---|---|
| Minecraft | 1.21.11 |
| Fabric Loader | ≥ 0.18.1 |
| Fabric API | Required |
| ModMenu | Optional |
| Cloth Config | Optional |
| Cracked servers | ✅ |
| Premium servers | ✅ |
| BungeeCord / Velocity | ✅ |

---

## 🛠️ Building from Source

Requirements: Java 21

```bash
git clone https://github.com/justhalit/player-chat-filter
cd player-chat-filter
./gradlew build
```

Output: `build/libs/playerfilter-1.0.0.jar`

---

## 📄 License

MIT License — feel free to use, modify and distribute.