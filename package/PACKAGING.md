# VocabHunter Packaging

If you'd like to build an installable bundle of VocabHunter for Mac, Linux or Windows, just like those you can find on the [VocabHunter download page](https://vocabhunter.github.io/download/) then follow these instructions.  These installers are self-contained files that have everything the user will need to run VocabHunter without the need to, for example, install Java separately.

## Preparing your Build Environment

### JDK 17

You will need to install JDK 17 to build the installable bundle of VocabHunter.  The version provided by [Adoptium](https://adoptium.net) is an excellent choice for this.  The build process uses the [jpackage](https://docs.oracle.com/en/java/javase/17/jpackage/) tool, included as a part of the JDK.

### Windows: WIX Toolset

The Windows build procedure requires [WIX Toolset](https://wixtoolset.org/).  Download and install this software on your PC.

### Ubuntu Linux: FakeRoot and BinUtils

In addition to the JDK, you will need "FakeRoot" and "BinUtils" installed on your system to build the bundle.  Both of these can be installed using "apt".

## Creating the Installable Bundle

### Creating the Bundle

You can create the installable bundle using the following command on Linux and Mac:
~~~
./gradlew clean createBundle
~~~

On Windows, the command is:
~~~
gradlew clean createBundle
~~~

If you're building using Windows, you will need to make sure that your path does not have a space in it anywhere.

By default, the Mac build will sign the created bundle using your Apple Developer ID.  If you want to skip this, pass `-Punsigned` to the Gradle command.

You will find the created installable bundle in the directory `package/build/bundle`.

## Installable Java Apps with jpackage

[![Installable Java Apps with jpackage](/assets/jpackage-installable-java-apps.png)][Installable Java Apps with jpackage]

For more general information about the approach taken to building the installable bundles for Mac, Linux and Windows and how you could apply this in your own project, see the article [Installable Java Apps with jpackage].

## Return to VocabHunter README

[Click here](../README.md) to return to the main VocabHunter README file for more information about the project.

[Installable Java Apps with jpackage]:https://vocabhunter.github.io/2021/07/10/installable-java-apps-with-jpackage.html
