# Chat Control

A Minecraft mod that allows more customization of the chat experience.

## Features

- **Message Filtering**: Automatically filter messages containing specific words
- **Chat Hiding**: Option to completely hide chat when needed
- **JSON Configuration**: Easy to configure through JSON files

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft
2. Download Chat Control from [GitHub Releases](https://github.com/user93390/chat-control/releases) or [CurseForge](https://www.curseforge.com/minecraft/mc-mods/chat-control)
3. Place the downloaded JAR file in your Minecraft `mods` folder
4. Launch Minecraft with Fabric

### Dependencies

- Fabric API
- Minecraft 1.21.4

## Configuration

Chat Control can be configured in two ways:

### Using ModMenu

1. Install [ModMenu](https://www.curseforge.com/minecraft/mc-mods/modmenu)
2. Launch Minecraft
3. Open the Mods screen and find Chat Control
4. Click the "Configure" button. This will automatically open the file if your desktop supports it.

### Manual Configuration

Configuration is stored in `config/chatcontrol.json` and includes:

```json
{
  "chatWidth" : 300,
  "hideChat" : false,
  "translation" : "sp",
  "suffixText" : "",
  "chatHeight" : 150,
  "suffix" : false,
  "FilterMessages" : true,
  "filteredMessages" : [ ],
  "chatScale" : 1.0,
  "translate" : false
}
```

Please note that translation is not yet implemented, but structured ready for future use.

## License

This project is licensed under the MIT License - see the LICENSE file for details.


More settings will be added soon!