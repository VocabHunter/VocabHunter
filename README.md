# VocabHunter

[![Build Status](https://travis-ci.org/VocabHunter/VocabHunter.svg?branch=travis-osx)](https://travis-ci.org/VocabHunter/VocabHunter)

VocabHunter is a system to help learners of foreign languages.  You can find details of the system at [vocabhunter.github.io](http://vocabhunter.github.io/)

# Prerequisites

You will need Java 8 to build and run VocabHunter.  You can download it from the [Oracle Website](http://www.oracle.com/technetwork/java/javase/downloads/index.html).  Everything else, including Gradle, will be downloaded by the build process.

# How To Build VocabHunter

You can build the entire system with the following command:
~~~
$ ./gradlew clean build
~~~

During the build you will see the automated GUI tests run.  The VocabHunter GUI will briefly appear and a few buttons will be pressed on it as a part of the test.

# How To Run VocabHunter

To try the system out, you can run it directly from the command line as follows:
~~~
$ ./gradlew :gui:run
~~~

# Running The Build Without The GUI Tests

If you run the build, the GUI tests will run.  This will cause the display to be briefly taken over.  To exclude just these tests but leave the other tests running as normal, start the build as follows:
~~~
$ ./gradlew clean build -PskipGuiTests
~~~

Alternatively, on Linux you can use the [XVFB](https://en.wikipedia.org/wiki/Xvfb) frame buffer to run the GUI tests.

# How To Build An Installable Bundle

VocabHunter is distributed as an installable bundle, with everything the user needs packed into the file.  On Mac this is a `.dmg` file, on Windows an `.exe` installer and on Linux a `.deb` package.  To generate this file you first need to ensure the following:
* The `JAVA_HOME` environment variable must be set.
* On Windows you need to install [Inno Setup](http://www.jrsoftware.org/isdl.php).

You can then launch the following command to create the bundle:
~~~
$ ./gradlew clean build jfxbundle
~~~

When the build is complete, you will find the bundle file in the directory `package/build/bundles`.
