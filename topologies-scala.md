#Topologies templates

In this folder you will find the implementations of the topology templates implemented.

## StarNetwork

You can use this template as follows :

```scala
val manager = new GNS3_Manager("http://localhost:8080")
val project = manager.createProject("StarNetwork")
project.addTopology(new StarNetwork(LocalHub("LocalHub"),List(LocalVpcs("PC1"),LocalVpcs("PC2"),LocalVpcs("PC3"),LocalVpcs("PC4"),LocalVpcs("PC5"),LocalVpcs("PC6"))))
```
It creates a network like this :

![](http://2.bp.blogspot.com/-K67RlyQ3p20/Tes27a2-3KI/AAAAAAAAAdg/iCJyF6igAYk/s1600/star_topology.JPG)


## RingNetwork

You can use this template as follows :

```scala
val manager = new GNS3_Manager("http://localhost:8080")
val project = manager.createProject("RingNetwork")
project.addTopology(new RingNetwork(List(LocalVpcs("PC1"),LocalVpcs("PC2"),LocalVpcs("PC3"),LocalVpcs("PC4"),LocalVpcs("PC5"),LocalVpcs("PC6"))))
```
It creates a network like this :

![](http://4.bp.blogspot.com/-E8Cw3fwJNCY/Tes28aeIQGI/AAAAAAAAAdo/YA00mpOjpKw/s1600/ring_topology.JPG)

## LinearNetwork

You can use this template as follows :

```scala
val manager = new GNS3_Manager("http://localhost:8080")
val project = manager.createProject("RingNetwork")
project.addTopology(new RingNetwork(List(LocalVpcs("PC1"),LocalVpcs("PC2"),LocalVpcs("PC3"),LocalVpcs("PC4"),LocalVpcs("PC5"),LocalVpcs("PC6"))))
```

## FullyConnectedNetwork

You can use this template as follows :

```scala
val manager = new GNS3_Manager("http://localhost:8080")
val project = manager.createProject("FullyConnectedNetwork")
project.addTopology(new FullyConnectedNetwork(List(LocalVpcs("PC1"),LocalVpcs("PC2"),LocalVpcs("PC3"),LocalVpcs("PC4"),LocalVpcs("PC5"),LocalVpcs("PC6"))))
```

## StarRingNetwork

You can use this template as follows :

```scala
val manager = new GNS3_Manager("http://localhost:8080")
val project = manager.createProject("StarRingNetwork")
project.addTopology(new StarRingNetwork(LocalHub("LocalHub"),List(LocalVpcs("PC1"),LocalVpcs("PC2"),LocalVpcs("PC3"),LocalVpcs("PC4"),LocalVpcs("PC5"),LocalVpcs("PC6"))))
```

## StarLinearNetwork

You can use this template as follows :

```scala
val manager = new GNS3_Manager("http://localhost:8080")
val project = manager.createProject("StarLinearNetwork")
project.addTopology(new StarLinearNetwork(LocalHub("LocalHub"),List(LocalVpcs("PC1"),LocalVpcs("PC2"),LocalVpcs("PC3"),LocalVpcs("PC4"),LocalVpcs("PC5"),LocalVpcs("PC6"))))
```
