[![Build Status](https://travis-ci.com/tushartushar/DesigniteJava.svg?token=qqYCSPgxVmWCxpyzFpz5&branch=master)](https://travis-ci.com/tushartushar/DesigniteJava)

# DesigniteJava
DesigniteJava is a code quality assessment tool for code written in Java. It detects numerous design and implementation smells. It also computes many commonly used object-oriented metrics.

## Features
* Detects 17 design smells
	- Imperative Abstraction
	- Multifaceted Abstraction
	- Unnecessary Abstraction
	- Unutilized Abstraction
	- Deficient Encapsulation
	- Unexploited Encapsulation
	- Broken Modularization
	- Cyclic-Dependent Modularization
	- Insufficient Modularization
	- Hub-like Modularization
	- Broken Hierarchy
	- Cyclic Hierarchy
	- Deep Hierarchy
	- Missing Hierarchy
	- Multipath Hierarchy
	- Rebellious Hierarchy
	- Wide Hierarchy
* Detects 10 implementation smells
	- Abstract Function Call From Constructor
	- Complex Conditional
	- Complex Method
	- Empty catch clause
	- Long Identifier
	- Long Method
	- Long Parameter List
	- Long Statement
	- Magic Number
	- Missing default
* Computes following object-oriented metrics
	- LOC (Lines Of Code - at method and class granularity)
	- CC (Cyclomatic Complexity - Method)
	- PC (Parameter Count - Method)
	- NOF (Number of Fields - Class)
	- NOPF (Number of Public Fields - Class)
	- NOM (Number of Methods - Class)
	- NOPM (Number of Public Methods - Class)
	- WMC (Weighted Methods per Class - Class)
	- NC (Number of Children - Class)
	- DIT (Depth of Inheritance Tree - Class)
	- LCOM (Lack of Cohesion in Methods - Class)
	- FANIN (Fan-in - Class)
	- FANOUT (Fan-out - Class)
	
## Compilation
We use maven to develop and build this application with the help of Eclipse IDE and libraries.
To create a runnable jar, run the following command in the directory where the repository is cloned:
```text
mvn clean install
```
If you use Eclipse: 
* open the project using Eclipse
* then right-click on the project name and select 'run as > maven install'


## Execute the tool
After the previous step is done:
* Open a command line and then ```cd``` inside the project folder.
 ```text
  cd target
  ```
* Run the jar
```text
  java -jar Designite-<some version>.jar <path of the input parameter file>
  ```

### Format of the input parameter file
```
[Source folder]
path/to/the/project/source/code
[Output folder]
path/to/the/output
```

