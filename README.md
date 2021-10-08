# ImageOverlay
Using the CLI you can search and display images atop of your screen.<br>
You can click through elements to reach everything on the desktop behind it.<br>
<br>
**Windows only.**

# Dependencies
This libary depends on [JSoup](https://github.com/jhy/jsoup) and [JNA](https://github.com/java-native-access/jna).<br>
Since this isn't a Maven project you will need to add them yourself before compiling.

You can also head towards the [Releases](https://github.com/Yukaru-san/ImageOverlay/releases) and download a precompiled one for yourself.

# Commands
``` 
position [x | reset] [y | reset]
pos      [x | reset] [y | reset]
 Places the image at the given position. You can also use "reset" to center it.

scale [width] [height]
 Scales the given Image to the given dimensions

window [show / hide]
 Toggles the overlay visible / invisible
  

help
 Display the help Message

Exit
  Exit the program

```
