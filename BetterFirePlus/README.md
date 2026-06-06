# BetterFire+ 🔥

A **client-side Fabric mod** for Minecraft 1.21.4 that removes the fire overlay from your screen — fully configurable.

---

## Features

| Feature | Details |
|---|---|
| **Remove fire overlay** | Hide the burning screen effect when conditions are met |
| **Configurable trigger** | Always / Fire Resistance / Creative Mode |
| **Fire type filter** | Both / Fire only / Lava only |
| **Custom fire color** | Tint fire blocks with an RGB color of your choice (visible when mod is OFF) |
| **Keybind** | Default **J** — change it in Options → Controls |
| **Vanilla-style GUI** | Opens with J; has cycle buttons + RGB sliders |
| **Config persistence** | Settings saved to `.minecraft/config/betterfire.json` |

---

## Requirements

- Minecraft **1.21.4**
- [Fabric Loader](https://fabricmc.net/use/) ≥ 0.16
- [Fabric API](https://modrinth.com/mod/fabric-api) ≥ 0.110.0
- Java 21

---

## Building from source

```bash
git clone <this-repo>
cd BetterFirePlus
./gradlew build
```

The compiled jar will be in `build/libs/BetterFirePlus-1.0.0.jar`.  
Copy it to your `.minecraft/mods/` folder.

---

## File structure

```
src/main/java/com/betterfire/betterfirehud/
├── BetterFirePlus.java          ← Mod entry point + keybind
├── config/
│   └── BetterFireConfig.java    ← Settings + JSON save/load
├── gui/
│   └── BetterFireScreen.java    ← Vanilla-style settings screen
└── mixin/
    ├── FireOverlayMixin.java     ← Cancels the fire overlay render
    ├── FireColorMixin.java       ← Block model render hook (stub)
    └── FireTintProviderMixin.java← Custom block color tint
```

---

## Config (`betterfire.json`)

```json
{
  "enabled": true,
  "overlayMode": "FIRE_RESISTANCE",
  "fireType": "BOTH",
  "fireColor": -10027008
}
```

`fireColor` is a packed ARGB integer (e.g. `0xFFFF6600` = orange).

---

## License

MIT
