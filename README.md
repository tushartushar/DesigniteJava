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
	
## Where can I get the latest release?
You may download the executable jar from the [Designite](http://www.designite-tools.com/DesigniteJava) website.

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
* Open terminal/command line console and run the jar
```text
  java -jar Designite.jar -i <path of the input source folder> -o <path of the output folder>
  ```
**Note:** Make sure that the output folder is empty. Tool deletes all the existing files in the output folder.

## Notes
The implemented LCOM is a custom implementation to avoid the problems of existing LCOM alternatives. Traditional, LCOM value may range only between 0 and 1. However, there are many cases, when computing LCOM is not feasible and traditional implementations give value 0 giving us a false sense of satisfaction. So, when you find -1 as LCOM value for a class, this means we do not have enough information or LCOM is not applicable (for instance, for an interface). More details can be found here (though, it is an old post): http://www.tusharma.in/technical/revisiting-lcom/

## Contribute
Feel free to clone/fork/contribute to the DesigniteJava open-source project.

## Report Bugs
Open an issue if you encounter a bug in the tool.

## Credits
Apart from [me](http://www.tusharma.in), following people generously contributed efforts to this project.
- Antonis Gkortzis
- Theodore Stassinopoulos
- Alexandra Chaniotakis
