
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
                       +- SSHApi.scala
                       +- SSHApi_Exceptions.scala
```

In the forcelayout folder, you can find the scala-force-layout lib part that we use to make the layout of the graph.

In objectTypes folder, you'll find all the classes that represent a particular object in GNS3 like nodes or links.

In the topologies folder, we've tried to implements some basic topologies of networks. You'll find also the Topology trait that defind how a topology needs to be to be compatible with our lib.

RESTApi, SSHApi and JSONApi are Object we use to make calls to libraries. This choice was made to use easily the library and be able to change library without doing a complete rework of the lib

GNS3_Manager is the class that define how we interact with a GNS3 VM. With this object you can define the SSH connection for the server, create and delete projects.

ProjectManager is the main class of this lib, because it's the one that will manage our GNS3 Project. All you can do in a GNS3 project like creating and deleting nodes, make and cut links, build networks with a bunch of topologies or simply start the simulation, all will be in this class. At least for now.

Files with a name ending by `_Exceptions` defines the exception that can be thrown by the connected class(SSHApi_Exceptions are exceptions for the SSHApi object), except for the GNS3_Exceptions that contains the exceptions for the GNS3_Manager as well as those for the ProjectManager. 

## Classes interactions

First, if you want to help us develop this lib, you need to understand how the different classes interact. The next diagram explain how the main classes interact and comunicate.
![](https://github.com/PierreMear/GNS3-Scala-lib/blob/master/docs/images/LibraryClassDiagram.png)
As you can see, the diagram is separated in two parts. At the top there is three objects that are the external libraries interface. As explain earlier, those object simplify the use of the different libraries we use, but the second goal of this software architecture is to make it easier to evolve. In fact, if you want to use a library with more features, you just have to change the pom.xml file and do the rework on the object to ensure that the behavior of each function in the object is the same and it will work on the whole project.

At the bottom, you can see the two classes used by the users of this lib. The GNS3_Manger class manage all things about the GNS3 Server including :

1. Server authorization
2. SSH connections
3. Projects' external management (creation, deletion, ...)

If you want to add functionnality at the server level like the Appliance template creation for example it will be in this class

For ProjectManager, this class manage all the projects' internal aspects including :

1. Nodes management (creation, deletion, ...)
2. Links management
3. High level topology creation
4. Simulation management (start and stop nodes for example)
5. Node's configuration (Network interfaces configuration for example)
6. Projects' interaction like project copy
7. Nodes' positionning

If you want to add fonctionality at a project management level like adding the telnet connection to nodes to send commands, it will be in this class

## Next features

If you want to help, we've thinked about some features that can be really interesting :

1. Network interfaces configuration copy
2. Better management of exceptions
3. Rework of the layout functionality
4. Nodes' Telnet terminal connection
5. Reliability-test functionality
6. Dynamic Appliance template

As you may have seen, the first three points are here to make a clean lib more than add features. But we think that create other features without making a proper basis will make this lib unstable one day or another. So we decide to build the first version of this lib firmly for the sake of the future.

### Network interfaces configuration copy

In the current version of the lib, when you copy a project into an other ProjectManager the configuration done by the `addNode(a:Appliance, pathToConfigFile:String)` function is not copied in the project.
The first thing we want to do is a perfect copy of the project for more consistency.

### Better management of exceptions

The current management of exceptions is indeed insufficient. We would like to improve this part of the lib to make it more user-friendly and easy to manage.
Of course, this will be done be re-organizing the exceptions' class tree of the project and by adding new exceptions that we have maybe forgotten.
We would like, for example, manage all the timeout and server connection errors in the RESTApi to hide this network management from the main classes. Of course the same goes for the SSHApi. 

### Rework of the layout functionality

The rework of the layout function in the project is a very important part for the future of this lib, because the lib currently used work on a deprecated version of the Scala lib.
If it's possible, we would like to have our own graph class that manage to do this part. 
But if a specialized tool for grph vizualization is wildly used we can think of making a connection with this tool.

### Nodes' Telnet terminal connection

As you may know, nodes like vpcs or docker image simulated inside GNS3 can be accessed by a telnet terminal. The first **new** features that we found interesting is the ability to send commands to those nodes via this telnet connection. It may be the start of a big features, because we will probabilly want to react to those commands and may be create a sequence-diagram-like DSL to simulate complex communications between nodes.

### Reliability-test functionality

At the begining of this project, one of the goals was to be able to test infrastructures in this simulation and maybe try a chaos engineering approach to make a powerful reliability test tool. That's why, create a bunch of functionality to help as much as we can the developpement of automatic infrastructures' testing is one of our main goal.

### Dynamic Appliance template

This maybe less interesting than the testing feature, but the possibility to dynamically add appliances to a remote GNS3 server without having to connect a virtual desktop or a GNS3 gui is really helpful since there can be compatibility problem between GNS3 version on the remote server and your own gui (two different version of GNS3 cannot connect)