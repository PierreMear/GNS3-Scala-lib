# Nodes

Here we will list all available nodes and quickly explain how they work.

## Nodes and Appliances Inheritance Diagram

![](https://github.com/PierreMear/GNS3-Scala-lib/blob/master/docs/images/nodesClassDiagram.jpg)


This diagram describe the inheritances between the different nodes classes. This show you how you can add your own Appliance node in this API.

Since all is about nodes an links in GNS3, the software REST API gives us the possibility to create personalised nodes known as appliance. If you want to use some of your own appliances in this lib you'll need to follow those steps :

1. Create the appliance template in GNS3 and remember the name and the node_type of this Appliance template.
2. After that, you'll need to create a class to represent the Appliance.
For that it's simple you just need to extends the Appliance abstract class with a new case class that will represent your Appliance(see after)
3. It's finish. You just have to use your case class as you do it for built-in nodes before

Example of Appliance extention :
```scala
case class Alpine(override val name:String) extends Appliance("Alpine Linux",name,"docker","local")
```
As you can see, we override the node's name in Appliance by our own Alpine's name. the parameters of the Appliance abstract class are :

1. The name of the Appliance template (that's how we search the ID of the template)
2. The name of this node (**WARNING** : this name will not be displayed on GNS3 because it's an appliance, but it will be the name to refer to when you're in the scala source code)
3. The node_type of the node to create (very often it's docker or Qemu)
4. The compute_id is the ID of a remoteGNS3 Server

## Nodes

There are two kinds of Nodes : local and configurable. They work the same way, the only difference is that the compute_id of the Local nodes are binded to the local server whereas you can choose on which server the configurable nodes will work (it can still be local).

Here is how you can create the different node types and since the nodes are case classes, you can create them when adding them to the gns3 project.

```scala
val manager = new GNS3_Manager("http://localhost:8080")

val project = manager.createProject("myFirstProject")

project.addNode(LocalVpcs("PC1"))
.addNode(LocalVpcs("PC2"))
.addNode(LocalHub("LocalHub"))
.addNode(LocalSwitch("LocalSwitch"))
```

## Appliances

Appliances are custom nodes.


## Links

You can create two types of links, RawLink and SimpleLink. SimpleLink binds the adapters of the link to 0 whereas you can configure the adapters ports for the RawLinks.

You can create links this way :

```scala
val manager = new GNS3_Manager("http://localhost:8080")

val project = manager.createProject("myFirstProject")

project.addNode(LocalVpcs("PC1"))
.addNode(LocalVpcs("PC2"))
.addNode(LocalHub("LocalHub"))
.addNode(LocalSwitch("LocalSwitch"))
.addLink(SimpleLink(LocalVpcs("PC1"),LocalHub("LocalHub"),0,0))
.addLink(SimpleLink(LocalVpcs("PC2"),LocalHub("LocalHub"),0,1))
```