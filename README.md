# SimpleGraph ![JCenter](https://img.shields.io/bintray/v/austinv11/maven/SimpleGraph.svg?style=flat-square)](https://bintray.com/austinv11/maven/SimpleGraph/_latestVersion)
This is a Java implementation of the graph data structure and some 
related algorithms. This library is meant to be as simple and 
easy to use as possible. Additionally, it attempts to be speedy. Although,
this means it is not afraid to use memory. So for large data sets 
[Neo4J](https://neo4j.com/) is better suited.

## Using SimpleGraph
### Importing the project
Using maven:
```xml
<repositories>
  <repository>
    <id>jcenter</id>
    <url>http://jcenter.bintray.com/</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>com.austinv11.graphs</groupId>
    <artifactId>SimpleGraph</artifactId>
    <version>1.0.0</version>
  </dependency>
</dependencies>
```
Using gradle:
```groovy
repositories {
  jcenter()
}

dependencies {
  compile "com.austinv11.graphs:SimpleGraph:1.0.0"
}
```
### Basic Usage:
Use the implementations in the `com.austinv11.graphs.impl` package
and algorithms in `com.austinv11.graphs.alg` package.

Additionally `com.austinv11.graphs.extra.dependency` provides a basic
use case of these graphs. In this case, for dependency resolution. 
