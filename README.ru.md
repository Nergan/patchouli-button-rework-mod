# Patchouli Button Rework

**[English](README.md)** · **[Русский](README.ru.md)**

Порт [PatchouliButton](https://modrinth.com/mod/patchoulibutton) от Globox_Z на **Minecraft 1.21.1**, NeoForge и Fabric. NeoForge использует [Kotlin for Forge](https://modrinth.com/mod/kotlin-for-forge). Fabric использует [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin). Ставится один загрузчик, не оба сразу.

Кнопка в инвентаре и экран настроек есть на английском и русском.

## Загрузки

Jar публикуются в [GitHub Releases](https://github.com/Nergan/patchouli-button-neoforge-port-mod/releases/latest) и на [Modrinth](https://modrinth.com/project/patchouli-button-neoforge-port). Пуш в `main` обновляет файлы текущего релиза. На Modrinth попадает только jar этого мода.

Скачайте один набор и положите эти файлы в папку `mods`.

### NeoForge

| Файл | Нужен | Что это |
| --- | --- | --- |
| `patchoulibutton-neoforge-1.21.1-1.0.0.jar` | Да | этот мод |
| `kotlinforforge-5.8.0-all.jar` | Да | [Kotlin for Forge](https://modrinth.com/mod/kotlin-for-forge) |
| `Patchouli-1.21.1-93-NEOFORGE.jar` | Да | [Patchouli](https://modrinth.com/mod/patchouli) |

### Fabric

| Файл | Нужен | Что это |
| --- | --- | --- |
| `patchoulibutton-fabric-1.21.1-1.0.0.jar` | Да | этот мод |
| `fabric-api-0.116.17+1.21.1.jar` | Да | [Fabric API](https://modrinth.com/mod/fabric-api). Строка «нужен fabric 0.100.3» означает этот jar, не Fabric Loader |
| `fabric-language-kotlin-1.13.2+kotlin.2.1.20.jar` | Да | [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin) |
| `Patchouli-1.21.1-93-FABRIC.jar` | Да | [Patchouli](https://modrinth.com/mod/patchouli). Fiber уже внутри этого jar, отдельно его ставить не нужно |
| `cloth-config-15.0.140-fabric.jar` | Да | [Cloth Config](https://modrinth.com/mod/cloth-config), для экрана настроек |
| `modmenu-11.0.4.jar` | Нет | [Mod Menu](https://modrinth.com/mod/modmenu), открывает экран настроек |
| `placeholder-api-2.4.2+1.21.jar` | Нет | [Text Placeholder API](https://modrinth.com/mod/placeholder-api), нужен Mod Menu |

Workflow релиза собирает оба jar и забирает чужие jar с Modrinth. SHA-256 у каждого файла GitHub считает сам и показывает рядом с ним на странице релиза. Файл `*-sources.jar` в `mods` класть не нужно.

## Что делает

- Добавляет книгу Patchouli «Книга сборки». Внутри неё гайды других модов показаны иконками, и клик открывает выбранную книгу. Полевой справочник Critters and Crawlers и атлас More Critters тоже попадают в сборку, хотя эти моды написаны не на Patchouli.
- Крафт: четыре обычные книги квадратом 2×2. Настройка **Выдавать общую книгу сборки на старте** включена по умолчанию: каждый игрок получает книгу один раз.
- Настройка **Убирать стартовые книги-гайды** включена по умолчанию. Предметы `patchouli:guide_book`, полевой справочник Critters and Crawlers и атлас More Critters, которые появляются в первые пять секунд после входа, удаляются. Сама книга сборки остаётся. Книги, которые уже лежали в инвентаре в момент входа, не трогаются.
- Кнопка в инвентаре выживания по умолчанию выключена. **Отображать кнопку книги сборки в инвентаре** открывает ту же книгу из инвентаря.

## Требования

| Компонент | NeoForge | Fabric |
| --- | --- | --- |
| Minecraft | 1.21.1 | 1.21.1 |
| Загрузчик | NeoForge 21.1.209 | Fabric Loader 0.16.14 или новее |
| Kotlin | Kotlin for Forge 5.8.0 | Fabric Language Kotlin `1.13.2+kotlin.2.1.20` |
| Patchouli | `1.21.1-93-neoforge` или новее | `1.21.1-93-fabric` или новее |
| Ещё | — | Fabric API `0.116.17+1.21.1`, Cloth Config 15.0.140 |
| Java | 21 | 21 |

Мод нужен и на клиенте, и на сервере.

## Настройки

На NeoForge: Mods → Patchouli Button Rework → Config. На Fabric тот же экран открывается из Mod Menu.

| Файл | Что задаёт |
| --- | --- |
| `config/patchoulibutton-client.toml` | показывать ли кнопку в инвентаре и где она стоит |
| `saves/<мир>/serverconfig/patchoulibutton-server.toml` | выдавать ли книгу сборки при первом входе и убирать ли стартовые гайды |

На выделенном сервере файл лежит в `world/serverconfig/patchoulibutton-server.toml`. `clear_starting_books` — настройка типа `SERVER`: её задаёт сервер и рассылает клиентам.

| Параметр | По умолчанию | Смысл |
| --- | --- | --- |
| `give_compendium` | `true` | выдать книгу сборки при первом входе игрока |
| `clear_starting_books` | `true` | убирать книги-гайды, появившиеся в первые 5 секунд после входа |
| `show_book_button` | `false` | показывать кнопку в инвентаре, которая открывает книгу сборки |
| `button_x` | `127` | положение кнопки в инвентаре выживания |
| `button_y` | `61` | положение кнопки в инвентаре выживания |

## Лицензия

Код — [MPL-2.0](LICENSE). PatchouliButton от Globox_Z остаётся под MIT.
