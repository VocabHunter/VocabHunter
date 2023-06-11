# VocabHunter

[![Build Status](https://img.shields.io/travis/VocabHunter/VocabHunter/master.svg)](https://travis-ci.org/VocabHunter/VocabHunter)
[![Code Coverage](https://img.shields.io/codecov/c/github/VocabHunter/VocabHunter.svg)](https://codecov.io/gh/VocabHunter/VocabHunter)
[![SonarCloud](/assets/SonarCloud.svg)](https://sonarcloud.io/dashboard?id=io.github.vocabhunter%3Avocabhunter)
[![Latest Release](https://img.shields.io/github/release/VocabHunter/VocabHunter.svg)](https://github.com/VocabHunter/VocabHunter/releases/latest)
[![Twitter Follow](https://img.shields.io/twitter/follow/vocabhunterapp.svg?style=social&label=Follow)](https://twitter.com/vocabhunterapp)

[![VocabHunter](/assets/VocabHunter.png)](https://vocabhunter.github.io/)

VocabHunter is a system to help learners of foreign languages.  The best place to go for information about VocabHunter is the project website: [vocabhunter.github.io](https://vocabhunter.github.io/).

To get all the latest news about VocabHunter including announcements of new releases, follow [@vocabhunterapp](https://twitter.com/vocabhunterapp) on Twitter.

[![VocabHunter](/assets/VocabHunter-in-use.png)](https://vocabhunter.github.io/)

# Prerequisites

You will need Java 17 (or above) to build and run VocabHunter.  The version provided by [Adoptium](https://adoptium.net) is an excellent choice for this.  Everything else, including Gradle, will be downloaded by the build process.

# How To Run VocabHunter

Go to the [download](https://vocabhunter.github.io/download/) page of the website to get the latest release of VocabHunter.  Alternatively, you can run the development version from the command line as follows:
~~~
./gradlew :gui:run
~~~

# How to Use VocabHunter

Do you want to use VocabHunter to help you to learn a foreign language?  You'll find everything you need to know to get you started in the guide [How to Use VocabHunter](https://vocabhunter.github.io/help/).

# How To Build VocabHunter

You can build the entire system with the following command:
~~~
./gradlew clean build
~~~

# How To Run The GUI Test

By default the GUI test runs as part of the standard Gradle build, in headless mode.  If you'd like to run the GUI test in a non-headless mode so that you can see what is happening, use the following command:
~~~
./gradlew :gui:test --tests io.github.vocabhunter.gui.main.GuiTest --rerun-tasks -PnoHeadless
~~~

# How to Build The Installable Bundle

[![Installable Java Apps with jpackage](/assets/jpackage-installable-java-apps.png)][Installable Java Apps with jpackage]

In the article [Installable Java Apps with jpackage] you can read about how installable bundles for Mac, Windows and Linux are created and how you can do this in your own project.  These self-contained installers allow users to setup VocabHunter on their computer without the need to first install Java.

You can also find full, step-by-step instructions for creating the VocabHunter installable bundle in [PACKAGING.md](package/PACKAGING.md).

# Technical Articles

If you'd like to know more about how VocabHunter works and the technologies used to build it, take a look at the following articles that explore various aspects of the implementation:

* [Read (Almost) Any Document in Java] (VocabHunter Blog) - VocabHunter uses Apache Tika to read documents in a wide variety of formats ranging from Microsoft Word through to PDF.  This article explains how it is done.
* [How JavaFX was used to build a desktop application] (King Tech Blog) - A detailed look at several important features of JavaFX using VocabHunter as an example.
* [Migrating to JUnit 5] (VocabHunter Blog) - How the VocabHunter project was updated to use JUnit 5 for testing.  This article explains the changes that were made, the problems that were encountered and how they were solved.
* [Dependency Injection in JavaFX] (VocabHunter Blog) - How Gluon Ignite and Google Guice are used for the Dependency Injection in VocabHunter.
* [User Interface Testing with TestFX] (VocabHunter Blog) - A guide to automating user interface tests using TestFX.  VocabHunter includes a complete automated GUI test suite and here you can learn how it works.
* [Building a JavaFX Search Bar] (VocabHunter Blog) - How the user interface for the VocabHunter search bar works with details of the use of ControlsFX in giving the bar a distinctive style.
* [VocabHunter – A tool for learners of foreign languages] (King Tech Blog) - An introduction to some of the technologies being used in VocabHunter.

[![VocabHunter](/assets/VocabHunter-Technical-Articles.png)](https://vocabhunter.github.io/blog/)

[Dependency Injection in JavaFX]:https://vocabhunter.github.io/2016/11/13/JavaFX-Dependency-Injection.html
[User Interface Testing with TestFX]:https://vocabhunter.github.io/2016/07/27/TestFX.html
[Building a JavaFX Search Bar]:https://vocabhunter.github.io/2017/01/15/Search-Bar.html
[Read (Almost) Any Document in Java]:https://vocabhunter.github.io/2017/04/30/Read-Any-Document-Format.html
[Migrating to JUnit 5]:https://vocabhunter.github.io/2017/10/17/migrating-to-junit-5.html
[Installable Java Apps with jpackage]:https://vocabhunter.github.io/2021/07/10/installable-java-apps-with-jpackage.html

[VocabHunter – A tool for learners of foreign languages]:https://medium.com/techking/vocabhunter-a-tool-for-learners-of-foreign-languages-55c467a6250c
[How JavaFX was used to build a desktop application]:https://medium.com/techking/how-javafx-was-used-to-build-a-desktop-application-7d4c680d8dc
