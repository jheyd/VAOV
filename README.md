# VAOV - Verified Anonymous Online Voting

This is an implementation of the VAOV voting procedure described [here.](https://wiki.piratenpartei.de/VAOV)

Mostly abandoned as of 07/2015

## How To Build ##

A maven installation with $MAVEN_HOME/bin in the PATH is required for this build.

Run `mvn clean package` in the repository root. This creates a `vaov.client-0.0.1-SNAPSHOT-jar-with-dependencies.jar` file in `vaov.client/target` that can be run with `java -jar vaov.client-0.0.1-SNAPSHOT-jar-with-dependencies.jar`.