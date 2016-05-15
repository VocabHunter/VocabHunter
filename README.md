# VocabHunter

[![Build Status](https://travis-ci.org/VocabHunter/VocabHunter.svg?branch=master)](https://travis-ci.org/VocabHunter/VocabHunter)

VocabHunter is a system to help learners of foreign languages.  You can find details of the system at [vocabhunter.github.io](http://vocabhunter.github.io/)

# Prerequisites

You will need Java 8 to build and run VocabHunter.  You can download it from the [Oracle Website](http://www.oracle.com/technetwork/java/javase/downloads/index.html).  Everything else, including Gradle, will be downloaded by the build process.

# How To Build VocabHunter

You can build the entire system with the following command:
~~~
$ ./gradlew clean build
~~~

# How To Run VocabHunter

Go to the [download](http://vocabhunter.github.io/download/) page of the website to get the latest release of VocabHunter.  Alternatively, you can run the development version from the command line as follows:
~~~
$ ./gradlew :gui:run
~~~

# How To Build An Installable Bundle

VocabHunter is distributed as an installable bundle, with everything the user needs packed into the file.  On Mac this is a `.dmg` file, on Windows an `.exe` installer and on Linux a `.deb` package.  To generate this file you first need to ensure the following:
* The `JAVA_HOME` environment variable must be set.
* On Windows you need to install [Inno Setup](http://www.jrsoftware.org/isdl.php).

You can then launch the following command to create the bundle:
~~~
$ ./gradlew clean build jfxbundle
~~~

When the build is complete, you will find the bundle file in the directory `package/build/bundles`.

# How To Run The GUI Test

By default the GUI test runs as part of the standard Gradle build, in headless mode.  If you'd like to run the GUI test in a non-headless mode so that you can see what is happening, use the following command:
~~~
$ ./gradlew :gui:test --tests io.github.vocabhunter.gui.main.GuiTest --rerun-tasks -PnoHeadless
~~~

# Using the OpenJDK

VocabHunter works well if you choose the OpenJDK 8 instead of the Oracle JDK.  If you choose the OpenJDK you may find you need to install JavaFX separately.  For example, on Ubuntu 16.04 you can install the OpenJDK 8 and JavaFX as follows:
~~~
$ sudo apt-get install openjdk-8-jdk
$ sudo apt-get install openjfx
~~~

Unfortunately the headless tests don't currently work on the OpenJDK when running the Gradle command line build (see related issue #13).   To work around this either run the build without the headless option by adding `-PnoHeadless` or skip the GUI tests using `-PskipGuiTests`.
