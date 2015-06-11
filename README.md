ESPloder
========

###Changes by h0uz3
There was so much wrong with the original implementation (dead code and a ton of generated lines of code in one single java class), I am currently in the process or refactoring.
This is going to be a lot different than the original, at least concerning the code base...

Integrated Development Environment (IDE) for ESP8266 developers

###Package Description
The essential multiplatforms tools for any ESP8266 developer from luatool author’s, including a LUA for NodeMCU and MicroPython. Also, all AT commands are supported.
Required [JAVA](http://java.com/download) (Standard Edition - SE ver 7 and above) installed.

###Supported platforms
Windows(x86, x86-64)
Linux(x86, x86-64, ARM soft & hard float)
Solaris(x86, x86-64)
Mac OS X(x86, x86-64, PPC, PPC64)

###Detailed features list
Syntax highlighting LUA and Python code
Code editor color themes: default, dark, Eclipse, IDEA, Visual Studio
Undo/Redo editors features
Code Autocomplete (Ctrl+Space)
Smart send data to ESP8266 (without dumb send with fixed line delay), check correct answer from ESP8266 after every lines.
Code snippets
Detailed logging
and more, more more…

##How to build
###Needed tools
Java 1.7 or newer
Ant
###Checking out and building the project
- Check out using git.
- Enter project root.
- Type "ant" and hit Enter.
- The executable jar file will be in the "dist" directory. You can start it from console using java -jar ESPloder.jar.