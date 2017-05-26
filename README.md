# VocabHunter

[![Build Status](https://img.shields.io/travis/VocabHunter/VocabHunter.svg)](https://travis-ci.org/VocabHunter/VocabHunter)
[![Code Coverage](https://img.shields.io/codecov/c/github/VocabHunter/VocabHunter.svg)](https://codecov.io/gh/VocabHunter/VocabHunter)
[![Latest Release](https://img.shields.io/github/release/VocabHunter/VocabHunter.svg)](https://github.com/VocabHunter/VocabHunter/releases/latest)
[![Twitter Follow](https://img.shields.io/twitter/follow/vocabhunterapp.svg?style=social&label=Follow)](https://twitter.com/vocabhunterapp)

[![VocabHunter](/assets/VocabHunter.png)](https://vocabhunter.github.io/)

VocabHunter is a system to help learners of foreign languages.  The best place to go for information about VocabHunter is the project website: [vocabhunter.github.io](https://vocabhunter.github.io/).

To get all the latest news about VocabHunter including announcements of new releases, follow [@vocabhunterapp](https://twitter.com/vocabhunterapp) on Twitter.

[![VocabHunter](/assets/VocabHunter-in-use.png)](https://vocabhunter.github.io/)

# Prerequisites

You will need Java 8 to build and run VocabHunter.  You can download it from the [Oracle Website](http://www.oracle.com/technetwork/java/javase/downloads/index.html).  Everything else, including Gradle, will be downloaded by the build process.

# How To Run VocabHunter

Go to the [download](https://vocabhunter.github.io/download/) page of the website to get the latest release of VocabHunter.  Alternatively, you can run the development version from the command line as follows:
~~~
$ ./gradlew :gui:run
~~~

# How to Use VocabHunter

Do you want to use VocabHunter to help you to learn a foreign language?  You'll find everything you need to know to get you started in the guide [How to Use VocabHunter](https://vocabhunter.github.io/help/).

# How To Build VocabHunter

You can build the entire system with the following command:
~~~
$ ./gradlew clean build
~~~

# How To Build An Installable Bundle

VocabHunter is distributed as an installable bundle, with everything the user needs packed into the file.  On Mac this is a `.dmg` file, on Windows an `.exe` installer and on Linux a `.deb` package.  On Windows you need to install [Inno Setup](http://www.jrsoftware.org/isdl.php).

You can launch the following command to create the bundle:
~~~
$ ./gradlew clean build jfxNative checkBundle
~~~

When the build is complete, you will find the bundle file in the directory `package/build/jfx/native`.

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

Unfortunately the headless tests don't currently work on the OpenJDK when running the Gradle command line build (see [related issue #13](../../issues/13)).   To work around this either run the build without the headless option by adding `-PnoHeadless` or skip the GUI tests using `-PskipGuiTests`.

# Technical Articles

If you'd like to know more about how VocabHunter works and the technologies used to build it, take a look at the following articles that explore various aspects of the implementation:

* [VocabHunter – A tool for learners of foreign languages][KingTechBlog1] (King Tech Blog) - An introduction to some of the technologies being used in VocabHunter.
* [Building a JavaFX Search Bar][SearchBar] (VocabHunter Blog) - How the user interface for the VocabHunter search bar works with details of the use of ControlsFX and FontAwesomeFX in giving the bar a distinctive style.
* [How JavaFX was used to build a desktop application][KingTechBlog2] (King Tech Blog) - A detailed look at several important features of JavaFX using VocabHunter as an example.
* [Dependency Injection in JavaFX][DependencyInjection] (VocabHunter Blog) - How to Gluon Ignite and Google Guice are used for the  Dependency Injection in VocabHunter.
* [User Interface Testing with TestFX][TestFXBlog] (VocabHunter Blog) - A guide to automating user interface tests using TestFX.  VocabHunter includes a complete automated GUI test suite and here you can learn how it works.
* [Read (Almost) Any Document in Java] (VocabHunter Blog) - VocabHunter uses Apache Tika to read documents in a wide variety of formats ranging from Microsoft Word through to PDF.  This article explains how it is done.

[![VocabHunter](/assets/VocabHunter-Technical-Articles.png)](https://vocabhunter.github.io/blog/)

[TestFXBlog]:https://vocabhunter.github.io/2016/07/27/TestFX.html
[DependencyInjection]:https://vocabhunter.github.io/2016/11/13/JavaFX-Dependency-Injection.html
[SearchBar]:https://vocabhunter.github.io/2017/01/15/Search-Bar.html
[Read (Almost) Any Document in Java]:https://vocabhunter.github.io/2017/04/30/Read-Any-Document-Format.html

[KingTechBlog1]:https://techblog.king.com/vocabhunter-a-tool-for-learners-of-foreign-languages/
[KingTechBlog2]:https://techblog.king.com/javafx-used-build-desktop-application/
