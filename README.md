
# ATM Simulation
The program is a simulation on how an atm machine works. Current Main features are login, withdraw, and fund transfer.

## Build
### Prerequisite 
Required you to install JDK(Java Development Kit) minimum version 8 you can check [OracleJDK](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html) or [AdoptOpenJDK](https://adoptopenjdk.net/?variant=openjdk8&jvmVariant=hotspot) for open source build suitable for your OS. Next you need Ant to do the build. Check [here](https://ant.apache.org/manual/) for guide to install Ant on your machine.

### Build The App
In project directory use this command to build the project.
```bat
ant compile jar
```
Ant will use build.xml file as build configuration and there will be a jar file generated on **target** folder after build success.

## Run 
From target folder open terminal and run this command
```bat
java -jar atm-simulation.jar
```
