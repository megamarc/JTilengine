![Tilengine logo](JTilengine.png)
# JTilengine
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
## About JTilengine
JTilengine is the Java binding for Tilengine. It is a direct 1:1 API translation of the original C library, so it is used and works exactly as its C counterpart.<br>
The single difference with the C API, is  that the original C API prepends every function calll with `TLN_` as a means of namespace, whereas in Java all the functions are housed as methods inside a single class called `Tilengine` without the `TLN_` prefix.
 > **Note:** this binding is not updated to the latest Tillengine release. It's stuck at release 1.11, but it's forwards compatible with current engine release.

## Contents

File(s)               | Description
----------------------|----------------------------------------------------------------
`Tilengine.java`      | Java source with the API binding, required by any Java program
`TilengineJNI.dll`    | prebuilt required JNI binding binary for 32-bit windows
`Tilengine.dll`       | prebuilt required Tilengine binary for 32-bit windows
`SDL2.dll`            | prebuilt required SDL2 binary for 32-bit windows
`TestWindow.java`     | basic background test with raster effects, uses the built-in window
`TestPanel.java`      | basic background test with raster effects, uses the AWT JPanel
`/assets`             | graphic data used by the samples
`/jni`                | source of the JNI native library, ready to be built

## Prerequisites
1. JDK (Java Development Kit) must be properly installed and the `JAVA_HOME` environment variable set accordingly. Please visit https://www.oracle.com/index.html to know more about the JDK.
2. Tilengine native shared library must be installed separately. Please refer to https://github.com/megamarc/Tilengine about how to do it.<br>
For convenience, Windows 32-bit versions of Tilengine.dll and SDL2.dll are bundled.<br>

## Documentation
Work in progress of the C API documentation can be found here:<br>
http://www.tilengine.org/doc/

## Installation
No install step is required. Just make sure that the Tilengine library (*Tilengine.dll*, *Tilengine.java* and TilengineJNI.dll) are accessible from within your own project.

## Building the JNI bridge
Unlike other languages, Java requires an intermediate shared library to interface to any external native library. This package already ships with a prebuilt 32-bit windows. However, to use it on other platform or upgrade it with more up to date features, it must be built from source. The source is just a single .c source file. Any C compiler can be used, for example gcc or the excellent [tiny C compiler (tcc)](https://bellard.org/tcc/).<br>
To build the source, open a console window inside the `jni` folder and type the following command depending on your target OS:

### Windows
```
gcc -shared -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\win32" ..\Tilengine.dll TilengineJNI.c -o ..\TilengineJNI.dll
```

### Linux (desktop, ARM raspberry...)
```
gcc -shared -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\linux" -lTilengine TilengineJNI.c -o ..\libTilengineJNI.so
```

### Mac OSX
```
gcc -shared -I"%JAVA_HOME%\include" -I"%JAVA_HOME%\include\darwin" Tilengine.dylib TilengineJNI.c -o ..\libTilengineJNI.jnilib
```

## Basic program
The following program does these actions:
1. Create an instance of the main `Tilengine` class and name it `tln`.
2. Initialize the engine with a resolution of 400x240, one layer, no sprites and 20 animation slots
3. Set the loading path to the assets folder
4. Load a *tilemap*, the asset that contains background layer data
5. Attach the loaded tilemap to the allocated background layer
6. Create a display window with default parameters: windowed, auto scale and CRT effect enabled
7. Run the window loop, updating the display at each iteration until the window is closed

Source code:
```java
public class Test {
	public static void main(String[] args) {
		Tilengine tln = new Tilengine ();
		tln.Init (400, 240, 1, 0, 20);
		tln.SetLoadPath ("assets");

		int foreground = tln.LoadTilemap ("sonic_md_fg1.tmx", "Layer 1");
		tln.SetLayer (0, 0, foreground);

		int frame = 0;
		tln.CreateWindow (null, 0);
		while (tln.ProcessWindow()){
			tln.DrawFrame (frame);
			frame += 1;
		}
		tln.Deinit ();
	}
}
```

Resulting output:

![Test](test.png)

## Building and running the samples
Two similar samples are provided to demonstrate the binding. Open a terminal console inside the main directory.

### `TestWindow.java`
This sample uses the built-in window (based on SDL2):
```
javac TestWindow.java
java TestWwindow
```

### `TestPanel.java`
This sample renders to an AWT JPanel, entirely discarding the built-in window:
```
javac TestPanel.java
java TestPanel
```

## License
JTilengine is released under the permissive MIT license
