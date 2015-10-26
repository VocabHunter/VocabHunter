# VocabHunter

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
