# GNS3-Scala-lib

GNS3 API DOCUMENTATION : http://api.gns3.net/en/2.0/

GNS3 API DEMO : http://api.gns3.net/en/2.0/curl.html

This library uses another library to lay out automatically the networks generated, you can find it [here](https://github.com/rsimon/scala-force-layout)

## Description

The goal of the GNS3-Scala-lib library is to use the GNS3 API and make it scriptale. GNS3 is mostly used with it's GUI but it's a lot of clicks and buttons and cannot be used for auto testing. This library uses the GNS3 API and allows you to create networks from Scala scripts.

This project is the one and only achievement of the GAPY team.

## Installation

Download or clone the project then use `mvn -Dmaven.test.skip=true package` to produce the JAR. Then all you have to do is to add the JAR to your project. The jar is created with built-in depedencies so you don't have to worry about it.

## Usage

Here is an example on how to use the library in Scala to create a project, add a few nodes and create links between them.
You will find a Java example [here](https://github.com/PierreMear/GNS3-Scala-lib/blob/master/java-example.md)

```scala
import GAPY.GNS3_Manager
import GAPY.objectTypes.{LocalHub, LocalVpcs, SimpleLink}
import GAPY.topologies.StarNetwork

object App {

  def main(args : Array[String]){
    //first create the project manager and give it the address of your GNS3 server
    val manager = new GNS3_Manager("http://localhost:8080")
    // then create the projet and give it a name
    val project = manager.createProject("myFirstProject")

    //this example shows how to create a network with 4 local VPCs connected to a local hub
    project.addNode(LocalVpcs("PC1"))
    .addNode(LocalVpcs("PC2"))
    .addNode(LocalVpcs("PC3"))
    .addNode(LocalVpcs("PC4"))
    .addNode(LocalHub("LocalHub"))
    //This link connects the PC1 on port 0 to the LocalHub on port 0
    .addLink(SimpleLink(LocalVpcs("PC1"), LocalHub("LocalHub"), 0, 0))
    .addLink(SimpleLink(LocalVpcs("PC2"), LocalHub("LocalHub"), 0, 1))
    //This link connects the PC1 on port 0 to the LocalHub on port 02
    .addLink(SimpleLink(LocalVpcs("PC3"), LocalHub("LocalHub"), 0, 2))
    .addLink(SimpleLink(LocalVpcs("PC4"), LocalHub("LocalHub"), 0, 3))
  }
}
```
You can find more information on the nodes and links [here](https://github.com/PierreMear/GNS3-Scala-lib/blob/master/nodes.md)
We also created templates for commonly used topologies, in the previous example we used a Star Network. Here is how you can create the same network faster

```scala
import GAPY.GNS3_Manager
import GAPY.objectTypes.{LocalHub, LocalVpcs, SimpleLink}
import GAPY.topologies.StarNetwork

object App {

  def main(args : Array[String]){
    //first create the project manager and give it the address of your GNS3 server
    val manager = new GNS3_Manager("http://localhost:8080")
    // then create the projet and give it a name
    val project = manager.createProject("myFirstProject")

    //Now use the Star network topology to create the nodes and links
    project.addTopology(new StarNetwork(LocalHub("LocalHub"),List(LocalVpcs("PC1"),LocalVpcs("PC2"),LocalVpcs("PC3"),LocalVpcs("PC4"))))
  }
}
```

If you want to learn more on the different topology templates you can look [here](https://github.com/PierreMear/GNS3-Scala-lib/blob/master/topologies-scala.md).
