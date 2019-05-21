package GAPY

import topologies._
import objectTypes._

object App {
  
  def foo(x : Array[String]) = x.foldLeft("")((a,b) => a + b)
  
  def main(args : Array[String]) {
    val p = new ProjectManager("e5e67027-ab19-4a4b-a883-774c028aa90d","http://localhost:3080")
    /*p.addTopology(new StarNetwork("SN1-","ethernet_hub",List("vpcs","vpcs","vpcs","vpcs","vpcs","vpcs","vpcs")))
    .addTopology(new StarNetwork("SN2-","ethernet_hub",List("vpcs","vpcs","vpcs","vpcs","vpcs","vpcs","vpcs")))
    .addLink("SN1-ethernet_hub", 0, 7, "SN2-ethernet_hub", 0, 7)*/

    p.addNode(Vpcs("PC1"))
    .addNode(Vpcs("PC2"))
    .addNode(Vpcs("PC3"))
    .addNode(Vpcs("PC4"))
    .addNode(Hub("hub"))
    .addLink(Vpcs("PC1"), Hub("hub"), 0, 0)
    .addLink(Vpcs("PC2"), Hub("hub"), 0, 1)
    .addLink(Vpcs("PC3"), Hub("hub"), 0, 2)
    .addLink(Vpcs("PC4"), Hub("hub"), 0, 3)
    .removeLink(Vpcs("PC1"), Hub("hub"))
    .removeLink(Vpcs("PC2"), Hub("hub"))
    .addLink(Vpcs("PC1"),Vpcs("PC2"),0,0)
    .removeNode(Vpcs("PC3"))
  }

}
