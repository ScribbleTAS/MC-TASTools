## Overview
Useful utilities for Minecraft TASsing  
  
Classical Minecraft TASing uses the Mod [TickrateChanger](https://minecraft.curseforge.com/projects/tickratechanger) to slow the game down... Now we record the speedrun in slow motion and speed it up in an editing program,  so the game goes to normal speed again.  
Emulator TASing however uses a playback file to play the game frame perfect, so there is no human input required...  
  
Emulator TASing also uses *Savestates* so you can replay a section over and over again to get it perfect.  
In an Emulator this happens automatically and it takes up to 1 second to reload these savestates...  
  
Minecraft TASing however, doesn't have this luxury so we have to manually quit out of the game and manage our "saves" folder.

So this mod adresses this issue and adds a savestate function and a "pause" function (even for multiplayer).  

**!!!THIS MOD IS NOT A TOOL FOR PLAYING BACK YOUR INPUTS!!!**

## Features
* Duping:
Minecraft has a duplication glitch which is heavily used by speedrunners to duplicate items using save and quit. Since forge is running an integrated server, this doesn't work (or just very rarely) in forge, which is why this makes it more consistent. More details on the [Wiki](https://github.com/ScribbleLP/MC-TASTools/wiki/Duping)  

* Keystrokes:
Display your keypresses! [Wiki](https://github.com/ScribbleLP/MC-TASTools/wiki/Keystrokes)
  
* Freezing and velocity:
TASing makes heavy use of S+Q. But as you might know, when you reload the world, all of your momentum will be set to zero... Which is a bit annoying when you want to simulate a motion during a TAS and don't want to stop all the time... And to better react on where you going, the game will freeze you in place once you start the world. More details on the [Wiki](https://github.com/ScribbleLP/MC-TASTools/wiki/Freeze)

