# DesigniteJava
Detects smells and computes metrics of Java code

##Compilation
We use maven to develop and build this application with the help of Eclipse IDE and libraries.
To create a runnable jar, got inside the directory where the repository is cloned and run:
```text
mvn clean install
```
If you use Eclipse: 
* open the project using Eclipse
* then right-click on the project name and select 'run as > maven install'


##Sanity check

After the previous step is done:
* Open a commandline and then 'cd' inside the project folder.
* ```text
  cd target
  ```
* ```text
  java -jar Designite-{some version}.jar {path where project lives}/DesigniteJava/tests/TestFiles/parameterTestInput.txt
  ```
* You should see a distinctive log without any errors


##Run it

In a asimilar manner with the previous section but when but instead run the jar 
with custom argument
