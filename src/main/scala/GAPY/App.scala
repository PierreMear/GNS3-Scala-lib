package gapy

import gapy.topologies._
import gapy.objectTypes._

object App {

  def main(args : Array[String]) {
    val p = new ProjectManager("50392e51-1334-49e6-858a-1649e22badf7","http://localhost:3080")
    p.addTopology(new StarNetwork(LocalHub("LocalHub1"),List(LocalVpcs("PC0-0"),LocalVpcs("PC0-1"),LocalVpcs("PC0-2"),LocalVpcs("PC0-3"),LocalVpcs("PC0-4"),LocalVpcs("PC0-5"))))
    .addTopology(new StarRingNetwork(LocalHub("LocalHub2"),List(LocalHub("PC1-0"),LocalHub("PC1-1"),LocalHub("PC1-2"),LocalHub("PC1-3"),LocalHub("PC1-4"),LocalHub("PC1-5"))))
    .addTopology(new StarLinearNetwork(LocalHub("LocalHub3"),List(LocalHub("PC2-0"),LocalHub("PC2-1"),LocalHub("PC2-2"),LocalHub("PC2-3"),LocalHub("PC2-4"),LocalHub("PC2-5"))))
    .addTopology(new StarNetwork(LocalHub("LocalHub4"),List(LocalHub("PC3-0"),LocalHub("PC3-1"),LocalHub("PC3-2"),LocalHub("PC3-3"),LocalHub("PC3-4"),LocalHub("PC3-5"))))
    .addTopology(new StarLinearNetwork(LocalHub("LocalHub5"),List(LocalHub("PC4-0"),LocalHub("PC4-1"),LocalHub("PC4-2"),LocalHub("PC4-3"),LocalHub("PC4-4"),LocalHub("PC4-5"))))
    .addTopology(new StarRingNetwork(LocalHub("LocalHub6"),List(LocalHub("PC5-0"),LocalHub("PC5-1"),LocalHub("PC5-2"),LocalHub("PC5-3"),LocalHub("PC5-4"),LocalHub("PC5-5"))))
    .addLink(SimpleLink(LocalHub("LocalHub1"),LocalHub("LocalHub2"),6,7))
    .addLink(SimpleLink(LocalHub("LocalHub2"),LocalHub("LocalHub3"),6,7))
    .addLink(SimpleLink(LocalHub("LocalHub3"),LocalHub("LocalHub4"),6,7))
    .addLink(SimpleLink(LocalHub("LocalHub4"),LocalHub("LocalHub5"),6,7))
    .addLink(SimpleLink(LocalHub("LocalHub5"),LocalHub("LocalHub6"),6,7))
    .addLink(SimpleLink(LocalHub("LocalHub6"),LocalHub("LocalHub1"),6,7))
    .layout(maxIteration = 10000)

    /*p.addNode(LocalVpcs("PC1"))
    .addNode(LocalVpcs("PC2"))
    .addNode(LocalVpcs("PC3"))
    .addNode(LocalVpcs("PC4"))
    .addNode(LocalHub("LocalHub"))
    .addLink(SimpleLink(LocalVpcs("PC1"), LocalHub("LocalHub"), 0, 0))
    .addLink(SimpleLink(LocalVpcs("PC2"), LocalHub("LocalHub"), 0, 1))
    .addLink(SimpleLink(LocalVpcs("PC3"), LocalHub("LocalHub"), 0, 2))
    .addLink(SimpleLink(LocalVpcs("PC4"), LocalHub("LocalHub"), 0, 3))
    .layout()*/

    /*val gnsManager:GNS3_Manager = new GNS3_Manager("http://localhost:3080")
    gnsManager
    .createProject("copied")
    .copyProject(p)
    .addTopology(new RingNetwork(List(LocalHub("copyLink"),LocalHub("copy1"),LocalHub("copy2"),LocalHub("copy3"),LocalHub("copy4"))))
    .addLink(SimpleLink(LocalHub("LocalHub1"),LocalHub("copyLink"),7,4))*/
  }

}
