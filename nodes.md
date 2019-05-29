# Nodes

Here we will list all available nodes and quickly explain how they work.

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



## Appliances

Appliances are custom nodes.
