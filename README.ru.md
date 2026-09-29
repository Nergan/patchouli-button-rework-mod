# Patchouli Button Rework

**[English](README.md)** · **[Русский](README.ru.md)**

Порт [PatchouliButton](https://modrinth.com/mod/patchoulibutton) от Globox_Z на **Minecraft 1.21.1** и NeoForge. Написан на Kotlin через [Kotlin for Forge](https://modrinth.com/mod/kotlin-for-forge).

Кнопка в инвентаре и экран настроек есть на английском и русском.

## Загрузки

Jar публикуются в [GitHub Releases](https://github.com/Nergan/patchouli-button-neoforge-port-mod/releases/latest) и на [Modrinth](https://modrinth.com/project/patchouli-button-neoforge-port). Пуш в `main` обновляет файлы текущего релиза.

Скачайте эти файлы и положите в папку `mods`:

| Файл | Нужен | Что это |
| --- | --- | --- |
| `patchoulibutton-1.0.0.jar` | Да | этот мод |
| `kotlinforforge-5.8.0-all.jar` | Да | [Kotlin for Forge](https://modrinth.com/mod/kotlin-for-forge) |
| `Patchouli-1.21.1-93-NEOFORGE.jar` | Да | [Patchouli](https://modrinth.com/mod/patchouli) |

Workflow релиза собирает мод и забирает два чужих jar с Modrinth. SHA-256 у каждого файла GitHub считает сам и показывает рядом с ним на странице релиза. Файл `*-sources.jar` в `mods` класть не нужно.

## Что делает

- Добавляет книгу Patchouli «Книга сборки». Внутри неё гайды других модов показаны иконками, и клик открывает выбранную книгу. Полевой справочник Critters and Crawlers и атлас More Critters тоже попадают в сборку, хотя эти моды написаны не на Patchouli.
- Крафт: четыре обычные книги квадратом 2×2. Настройка **Выдавать общую книгу сборки на старте** включена по умолчанию: каждый игрок получает книгу один раз.
- Настройка **Убирать стартовые книги-гайды** включена по умолчанию. Предметы `patchouli:guide_book`, полевой справочник Critters and Crawlers и атлас More Critters, которые появляются в первые пять секунд после входа, удаляются. Сама книга сборки остаётся. Книги, которые уже лежали в инвентаре в момент входа, не трогаются.
- Кнопка в инвентаре выживания по умолчанию выключена. **Отображать кнопку книги сборки в инвентаре** открывает ту же книгу из инвентаря.

## Требования

| Компонент | Версия |
| --- | --- |
| Minecraft | 1.21.1 |
| NeoForge | 21.1.209 (подойдёт любой 21.1.x) |
| Kotlin for Forge | 5.8.0, сборка **NeoForge** |
| Patchouli | 1.21.1-93 или новее для NeoForge |
| Java | 21 |

Мод нужен и на клиенте, и на сервере.

## Настройки

В игре: Mods → Patchouli Button Rework → Config.

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
