# VocabHunter Packaging

If you'd like to build an installable bundle of VocabHunter for Mac, Linux or Windows, just like those you can find on the [VocabHunter download page](https://vocabhunter.github.io/download/) then follow these instructions.  These installers are self-contained files that have everything the user will need to run VocabHunter without the need to, for example, install Java separately.

## Preparing your Build Environment

### JDK 16

You will need to install JDK 16 to build the installable bundle of VocabHunter.  The version provided by [AdoptOpenJDK](https://adoptopenjdk.net/) is an excellent choice for this.  The build process uses the [jpackage](https://docs.oracle.com/en/java/javase/16/jpackage/) tool that as of JDK 16 is marked as "production-ready" and is included as a part of the JDK.

### Windows: WIX Toolset

The Windows build procedure requires [WIX Toolset](https://wixtoolset.org/).  Download and install this software on your PC.

### Ubuntu Linux: FakeRoot and BinUtils

In addition to the JDK, you will need "FakeRoot" and "BinUtils" installed on your system to build the bundle.  Both of these can be installed using "apt".

## Creating the Installable Bundle

### Creating the Bundle

You can create the installable bundle using the following command on Linux and Mac:
~~~
$ ./gradlew clean createBundle
~~~

On Windows, the command is:
~~~
gradlew clean createBundle
~~~

If you're building using Windows, you will need to make sure that your path does not have a space in it anywhere.

By default, the Mac build will sign the created bundle using your Apple Developer ID.  If you want to skip this, pass `-Punsigned` to the Gradle command.

You will find the created installable bundle in the directory `package/build/bundle`.

## Using the Java Packager with JDK 11

Prior to the release of JDK 16, the VocabHunter installable bundle used to be created using JDK 11.  You can find more information about this in the article [Using the Java Packager with JDK 11].

[![Using the Java Packager with JDK 11](/assets/Using-The-Java-Packager-With-JDK-11.png)][Using the Java Packager with JDK 11]

## Return to VocabHunter README

[Click here](../README.md) to return to the main VocabHunter README file for more information about the project.

[Using the Java Packager with JDK 11]:https://medium.com/@adam_carroll/java-packager-with-jdk11-31b3d620f4a8
