# GNS3-Scala-lib for Maintainers

In this document, we will talk about how this lib was created and the choice we've made. This is for future developpers who want to implements new functionalities.

## Description

The file's tree as it is now, is organized like this :
```
src/main/scala/gapy ---\
                       +- forcelayout -\
                       |               +- quadtree -\
                       |               |            +- Body.scala
                       |               |            +- Quad.scala
                       |               |            +- QuadTree.scala
                       |               |
                       |               +- Bounds.scala
                       |               +- Edge.scala
                       |               +- Node.scala
                       |               +- SpringGraph.scala
                       |               +- Vector2D.scala
                       |
                       +- objectTypes -\
                       |               +- Appliance.scala
                       |               +- Link.scala
                       |               +- LinksClasses.scala
                       |               +- LocalNode.scala
                       |               +- LocalNodesClasses.scala
                       |               +- Node.scala
                       |               +- NodesClasses.scala
                       |
                       +- topologies --\
                       |               +- FullyConnectedNetwork.scala
                       |               +- LinearNetwork.scala
                       |               +- RingNetwork.scala
                       |               +- StarLinearNetwork.scala
                       |               +- StarNetwork.scala
                       |               +- StarRingNetwork.scala
                       |               +- Topology.scala
                       |
                       +- GNS3_Exceptions.scala
                       +- GNS3_Manager.scala
                       +- JSON_Exceptions.scala
                       +- JSONApi.scala
                       +- ProjectManager.scala
                       +- RESTApi.scala
```

In the forcelayout folder, you can find the scala-force-layout lib part that we use to make the layout of the graph.

In objectTypes

## Installation

Download or clone the project then use `mvn -Dmaven.test.skip=true package` to produce the JAR. Then all you have to do is to add the JAR to your project. The jar is created with built-in depedencies so you don't have to worry about it.

## Usage

Here is an example on how to use the library in Scala to create a project, add a few nodes and create links between them.
You will find a Java example [here](https://github.com/PierreMear/GNS3-Scala-lib/blob/master/java-example.md)

```scala
import GAPY.GNS3_Manager
import objectTypes.{LocalHub, LocalVpcs, SimpleLink}
import topologies.StarNetwork

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

We also created templates for commonly used topologies, in the previous example we used a Star Network. Here is how you can create the same network faster

```scala
import GAPY.GNS3_Manager
import objectTypes.{LocalHub, LocalVpcs, SimpleLink}
import topologies.StarNetwork

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
