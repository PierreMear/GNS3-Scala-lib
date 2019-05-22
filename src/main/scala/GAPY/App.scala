package GAPY

import topologies._
import objectTypes._

object App {
  
  def main(args : Array[String]) {
    val p = new ProjectManager("e5e67027-ab19-4a4b-a883-774c028aa90d","http://localhost:3080")
    //p
    //.addTopology(new RingNetwork(List(LocalHub("PC1-0"),LocalHub("PC1-1"),LocalHub("PC1-2"),LocalHub("PC1-3"),LocalHub("PC1-4"),LocalHub("PC1-5"),LocalHub("PC1-6"))))
    //.addTopology(new StarRingNetwork(LocalHub("LocalHub1"),List(LocalHub("PC1-0"),LocalHub("PC1-1"),LocalHub("PC1-2"),LocalHub("PC1-3"),LocalHub("PC1-4"),LocalHub("PC1-5"),LocalHub("PC1-6"))))
    //.addTopology(new StarLinearNetwork(LocalHub("LocalHub2"),List(LocalHub("PC2-0"),LocalHub("PC2-1"),LocalHub("PC2-2"),LocalHub("PC2-3"),LocalHub("PC2-4"),LocalHub("PC2-5"),LocalHub("PC2-6"))))
    //.addLink(SimpleLink(LocalHub("LocalHub1"),LocalHub("LocalHub2"),7,7))

    p.addNode(LocalVpcs("PC1"))
    .addNode(LocalVpcs("PC2"))
    .addNode(LocalVpcs("PC3"))
    .addNode(LocalVpcs("PC4"))
    .addNode(LocalHub("LocalHub"))
    .addLink(SimpleLink(LocalVpcs("PC1"), LocalHub("LocalHub"), 0, 0))
    .addLink(SimpleLink(LocalVpcs("PC2"), LocalHub("LocalHub"), 0, 1))
    .addLink(SimpleLink(LocalVpcs("PC3"), LocalHub("LocalHub"), 0, 2))
    .addLink(SimpleLink(LocalVpcs("PC4"), LocalHub("LocalHub"), 0, 3))
    .startNode(LocalVpcs("PC1"))
    .startNode(LocalVpcs("PC2"))
    .stopNode(LocalVpcs("PC1"))
    .startAll()
    
    /*val gnsManager:GNS3_Manager = new GNS3_Manager("http://localhost:3080")
    gnsManager
    .createProject("copied")
    .copyProject(p)
    .addTopology(new RingNetwork(List(LocalHub("copyLink"),LocalHub("copy1"),LocalHub("copy2"),LocalHub("copy3"),LocalHub("copy4"))))
    .addLink(SimpleLink(LocalHub("LocalHub"),LocalHub("copyLink"),4,4))*/
  }

}
