# Patchouli Button Rework

**[English](README.md)** · **[Русский](README.ru.md)**

A **Minecraft 1.21.1** NeoForge port of [PatchouliButton](https://modrinth.com/mod/patchoulibutton) by Globox_Z. Written in Kotlin with [Kotlin for Forge](https://modrinth.com/mod/kotlin-for-forge).

The inventory button and the config screen are available in English and Russian.

## Downloads

Jars are published to [GitHub Releases](https://github.com/Nergan/patchouli-button-neoforge-port-mod/releases/latest) and [Modrinth](https://modrinth.com/project/patchouli-button-neoforge-port). A push to `main` updates the files on the current version’s release.

Download these files and put them in the `mods` folder:

| File | Required | What it is |
| --- | --- | --- |
| `patchoulibutton-1.0.0.jar` | Yes | this mod |
| `kotlinforforge-5.8.0-all.jar` | Yes | [Kotlin for Forge](https://modrinth.com/mod/kotlin-for-forge) |
| `Patchouli-1.21.1-93-NEOFORGE.jar` | Yes | [Patchouli](https://modrinth.com/mod/patchouli) |

The release workflow builds the mod and fetches the companion jars from Modrinth. GitHub shows a SHA-256 digest next to each file on the release page. Do not install `*-sources.jar`.

## What it does

- Adds one Patchouli book, the Guide Book, that lists every other loaded Patchouli book as a clickable icon inside the book itself. Critters and Crawlers' field guide and the More Critters atlas are included too, even though those mods do not use Patchouli.
- The book is crafted from four vanilla books in a 2×2 square. With **Give the compendium book on first join** on (the default), each player receives it once.
- With **Clear starter guide books** on (the default), `patchouli:guide_book` items, the Critters and Crawlers field guide, and the More Critters atlas that show up during the first five seconds after joining are removed. The Guide Book itself is kept. Books that were already in the inventory at the moment of joining stay there.
- The survival-inventory button is off by default. Turn on **Show the compendium button in the inventory** to open the same book from the inventory.

## Requirements

| Component | Version |
| --- | --- |
| Minecraft | 1.21.1 |
| NeoForge | 21.1.209 (any 21.1.x should work) |
| Kotlin for Forge | 5.8.0, **NeoForge** build |
| Patchouli | 1.21.1-93 or newer for NeoForge |
| Java | 21 |

The mod is required on both client and server.

## Configuration

In-game: Mods → Patchouli Button Rework → Config.

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
