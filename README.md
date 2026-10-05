# Patchouli Button Rework

**[English](README.md)** · **[Русский](README.ru.md)**

A **Minecraft 1.21.1** port of [PatchouliButton](https://modrinth.com/mod/patchoulibutton) by Globox_Z, for NeoForge and Fabric. NeoForge uses [Kotlin for Forge](https://modrinth.com/mod/kotlin-for-forge). Fabric uses [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin). Install one loader, not both.

The inventory button and the config screen are available in English and Russian.

## Downloads

Jars are published to [GitHub Releases](https://github.com/Nergan/patchouli-button-neoforge-port-mod/releases/latest) and [Modrinth](https://modrinth.com/project/patchouli-button-neoforge-port). A push to `main` updates the files on the current version’s release. Modrinth receives only this mod’s jar.

Download one set and put those files in the `mods` folder.

### NeoForge

| File | Required | What it is |
| --- | --- | --- |
| `patchoulibutton-neoforge-1.21.1-1.0.0.jar` | Yes | this mod |
| `kotlinforforge-5.8.0-all.jar` | Yes | [Kotlin for Forge](https://modrinth.com/mod/kotlin-for-forge) |
| `Patchouli-1.21.1-93-NEOFORGE.jar` | Yes | [Patchouli](https://modrinth.com/mod/patchouli) |

### Fabric

| File | Required | What it is |
| --- | --- | --- |
| `patchoulibutton-fabric-1.21.1-1.0.0.jar` | Yes | this mod |
| `fabric-api-0.116.17+1.21.1.jar` | Yes | [Fabric API](https://modrinth.com/mod/fabric-api). A message that says "fabric 0.100.3" means this jar, not Fabric Loader |
| `fabric-language-kotlin-1.13.2+kotlin.2.1.20.jar` | Yes | [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin) |
| `Patchouli-1.21.1-93-FABRIC.jar` | Yes | [Patchouli](https://modrinth.com/mod/patchouli). It already contains Fiber. Do not install Fiber again |
| `cloth-config-15.0.140-fabric.jar` | Yes | [Cloth Config](https://modrinth.com/mod/cloth-config), for the config screen |
| `modmenu-11.0.4.jar` | No | [Mod Menu](https://modrinth.com/mod/modmenu), opens the config screen |
| `placeholder-api-2.4.2+1.21.jar` | No | [Text Placeholder API](https://modrinth.com/mod/placeholder-api), required by Mod Menu |

The release workflow builds both jars and fetches the companion jars from Modrinth. GitHub shows a SHA-256 digest next to each file on the release page. Do not install `*-sources.jar`.

## What it does

- Adds one Patchouli book, the Guide Book, that lists every other loaded Patchouli book as a clickable icon inside the book itself. Critters and Crawlers' field guide and the More Critters atlas are included too, even though those mods do not use Patchouli.
- The book is crafted from four vanilla books in a 2×2 square. With **Give the compendium book on first join** on (the default), each player receives it once.
- With **Clear starter guide books** on (the default), `patchouli:guide_book` items, the Critters and Crawlers field guide, and the More Critters atlas that show up during the first five seconds after joining are removed. The Guide Book itself is kept. Books that were already in the inventory at the moment of joining stay there.
- The survival-inventory button is off by default. Turn on **Show the compendium button in the inventory** to open the same book from the inventory.

## Requirements

| Component | NeoForge | Fabric |
| --- | --- | --- |
| Minecraft | 1.21.1 | 1.21.1 |
| Loader | NeoForge 21.1.209 | Fabric Loader 0.16.14 or newer |
| Kotlin | Kotlin for Forge 5.8.0 | Fabric Language Kotlin `1.13.2+kotlin.2.1.20` |
| Patchouli | `1.21.1-93-neoforge` or newer | `1.21.1-93-fabric` or newer |
| Other | — | Fabric API `0.116.17+1.21.1`, Cloth Config 15.0.140 |
| Java | 21 | 21 |

The mod is required on both client and server.

## Configuration

On NeoForge: Mods → Patchouli Button Rework → Config. On Fabric the same screen opens from Mod Menu.

| File | What it controls |
| --- | --- |
| `config/patchoulibutton-client.toml` | whether the inventory button is shown, and where it sits |
| `saves/<world>/serverconfig/patchoulibutton-server.toml` | whether the compendium is given on first join, and whether starter books are cleared |

On a dedicated server the server file is `world/serverconfig/patchoulibutton-server.toml`. `clear_starting_books` is a `SERVER` option: the server owns it and syncs it to clients.

| Option | Default | Meaning |
| --- | --- | --- |
| `give_compendium` | `true` | give the Guide Book the first time a player joins |
| `clear_starting_books` | `true` | remove guide books gained in the first 5 seconds after joining |
| `show_book_button` | `false` | show the inventory button that opens the Guide Book |
| `button_x` | `127` | button position inside the survival inventory |
| `button_y` | `61` | button position inside the survival inventory |

## License

The code is [MPL-2.0](LICENSE). PatchouliButton by Globox_Z remains MIT.
