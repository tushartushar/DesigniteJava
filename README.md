# DesigniteJava
Detects smells and computes metrics of Java code

##Compilation
We use Eclipse to develop and build this application. You need to add libraries in the "lib" folder to Java Build Path -> Libraries.

Create 'Designite.jar' by using Export -> runnable jar (choose 'package libraries into generated jar' to create a single jar file).

##How to execute
We need to provide an input batch file containing options and settings. Here is a sample of the input batch file.

```text
[Source folder]
/Users/Tushar/Documents/Workspace/DesigniteJava/src/

[Output folder]
/Users/Tushar/Documents/Workspace/DesigniteJava/tests/temp/
```

You need to execute 'Designite.jar' located in 'bin' folder something like this

```
java -jar Designite.jar <path to input batch file>
```